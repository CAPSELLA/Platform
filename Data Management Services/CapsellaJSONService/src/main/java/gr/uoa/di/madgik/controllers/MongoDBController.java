package gr.uoa.di.madgik.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
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
	
	@Value("${dataset.service.url}")
	private String serviceUrl;
	
	@Autowired
	private DatabaseConfiguration databaseConfiguration;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> saveJSONDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {

		Map<String, String> responseMap = new HashMap<String, String>();

		
		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(uploadfile.getBytes());
	    fos.close();
		JSONArray jArray = null;
		String str = FileUtils.readFileToString(convFile, "UTF-8");
		Object object = new JSONTokener(str).nextValue();
		if (object instanceof JSONObject) {
			//you have an object
			JSONObject jsonObject = new JSONObject(str);
			jArray = new JSONArray();
			jArray.put(jsonObject);
		}
		else if (object instanceof JSONArray) {
			jArray =new JSONArray(str);
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		

		List<DBObject> documents = new ArrayList<>();
		for(int i =0; i < jArray.length(); i++){
			Document doc = Document.parse(jArray.getJSONObject(i).toString());
			
			JSONObject json = jArray.getJSONObject(i);
			json.put("uuid", uuid);
			
			DBObject dbObject = (DBObject) JSON
					.parse(json.toString());
			documents.add(dbObject);

		}
	
		databaseConfiguration.getMongo().getDB(database).getCollection(collection).insert(documents);
		convFile.delete();

		responseMap.put("mongo-service", serviceUrl + uuid + "?collection=" + collection);

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteJSONDataset(@RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {
		
		BasicDBObject query = new BasicDBObject();
		query.put("uuid", uuid);
		databaseConfiguration.getMongo().getDB(database).getCollection(collection).remove(query);

		return new ResponseEntity<>(HttpStatus.OK);


	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> updateJSONDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {
		
		deleteJSONDataset(uuid, collection);
		saveJSONDataset(uploadfile, uuid, collection);
		
		return new ResponseEntity<>(HttpStatus.OK);


	}
	
	@RequestMapping(value = "/getJSONDataset/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getJSONDataset(@PathVariable String id,  @RequestParam("collection") String collection) throws Exception  {
		
		BasicDBObject query = new BasicDBObject();
		query.put("uuid", id);
		DBCursor find = databaseConfiguration.getMongo().getDB(database).getCollection(collection).find(query);
		
		StringBuilder stringBuilder = new StringBuilder();
		if (find.hasNext())
		{
			stringBuilder.append("[");
			stringBuilder.append("\n");
			while(find.hasNext())
			{
				stringBuilder.append(find.next());
				if(find.hasNext()){
					
					stringBuilder.append(",");
					stringBuilder.append("\n");
					
				}
			}
			stringBuilder.append("]");
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.OK);


	}
	

}
