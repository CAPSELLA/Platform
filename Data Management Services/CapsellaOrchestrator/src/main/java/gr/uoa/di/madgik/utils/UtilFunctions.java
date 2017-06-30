package gr.uoa.di.madgik.utils;

import java.io.File;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;

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
				metadata.setDatasetName(entry.getValue());
			}
			else if(entry.getKey().equals("description")){
				metadata.setDescription(entry.getValue());
			}
			else if(entry.getKey().equals("license")){
				metadata.setLicense(entry.getValue());
			}
			else if(entry.getKey().equals("owner_group")){
				metadata.setLicense(entry.getValue());
			}
			else if(entry.getKey().equals("maintainer")){
				metadata.setMaintainer(entry.getValue());
			}
			else if(entry.getKey().equals("maintainer_email")){
				metadata.setMaintainerEmail(entry.getValue());
			}
			else if(entry.getKey().equals("author_email")){
				metadata.setAuthorEmail(entry.getValue());
			}
			else  if(entry.getKey().equals("source")){
				metadata.setSource(entry.getValue());
			}
			


	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        metadata.setTimeCreated(timestamp);
	        metadata.setLastUpdated(timestamp);
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		
		}
		metadata.setOwnerGroup(group);
		
		//metadata = addDerrivedMetadata(metadata);
		
		return metadata;
	}
	
	public static Metadata addDerrivedMetadata(Metadata metadata, String default_license){
		if(metadata.getAccess() == null){
			metadata.setAccess(Access.PUBLIC.toString());
		}
		if(metadata.getAuthor() == null){
			metadata.setUsername(metadata.getUsername());
		}
		if(metadata.getLicense() == null){
			metadata.setLicense(default_license);
		}
		
		
		return metadata;
	}
	
	public static Metadata addContentType(Metadata metadata,String filename){
		 if(metadata.getContentType() == null){
				if(FilenameUtils.getExtension(filename).equals("zip")){
					metadata.setContentType("shapefile");
				}
				if(FilenameUtils.getExtension(filename).equals("csv")){
					metadata.setContentType("csv");

				}
				if(FilenameUtils.getExtension(filename).equals("json")){
					metadata.setContentType("json");
				}
			}
		 return metadata;
	}

	public static Metadata mergeMetadata(Metadata newMetadata, Metadata metadata) throws InstantiationException, IllegalAccessException {
	    // would require a noargs constructor for the class, maybe you have a different way to create the result.
	    Metadata result = (Metadata) newMetadata.getClass().newInstance();
	    BeanUtils.copyProperties(newMetadata, result);
	    BeanUtils.copyProperties(metadata, result);
	    return result;
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
