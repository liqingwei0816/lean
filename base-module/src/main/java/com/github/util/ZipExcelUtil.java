package com.github.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipExcelUtil {

    /**
     *  在线读取文件zip 未校验文件格式
     * @param file
     */
    public void upload(MultipartFile file) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(file.getBytes()), Charset.forName("GBK"));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                String name = zipEntry.getName();
                System.out.println(name);
                byte[] buffer = new byte[1024];
                int len;
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((len = zipInputStream.read(buffer)) != -1) {
                    byteStream.write(buffer, 0, len);
                }
                // 关闭流
                byteStream.close();
                // 将之前的 ByteArrayOutputStream 对象转化为 byte数组，然后使用 EasyExcel 进行解析
                ExcelReaderBuilder read = EasyExcel.read(new ByteArrayInputStream(byteStream.toByteArray()));
                List<Map<Integer, Object>> objects = read.doReadAllSync();
                objects.forEach(t -> System.out.println(t.get(0) + "  :  " + t.get(1)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    /**
     * 读取本地文件zip 未校验文件格式
     * @param file
     */
    public void upload(File file) {
        try (ZipFile zipFile = new ZipFile(file, Charset.forName("GBK"))) {

            zipFile.stream().forEach(zipEntry -> {
                if (zipEntry.isDirectory()) {
                    return;
                }
                try {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    ExcelReaderBuilder read = EasyExcel.read(inputStream);
                    List<Map<Integer, Object>> objects = read.doReadAllSync();
                    objects.forEach(t -> System.out.println(t.get(0) + "  :  " + t.get(1)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }


}
