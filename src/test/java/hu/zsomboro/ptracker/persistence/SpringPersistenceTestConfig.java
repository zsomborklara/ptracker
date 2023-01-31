package hu.zsomboro.ptracker.persistence;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "hu.zsomboro.ptracker.persistence" })
public class SpringPersistenceTestConfig {

}