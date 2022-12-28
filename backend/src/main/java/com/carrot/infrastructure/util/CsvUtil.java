package com.carrot.infrastructure.util;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CsvUtil {

    public static List<String[]> readCsvLines(String csvLocation) {
        try {
            FileReader fileReader = new FileReader(Objects.requireNonNull(CsvUtil.class.getResource(csvLocation)).getFile());
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            return csvReader.readAll();
        }catch (Exception e){
            log.info("[Error] CSV 파일 읽기중 에러가 발생했습니다. ", e);
            return null;
        }
    }
}
