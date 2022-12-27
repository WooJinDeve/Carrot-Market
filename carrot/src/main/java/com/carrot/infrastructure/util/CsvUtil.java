package com.carrot.infrastructure.util;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public class CsvUtil {

    public static List<String[]> readCsvLines(String csvLocation) throws Exception {
        FileReader fileReader = new FileReader(Objects.requireNonNull(CsvUtil.class.getResource(csvLocation)).getFile());
        CSVReader csvReader = new CSVReader(fileReader);
        csvReader.readNext();
        return csvReader.readAll();
    }
}
