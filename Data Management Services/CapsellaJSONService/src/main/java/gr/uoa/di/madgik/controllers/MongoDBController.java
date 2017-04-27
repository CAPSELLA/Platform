package gr.uoa.di.madgik.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import gr.uoa.di.madgik.entities.JsonEntities;
import gr.uoa.di.madgik.repositories.CustomerRepository;
import gr.uoa.di.madgik.repositories.DatabaseConfiguration;

@RestController

public class MongoDBController {

	@Autowired
	private CustomerRepository repository;

	@Value("${json.directory}")
	private String path; 
	@Value("${spring.data.mongodb.database}")
	private String database;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> saveJSONDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {

		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(uploadfile.getBytes());
	    fos.close(); 
		String str = FileUtils.readFileToString(convFile, "UTF-8");
		JSONArray jArray =new JSONArray(str);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DatabaseConfiguration databaseConfig = new DatabaseConfiguration();
		databaseConfig.mongo();

		List<DBObject> documents = new ArrayList<>();
		for(int i =0; i < jArray.length(); i++){
			Document doc = Document.parse(jArray.getJSONObject(i).toString());
			
			JSONObject json = jArray.getJSONObject(i);
			json.put("uuid", uuid);
			
			DBObject dbObject = (DBObject) JSON
					.parse(json.toString());
			documents.add(dbObject);

		}
	
		databaseConfig.mongo().getDB(database).getCollection(collection).insert(documents);
		convFile.delete();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteJSONDataset(@RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {
		DatabaseConfiguration databaseConfig = new DatabaseConfiguration();
		databaseConfig.mongo();
		
		BasicDBObject query = new BasicDBObject();
		query.put("uuid", uuid);
		databaseConfig.mongo().getDB(database).getCollection(collection).remove(query);
		
		return new ResponseEntity<>(HttpStatus.OK);


	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> updateJSONDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {
		
		deleteJSONDataset(uuid, collection);
		saveJSONDataset(uploadfile, uuid, collection);
		
		return new ResponseEntity<>(HttpStatus.OK);


	}
	

}
