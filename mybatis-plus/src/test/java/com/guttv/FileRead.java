package com.guttv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileRead {

    public static void main(String[] args) {

        File file = new File("D:\\迅雷下载\\任务组_20200212_1439");
        File[] files = file.listFiles();
        assert files != null;
        String home="<Description>Failed to Prepare the Procedure's Parameter: ParamName = :sPICTURE, ErrDetail = Failed to Get the Picture File by FTP:";
        String end="</Description>";
        Stream.of(files).forEach(e->{
            try {
                List<String> strings = Files.readAllLines(e.toPath());
                if (strings.get(6).contains("Failed to Get the Picture")){
                    System.out.println(strings.get(6).replace(home,"").replace(end,""));
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });

    }

}
