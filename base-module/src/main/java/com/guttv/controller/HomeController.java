package com.guttv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping({"/","index"})
    public String index() {
        return "index";
    }
    @RequestMapping({"loginPage"})
    public String login() {
        return "login";
    }

   /* @RequestMapping({"error"})
    @ResponseBody
    public String error() {
        return "error";
    }
*/

}
