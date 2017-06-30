package gr.uoa.di.madgik.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import gr.uoa.di.madgik.utils.ResultSetConverter;


@Component


public class DatabaseService {
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public DatabaseService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	public void excecuteScriptFromFile(String sqlScript, Connection con, String filePath)
	{
		if(con != null)
		{
		   
			//	ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ByteArrayResource(sqlScript.getBytes()));
	    	ScriptRunner runner = new ScriptRunner(con);
	    	try {
				runner.runScript(new BufferedReader(new FileReader(filePath)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (ScriptException e) {
			// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	} 
		}
		else
		{
			try {
				//ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ByteArrayResource(sqlScript.getBytes()));
				Statement s =jdbcTemplate.getDataSource().getConnection().createStatement();
				int Result=s.executeUpdate(sqlScript);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	public String excecuteQuery(String query, Connection con)
	{
		if(con != null)
		{		
			try 
			{
				Statement stmt = null;
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				JSONArray jsonArray = ResultSetConverter.convert(rs);
				
				return jsonArray.toString();
			} catch (SQLException | JSONException e) {
				e.printStackTrace();
				
				return e.toString();
			}
	    	
		}
	
		return null;

	}
	

}
