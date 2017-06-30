package gr.uoa.di.madgik.repositories;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories
@Component
public class DatabaseConfiguration extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private Integer port ;
    @Value("${spring.data.mongodb.username}")
    private String username ;
    @Value("${spring.data.mongodb.database}")
    private String database ;
    @Value("${spring.data.mongodb.password}")
    private String password ;
    @Bean
    
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
    
    @Override
    public String getDatabaseName() {
        return database;
    }
    
	
	@Override
	public Mongo mongo() throws Exception {
		MongoCredential credential = MongoCredential.createCredential(database, username, password.toCharArray());
        return new MongoClient(new ServerAddress(host,port), Arrays.asList(credential));
	}
}