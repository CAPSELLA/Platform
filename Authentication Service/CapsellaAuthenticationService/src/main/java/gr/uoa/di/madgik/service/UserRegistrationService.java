package gr.uoa.di.madgik.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.config.JWTFilter;
import gr.uoa.di.madgik.config.Token;
import gr.uoa.di.madgik.model.ClientIds;
import gr.uoa.di.madgik.model.User;
import io.jsonwebtoken.Claims;

@Component
public class UserRegistrationService {

	private final JdbcTemplate jdbcTemplate;

	@Value("${ldap.homeDirectory}")
	private String homeDirectory;

	@Value("${ldap.gidNumber}")
	private String gidNumber;

	
	
	@Autowired
	public UserRegistrationService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertUser(User user, List<String> roles) {
		String sql = "INSERT INTO users(username, password,fullname,\"group\", email) VALUES (?,?,?,string_to_array(?,','), ?)";
		String commaSeparatedString = String.join(",", roles);
		jdbcTemplate.update(sql,
				new Object[] { user.getUsername(), user.getUserPassword(), user.getFullName(), commaSeparatedString, user.getEmail() });

	}
	
	public void insertUserToken(User user, String token) {
		
		String sql = "SELECT * FROM user_token WHERE username=?";


		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,user.getUsername());
		if(!rows.isEmpty())
		{
			deteleUserToken(user.getUsername());
		}
		
		
		sql = "INSERT INTO user_token(username, token, login_timestamp) VALUES (?,?,CAST (? AS timestamp))";
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Use Madrid's time zone to format the date in
		df.setTimeZone(TimeZone.getDefault());

		
		jdbcTemplate.update(sql,
				new Object[] { user.getUsername(), token, df.format(date) });

	}
	
	public void deteleUserToken(String username)
	{
		String deleteStatement = "DELETE FROM user_token WHERE username=?";
		try {
			jdbcTemplate.update(deleteStatement, username);
		} catch (RuntimeException runtimeException) {

			throw runtimeException;
		}
	}
	
	public void generateToken(String name, String hostname, String groups, String user)
	{
		ArrayList<Object> values = Token.createJWTClientId(groups, name, hostname);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Date exp = (Date) values.get(0);
			User userEntity = mapper.readValue(user, User.class);
			String token = (String) values.get(1);
			String sql = "INSERT INTO \"clientIds\"(name, hostname, groups, token, \"expirationDate\", \"user\") VALUES (?,?,?,?,?,?)";
			jdbcTemplate.update(sql,
					new Object[] { name, hostname, groups, token, exp, userEntity.getUsername() });

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<ClientIds> getUserTokens(String username)
	{
		List<ClientIds> clientIds = new ArrayList<ClientIds>();

		ObjectMapper mapper = new ObjectMapper();
		String sql = "SELECT * FROM  \"clientIds\" WHERE \"user\"=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username);
		for (Map row : rows) {
			ClientIds clientId = new ClientIds();
			DateFormat df = new SimpleDateFormat("yyyy-dd-MM"); 
			Date expirationDate;
			expirationDate = (Date) row.get("expirationDate");
			String date = df.format(expirationDate);
			clientId.setExpiration_date(date);
			clientId.setGroups((String) row.get("groups"));
			clientId.setHostname((String) row.get("hostname"));
			clientId.setName((String) row.get("name"));
			clientId.setToken((String) row.get("token"));
			clientIds.add(clientId);
			
		}
		return clientIds;
	}
	
	public List<ClientIds> checkToken(String token)
	{
		List<ClientIds> clientIds = new ArrayList<ClientIds>();

		ObjectMapper mapper = new ObjectMapper();
		String sql = "SELECT * FROM  \"clientIds\" WHERE \"token\"=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, token);
		for (Map row : rows) {
			ClientIds clientId = new ClientIds();
			DateFormat df = new SimpleDateFormat("yyyy-dd-MM"); 
			Date expirationDate;
			expirationDate = (Date) row.get("expirationDate");
			String date = df.format(expirationDate);
			clientId.setExpiration_date(date);
			clientId.setGroups((String) row.get("groups"));
			clientId.setHostname((String) row.get("hostname"));
			clientId.setName((String) row.get("name"));
			clientId.setToken((String) row.get("token"));
			clientIds.add(clientId);
			
		}
		return clientIds;
	}
	
	
	public List<String> getGeneratedTokenGroups(String token)
	{
		String sql = "SELECT * FROM \"clientIds\" WHERE token=?";

		List<String> groups = new ArrayList<String>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, token);
		for (Map row : rows) {
			
			 String group = (String) row.get("groups");
			
			 if(group.length() > 0)
			 {
				 if (group.contains(","))
					 groups = Arrays.asList(group.split("\\s*,\\s*"));
				 else
					 groups.add(group);
			 }
			 
			 
			 return groups;
		}
		
		return null;
	}
	
	public void deleteClientId(String token) {
		String deleteStatement = "DELETE FROM \"clientIds\" WHERE token=?";
		try {
			jdbcTemplate.update(deleteStatement, token);
		} catch (RuntimeException runtimeException) {

			throw runtimeException;
		}
	}

	public List<User> getUsersForRegistration() {
		String sql = "SELECT * FROM users";

		List<User> users = new ArrayList<User>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			User user = new User();
			user.setUidNumber(String.valueOf((int) (row.get("id"))));
			user.setUsername((String) row.get("username"));
			user.setUserPassword((String) row.get("password"));
			user.setFullName((String) row.get("fullname"));
			user.setLastName((String) row.get("fullname"));
			user.setEmail((String) row.get("email"));

			user.setUserPassword((String) row.get("password"));
			user.setHomeDirectory(homeDirectory + "/" + (String) row.get("username"));
			user.setGidNumber(gidNumber);
			PgArray groupsArray = (PgArray) row.get("group");
			String[] str_groups;
			try {
				str_groups = (String[]) groupsArray.getArray();
				List<String> groups = Arrays.asList(str_groups);
				user.setGroups(groups);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			users.add(user);
		}

		return users;
	}

	public User getUserForRegistration(String username) {
		String sql = "SELECT * FROM users WHERE username=?";

		User fullUser = new User();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username);
		for (Map row : rows) {
			User user = new User();
			user.setUidNumber(String.valueOf((int) (row.get("id"))));
			user.setUsername((String) row.get("username"));
			user.setUserPassword((String) row.get("password"));
			user.setFullName((String) row.get("fullname"));
			user.setLastName((String) row.get("fullname"));
			user.setUserPassword((String) row.get("password"));
			user.setHomeDirectory(homeDirectory);
			user.setGidNumber(gidNumber);
			PgArray groupsArray = (PgArray) row.get("group");
			String[] str_groups;
			try {
				str_groups = (String[]) groupsArray.getArray();
				List<String> groups = Arrays.asList(str_groups);
				user.setGroups(groups);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			fullUser = user;
			fullUser.setHomeDirectory(homeDirectory + "/" + fullUser.getUsername()); 
		}

		return fullUser;
	}

	public void deleteUser(String id) {
		String deleteStatement = "DELETE FROM users WHERE id=?";
		try {
			jdbcTemplate.update(deleteStatement, Integer.parseInt(id));
		} catch (RuntimeException runtimeException) {

			throw runtimeException;
		}
	}

}
