NoeTestAssesment Repository
===========================

This CSV Parser is a Java application that reads and parses one or more CSV files. This application can handle edge cases, such as escaped quotes, duplicate column headers, empty column headers, and quoted fields. The parsed CSV file(s) is stored in a CSVFile object(s), allowing easy access and manipulation. I programmed this project in the IntelliJ IDEA IDE and used git for verision control.

Link to GitHub repo: https://github.com/noebrown/NoeTechAssessment

How to Use the Program
----------------------
* External Libraries Required:
  - Jdk 23
  - Junit5
* Suggested method of running the project:
  - Opening the project in IntelliJ
  - Downloading the necessary external libraries
  - Selecting Main.java file and running 
*  Enter CSV file path(s):
    - The program will prompt you to enter the path of the CSV file(s) to be parsed
    - For a single file: `path/to/your/file.csv`
    - For multiple files: `path/to/file1.csv, path/to/file2.csv, path/to/file3.csv`
    - Press Enter/Return when finished
* View Results:
    - The program will display the file path, number of rows, number of columns, and the first 10 rows of the parsed CSV file. If the table is less than 10 rows, it will display the full table.
*   Example Usage:
```
Please type the path of the CSV file(s) to be parsed.
If you are parsing multiple CSV files please separate each path with a comma.
Once finished press enter/return.
/Users/yourUser/Desktop/carPriceData.csv,/Users/yourUser/Desktop/ethPriceData.csv
2 CSV file(s) will be parsed
==================================================
File: '/Users/yourUser/Desktop/carPriceData.csv'
Rows: 5
Columns: 4
First 10 Rows:
    Row 1: {color=Green, year=2012, price=$5,000, make=Toyota}
    Row 2: {color=Brown, year=2021, price=$567,000, make=Ferrari}
    Row 3: {color=Blue, year=2010, price=$500, make=Chevy}
    Row 4: {color=Red, year=2026, price=$40,000, make=Mazda}
    Row 5: {color=Black, year=2001, price=$100,000, make=Mercedes Benz}

==================================================
File: '/Users/yourUser/Desktop/ethPriceData.csv'
Rows: 5
Columns: 7
First 10 Rows:
    Row 1: {High=3,397.67, Vol.=4.71M, Price=3,356.56, Low=2,971.69, Date=Jan 01, 2026, Change %=0.1295, Open=2,971.69}
    Row 2: {High=3,444.22, Vol.=13.30M, Price=2,971.69, Low=2,719.79, Date=Dec 01, 2025, Change %=-0.67%, Open=2,993.69}
    Row 3: {High=3,916.08, Vol.=19.17M, Price=2,991.73, Low=2,630.67, Date=Nov 01, 2025, Change %=-22.25%, Open=3,848.00}
    Row 4: {High=4,752.44, Vol.=18.67M, Price=3,848.00, Low=3,468.02, Date=Oct 01, 2025, Change %=-7.16%, Open=4,145.18}
    Row 5: {High=4,764.88, Vol.=13.26M, Price=4,144.60, Low=3,825.38, Date=Sep 01, 2025, Change %=-5.61%, Open=4,392.75}


Process finished with exit code 0
```

Walking Through Each Method
---------------------------

### main()

Prompts the user to enter CSV file path(s) and calls validatePathList() to validate each path.

### validatePathList()

Prevents partial parsing when files are invalid, catches file accessibility issues before parsing, and informs users which file path(s) are invalid. Calls parseFile().

### parseFile()

Reads the first line as the column headers using parseCSVLine(). Empty column headers and duplicate column header names are two edge case this method handles. Reads are parses subsequent lines with parseCSVLine(). Maps each row’s column to its corresponding column header. Returns a CSVFile object with the parsed data.

### parseCSVLine()

Traverses through each character in a line. Uses the insideTheQuotes boolean flag to track whether the character is inside or outside quotes. Comma delimiters are only acknowledged when they are outside quotes (insideTheQuotes=false). Handles edge case when multiple sets of quotes occur in a row. Returns an ArrayList of field values. This logic is reused for column headers and rows.

Design Decisions
----------------

### ArrayList to Array Conversion

The number of rows in each CSV file is unknown initially. ArrayList has dynamic sizing that can grow as we add elements. Arrays have fixed sizing, which would require us to first count or resize manually. We initially start with an ArrayList, then later convert to an array to fulfil the requirement of the assessment.

### Encapsulation

Since our application allows for multiple CSV files to be parsed at once, encapsulation was a natural choice. All CSV files have common properties and behavior. Encapsulation keeps all the CSV file data (path, parsedFile (array of row hashmaps), column headers) together in one object. This object has private fields that prevent outside code from accidentally changing the data.

Follow-up Questions
----------------
**What was the most challenging aspect of completing this project?**

The most challenging aspect of this project for me was considering potential edge cases. For this portion of the project, I spent a considerable amount of time creating various test CSV files and identifying realistic challenges that could be present. After that, it was also pertinent to decide which edge cases took priority. There is no way to determine all possible edge cases or even account for all edges without sacrificing execution speed, decreasing code maintainability, or increasing the difficulty of tracing errors thrown.

**What else would you choose to add to this project if given more time to work on it?**
- I would choose to implement type detection for fields. Everything is currently stored as a string in the hashmap. 
- My current approach silently handles column discrepancies between lines. Say the first line of the CSV file (the header) has three columns and the fourth line has four columns. My solution would skip the extra column in the fourth line. Instead, I would warn the user and offer a path for them to decide how it’s reconciled.
- When there are two or more column headers of the same name or empty columns, the default process is auto-generating generic names. I would want to warn the user and give them the opportunity to either name the columns or go by the default.
- I would add pagination to handle large CSV files. I would also give the user the option to give input on how the file is split. For example, have the file split every x number of lines.
- Give the user the option to select if leading/trailing spaces in fields are kept or trimmed.
- Figure out a way to account for newlines within fields using parse by line method although, the only way I can think of doing that is parsing by character.

**Why did you choose your specific approach? Did it come with any advantages vs.
another one?**

The three most important aspects for developing my approach were creating a solution that can handle a robust variety of CSV files, be easy to use, and have good code clarity. Keeping this in mind, I made the following decisions:
- Parsing line by line allows rows to be processed independently and incrementally. You can handle each row as soon as it's read. rather than loading the entire file into memory first. 
- Splitting on the comma delimiters forces edge cases to be handled explicitly. This adds to code complexity but allows the option to alert the user and decide how to treat the edge case. 
- The automatic renaming of empty and duplicate column headers prevents most data loss. It also allows the parser to still parse malformed CSV files and warn users of these occurrences. 
- I implemented encapsulation to access CSV file data through simple methods like getNumRows(), getNumColumns(), and getRow() to retrieve information. This helps with code readability, prevents external code from directly modifying CSVFile objects, and helps validate data (null checks).
- My approach takes the header row columns (first line of the CSV file) as the 'source' of truth. This means if there was a mismatch between the number of columns in header and the following rows; such as less/more columns than header in those columns are considered mistakes. Fields are left empty (if less columns than the header row) or discarded (if more columns than header row).

I like to believe that my solution prioritizes simplicity but not at the cost of functionality.