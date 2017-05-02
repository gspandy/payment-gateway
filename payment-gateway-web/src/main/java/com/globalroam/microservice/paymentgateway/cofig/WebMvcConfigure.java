package com.globalroam.microservice.paymentgateway.cofig;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @Author zhangjian
 * @Date 2017/3/22
 * @Copyright:
 * @Describe:
 */
@EnableWebMvc
@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/","/public/swagger/index.html");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/public/");
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
        registry.addResourceHandler("/resources/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/resources/");
        super.addResourceHandlers(registry);
    }


}
