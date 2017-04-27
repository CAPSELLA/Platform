package gr.uoa.di.madgik.utils;

import java.io.File;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import gr.uoa.di.madgik.model.Access;
import gr.uoa.di.madgik.model.ContentType;
import gr.uoa.di.madgik.model.Metadata;

public class UtilFunctions {
	
	public static Metadata convertMapToMetadata(Map<String,String> metadataMap, String group){
		Metadata metadata = new Metadata();
		for (Map.Entry<String, String> entry : metadataMap.entrySet())
		{
			if (entry.getKey().equals("username")){
				metadata.setUsername(entry.getValue());
			}
			else if(entry.getKey().equals("content-type")){
				
				metadata.setContentType(entry.getValue());
			}
			else if(entry.getKey().equals("author")){
				metadata.setAuthor(entry.getValue());
			}
			else if(entry.getKey().equals("comments")){
				metadata.setComments(entry.getValue());
			}
			else if(entry.getKey().equals("size")){
				metadata.setSize(Integer.parseInt(entry.getValue()));

			}
			else if(entry.getKey().equals("tags")){
				metadata.setTags(entry.getValue());
			}
			else if(entry.getKey().equals("dataset_name")){
				metadata.setTags(entry.getValue());
			}
			else if(entry.getKey().equals("size")){
				metadata.setSize(Integer.parseInt(entry.getValue()));
			}
//			else if(entry.getKey().equals("group")){
//				metadata.setOwnerGroup(entry.getValue());
//			}
			
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        metadata.setTimeCreated(timestamp);
	        metadata.setLastUpdated(timestamp);
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		
		}
		metadata.setOwnerGroup(group);
		if(metadata.getAccess() == null){
			metadata.setAccess(Access.PUBLIC.toString());
		}
		return metadata;
	}

//	public static String findShapefileDatasetName(File file){
//		ZipFile zipFile = new ZipFile(file);
//		String datasetName="";
//		
//		 Enumeration zipEntries = zipFile.entries();
//		    while (zipEntries.hasMoreElements()) {
//		        String fileName = ((ZipEntry) zipEntries.nextElement()).getName();
//		        if(FilenameUtils.getExtension(fileName).equals("shp")){
//	    	   		return FilenameUtils.removeExtension(fileName);
//	    	   			
//	    	   		 }		 
//		        }
//	}
}
