package gr.uoa.di.madgik.publicfood.security;

import gr.uoa.di.madgik.publicfood.model.UserEntity;
import gr.uoa.di.madgik.publicfood.security.service.JwtUserDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authToken = request.getHeader(this.tokenHeader);
        int providerId = -1;
        String domain = request.getHeader("Domain");


        // authToken.startsWith("Bearer ")
        // String authToken = header.substring(7);

        if(authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
            if(request.getHeader("providerId") != null)
                providerId = Integer.valueOf(request.getHeader("providerId"));


            String providerUUID = jwtTokenUtil.getProviderUUIDFromToken(authToken);

            logger.info("checking authentication für user " + providerUUID);

            if (providerUUID != null && SecurityContextHolder.getContext().getAuthentication() == null && providerId != -1) {

                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                UserEntity user = this.jwtUserDetailsService.loadUserByID(providerUUID, providerId, domain);

                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                if (jwtTokenUtil.validateToken(authToken, user)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user " + providerUUID + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
//            else {
//                if(!jwtTokenUtil.isTokenExpired(authToken)){
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, null);
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    logger.info("authenticated user " + providerUUID + ", setting security context");
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
        }

        chain.doFilter(request, response);
    }
}