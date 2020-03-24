package com.github.dataSource;


import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface SwitchDataSource {
    String value();

}
