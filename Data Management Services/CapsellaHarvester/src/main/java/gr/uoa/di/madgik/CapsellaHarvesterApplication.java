package gr.uoa.di.madgik;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableScheduling
public class CapsellaHarvesterApplication  extends SpringBootServletInitializer  {

	
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(CapsellaHarvesterApplication.class);
	    }    
	
	public static void main(String[] args) {
		SpringApplication.run(CapsellaHarvesterApplication.class, args);
	}
	
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();
//          .globalOperationParameters(
//              newArrayList(new ParameterBuilder()
//                  .name("header")
//                  .description("Description of header")
//                  .modelRef(new ModelRef("string"))
//                  .parameterType("header")
//                  .required(true)
//                  .build()));                                           
    }

	
	
	
}
