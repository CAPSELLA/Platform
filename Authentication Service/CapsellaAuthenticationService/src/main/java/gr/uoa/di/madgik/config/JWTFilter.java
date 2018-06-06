package gr.uoa.di.madgik.config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import gr.uoa.di.madgik.model.ClientIds;
import gr.uoa.di.madgik.service.UserRegistrationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JWTFilter extends GenericFilterBean {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORITIES_KEY = "roles";
	private static String secretKey = "&^%ERTYUIJO&^%FTGYHUJ";
	
	private UserRegistrationService userDAO;


	
	@Autowired
	public JWTFilter(UserRegistrationService userDAO)
	{
		this.userDAO = userDAO;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
		} else {
			try {
				
				

		        // Get client's IP address
		        String ipAddress = request.getHeader("clientIp"); 
		        System.out.println("Ip address:::::: " + ipAddress);
				String token = authHeader.substring(7);
				List<ClientIds> clientIds = userDAO.checkToken(token);
				if(!clientIds.isEmpty() && ipAddress!= null)
				{
			        System.out.println("Hostname:::::: " + clientIds.get(0).getHostname());

//					if(!clientIds.get(0).getHostname().equals(ipAddress))
//						((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unacceptable IP address.");
				}
				
				Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
				request.setAttribute("claims", claims);
				SecurityContextHolder.getContext().setAuthentication(getAuthentication(claims));
				filterChain.doFilter(req, res);
			} catch (SignatureException e) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			}

		}
	}

	/**
	 * Method for creating Authentication for Spring Security Context Holder
	 * from JWT claims
	 * 
	 * @param claims
	 * @return
	 */
	public Authentication getAuthentication(Claims claims) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		List<String> roles ;
		if (claims.get(AUTHORITIES_KEY) instanceof List<?>){
			roles = (List<String>) claims.get(AUTHORITIES_KEY);

		}
		else
		{
			roles = new ArrayList<String>();
			roles.add((String) claims.get(AUTHORITIES_KEY));
		}
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		User principal = new User(claims.getSubject(), "", authorities);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				principal, "", authorities);
		return usernamePasswordAuthenticationToken;
	}
	
	private static String getClientHost(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-HOST");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
