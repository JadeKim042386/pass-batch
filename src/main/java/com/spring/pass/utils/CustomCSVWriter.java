package com.spring.pass.utils;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CustomCSVWriter {
    public static int write(String fileName, List<String[]> data) {
        int rows = 0;
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            writer.writeAll(data);
            rows = data.size();
        } catch (IOException e) {
            log.error("CustomCSVWriter - write: CSV 파일 생성 실패, fileName: {}", fileName);
        }
        return rows;
    }
}
