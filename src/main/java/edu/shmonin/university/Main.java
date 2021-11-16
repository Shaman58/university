package edu.shmonin.university;

import config.ApplicationConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) {
        var logger = LoggerFactory.getLogger(Main.class);
        logger.info("Starting application");
        var context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var menu = context.getBean("menuRunner", MenuRunner.class);
        menu.run();
    }
}