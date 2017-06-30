package gr.uoa.di.madgik;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.catalina.connector.Connector;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
@EnableScheduling
public class CapsellaOrchestratorApiApplication extends SpringBootServletInitializer {   
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapsellaOrchestratorApiApplication.class);
    }    
	


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




	
//	
//	 @Override
//	    public Executor getAsyncExecutor() {
//	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//	        executor.setCorePoolSize(2);
//	        executor.setMaxPoolSize(2);
//	        executor.setQueueCapacity(500);
//	        executor.setThreadNamePrefix("OrchestratorLookup-");
//	        executor.initialize();
//	        return executor;
//	    }
	
	
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
	
	
	
	@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}
	
	
}