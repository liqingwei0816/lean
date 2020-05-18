package com.github;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class TaskController {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public TaskController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
