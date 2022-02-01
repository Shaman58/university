package config;

import edu.shmonin.university.controller.CourseFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan("edu.shmonin.university.controller")
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver getTemplateResolver() {
        var templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/views/");
        templateResolver.setSuffix(".html");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine getTemplateEngine() {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(getTemplateResolver());
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        var resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(getTemplateEngine());
        registry.viewResolver(resolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        var groupFormatter = new CourseFormatter();
        registry.addFormatter(groupFormatter);
    }
}