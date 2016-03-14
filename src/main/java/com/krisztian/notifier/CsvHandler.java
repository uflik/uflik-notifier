package com.krisztian.notifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uflik on 3/12/16.
 */
public final class CsvHandler {

    private static final String PROVIDERS_CSV_FILE = "providers.csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvHandler.class);

    public static List<SourcePageDescriptor> getSourcePageDescriptorListFromCsv() throws IOException {
        List<SourcePageDescriptor> sourcePageDescriptorList = new ArrayList<>();
        try (Reader in = new InputStreamReader(CsvHandler.class.getClassLoader()
                .getResourceAsStream(PROVIDERS_CSV_FILE), "UTF-8")
        ) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
            for (CSVRecord record : records) {
                sourcePageDescriptorList.add(new SourcePageDescriptor(
                        record.get("ID"), record.get("URL"), record.get("AD_SELECTOR"), record.get("NEXT_PAGE_SELECTOR")));
            }

        } catch (IllegalArgumentException e) {
            LOGGER.warn("Exception during the {} file reading: {}", PROVIDERS_CSV_FILE, e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Error occurred during the {}  file reading. Teh application is stopping. ", PROVIDERS_CSV_FILE, e);
            throw e;
        }
        return sourcePageDescriptorList;
    }

    private CsvHandler() {
    }

}
