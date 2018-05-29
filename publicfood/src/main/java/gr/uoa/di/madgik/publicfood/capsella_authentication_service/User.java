package gr.uoa.di.madgik.publicfood.capsella_authentication_service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uoa.di.madgik.publicfood.facades.Facade;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class User extends Facade implements Serializable {

	private static final long serialVersionUID = -4921631937954243400L;

	private String username;

	private String fullName;

	private String email;



	private List<String> groups = new ArrayList<>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getGroups() {

		return groups;
	}

	@Override
	public JSONObject Serialize() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username",username);
		jsonObject.put("fullName",fullName);
		jsonObject.put("email",email);

		return jsonObject;
	}

	@Override
	public User Deserialize(Object payload) throws JSONException {
		JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);

		User user = new User();
		user.setEmail(jsonObject.getString("email"));
		user.setFullName(jsonObject.getString("fullName"));
		user.setUsername(jsonObject.getString("username"));

		JSONArray temp = jsonObject.getJSONArray("groups");
		int length = temp.length();
		if (length > 0) {
			List<String> groups = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				groups.add(temp.getString(i));
			}
			user.setGroups(groups);
		}
		return user;
	}

	public boolean validateDomain(String domain)
	{
		for(String s : groups)
		{
			if(s.equals(domain))
				return true;
		}

		return false;
	}
}