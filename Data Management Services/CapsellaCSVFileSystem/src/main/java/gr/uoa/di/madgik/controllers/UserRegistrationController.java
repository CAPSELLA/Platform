package gr.uoa.di.madgik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationController {

	@Autowired
	JdbcTemplate template;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<?> saveJSONFile(@RequestParam String username, @RequestParam String password,
			@RequestParam String fullname) throws Exception {

		String sql = "INSERT INTO users(username, password,fullname) VALUES (?,?,?)";

		template.update(sql, new Object[] { username, password, fullname });

//		template.batchUpdate("INSERT INTO users(username, password,fullname) VALUES (?,?,?)", username, password,
//				fullname);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
