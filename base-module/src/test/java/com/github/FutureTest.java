package com.github;

import java.util.concurrent.*;

public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            return "aaa";
        });
        System.out.println(submit.get());
    }

}
