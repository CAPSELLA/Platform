package gr.uoa.di.madgik.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.facebook.presto.sql.parser.SqlParser;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import gr.uoa.di.madgik.service.DatabaseService;
import gr.uoa.di.madgik.utils.ResultSetConverter;
import gr.uoa.di.madgik.utils.ResultSetSerializer;
import io.swagger.annotations.Api;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statements;
import com.google.gson.Gson;


@Api
@RestController
@Component
public class RelationalController {
	
	@Value("${temp.directory}")
	private String path; 
	@Value("${postgres.url}")
	private String databaseUrl; 
	@Value("${spring.datasource.username}")
	private String username; 
	@Value("${spring.datasource.password}")
	private String password; 
	
	
	@Autowired
	private DatabaseService databaseService;
	
	@RequestMapping(value = "/storeDatabase", method = RequestMethod.POST)
	public ResponseEntity<?> uploadDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("database") String database) throws Exception {   //, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {

		
		
		Map<String, String> responseMap = new HashMap<String, String>();

		//path = System.getProperty("user.dir");		//for local testing
		
		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(uploadfile.getBytes());
	    fos.close(); 
		String str = FileUtils.readFileToString(convFile, "UTF-8");

		final String regex = getFile("createDatasetRules.properties");     
		final Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
		
		final Matcher matcher = p.matcher(str);

		while (matcher.find()) {
		   System.out.println(matcher.group()); //now contains the full SQL command
		   
		   responseMap.put("error", "Sql script problem! ");
		   
		   return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
		   
		}
 		System.out.println("Starting!");

		 Connection c = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection(databaseUrl+database,
	            username, password);

	 		databaseService.excecuteScriptFromFile(str, c, convFile.getPath());

	 		System.out.println("Finished!");
	         
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		 		System.out.println("Exception!");

	         System.exit(0);
	      }
	
        convFile.delete();
		

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/updateDatabase", method = RequestMethod.POST)
	public ResponseEntity<?> updateDataset(@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("database") String database) throws Exception {   //, @RequestParam("uuid") String uuid,  @RequestParam("collection") String collection) throws Exception  {

		deleteDatabase(database);
		
		createDatabase(database);
		
		Map<String, String> responseMap = new HashMap<String, String>();

		//path = System.getProperty("user.dir");		//for local testing
		
		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(uploadfile.getBytes());
	    fos.close(); 
		String str = FileUtils.readFileToString(convFile, "UTF-8");

		final String regex = getFile("createDatasetRules.properties");     
		final Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
		
		final Matcher matcher = p.matcher(str);

		while (matcher.find()) {
		   System.out.println(matcher.group()); //now contains the full SQL command
		   
		   responseMap.put("error", "Sql script problem! ");
		   
		   return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
		   
		}
 		System.out.println("Starting!");

		 Connection c = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection(databaseUrl+database,
	            username, password);

	 		databaseService.excecuteScriptFromFile(str, c, convFile.getPath());

	 		System.out.println("Finished!");
	         
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		 		System.out.println("Exception!");

	         System.exit(0);
	      }
	
        convFile.delete();
		

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createDatabase", method = RequestMethod.POST)
	public ResponseEntity<?> createDatabase(@RequestParam("database") String database ) throws Exception {   

		Map<String, String> responseMap = new HashMap<String, String>();
		databaseService.excecuteScriptFromFile("CREATE DATABASE \""+ database +"\" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8' ;", null,null);

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/deleteDatabase", method = RequestMethod.POST)
	public ResponseEntity<?> deleteDatabase(@RequestParam("database") String database ) throws Exception {   

		Map<String, String> responseMap = new HashMap<String, String>();
		databaseService.excecuteScriptFromFile("DROP DATABASE \"" + database + "\" ", null,null);

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/queryToJson", method = RequestMethod.POST)
	public  ResponseEntity<?> queryToJson(@RequestParam("database") String database) {
        
		BufferedWriter bw = null;
		FileWriter fw = null;
		String databaseFilePath = null;
		
		Connection conn = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         conn = DriverManager
	            .getConnection(databaseUrl+database,
	            username, password);
	         
	 //      path = System.getProperty("user.dir");		//for local testing
	         
	       System.out.println(path + database +".json");
	        File file = new File(path + File.separator + database +".json");
	         
	  
	 		if (!file.exists()) {
	
	 			file.createNewFile();
	 		}
	 		else
	 		{
	 			file.delete();
	 			file.createNewFile();
	
	 			
	 		}
	 		databaseFilePath = file.getAbsolutePath();
	      
	         DatabaseMetaData md = conn.getMetaData();
				String[] types = {"TABLE"};
	
			 ResultSet schemas = md.getSchemas();
			 while(schemas.next())
			 {
		         ResultSet rs = md.getTables(null, schemas.getString(1), "%", types);
		         fw = new FileWriter(file.getAbsoluteFile(), true);
		         bw = new BufferedWriter(fw);
		
		         bw.write("{ \""+ database + "\" : {");
	 			 bw.newLine();
	
		
		 		 while (rs.next()) 
		 		 {
		 			 System.out.println(rs.getString(3));
		
		 			 bw.write("\"" + rs.getString(3) + "\" : ");
		 			 bw.newLine();
		 			 
		 			 String s = resultSetToJson(conn,"SELECT * FROM \"" + schemas.getString(1) +"\". \"" + rs.getString(3) + "\";");
		 			 if(!rs.isLast())
		 				 bw.write(s +", ");
		 			 else
		 				 bw.write(s);
		 			 bw.newLine();
	
		 		 }
			 }
	         bw.write("} }");

	 		 
	      }
	      catch(Exception e){
	    	  
	    	 e.printStackTrace();
	    	 try {
				conn.close();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	  
	      }
	      finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

					conn.close();

				} catch (IOException | SQLException ex) {

					ex.printStackTrace();

				}
			}
		
		
        return new ResponseEntity<String>( readFile(databaseFilePath),  HttpStatus.OK);
    }
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResponseEntity<?> query(@RequestParam("query") String query , @RequestParam("database") String database ) throws Exception {   

		
		final String regex = getFile("selectDatasetRules.properties");      //"^(INSERT INTO|CREATE TABLE|ALTER TABLE|UPDATE|SELECT|WITH|DELETE)(?:[^;']|(?:'[^']+'))+;\\s*$";
		final Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
		
		final Matcher matcher = p.matcher(query);

		while (matcher.find()) {
		   System.out.println(matcher.group()); //now contains the full SQL command
		   
		   
		   return new ResponseEntity<String>("Only select command is permitted! ", HttpStatus.OK);
		   
		}
		
		
		Map<String, String> responseMap = new HashMap<String, String>();
		Connection c = null;
		String result = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection(databaseUrl+database,
	            username, password);
	         result = databaseService.excecuteQuery(query, c);
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }

		return new ResponseEntity<String>(result, HttpStatus.OK);

	}
	
	private String getFile(String fileName) {

		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	  }
	
	public static String resultSetToJson(Connection connection, String query) {

		SimpleModule module = new SimpleModule();
		module.addSerializer(new ResultSetSerializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);


		Statement statement;
		try {
			statement = connection.createStatement();
			System.out.println(query);
			ResultSet resultset = statement.executeQuery(query);

			// Use the DataBind Api here
			ObjectNode objectNode = objectMapper.createObjectNode();

			// put the resultset in a containing structure
			objectNode.putPOJO("results", resultset);

			// generate all
			String json = objectMapper.writeValueAsString(objectNode);

			return json;
			//objectMapper.writeValue(stringWriter, objectNode);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        return null;
    }
	
	String readFile(String fileName)  {
		BufferedReader br = null;
	    try {
		    br = new BufferedReader(new FileReader(fileName));

	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        br.close();
			File file = new File(fileName);
			file.delete();

	        return sb.toString();
	    } 
	    
	    
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally {
	        try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
        return null;

	}

}
