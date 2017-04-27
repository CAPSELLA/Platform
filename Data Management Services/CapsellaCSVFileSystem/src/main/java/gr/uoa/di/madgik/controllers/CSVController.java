package gr.uoa.di.madgik.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	
	@RequestMapping(value = "/uploadCSVFile", method = RequestMethod.POST)
	public ResponseEntity<?> saveCSVFile(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid ) throws Exception {

		System.out.println(path);

		deleteCSVFile(uuid);
		uploadfile.transferTo(new File(path + File.separator + uploadfile.getOriginalFilename()));
//		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
//		convFile.setWritable(true);
//		convFile.createNewFile();
		CSVFile file = new CSVFile(uuid,path,uploadfile.getOriginalFilename());
		database.insertCSVFile(file);

		return new ResponseEntity<>(HttpStatus.OK);
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
	
	
}
