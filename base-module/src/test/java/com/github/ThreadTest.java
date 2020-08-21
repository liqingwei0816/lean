package com.github;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadTest implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(10000);
        return Thread.currentThread().getName();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadTest threadTest = new ThreadTest();
        FutureTask<String> futureTask1 = new FutureTask<>(threadTest);
        Thread thread1 = new Thread(futureTask1);
        thread1.start();
        System.out.println(futureTask1.get());
    }


}
