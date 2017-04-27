package gr.uoa.di.madgik.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import gr.uoa.di.madgik.model.Group;

@Component
public class GroupService {
	
	private final JdbcTemplate jdbcTemplate;
	private Group group;

	@Autowired
	public GroupService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Group createGroup(String name, List<String> groupRights) {
		String sql = "INSERT INTO groups(name, rights) VALUES (?,string_to_array(?,','))";
		
		group = new Group();
		group.getRoles(groupRights);
		
		String commaSeparatedString = String.join(",", group.getRoles());
		jdbcTemplate.update(sql,
				new Object[] { name, commaSeparatedString });
		sql = "SELECT * FROM groups WHERE name=?";

		group = new Group();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, name);
		for (Map row : rows) {
			group.setName(name);
			group.setGidNumber(String.valueOf( row.get("id")));
			
		}

		return group;
		
		

	}
	
	public void delete(String name) {
		String sql = "DELETE FROM groups WHERE name=?";
		
		
		jdbcTemplate.update(sql,
				new Object[] { name});
	}
	
	
	public void update(Group group) {
		String sql = "UPDATE public.groups SET name=?, rights= cast (? as text[]) WHERE name =?";
		
		String list = Arrays.toString(group.getRoles().toArray()).replace("[", "{").replace("]", "}");
		jdbcTemplate.update(sql,
				new Object[] {group.getNewName(), list, group.getName()});
		
	}
	
	public Group getGroup(String name)
	{
		group = new Group();
		

		String 	sql = "SELECT * FROM groups WHERE name=?";

		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, name);
		for (Map row : rows) {
			group.setName(name);
			group.setGidNumber(String.valueOf( row.get("id")));
			PgArray rightsArray = (PgArray) row.get("rights");
			String[] str_rights;
			try {
				str_rights = (String[]) rightsArray.getArray();
				List<String> groups = Arrays.asList(str_rights);
				group.setRoles(groups);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return group;
	}
	
	
	public List<Group> getAllGroups()
	{
		
		List<Group> groups = new ArrayList<Group>();

		String 	sql = "SELECT * FROM groups ";

		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map row : rows) {
			group = new Group();
			group.setName(String.valueOf( row.get("name")));
			group.setGidNumber(String.valueOf( row.get("id")));
			PgArray rightsArray = (PgArray) row.get("rights");
			String[] str_rights;
			try {
				str_rights = (String[]) rightsArray.getArray();
				List<String> privileges = Arrays.asList(str_rights);
				group.setRoles(privileges);
				group.setNewName(group.getName());
				groups.add(group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return groups;
	}

}
