package com.github;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.*;

@Slf4j
public class SingleThreadPool {

    private  ExecutorService executorService;
    //public  BlockingQueue<TaskController> queue;

   /* public  void addTask(TaskController task){
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

   /* public SingleThreadPool(BlockingQueue<TaskController> queue) {
        this.queue = queue;

        while (true){
            TaskController task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            HttpServletRequest request = task.getRequest();
            HttpServletResponse response = task.getResponse();
            try (PrintWriter writer = response.getWriter()) {
                String name = request.getParameter("name");
                writer.write("hello " + name);
                writer.flush();
                log.error("hello " + name);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("获取response输出流失败");
            }
        }

    }*/


    public  void submit(HttpServletRequest request,
                              HttpServletResponse response) {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }

        Future<String> submit = executorService.submit(() -> {
            try (PrintWriter writer = response.getWriter()) {
                String name = request.getParameter("name");
                writer.println("hello " + name);
                writer.flush();
                log.error("hello " + name);
                return "hello " + name;
            }

        });
        try {
            submit.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

    }


}
