package edu.shmonin.university;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("edu.shmonin.university")
@PropertySource("classpath:db.properties")
public class SpringConfig {

    @Value("${db.driver}")
    private String dbDriverClass;
    @Value("${db.sourceName}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;

    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverClass);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}