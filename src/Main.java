import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

    static String path = "";

    public static void main(String[] args) {
        Scanner getPath = new Scanner(System.in);
        System.out.printf("Please type the path of the CSV file(s) to be parsed." +
                "\nIf you are parsing multiple CSV files please separate each path with a comma." +
                "\nOnce finished press enter/return.%n");
        path = getPath.nextLine();

        ArrayList<CSVFile> csvFiles = validatePathList(path);

        for (CSVFile csvFile : csvFiles) {
            System.out.println("=".repeat(50));
            System.out.println(csvFile);
        }
        getPath.close();
    }

    public static ArrayList<CSVFile> validatePathList(String CSVFiles) {
        String[] filePaths = CSVFiles.split(",");
        ArrayList<CSVFile> csvFileList = new ArrayList<>();

        boolean fileFound = true;
        boolean fileClosed = true;
        int pathCount = 0;

        // Try to open and close each CSV file to validate paths
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
                System.err.println("Error closing one of the files: " + trimmedPath);
                e.printStackTrace();
                fileClosed = false;
            }
            pathCount++;
        }

        if (!fileFound || !fileClosed) {
            System.out.println("Error occurred, no parsing will be done.");
            return csvFileList; // Return empty list
        } else {
            System.out.println(pathCount + " CSV file(s) will be parsed");
        }

        // Parse each CSV file
        for (String filePath : filePaths) {
            CSVFile csvFile = parseFile(filePath.trim());
            if (csvFile != null) {
                csvFileList.add(csvFile);
            }
        }

        return csvFileList;
    }


    public static CSVFile parseFile(String CSVFile) {
        String line = "";
        String[] columnHeaders = null;
        int numOfColumns = 0;
        ArrayList<HashMap<String, String>> rows = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CSVFile));

            // Read first line of CSV (ColumnHeaders) and save as keys
            if ((line = bufferedReader.readLine()) != null) {

                ArrayList<String> headerList = new ArrayList<>();
                StringBuilder currentField = new StringBuilder();
                boolean insideQuotes = false;

                for (int i = 0; i < line.length(); i++) {
                    char currentChar = line.charAt(i);

                    if (currentChar == '"') {
                        if (i + 1 < line.length() && line.charAt(i + 1) == '"' && insideQuotes) {
                            currentField.append('"');
                            i++;
                        } else {
                            insideQuotes = !insideQuotes; // Toggle quote mode
                        }
                    } else if (currentChar == ',' && !insideQuotes) {
                        headerList.add(currentField.toString().trim());
                        currentField = new StringBuilder();
                    } else {
                        currentField.append(currentChar);
                    }
                }
                headerList.add(currentField.toString());

                HashMap<String, Integer> headerCount = new HashMap<>();
                for (int i = 0; i < headerList.size(); i++) {
                    String header = headerList.get(i);

                    // handles empty column edge case
                    if(header.isEmpty()){
                        header = "Column_" + (i + 1);
                        headerList.set(i, header);
                        System.out.println("Empty column header detected in path: '" + CSVFile + "' at column " + (i+1) +
                                " renamed to '" + header + "'");
                    }

                    // handles duplicate column headers edge case
                    if (headerCount.containsKey(header)) {
                        int count = headerCount.get(header) + 1;
                        headerCount.put(header, count);
                        headerList.set(i, header + "_" + count);
                        System.out.println("Duplicate column name detected in path: '" + CSVFile + "' '" + header + "' renamed to '" + header + "_" + count + "'");
                    } else {
                        headerCount.put(header, 1);
                    }
                }

                columnHeaders = headerList.toArray(new String[0]);
                numOfColumns = columnHeaders.length;
            }

            // read rest of lines
            while ((line = bufferedReader.readLine()) != null) {

                ArrayList<String> valueList = new ArrayList<>();
                StringBuilder currentField = new StringBuilder();
                boolean insideQuotes = false;

                for (int i = 0; i < line.length(); i++) {
                    char currentChar = line.charAt(i);

                    if (currentChar == '"') {
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

                String[] values = valueList.toArray(new String[0]);

                HashMap<String, String> row = new HashMap<>();
                for (int i = 0; i < columnHeaders.length && i < values.length; i++) {
                    row.put(columnHeaders[i], values[i]);
                }

                rows.add(row);
            }

            bufferedReader.close();

            return new CSVFile(CSVFile, rows, columnHeaders);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + CSVFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + CSVFile);
            e.printStackTrace();
        }

        return null;
    }
}
