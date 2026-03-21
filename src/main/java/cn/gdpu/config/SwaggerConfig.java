package cn.gdpu.config;

import io.swagger.models.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig{
    /**
     * 创建一个Docket对象
     * 调用select()方法，
     * 生成ApiSelectorBuilder对象实例，该对象负责定义外漏的API入口
     * 通过使用RequestHandlerSelectors和PathSelectors来提供Predicate，在此我们使用any()方法，将所有API都通过Swagger进行文档管理
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()//RequestHandlerSelectors.any()
                .apis(RequestHandlerSelectors.basePackage("cn.gdpu.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("SpringBoot Swagger")
                //简介
                .description("<h1 style='color: red;'>略略略!</h1><br/><img src=\"http://ww4.sinaimg.cn/bmiddle/6af89bc8gw1f8ov85w0m9g2032037jrd.gif\"/>")
                //服务条款
                .termsOfServiceUrl("")
                //作者个人信息
                .contact("965504148@qq.com-pallidlight同学")
                //版本
                .version("2.0")
                .build();
    }

}
