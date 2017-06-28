package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
    System.out.println("Running!!!!!!!");
  }

  @Bean
  public CommandLineRunner commandLineRunner2(ApplicationContext context) {
    System.out.println("ApplicationContext Instance: " + context.getClass().getName());

    return args -> {
      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = context.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

      MyBean bean = context.getBean(MyBean.class);
      System.out.println("bean = " + bean);
    };
  }

  @Bean
  public MyBean getMyBean() {
    return new MyBean("DaValue");
  }

  public static class MyBean {
    @Nonnull
    private final String daName;

    public MyBean(@Nonnull String daName) {
      this.daName = daName;
    }

    @Nonnull
    public String getDaName() {
      return daName;
    }

    @Override
    public String toString() {
      return "MyBean{" +
        "daName='" + daName + '\'' +
        '}';
    }
  }
}