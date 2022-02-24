package edu.shmonin.university.controller.formatter;

import edu.shmonin.university.model.Course;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class CourseFormatter implements Formatter<Course> {

    @Override
    public Course parse(String text, Locale locale) throws ParseException {
        var course = new Course();
        if (text != null) {
            String[] parts = text.split(",");
            course.setId(Integer.parseInt(parts[0]));
        }
        return course;
    }

    @Override
    public String print(Course course, Locale locale) {
        return course.toString();
    }
}