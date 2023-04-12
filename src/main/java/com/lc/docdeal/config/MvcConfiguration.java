package com.lc.docdeal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Resource(name="thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;
    @Value("${files.docservice.url.site}")
    private String documentServerHost;
    @Value("${files.docservice.url.api}")
    private String documentServerApiJs;
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>(8);
            vars.put("documentServerApiJs", documentServerHost+documentServerApiJs);
//            System.out.println( String.format(documentServerApiJs, documentServerHost));
//            System.out.println( String.format(documentServerApiJs, documentServerHost));
            // 静态参数，只取一次值
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }
}
