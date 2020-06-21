package com.example;

import com.example.demo.Demo1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);


        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();


        System.out.println(contextClassLoader);
        System.out.println(Demo1.class.getClassLoader());
        System.out.println(System.getProperty("java.home"));
        String source="package com.example;\n" +
                "\n" +
                "import com.example.demo.Demo1;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "public class Hello {\n" +
                "\n" +
                "    private String aaa;\n" +
                "     public static void Hello(){\n" +
                "         ArrayList<Object> objects = new ArrayList<>();\n" +
                "         Demo1.satHi();\n" +
                "     }\n" +
                "\n" +
                "}";
        String className="com.example.Hello";


        new CompilerUtil().compileOne(className,source);

    }

}
