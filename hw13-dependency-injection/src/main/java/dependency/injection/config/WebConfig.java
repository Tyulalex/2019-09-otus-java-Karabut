package dependency.injection.config;

import dependency.injection.repository.cache.HwCache;
import dependency.injection.repository.cache.HwListener;
import dependency.injection.repository.cache.impl.CacheImpl;
import dependency.injection.repository.db.api.dao.UserDao;
import dependency.injection.repository.db.api.model.Address;
import dependency.injection.repository.db.api.model.Phone;
import dependency.injection.repository.db.api.model.User;
import dependency.injection.repository.db.api.service.DbServiceUser;
import dependency.injection.repository.db.hibernateimpl.service.CacheDbServiceUserImpl;
import dependency.injection.repository.db.hibernateimpl.service.DbServiceUserImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Arrays;

@Slf4j
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "dependency.injection")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private static StandardServiceRegistry createServiceRegistry(org.hibernate.cfg.Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }

    @Bean
    public HwCache<String, User> cache() {
        HwCache<String, User> cache = new CacheImpl<>();
        HwListener<String, User> listener =
                (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(listener);

        return cache;
    }

    @Bean
    public DbServiceUser cacheDbServiceUser(UserDao userDao, HwCache<String, User> cache) {
        return new CacheDbServiceUserImpl(new DbServiceUserImpl(userDao), cache);
    }

    @Bean
    public SessionFactory buildSessionFactory() {
        Class[] annotatedClasses = new Class[]{User.class, Address.class, Phone.class};
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
        Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);
        Metadata metadata = metadataSources.getMetadataBuilder().build();

        return metadata.getSessionFactoryBuilder().build();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/static/");
    }*/

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/img/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/WEB-INF/static/img/",
                        "classpath:/WEB-INF/static/css/",
                        "classpath:/WEB-INF/static/js/");
    }
}
