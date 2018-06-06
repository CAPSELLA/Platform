package gr.uoa.di.madgik.config;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.jsonwebtoken.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;    

public class Token {
	
	private static long ttlMillis = 60000 * 60 * 24 * 10;
	private static long ttlMillisClientId = 1000 * 60  *24 * 345 * 2 ;

	private static String secretKey = "&^%ERTYUIJO&^%FTGYHUJ";

	
	//Sample method to construct a JWT
	public static  String createJWT(List<String> roles, String username) {
	 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder()
	                                .setIssuedAt(now)
	                                .setSubject(username)
	                                .claim("roles",roles)
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	
	//Sample method to construct a JWT
		public static  ArrayList<Object> createJWTClientId(String roles, String name, String hostname) {
		 
			ArrayList<Object> values = new ArrayList<Object>();
			
		    //The JWT signature algorithm we will be using to sign the token
		    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		 
		    long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis);
		 
		    //We will sign our JWT with our ApiKey secret
		    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		 
		    //Let's set the JWT Claims
		    JwtBuilder builder = Jwts.builder()
		                                .setIssuedAt(now)
		                                .setSubject(name)
		                                .claim("roles",roles)
		                                .setIssuer(hostname)
		                                .signWith(signatureAlgorithm, signingKey);
		 
		    //if it has been specified, let's add the expiration
		    if (ttlMillisClientId >= 0) {
		    long expMillis = nowMillis + ttlMillisClientId;
		        Date exp = new Date();
		        exp.setYear(exp.getYear() + 5);
		        values.add(exp);
		        builder.setExpiration(exp);
		    }
		 
		    values.add(builder.compact());
		    //Builds the JWT and serializes it to a compact, URL-safe string
		    return values;
		}
	
	
	//Sample method to validate and read the JWT
	public static List<String> parseJWT(String jwt) {
	 
		List<String> values = new ArrayList<String>();
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
	       .parseClaimsJws(jwt).getBody();
	    System.out.println("Username: " + claims.getSubject());
	    System.out.println("Expiration: " + claims.getExpiration());
	    
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    
	    String date = df.format(claims.getExpiration());
	    
	    values.add(claims.getSubject());
	    values.add(date);
	    
	    return values;
	    
	}

}
