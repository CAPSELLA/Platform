//package gr.uoa.di.madgik.publicfood.config;
//
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.ParticipantEntity;
//import gr.uoa.di.madgik.publicfood.model.AllergyEntity;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "limesurveyEntityManagerFactory",
//        transactionManagerRef = "limesurveyTransactionManager",
//        basePackages = { "gr.uoa.di.madgik.publicfood.limesurvey.limesurvey_repository" })
//
//public class LimeSurveyDatabaseConfig {
//
//    @Bean(name = "limesurveyDataSource")
//    @ConfigurationProperties(prefix="limesurvey.datasource")
//    public DataSource limesurveyDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
////    @Bean(name = "limesurveyEntityManagerFactory")
////    public LocalContainerEntityManagerFactoryBean limesurveyEntityManagerFactory(
////            EntityManagerFactoryBuilder builder,
////            @Qualifier("limesurveyDataSource") DataSource limesurveyDataSource) {
////        return builder
////                .dataSource(limesurveyDataSource)
////                .packages("gr.uoa.di.madgik.publicfood.limesurvey.limesurvey_model")
////                .persistenceUnit("limesurvey")
////                .build();
////    }
//
//    @Bean(name = "limesurveyEntityManagerFactory")
//    LocalContainerEntityManagerFactoryBean customerEntityManagerFactory() {
//
//        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setGenerateDdl(true);
//
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//
//        factoryBean.setDataSource(limesurveyDataSource());
//        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
//        factoryBean.setPersistenceUnitName("limesurvey");
//        factoryBean.setPackagesToScan(ParticipantEntity.class.getPackage().getName());
//
//        return factoryBean;
//    }
//
//    @Bean(name = "limesurveyTransactionManager")
//    public PlatformTransactionManager limesurveyTransactionManager(
//            @Qualifier("limesurveyEntityManagerFactory") EntityManagerFactory limesurveyEntityManagerFactory) {
//        return new JpaTransactionManager(limesurveyEntityManagerFactory);
//    }
//
//}