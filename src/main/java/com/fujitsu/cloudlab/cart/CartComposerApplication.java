package com.fujitsu.cloudlab.cart;

import com.fujitsu.cloudlab.commons.exception.ApiErrorHandler;
import com.fujitsu.cloudlab.commons.http.RestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration({RestConfig.class, ApiErrorHandler.class})
public class CartWriterApplication {

  public static void main(String[] args) {
    SpringApplication.run(CartWriterApplication.class, args);
  }
}
