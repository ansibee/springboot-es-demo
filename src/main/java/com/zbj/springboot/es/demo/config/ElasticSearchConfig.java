package com.zbj.springboot.es.demo.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/24
 * @description：
 */
@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfig {

    @Value("${host}")
    private String host;
    @Value("${port}")
    private Integer port;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client(){
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(host,port,"http")
        ));
    }

}
