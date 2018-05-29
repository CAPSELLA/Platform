package gr.uoa.di.madgik.publicfood.config;

import gr.uoa.di.madgik.publicfood.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import gr.uoa.di.madgik.publicfood.security.JwtAuthenticationEntryPoint;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;



    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(this.userDetailsService)
//                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                // ignoring the "/", "/index.html", "/app/**", "/register",
                // "/favicon.ico"
                .antMatchers("/swagger-ui.html", "/webjars/springfox-swagger-ui/css/typography.css",
                        "/webjars/springfox-swagger-ui/css/reset.css","/webjars/springfox-swagger-ui/css/screen.css",
                        "/webjars/springfox-swagger-ui/lib/object-assign-pollyfill.js", "/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js",
                        "/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js", "/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js",
                        "/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js", "/webjars/springfox-swagger-ui/lib/handlebars-4.0.5.js",
                        "/webjars/springfox-swagger-ui/lib/lodash.min.js", "/webjars/springfox-swagger-ui/lib/backbone-min.js",
                        "/webjars/springfox-swagger-ui/swagger-ui.min.js", "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack.js",
                        "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack_extended.js", "/webjars/springfox-swagger-ui/lib/jsoneditor.min.js",
                        "/webjars/springfox-swagger-ui/lib/marked.js", "/webjars/springfox-swagger-ui/lib/swagger-oauth.js",
                        "/webjars/springfox-swagger-ui/springfox.js", "/webjars/springfox-swagger-ui/images/logo_small.png",
                        "/webjars/springfox-swagger-ui/css/print.css", "/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js",
                        "/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js", "/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js",
                        "/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js", "/webjars/springfox-swagger-ui/lib/handlebars-4.0.5.js",
                        "/webjars/springfox-swagger-ui/lib/lodash.min.js", "/webjars/springfox-swagger-ui/lib/backbone-min.js",
                        "/webjars/springfox-swagger-ui/swagger-ui.min.js", "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack.js",
                        "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack_extended.js", "/webjars/springfox-swagger-ui/lib/jsoneditor.min.js",
                        "webjars/springfox-swagger-ui/lib/marked.js", "/webjars/springfox-swagger-ui/lib/swagger-oauth.js",
                        "/webjars/springfox-swagger-ui/springfox.js", "/webjars/springfox-swagger-ui/images/logo_small.png",
                        "/webjars/springfox-swagger-ui/images/favicon-16x16.png","/webjars/springfox-swagger-ui/fonts/DroidSans-Bold.ttf",
                        "/webjars/springfox-swagger-ui/fonts/DroidSans.ttf", "/swagger-resources/configuration/ui", "/swagger-resources",
                        "/v2/api-docs", "/swagger-resources/configuration/security",  "/reset.css", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .authorizeRequests().antMatchers("/domain/**").authenticated()
//                .and()
//                .authorizeRequests().antMatchers("/**").permitAll()
                .authorizeRequests()
                    .antMatchers("/", "/index.html", "/questionnaire", "/ldapLogin","/datasets/**","/charts2/**", "/login/**","/auth/**", "/main/**","/assets/**", "/charts/**" ,"/menu/**", "/newsfeed/**", "/domain/**", "/kid/**", "/menu/**", "/user/**", "/insert/**", "/weeklyQuestionnaire/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterBefore(new WebSecurityCorsFilter(), ChannelProcessingFilter.class);
        ;

        //                    .antMatchers("/", "/index.html",  "/ldapLogin","/datasets/**","/charts2/**", "/login/**","/auth/**", "/main/**","/assets/**").permitAll()

        // Custom JWT based security filter
        httpSecurity
                    .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }

    public class WebSecurityCorsFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
            res.setHeader("Access-Control-Max-Age", "3600");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, providerId, Domain, Content-Type, Accept, x-requested-with, Cache-Control");
            chain.doFilter(request, res);
        }
        @Override
        public void destroy() {
        }
    }
}