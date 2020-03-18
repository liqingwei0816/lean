package com.github;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test3 {

    public static void main(String[] args) {
        Path path = Paths.get("/SampleJob1.java");
        System.out.println(path.toFile().getAbsolutePath());
    }

}
