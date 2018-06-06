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
    			environment.getRequiredProperty("shapefile.server"), environment.getRequiredProperty("json.server"), environment.getRequiredProperty("relational.server"), environment.getRequiredProperty("image.server"),
    			environment.getRequiredProperty("csv.server.upload.csv.file"), environment.getRequiredProperty("csv.server.delete.csv.file"), environment.getRequiredProperty("csv.server.get.csv.file"),
    			environment.getRequiredProperty("json.server.upload.json.file"),environment.getRequiredProperty("json.server.update.json.file"),
    			environment.getRequiredProperty("json.server.delete.json.file"), environment.getRequiredProperty("json.server.get.json.file"),  environment.getRequiredProperty("authorization.server"),
    			environment.getRequiredProperty("authorization.server.getUsers"), environment.getRequiredProperty("shapefile.server.delete"), environment.getRequiredProperty("server.action.new"), environment.getRequiredProperty("authorization.server.authenticateByUser"),
    			environment.getRequiredProperty("relational.server.create.database"),environment.getRequiredProperty("relational.server.delete.database"),environment.getRequiredProperty("relational.server.query"),
    			environment.getRequiredProperty("relational.server.store.database"),environment.getRequiredProperty("relational.server.update.database"), environment.getRequiredProperty("relational.server.database.to.json"),
    			environment.getRequiredProperty("image.server.upload.image.file"), environment.getRequiredProperty("image.server.delete.image.file"), environment.getRequiredProperty("image.server.get.image.file")
);
    	

    }
	
	public class ApiConfigTemplate{
		private String csvServer;
		private String shapeServer, shapeDelete;
		private String mongoDBServer;
		private String relationalServer;
		private String imageServer;


		private String newDataset;
		
		private String csvServer_uploadCSVFile, csvServer_deleteCSVFile, csvServer_getCSVFile;
		
		private String imageServer_uploadImageFile, imageServer_deleteImageFile, imageServer_getImageFile;

		
		private String mongoDBServer_uploadJSONFile, mongoDBServer_updateJSONFile, mongoDBServer_deleteJSONFile, mongoDBServer_getJSONFile;
		
		private String relationalServer_createDatabase, relationalServer_deleteDatabase, relationalServer_query, relationalServer_storeDatabase , relationalServer_updateDatabase, relationalServer_DatabaseToJson;

		
		private String authorizationServer, authorizationGetUserGroups, authorizationByUsername;
		
		
		
		public ApiConfigTemplate(String csvServer, String shapeServer, String mongoDBServer, String relationalServer, String  imageServer, String csvServer_uploadCSVFile, String csvServer_deleteCSVFile, String csvServer_getCSVFile,
				String mongoDBServer_uploadJSONFile, String mongoDBServer_updateJSONFile, 
				String mongoDBServer_deleteJSONFile, String mongoDBServer_getJSONFile, String authorizationServer, String authorizationGetUserGroups, String shapeDelete, String newDataset, String authorizationByUsername,
				String relationalServer_createDatabase, String relationalServer_deleteDatabase, String relationalServer_query, String relationalServer_storeDatabase, String relationalServer_updateDatabase, String relationalServer_DatabaseToJson,
				String imageServer_uploadImageFile, String imageServer_deleteImageFile, String imageServer_getImageFile) {
			
			super();
			this.csvServer = csvServer;
			this.shapeServer = shapeServer;
			this.mongoDBServer = mongoDBServer;
			this.relationalServer = relationalServer;
			this.imageServer = imageServer;
			
			this.csvServer_uploadCSVFile = csvServer_uploadCSVFile;
			this.csvServer_deleteCSVFile = csvServer_deleteCSVFile;
			this.csvServer_getCSVFile = csvServer_getCSVFile;
			
			this.imageServer_uploadImageFile = imageServer_uploadImageFile;
			this.imageServer_deleteImageFile = imageServer_deleteImageFile;
			this.imageServer_getImageFile = imageServer_getImageFile;
			
			this.mongoDBServer_uploadJSONFile = mongoDBServer_uploadJSONFile;
			this.mongoDBServer_updateJSONFile = mongoDBServer_updateJSONFile;
			this.mongoDBServer_deleteJSONFile = mongoDBServer_deleteJSONFile;
			this.mongoDBServer_getJSONFile = mongoDBServer_getJSONFile;
			
			this.authorizationServer = authorizationServer;
			this.authorizationGetUserGroups = authorizationGetUserGroups;
			this.authorizationByUsername = authorizationByUsername;
		
			this.relationalServer_createDatabase = relationalServer_createDatabase;
			this.relationalServer_deleteDatabase = relationalServer_deleteDatabase;
			this.relationalServer_query = relationalServer_query;
			this.relationalServer_storeDatabase = relationalServer_storeDatabase;
			this.relationalServer_updateDatabase = relationalServer_updateDatabase;
			this.relationalServer_DatabaseToJson = relationalServer_DatabaseToJson;

	
			this.shapeDelete = shapeDelete;
			this.newDataset = newDataset;
		}


		
		
		
		public String getRelationalServer_DatabaseToJson() {
			return relationalServer_DatabaseToJson;
		}





		public void setRelationalServer_DatabaseToJson(String relationalServer_DatabaseToJson) {
			this.relationalServer_DatabaseToJson = relationalServer_DatabaseToJson;
		}





		public String getRelationalServer_updateDatabase() {
			return relationalServer_updateDatabase;
		}





		public void setRelationalServer_updateDatabase(String relationalServer_updateDatabase) {
			this.relationalServer_updateDatabase = relationalServer_updateDatabase;
		}





		public String getImageServer() {
			return imageServer;
		}





		public void setImageServer(String imageServer) {
			this.imageServer = imageServer;
		}





		public String getImageServer_uploadImageFile() {
			return imageServer_uploadImageFile;
		}





		public void setImageServer_uploadImageFile(String imageServer_uploadImageFile) {
			this.imageServer_uploadImageFile = imageServer_uploadImageFile;
		}





		public String getImageServer_deleteImageFile() {
			return imageServer_deleteImageFile;
		}





		public void setImageServer_deleteImageFile(String imageServer_deleteImageFile) {
			this.imageServer_deleteImageFile = imageServer_deleteImageFile;
		}





		public String getImageServer_getImageFile() {
			return imageServer_getImageFile;
		}





		public void setImageServer_getImageFile(String imageServer_getImageFile) {
			this.imageServer_getImageFile = imageServer_getImageFile;
		}





		public String getRelationalServer() {
			return relationalServer;
		}




		public void setRelationalServer(String relationalServer) {
			this.relationalServer = relationalServer;
		}




		public String getRelationalServer_createDatabase() {
			return relationalServer_createDatabase;
		}




		public void setRelationalServer_createDatabase(String relationalServer_createDatabase) {
			this.relationalServer_createDatabase = relationalServer_createDatabase;
		}




		public String getRelationalServer_deleteDatabase() {
			return relationalServer_deleteDatabase;
		}




		public void setRelationalServer_deleteDatabase(String relationalServer_deleteDatabase) {
			this.relationalServer_deleteDatabase = relationalServer_deleteDatabase;
		}




		public String getRelationalServer_query() {
			return relationalServer_query;
		}




		public void setRelationalServer_query(String relationalServer_query) {
			this.relationalServer_query = relationalServer_query;
		}




		public String getRelationalServer_storeDatabase() {
			return relationalServer_storeDatabase;
		}




		public void setRelationalServer_storeDatabase(String relationalServer_storeDatabase) {
			this.relationalServer_storeDatabase = relationalServer_storeDatabase;
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

		

		public String getMongoDBServer_getJSONFile() {
			return mongoDBServer_getJSONFile;
		}


		public void setMongoDBServer_getJSONFile(String mongoDBServer_getJSONFile) {
			this.mongoDBServer_getJSONFile = mongoDBServer_getJSONFile;
		}


		public String getCsvServer_getCSVFile() {
			return csvServer_getCSVFile;
		}


		public void setCsvServer_getCSVFile(String csvServer_getCSVFile) {
			this.csvServer_getCSVFile = csvServer_getCSVFile;
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
