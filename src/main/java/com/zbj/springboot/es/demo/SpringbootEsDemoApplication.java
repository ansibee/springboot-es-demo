package com.zbj.springboot.es.demo;

import com.zbj.springboot.es.demo.config.ElasticSearchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ElasticSearchConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class SpringbootEsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEsDemoApplication.class, args);
    }

}
