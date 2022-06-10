package com.atguigu.servicebase;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //swagger注解
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        /**
         * groupName 分组
         * apiInfo 编辑文档的信息
         *
         * paths 当接口路径中包含admin或者error 就不显示
         */
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                //.paths(Predicates.not(PathSelectors.regex("/admin/.*"))) // 路径中存在admin，不会显示
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
    public ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("网站-课程中心API文档")
                .description("本文档描述了课程中心微服务接口定义")
                .version("1.0")
                .contact(new Contact("Helen","http://atguigu.com","2560214456@qq.com"))
                .build();
    }
}
