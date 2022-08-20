package NumberClassifier.stats;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.io.BufferedReader;

public class CSVWriter<T> {

    Class<T> c;

    public CSVWriter(Class<T> c) {
        this.c = c;
    }

    public void writeToCSV(String fileName, T result) throws Exception {
        // CSV fields are quoted. comma is used as separator.

        StringBuffer updatedFile = new StringBuffer();
        String header = getHeader();
        int numFields = header.split(",").length - 1;
        updatedFile.append(header);
        updatedFile.append("\n");

        File curreentFile = new File(fileName);
        if (curreentFile.exists()) {
            // read first line of fileName
            FileReader fr = new FileReader(curreentFile);
            BufferedReader br = new BufferedReader(fr);

            // discard old header
            br.readLine();

            // read file line by line
            String line;
            while ((line = br.readLine()) != null) {
                // add necessary number of commas
                int numFieldsLine = line.split(",").length - 1;
                for (int i = 0; i < numFields - numFieldsLine; i++) {
                    line += ",\"\"";
                }
                updatedFile.append(line);
                updatedFile.append("\n");
            }

            br.close();
        }

        updatedFile.append(createRow(result));

        FileWriter fw = new FileWriter(curreentFile, false);
        fw.write(updatedFile.toString());
        fw.close();        
    }

    private String getHeader() {
        StringBuilder header = new StringBuilder();
        boolean first = true;
        for( Field f : c.getFields() ) {
            if ( !first ) {
                header.append(",");
            }

            header.append("\"" + f.getName() + "\"");

            first = false;
        }

        return header.toString();
    }

    private String createRow(T obj) throws IllegalAccessException {
        StringBuilder row = new StringBuilder();
        boolean first = true;
        for( Field f : c.getFields() ) {
            if ( !first ) {
                row.append(",");
            }

            row.append("\"" + f.get(obj).toString() + "\"");

            first = false;
        }

        return row.toString();
    }

}
