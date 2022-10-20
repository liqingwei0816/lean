package com.example.consumer;

import com.example.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDubbo
public class ConsumerApplication {
    @DubboReference
    private DemoService demoService;


    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(ConsumerApplication.class, args);

        //applicationContext.getBean(ConsumerApplication.class).sayHello("aaaaaa");
    }

    private void sayHello(String name) {
        AtomicInteger atomic80 = new AtomicInteger(0);
        AtomicInteger atomic90 = new AtomicInteger(0);
        AtomicInteger atomic8080 = new AtomicInteger(0);
        IntStream.range(1,10000).parallel().forEach((i)->{
            String s = demoService.sayHello(name);
            if (s.contains("80")){
                atomic80.incrementAndGet();
            }else if(s.contains("90")){
                atomic90.incrementAndGet();
            }else {
                atomic8080.incrementAndGet();
            }
        } );
        System.out.println(atomic80.get());
        System.out.println(atomic90.get());
        System.out.println(atomic8080.get());
    }

}
