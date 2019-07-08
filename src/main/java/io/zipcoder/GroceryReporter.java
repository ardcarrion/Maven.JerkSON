package io.zipcoder;

import io.zipcoder.utils.FileReader;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }


    @Override
    public String toString() {
        return null;
    }
}
