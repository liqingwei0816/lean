package com.github;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class ControllerAsync {
    private SingleThreadPool singleThreadPool;
    @RequestMapping("async")
    public void return1(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        if (singleThreadPool==null){
            singleThreadPool = new SingleThreadPool();

        }
        singleThreadPool.submit(request,response);
    }

}
