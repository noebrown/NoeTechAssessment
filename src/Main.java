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
        // Gets path of each CSV file from user
        Scanner getPath = new Scanner(System.in);
        System.out.printf("Please type the path of the CSV file to be parsed.\nIf you are parsing multiple CSV files please separate each path with a comma.\nDo not put any spaces in between comma and path.\nOnce finished press enter/return.%n");
        path = getPath.nextLine();

        // Test CSV Files
        // /Users/noebrown/Desktop/athletes.csv
        // /Users/noebrown/Desktop/ethPriceData.csv
        // /Users/noebrown/Desktop/cars.csv
        // /Users/noebrown/Desktop/carsUnique.csv
        // /Users/noebrown/Desktop/athletes.csv,/Users/noebrown/Desktop/ethPriceData.csv
        // /Users/noebrown/Desktop/athletes.csv,/Users/noebrown/Desktop/cars.csv,/Users/noebrown/Desktop/ethPriceData.csv,/Users/noebrown/Desktop/carsUnique.csv,
        verifyPathList();
        parsePathList();
    }

    // Traverse the list of CSV paths and ensure each patch can be opened and closed
    public static void verifyPathList() {
        String[] pathList = path.split(",");
        int pathCount = 0;
        boolean fileFound = true;
        boolean fileClosed = true;

        // Try to open and close each CSV file
        for (String s : pathList) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(s));
                bufferedReader.close();
            } catch (FileNotFoundException noFileFound) {
                System.err.println("One of the files cannot be found");
                noFileFound.printStackTrace();
                fileFound = false;
            } catch (IOException e) {
                System.err.println("Error closing one of the files");
                e.printStackTrace();
                fileClosed = false;
            }
            pathCount++;
        }

        // System.out.println("\nTotal count: " + pathCount);
        if (!fileFound || !fileClosed) {
            System.out.println("Error occurred, no parsing will be done.");
        } else {
            System.out.println(pathCount + " CSV file(s) will be parsed");
        }

    }

    // Traverse the list of CSV Paths and Parse each
    public static void parsePathList() {
        String[] pathList = path.split(",");
        for (String s : pathList) parse(s);
    }

    public static ArrayList<HashMap<String, String>> parse(String CSVFile) {
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
                        headerList.add(currentField.toString());
                        currentField = new StringBuilder();
                    } else {
                        currentField.append(currentChar);
                    }
                }
                headerList.add(currentField.toString());

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
                        // Check if next char is also a quote (escaped quote)
                        if (i + 1 < line.length() && line.charAt(i + 1) == '"' && insideQuotes) {
                            currentField.append('"'); // Add single quote
                            i++; // Skip next quote
                        } else {
                            insideQuotes = !insideQuotes; // Toggle quote mode
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

            System.out.println(numOfColumns + " Columns");

            // print rows
            for (HashMap<String, String> rowMap : rows) {
                System.out.println(rowMap);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + CSVFile);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + CSVFile);
            e.printStackTrace();
        }

        return rows;
    }
}
