package com.idat.continua2.demo;

import com.idat.continua2.demo.config.CompanyProperties;
import com.idat.continua2.demo.config.ComprobanteProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CompanyProperties.class, ComprobanteProperties.class})
public class Continua2Application {

    public static void main(String[] args) {
        SpringApplication.run(Continua2Application.class, args);
    }
}