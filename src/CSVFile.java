import java.util.HashMap;


class CSVFile {
    private String path;
    private HashMap<String, String>[] parsedFile;
    private String[] columnHeaders;

    public CSVFile(String path, HashMap<String, String>[] parsedFile, String[] columnHeaders) {
        this.path = path;
        this.parsedFile = parsedFile;
        this.columnHeaders = columnHeaders;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String>[] getParsedFile() {
        return parsedFile;
    }

    public String[] getColumnHeaders() {
        return columnHeaders;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setParsedFile(HashMap<String, String>[] parsedFile) {
        this.parsedFile = parsedFile;
    }

    public void setColumnHeaders(String[] columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public int getNumRows() {
        return parsedFile.length;
    }

    public int getNumColumns() {
        return columnHeaders.length;
    }

    public HashMap<String, String> getRow(int index) {
        if (index >= 0 && index < parsedFile.length) {
            return parsedFile[index];
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File: '").append(path).append('\'')
                .append("\nRows: ").append(getNumRows())
                .append("\nColumns: ").append(getNumColumns())
                .append("\nFirst 10 Rows:\n");

        for (int i = 0; i < Math.min(10, parsedFile.length); i++) {
            sb.append("    Row ").append(i+1).append(": ").append(parsedFile[i]).append("\n");
        }

        return sb.toString();
    }
}
