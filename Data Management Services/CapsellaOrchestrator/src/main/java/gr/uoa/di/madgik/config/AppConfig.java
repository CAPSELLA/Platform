package gr.uoa.di.madgik.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
public class AppConfig {
	
	@Autowired
	public Environment environment;
	
	public ApiConfigTemplate apiconfig;


	
	public ApiConfigTemplate getProperties() {
    	
    	System.out.println("I'm in config and im instantiating");
    	
    	return new ApiConfigTemplate(environment.getRequiredProperty("csv.server"), 
    			environment.getRequiredProperty("shapefile.server"), environment.getRequiredProperty("json.server"),
    			environment.getRequiredProperty("csv.server.upload.csv.file"), environment.getRequiredProperty("csv.server.delete.csv.file"),
    			environment.getRequiredProperty("json.server.upload.json.file"),environment.getRequiredProperty("json.server.update.json.file"),
    			environment.getRequiredProperty("json.server.delete.json.file"), environment.getRequiredProperty("authorization.server"),
    			environment.getRequiredProperty("authorization.server.getUsers"), environment.getRequiredProperty("shapefile.server.delete"), environment.getRequiredProperty("server.action.new"), environment.getRequiredProperty("authorization.server.authenticateByUser"));
    	

    }
	
	public class ApiConfigTemplate{
		private String csvServer;
		private String shapeServer, shapeDelete;
		private String mongoDBServer;
		private String newDataset;
		
		private String csvServer_uploadCSVFile, csvServer_deleteCSVFile;
		
		private String mongoDBServer_uploadJSONFile, mongoDBServer_updateJSONFile, mongoDBServer_deleteJSONFile;
		
		
		private String authorizationServer, authorizationGetUserGroups, authorizationByUsername;
		
		
		
		public ApiConfigTemplate(String csvServer, String shapeServer, String mongoDBServer, String csvServer_uploadCSVFile, String csvServer_deleteCSVFile,
				String mongoDBServer_uploadJSONFile, String mongoDBServer_updateJSONFile, 
				String mongoDBServer_deleteJSONFile, String authorizationServer, String authorizationGetUserGroups, String shapeDelete, String newDataset, String authorizationByUsername) {
			super();
			this.csvServer = csvServer;
			this.shapeServer = shapeServer;
			this.mongoDBServer = mongoDBServer;
			
			this.csvServer_uploadCSVFile = csvServer_uploadCSVFile;
			this.csvServer_deleteCSVFile = csvServer_deleteCSVFile;
			
			this.mongoDBServer_uploadJSONFile = mongoDBServer_uploadJSONFile;
			this.mongoDBServer_updateJSONFile = mongoDBServer_updateJSONFile;
			this.mongoDBServer_deleteJSONFile = mongoDBServer_deleteJSONFile;
			
			this.authorizationServer = authorizationServer;
			this.authorizationGetUserGroups = authorizationGetUserGroups;
			this.authorizationByUsername = authorizationByUsername;
	
			this.shapeDelete = shapeDelete;
			this.newDataset = newDataset;
		}


		public String getCsvServer() {
			return csvServer;
		}

		public void setCsvServer(String csvServer) {
			this.csvServer = csvServer;
		}
		public String getShapeServer() {
			return shapeServer;
		}
		public void setShapeServer(String shapeServer) {
			this.shapeServer = shapeServer;
		}


		public String getCsvServer_uploadCSVFile() {
			return csvServer_uploadCSVFile;
		}


		public void setCsvServer_uploadCSVFile(String csvServer_uploadCSVFile) {
			this.csvServer_uploadCSVFile = csvServer_uploadCSVFile;
		}


		public String getCsvServer_deleteCSVFile() {
			return csvServer_deleteCSVFile;
		}


		public void setCsvServer_deleteCSVFile(String csvServer_deleteCSVFile) {
			this.csvServer_deleteCSVFile = csvServer_deleteCSVFile;
		}
		
				public String getMongoDBServer() {
			return mongoDBServer;
		}


		public void setMongoDBServer(String mongoDBServer) {
			this.mongoDBServer = mongoDBServer;
		}


		public String getMongoDBServer_uploadJSONFile() {
			return mongoDBServer_uploadJSONFile;
		}


		public void setMongoDBServer_uploadJSONFile(String mongoDBServer_uploadJSONFile) {
			this.mongoDBServer_uploadJSONFile = mongoDBServer_uploadJSONFile;
		}


		public String getMongoDBServer_updateJSONFile() {
			return mongoDBServer_updateJSONFile;
		}


		public void setMongoDBServer_updateJSONFile(String mongoDBServer_updateJSONFile) {
			this.mongoDBServer_updateJSONFile = mongoDBServer_updateJSONFile;
		}


		public String getMongoDBServer_deleteJSONFile() {
			return mongoDBServer_deleteJSONFile;
		}


		public void setMongoDBServer_deleteJSONFile(String mongoDBServer_deleteJSONFile) {
			this.mongoDBServer_deleteJSONFile = mongoDBServer_deleteJSONFile;
		}


		public String getAuthorizationServer() {
			return authorizationServer;
		}


		public void setAuthorizationServer(String authorizationSerer) {
			this.authorizationServer = authorizationSerer;
		}


		public String getAuthorizationGetUserGroups() {
			return authorizationGetUserGroups;
		}


		public void setAuthorizationGetUserGroups(String authorizationGetUserGroups) {
			this.authorizationGetUserGroups = authorizationGetUserGroups;
		}


		public String getShapeDelete() {
			return shapeDelete;
		}


		public void setShapeDelete(String shapeDelete) {
			this.shapeDelete = shapeDelete;
		}


		public String getNewDataset() {
			return newDataset;
		}


		public void setNewDataset(String newDataset) {
			this.newDataset = newDataset;
		}


		public String getAuthorizationByUsername() {
			return authorizationByUsername;
		}


		public void setAuthorizationByUsername(String authorizationByUsername) {
			this.authorizationByUsername = authorizationByUsername;
		}
		
//		
		
	}
	   
	 
}
