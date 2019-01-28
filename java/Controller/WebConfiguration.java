package Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
@EnableWebMvc
public class WebConfiguration extends WebMvcConfigurerAdapter implements WebMvcConfigurer{

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/main2").setViewName("main2");
        registry.addViewController("/main3").setViewName("main3");
        registry.addViewController("/main4").setViewName("main4");
        registry.addViewController("/tree").setViewName("tree");
        registry.addViewController("/modSpace").setViewName("modSpace");
        registry.addViewController("/createInf").setViewName("createInf");
        registry.addViewController("/editProf").setViewName("editProf");
        registry.addViewController("/topPages.html").setViewName("topPages");
        registry.addViewController("/createUser").setViewName("createUser");
        registry.addViewController("/chPrivileges").setViewName("chPrivileges");
        registry.addViewController("/modInf").setViewName("modInf");
        registry.addViewController("/roomOcup").setViewName("roomOcup");
        registry.addViewController("/createDevice").setViewName("createDevice");
        registry.addViewController("/createPeople").setViewName("createPeople");
        registry.addViewController("/index3.html").setViewName("index3");
        registry.addViewController("/modSpace.html").setViewName("modSpace");
        registry.addViewController("/testmain.html").setViewName("testmain");




    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
        SpringResourceTemplateResolver templateResolver
                = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }
}