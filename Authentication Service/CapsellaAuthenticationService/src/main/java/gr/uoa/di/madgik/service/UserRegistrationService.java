package gr.uoa.di.madgik.service;

import java.sql.SQLException;
import java.text.DateFormat;
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

import gr.uoa.di.madgik.model.User;

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
		String sql = "INSERT INTO users(username, password,fullname,\"group\") VALUES (?,?,?,string_to_array(?,','))";
		String commaSeparatedString = String.join(",", roles);
		jdbcTemplate.update(sql,
				new Object[] { user.getUsername(), user.getUserPassword(), user.getFullName(), commaSeparatedString });

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
