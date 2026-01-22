import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

    public static void main(String[] args) {
        Scanner getPath = new Scanner(System.in);
        System.out.printf("Please type the path of the CSV file(s) to be parsed." +
                "\nIf you are parsing multiple CSV files please separate each path with a comma." +
                "\nOnce finished press enter/return.%n");
        
        String path = getPath.nextLine();
        CSVFile[] csvFiles = validatePathList(path);

        for (CSVFile csvFile : csvFiles) {
            System.out.println("=".repeat(50));
            System.out.println(csvFile);
        }
        getPath.close();
    }

    /**
     * Validates comma-separated list of CSV file paths and parses each valid file.
     * Checks that all CSV files can be found, opened, and closed before parsing.
     * If any of CSV file cannot be validated, no parsing will be done for all files.
     *
     * @param CSVFiles A comma-separated string of file paths to validate and parse
     * @return ArrayList of successfully parsed CSVFile objects, or empty list if validation fails
     */
    public static CSVFile[] validatePathList(String CSVFiles) {
        String[] filePaths = CSVFiles.split(",");
        ArrayList<CSVFile> csvFileList = new ArrayList<>();
        boolean fileFound = true;
        boolean fileClosed = true;
        int pathCount = 0;

        for (String s : filePaths) {
            String trimmedPath = s.trim();

            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(trimmedPath));
                bufferedReader.close();
            } catch (FileNotFoundException noFileFound) {
                System.err.println("One of the files cannot be found: " + trimmedPath);
                noFileFound.printStackTrace();
                fileFound = false;
            } catch (IOException e) {
                System.err.println("One of the files cannot be closed: " + trimmedPath);
                e.printStackTrace();
                fileClosed = false;
            }
            pathCount++;
        }

        if (!fileFound || !fileClosed) {
            System.out.println("Error occurred, no parsing will be done.");
            return new CSVFile[0];
        } else {
            System.out.println(pathCount + " CSV file(s) will be parsed");
        }

        for (String filePath : filePaths) {
            CSVFile csvFile = parseFile(filePath.trim());
            if (csvFile != null) {
                csvFileList.add(csvFile);
            }
        }
        return csvFileList.toArray(new CSVFile[0]);
    }

    /**
     * Parses a CSV file into CSVFile object.
     * Handles duplicate column header name and empty column header edge cases.
     *
     * @param CSVFile Path to the CSV file to parse
     * @return CSVFile object containing parsed data, or null if an error occurs
     */
    public static CSVFile parseFile(String CSVFile) {
        String line;
        String[] columnHeaders = null;
        ArrayList<HashMap<String, String>> rows = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CSVFile));

            // Reads first line of CSV file (ColumnHeaders) and save as keys
            if ((line = bufferedReader.readLine()) != null) {

                ArrayList<String> columnHeaderList = parseCSVLine(line);

                HashMap<String, Integer> headerCount = new HashMap<>();
                for (int i = 0; i < columnHeaderList.size(); i++) {
                    String header = columnHeaderList.get(i);

                    // Handles empty column headers edge case
                    if(header.isEmpty()){
                        header = "Column_" + (i + 1);
                        columnHeaderList.set(i, header);
                        System.out.println("Empty column header detected in path: '" + CSVFile + "' at column " + (i+1) +
                                " renamed to '" + header + "'");
                    }

                    // Handles duplicate column headers edge case
                    if (headerCount.containsKey(header)) {
                        int count = headerCount.get(header) + 1;
                        headerCount.put(header, count);
                        columnHeaderList.set(i, header + "_" + count);
                        System.out.println("Duplicate column name detected in path: '" + CSVFile + "' '" + header + "' renamed to '" + header + "_" + count + "'");
                    } else {
                        headerCount.put(header, 1);
                    }
                }
                columnHeaders = columnHeaderList.toArray(new String[0]);
            }

            while ((line = bufferedReader.readLine()) != null) {

                ArrayList<String> valueList = parseCSVLine(line);

                String[] values = valueList.toArray(new String[0]);

                HashMap<String, String> row = new HashMap<>();
                for (int i = 0; i < Objects.requireNonNull(columnHeaders).length && i < values.length; i++) {
                    row.put(columnHeaders[i], values[i]);
                }
                rows.add(row);
            }

            bufferedReader.close();

            @SuppressWarnings("unchecked")
            HashMap<String, String>[] rowsArray = (HashMap<String, String>[]) rows.toArray(new HashMap[0]);
            return new CSVFile(CSVFile, rowsArray, columnHeaders);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + CSVFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + CSVFile);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses a CSV File line into individual field values.
     * Handles quotes within quotes edge case.
     *
     * @param line The CSV line to parse
     * @return ArrayList of field values from the line
     */
    private static ArrayList<String> parseCSVLine(String line) {
        ArrayList<String> valueList = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                // Handles quotes within quotes edge cases
                if (i + 1 < line.length() && line.charAt(i + 1) == '"' && insideQuotes) {
                    currentField.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (currentChar == ',' && !insideQuotes) {
                valueList.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(currentChar);
            }
        }
        valueList.add(currentField.toString());
        return valueList;
    }
}
