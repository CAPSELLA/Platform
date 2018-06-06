package gr.uoa.di.madgik.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import com.google.common.io.ByteStreams;

import gr.uoa.di.madgik.beans.Database;
import gr.uoa.di.madgik.model.ImageFile;

@RestController
public class FileController {
	
	
	@Autowired
	Database database;

	@Value("${image.directory}")
	private String path; 
	
	@Value("${image.url}")
	private String imageUrl; 
	
	
	@RequestMapping(value = "/uploadImageFile", method = RequestMethod.POST)
	public ResponseEntity<?> saveImageFile(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("uuid") String uuid ) throws Exception {

		deleteImageFile(uuid);
		uploadfile.transferTo(new File(path + File.separator + uploadfile.getOriginalFilename()));
		ImageFile file = new ImageFile(uuid,path,uploadfile.getOriginalFilename());
		database.insertImageFile(file);
		String path = new String(imageUrl + uuid);

		return new ResponseEntity< String>(path,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteImageFile", method = RequestMethod.POST)
	public ResponseEntity<?> deleteImageFile(@RequestParam("uuid") String uuid ) throws Exception {

		ImageFile imageFile = database.selectImageFile(uuid);
		if(imageFile != null)
		{
			File convFile = new File(imageFile.getPath() + File.separator+ imageFile.getFilename());
			
			if(convFile.exists())
			{
				convFile.delete();
				database.deleteImageFile(uuid);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	@GetMapping(value = "/getImageFile/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public @ResponseBody String getImageFile(@PathVariable String id , @RequestParam("image_type") String type ) throws Exception {

		ImageFile imageFile = database.selectImageFile(id);
		if(imageFile != null)
		{
			 InputStream in = new FileInputStream(new File(imageFile.getPath() + File.separator+ imageFile.getFilename()));


			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 BufferedImage img = ImageIO.read(new File(imageFile.getPath() + File.separator+ imageFile.getFilename()));
			 ImageIO.write(img, type, baos);
			 baos.flush();
		 
			 String base64String = Base64.encode(baos.toByteArray());
			 baos.close();

			 return base64String;
		}
		
		return null;
		
	}

}
