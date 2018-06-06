package gr.uoa.di.madgik.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gr.uoa.di.madgik.config.Hash;
import gr.uoa.di.madgik.model.Group;
import gr.uoa.di.madgik.model.User;
import gr.uoa.di.madgik.repo.GroupRepo;
import gr.uoa.di.madgik.repo.GroupRepoImpl;
import gr.uoa.di.madgik.service.GroupService;
import gr.uoa.di.madgik.service.UserRegistrationService;
import gr.uoa.di.madgik.service.UserService;

@RestController
public class AdminController {
	
	private static Logger logger = LoggerFactory.getLogger(AdminController.class);


	@Autowired
	private UserService ldapUserService;

	@Autowired
	private UserRegistrationService userDAO;
	
	@Autowired
	private GroupService groupDAO;
	
	@Autowired
	private GroupRepo groupRepo;
	
	@Autowired
	private GroupRepoImpl groupRepoImpl;
	
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Group> createGroup(@RequestParam (value = "groupName") String groupName, @RequestParam(value = "groupRights") List<String> groupRights) {
		
		
		Group group = groupDAO.createGroup(groupName, groupRights);
	//	Group g = ldapUserService.createGroup(group);
		groupRepoImpl.create(group);
		return new ResponseEntity<Group>(group, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Group> update(@RequestBody Group group) {
		
	    groupDAO.update(group);
	    ldapUserService.updateGroup(group);
	    
		return new ResponseEntity<Group>(group, HttpStatus.OK);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<User>> users() {
		return new ResponseEntity<List<User>>(userDAO.getUsersForRegistration(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/registeredUsers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<User>> registeredUsers() {
		List<User> users =   new ArrayList<>(); //ldapUserService.findAll();
		List<String> userss = groupRepoImpl.getAllUsers();
		
		for(String s : userss)
		{
			User u = ldapUserService.findOneByUsername(s);
			if(u != null)
				users.add(u);
		}
		
		for(User u: users)
		{
			u.setNewUsername(u.getUsername());
			u.getGroups(groupRepo, groupRepoImpl, groupDAO);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.CREATED);
	}

	
	@RequestMapping(value = "/getGroups", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<String>> getGroups() {
		return new ResponseEntity<List<String>>(groupRepo.getAllGroupNames(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getFullGroups", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Group>> getFullGroups() {
		return new ResponseEntity<List<Group>>(groupDAO.getAllGroups(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteGroup(@RequestParam String name) {
		
		groupDAO.delete(name);
		Group g = ldapUserService.getUserGroup(name);
		groupRepo.delete(g);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/acceptUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> acceptUser(@RequestBody User user) {
		ldapUserService.createUser(user);
		userDAO.deleteUser(user.getUidNumber());
		ResponseEntity<?> geoResponse = ldapUserService.createGeoserverUser(user);
		logger.info(geoResponse.toString());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		ldapUserService.updateUser(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		ldapUserService.deleteUser(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/logOutUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> logOutUser(@RequestBody User user) {
		userDAO.deteleUserToken(user.getUsername());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/createUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		if (ldapUserService.findOneByUsername(user.getUsername()) != null) {
			throw new RuntimeException("Username already exist");
		}
		String generatedSecuredPasswordHash;
		try {
			generatedSecuredPasswordHash = Hash.hashMD5Password(user.getUserPassword());
			user.setUserPassword(generatedSecuredPasswordHash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userDAO.insertUser(user, user.getGroups());
		user = userDAO.getUserForRegistration(user.getUsername());
		ldapUserService.createUser(user);
		userDAO.deleteUser(user.getUidNumber());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<User> deleteUser(@PathVariable String id) {
		userDAO.deleteUser(id);
		return new ResponseEntity<User>(HttpStatus.OK);

	}
	
	
	
	@RequestMapping(value = "/admin/users", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> registerAppUser(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestHeader("Authorization") String token) {
	    String tokenValue = token.substring(7);
	    
	    User user = new User();
	    List<String> groups = new ArrayList<>() ;
	    
	    
		if (ldapUserService.findOneByUsername(username) != null) {
			throw new RuntimeException("Username already exist");
		}
		if(userDAO.getGeneratedTokenGroups(tokenValue) != null)
		{
			groups = userDAO.getGeneratedTokenGroups(tokenValue);
			System.out.println("the size of groups:"+groups.size());
			
		}
		
		String generatedSecuredPasswordHash;
		try {
			generatedSecuredPasswordHash = Hash.hashMD5Password(password);
			user.setUserPassword(generatedSecuredPasswordHash);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //  BCrypt.hashpw(password, BCrypt.gensalt(12));
		user.setUsername(username);
		user.setGroups(groups);
		user.setFullName(name);
		user.setLastName(name);
	
		userDAO.insertUser(user, user.getGroups());
		user = userDAO.getUserForRegistration(username);
		userDAO.deleteUser(user.getUidNumber());

		ldapUserService.createUser(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/admin/user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> registerOneUser(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
	    
	    User user = new User();
	    List<String> groups = new ArrayList<>() ;
	    
	    
		if (ldapUserService.findOneByUsername(username) != null) {
			throw new RuntimeException("Username already exist");
		}
//		if(userDAO.getGeneratedTokenGroups(tokenValue) != null)
//		{
//			groups = userDAO.getGeneratedTokenGroups(tokenValue);
//			System.out.println("the size of groups:"+groups.size());
//			
//		}
		
	//	String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
		
		String generatedSecuredPasswordHash;
		try {
			generatedSecuredPasswordHash = Hash.hashMD5Password(password);
			user.setUserPassword(generatedSecuredPasswordHash);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUsername(username);
		user.setFullName(name);
		user.setLastName(name);

	
		userDAO.insertUser(user, user.getGroups());
		user = userDAO.getUserForRegistration(username);
		userDAO.deleteUser(user.getUidNumber());
		user.setName(user.getUsername());

		ldapUserService.createUser(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	

}
