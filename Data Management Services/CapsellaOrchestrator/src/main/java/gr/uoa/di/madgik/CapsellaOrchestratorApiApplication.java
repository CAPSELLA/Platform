package gr.uoa.di.madgik;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(basePackages = { "gr.uoa.di.madgik.model" }) 
@EnableJpaRepositories(basePackages = { "gr.uoa.di.madgik.repository" })
@PropertySource("classpath:application.properties")
@EnableSwagger2
@SpringBootApplication
public class CapsellaOrchestratorApiApplication extends SpringBootServletInitializer{
	


	public static void main(String[] args) {
		SpringApplication.run(CapsellaOrchestratorApiApplication.class, args);
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


	private List<Parameter> newArrayList(Parameter build) {
		List<Parameter> list = new ArrayList<Parameter>();
		list.add(build);
		return list;
	}
	
	
//	@Bean
//	public TomcatEmbeddedServletContainerFactory containerFactory() {
//	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//	     factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
//	        @Override
//	        public void customize(Connector connector) {
//	         ((AbstractHttpProtocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
//	        }
//	     });
//	     return factory;
//	}
}
