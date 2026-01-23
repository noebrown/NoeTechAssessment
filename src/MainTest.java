import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.HashMap;


class MainTest {

    @org.junit.jupiter.api.Test
    void main() {
    }

    @org.junit.jupiter.api.Test
    void validatePathList() {

        // CSV Files
        String emptyPath = "";
        String invalidPath = "/invalid/path/file.csv";
        String validPath = "/Users/noebrown/Desktop/cars.csv";
        String pathWithSpaces = validPath + "   ";
        String multiplePaths = invalidPath + "," + validPath;

        // Test 1 valid path with spaces
        CSVFile[] result = Main.validatePathList(pathWithSpaces);
        assertEquals(1, result.length);
        assertEquals(pathWithSpaces.trim(), result[0].getPath(), "Path should be trimmed");

        // Test 1 empty path
        result = Main.validatePathList(emptyPath);
        assertEquals(0, result.length);

        // Test 1 invalid path
        result = Main.validatePathList(invalidPath);
        assertEquals(0, result.length);

        // Test 2 paths; 1 invalid and 1 valid
        result = Main.validatePathList(multiplePaths);
        assertEquals(0, result.length);

    }

    @org.junit.jupiter.api.Test
    void parseFile() {

        // CSV Files
        String twoSameNameHeaders = "/Users/noebrown/Desktop/sameName.csv";
        String quotesOutsideAndInMiddle = "/Users/noebrown/Desktop/quoteInMiddle.csv";
        String blankRowInCSVFile = "/Users/noebrown/Desktop/athletes.csv";
        String mismatchInColsCSVFile = "/Users/noebrown/Desktop/emptyColumns.csv";

        // Correct Output Arrays
        String[] twoSameNameHeadersArray = {"fu,n","d@te","$iz3","d@te_2"};
        String[] quotesOutsideAndInMiddleArray = {"fu,n","\"dat\"et\"","Column_3","d@te","Column_5","d@te_2"};

        // Test Headers
        // Test header with comma in the middle(fu,n) and same name column headers(d@te)
        CSVFile result = Main.parseFile(twoSameNameHeaders);
        assertArrayEquals(twoSameNameHeadersArray,result.getColumnHeaders());

        // Test complex quoted headers("dat"et"), empty column headers(""), and same name column headers(d@te)
        result = Main.parseFile(quotesOutsideAndInMiddle);
        assertArrayEquals(quotesOutsideAndInMiddleArray,result.getColumnHeaders());

        // Correct HashMap Outputs
        HashMap<String, String>[] blankRowCSVFile = new HashMap[2];
        blankRowCSVFile[0] = new HashMap<>();
        blankRowCSVFile[0].put("Name", "Serena");
        blankRowCSVFile[0].put("Age", "24");
        blankRowCSVFile[0].put("Sport", "Soccer");
        blankRowCSVFile[1] = new HashMap<>();
        blankRowCSVFile[1].put("Name", "Jaylen");
        blankRowCSVFile[1].put("Age", "45");
        blankRowCSVFile[1].put("Sport", "Baseball");

        HashMap<String, String>[] mismatchColsCSVFile = new HashMap[3];
        mismatchColsCSVFile[0] = new HashMap<>();
        mismatchColsCSVFile[0].put("make", "Toyota");
        mismatchColsCSVFile[0].put("price", "");
        mismatchColsCSVFile[0].put("color", "Green");
        mismatchColsCSVFile[0].put("year", "2012");
        mismatchColsCSVFile[1] = new HashMap<>();
        mismatchColsCSVFile[1].put("make", "");
        mismatchColsCSVFile[1].put("price", "$7,000");
        mismatchColsCSVFile[1].put("color", "");
        mismatchColsCSVFile[1].put("year", "2016");
        mismatchColsCSVFile[2] = new HashMap<>();
        mismatchColsCSVFile[2].put("make", "Mazda");
        mismatchColsCSVFile[2].put("price", "$40,000");
        mismatchColsCSVFile[2].put("color", "Red");
        mismatchColsCSVFile[2].put("year", "");

        // Test complex quoted headers("dat"et"), empty column headers(""), and same name column headers(d@te)
        result = Main.parseFile(blankRowInCSVFile);
        assertArrayEquals(blankRowCSVFile,result.getParsedFile());

        // Test mismatch columns between headers and rows
        result = Main.parseFile(mismatchInColsCSVFile);
        assertArrayEquals(mismatchColsCSVFile,result.getParsedFile());
    }
}