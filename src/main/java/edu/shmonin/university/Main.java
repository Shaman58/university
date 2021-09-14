package edu.shmonin.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(SpringConfig.class);
        var menu = context.getBean("menuRunner", MenuRunner.class);
        menu.run();
    }
}