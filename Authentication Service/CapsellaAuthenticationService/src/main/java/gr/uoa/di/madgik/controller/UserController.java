 package gr.uoa.di.madgik.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gr.uoa.di.madgik.config.Hash;
import gr.uoa.di.madgik.config.Token;
import gr.uoa.di.madgik.model.ClientIds;
import gr.uoa.di.madgik.model.User;
import gr.uoa.di.madgik.repo.GroupRepo;
import gr.uoa.di.madgik.repo.GroupRepoImpl;
import gr.uoa.di.madgik.service.GroupService;
import gr.uoa.di.madgik.service.UserRegistrationService;
import gr.uoa.di.madgik.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController

public class UserController {


	@Autowired
	private UserService ldapUserService;

	@Autowired
	private GroupRepoImpl groupRepoImpl;
	
	@Autowired
	private GroupRepo groupRepo;
	
	@Autowired
	private UserRegistrationService userDAO;
	
	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/usernames", method = RequestMethod.GET)

	public @ResponseBody List<User> getUserNameList() {
		Iterable<User> users = ldapUserService.findAll();
		List<User> usersList = new ArrayList<User>();
		for (User u : users) {
			usersList.add(u);
		}

		return usersList;
	}

	@RequestMapping(value="/getUser", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getUser(@RequestParam(value = "uid") String uid) {
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		tokenMap.put("user", ldapUserService.findOneByUsername(uid));
		return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getUserGroups", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> getUserGroups(@RequestHeader("Authorization") String authHeader) {
		
		List<String> groups ;
		String token = authHeader.substring(7);
		
		if(userDAO.getGeneratedTokenGroups(token) != null)
		{
			groups = userDAO.getGeneratedTokenGroups(token);
		}
		else
		{
			ResponseEntity<String> username = authorizationbyUsename(authHeader);
		
			groups = groupRepoImpl.getUserGroupsByUsername(username.getBody());
		}
		
		return new ResponseEntity<List<String>>(groups, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getUserClientIds", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<ClientIds>> getUserClientIds(@RequestParam(value = "username") String username) {
		
		List<ClientIds> groups = userDAO.getUserTokens(username);
		
		return new ResponseEntity<List<ClientIds>>(groups, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteClientId", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> deleteClientId(@RequestParam(value = "clientIdToken") String token) {
		
		userDAO.deleteClientId(token);
		
		return new ResponseEntity<>( HttpStatus.OK);
	}
	
	@RequestMapping(value = "/generateToken", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> generateToken(@RequestParam(value = "name") String name, @RequestParam(value = "hostname") String hostname, @RequestParam(value = "groups") String groups, @RequestParam(value = "user") String user) {
		
		//List<String> groups = groupRepoImpl.getUserGroupsByUsername(username);
		userDAO.generateToken(name, hostname, groups, user);
		return new ResponseEntity<List<String>>( HttpStatus.OK);
	}
	

	/**
	 * This method is used for user registration. Note: user registration is not
	 * require any authentication.
	 * 
	 * @param appUser
	 * @return
	 */
	
	
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "user", value = "{" +
		" \n\"fullName\":\"Emilia Clark\", \n"+
        "\"username\":\"eclark\", \n" +
       "\"userPassword\":\"pass\", \n"+
		"\n\"groups\":[\n\"capsella\" \n ]"+
		"\n}")
	   
	  })
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (ldapUserService.findOneByUsername(user.getUsername()) != null) {
			return new ResponseEntity<User>( HttpStatus.CONFLICT);
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
		} /// BCrypt.hashpw(user.getUserPassword(), BCrypt.gensalt(12));
		userDAO.insertUser(user, user.getGroups());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/authorizationbyUsename", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> authorizationbyUsename(@RequestHeader("Authorization") String token ) {
		
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    
	    String tokenValue = token.substring(7);

		List<String> auth = Token.parseJWT(tokenValue);
		try {
			Date date = df.parse(auth.get(1));
			Date now = new Date();
			
			if (date.before(now))
			{
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if(!auth.get(0).equals(username))
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
		
		return new ResponseEntity<>(auth.get(0), HttpStatus.OK);
		
	}

	/**
	 * @param username
	 * @param password
	 * @param response
	 * @return JSON contains token and user after success authentication.
	 * @throws IOException
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password
			) throws IOException {
		String token = null;
		User user = ldapUserService.findOneByUsername(username);
		Map<String, Object> tokenMap = new HashMap<String, Object>();
	
		if(user != null)
		{
			//String p = user.getUserPasswordASCII();
			

//			
			if ( groupRepoImpl.authenticate(username, password)) {
	//		if ( hashedPass.equals(user.getUserPasswordASCII())) {
				user.getRoles(groupRepo, groupRepoImpl, groupService);
//				token = Jwts.builder().setSubject(username).claim("roles", user.getGroups()).setIssuedAt(new Date())
//						.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
				token = Token.createJWT(user.getGroups(), username);
				Token.parseJWT(token);
				tokenMap.put("token", token);
				tokenMap.put("user", user);
				
				userDAO.insertUserToken(user, token);
				
				return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
			} else {
				tokenMap.put("token", null);
				return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
			}
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.INTERNAL_SERVER_ERROR);
//			}  
		}
		else
		{
			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.NOT_FOUND);
		}
	

	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> changePassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword
	) throws IOException {
		String token = null;
		User user = ldapUserService.findOneByUsername(username);
		Map<String, Object> tokenMap = new HashMap<String, Object>();

		if(user != null)
		{

			if ( groupRepoImpl.authenticate(username, password)) {

				user.setNewPassword(newPassword);
				User user1 = ldapUserService.updateUser(user);
				return new ResponseEntity<User>(user1, HttpStatus.OK);
			}
			else{
				tokenMap.put("token", null);
				return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.NOT_FOUND);

			}

		}
		else
		{
			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.NOT_FOUND);
		}


	}
	
	
	

}
