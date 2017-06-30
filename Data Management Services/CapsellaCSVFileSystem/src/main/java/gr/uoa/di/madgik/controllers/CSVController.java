package gr.uoa.di.madgik.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gr.uoa.di.madgik.beans.Database;
import gr.uoa.di.madgik.csv.model.CSVFile;

@RestController
public class CSVController {

	@Autowired
	Database database;

	@Value("${csv.directory}")
	private String path; 
	
	@Value("${csv.url}")
	private String csvUrl; 
	
	
	@RequestMapping(value = "/uploadCSVFile", method = RequestMethod.POST)
	public ResponseEntity<?> saveCSVFile(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid ) throws Exception {

		deleteCSVFile(uuid);
		uploadfile.transferTo(new File(path + File.separator + uploadfile.getOriginalFilename()));
		CSVFile file = new CSVFile(uuid,path,uploadfile.getOriginalFilename());
		database.insertCSVFile(file);
		String path = new String(csvUrl + uuid);

		return new ResponseEntity< String>(path,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteCSVFile", method = RequestMethod.POST)
	public ResponseEntity<?> deleteCSVFile(@RequestParam("uuid") String uuid ) throws Exception {

		CSVFile csvFile = database.selectCSVFile(uuid);
		if(csvFile != null)
		{
			File convFile = new File(csvFile.getPath() + File.separator+ csvFile.getFilename());
			
			if(convFile.exists())
			{
				convFile.delete();
				database.deleteCSVFile(uuid);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(value = "/getCSVFile/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCSVFile(@PathVariable String id ) throws Exception {

		CSVFile csvFile = database.selectCSVFile(id);
		if(csvFile != null)
		{
			
			 String content = new String(Files.readAllBytes(Paths.get(csvFile.getPath() + File.separator+ csvFile.getFilename())));
			 return new ResponseEntity<>(content,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	
}
