package NumberClassifier.stats;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;

public class TrainingResultWriter {

    public static void writeToCSV(String fileName, TrainingResult result) throws Exception {
        // CSV fields are quoted. comma is used as separator.

        StringBuffer updatedFile = new StringBuffer();
        String header = TrainingResult.getCSVHeader();
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

        updatedFile.append(result.toCSVRow());

        FileWriter fw = new FileWriter(curreentFile, false);
        fw.write(updatedFile.toString());
        fw.close();        
    }


}
