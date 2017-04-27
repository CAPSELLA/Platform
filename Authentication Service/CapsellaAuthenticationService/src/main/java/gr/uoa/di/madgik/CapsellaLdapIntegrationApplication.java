package gr.uoa.di.madgik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathBeanPostProcessor;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.repository.config.EnableLdapRepositories;

import gr.uoa.di.madgik.repo.GroupRepoImpl;
import gr.uoa.di.madgik.service.UserService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication()
@EnableLdapRepositories()
public class CapsellaLdapIntegrationApplication extends SpringBootServletInitializer {

	@Value("${ldap.contextSource.url}")
	private String ldapUrl;
	@Value("${ldap.contextSource.base}")
	private String ldapBase;
	@Value("${ldap.contextSource.userDn}")
	private String ldapUserDN;
	@Value("${ldap.contextSource.password}")
	private String ldapPassword;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupRepoImpl impl;


	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapsellaLdapIntegrationApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(CapsellaLdapIntegrationApplication.class, args);
	}
	
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }

	@Bean
	public BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor() {
		BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor = new BaseLdapPathBeanPostProcessor();
		baseLdapPathBeanPostProcessor.setBaseLdapPathSourceName("LdapContextSource");
		return baseLdapPathBeanPostProcessor;
	}

	@Bean
	public LdapContextSource getContextSource() throws Exception {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(ldapUrl);
		ldapContextSource.setBase(ldapBase);
		ldapContextSource.setUserDn(ldapUserDN);
		ldapContextSource.setPassword(ldapPassword);
		return ldapContextSource;
	}

	@Bean
	public LdapTemplate ldapTemplate() throws Exception {
		LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
		ldapTemplate.setIgnorePartialResultException(true);
		ldapTemplate.setContextSource(getContextSource());
		return ldapTemplate;
	}

}
