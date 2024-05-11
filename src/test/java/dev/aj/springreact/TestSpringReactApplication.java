package dev.aj.springreact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringReactApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringReactApplication::main).with(TestSpringReactApplication.class).run(args);
    }

}
