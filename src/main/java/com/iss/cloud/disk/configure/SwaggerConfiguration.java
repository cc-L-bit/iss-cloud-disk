package com.iss.cloud.disk.configure;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
@EnableKnife4j
//@EnableConfigurationProperties(value = {SwaggerProperties.class})
// http://localhost:8080/disk/swagger-ui/index.html
// http://localhost:8080/disk/doc.html
public class SwaggerConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).groupName("disk").apiInfo(apiInfo()).select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.iss.cloud.disk.controller")).build();
    }

    /**
     * API信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().version("1.0").title("iss-cloud-disk API Interface document.")
                .description("本项目采用云主机构建服务器，采用javaEE企业级应用开发的B/S架构。实现云端文件系统的存储、上传、下载、删除、重命名、多媒体预览、复制、分享、回收站、用户存储空间管理等功能，并对云盘的用户和权限进行管理。")
                .contact(new Contact("周卓", "http://localhost:8080/disk", "121727838@qq.com")).build();
    }
}
