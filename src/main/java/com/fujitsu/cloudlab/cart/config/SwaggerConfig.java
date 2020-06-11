package com.fujitsu.cloudlab.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Cart Writer")
        .description("The CartWriter API enables consumers to add offers to the cart")
        .termsOfServiceUrl("")
        .version("1.0.1")
        .contact(new Contact("", "", ""))
        .build();
  }

  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.fujitsu.cloudlab.cart.api"))
        .build()
        .apiInfo(apiInfo());
  }
}
