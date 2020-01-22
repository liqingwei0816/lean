package com.guttv;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {

        Path path = Paths.get(".");
        System.out.println(path.toFile().getAbsolutePath());
    }

}
