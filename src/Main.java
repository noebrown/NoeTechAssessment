import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String path = "";
    static Integer pathCount = 0;

    public static void main(String[] args) {
        // Gets path of each CSV file from user
        Scanner getPath = new Scanner(System.in);
        System.out.printf("Please type the path of the CSV file to be parsed.\nIf you are parsing multiple CSV files please seperate each path with a comma.\nDo not put any spaces inbetween comma and path.\nOnce finished press enter/return.%n");
        path = getPath.nextLine();
        // System.out.println("Path(s):" + path);
        // /Users/noebrown/Desktop/athletes.csv,/Users/noebrown/Desktop/ethPriceData.csv
        // /Users/noebrown/Desktop/ethPriceData.csv

        verifyPathList();
        parsePathList();
    }

    // Traverse the list of CSV paths and ensure each patch can be opened
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
        if(!fileFound || !fileClosed) {
            System.out.println("Error occurred, no parsing will be done.");
        }else {
            System.out.println(pathCount + " CSV file(s) will be parsed");
        }

    }

    // Traverse the list of CSV Paths and Parse each
    public static void parsePathList() {
        parse();
    }

    // Parsing Logic
    public static void parse() {
    }

}