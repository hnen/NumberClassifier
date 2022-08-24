package NumberClassifier.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.jupiter.api.Test;

public class CSVWriterTest {

    class WriteTest {
        public int a;
        public String b;
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCSVWriter() throws Exception {

        WriteTest test = new WriteTest();
        test.a = 1;
        test.b = "A";

        CSVWriter<WriteTest> writer = new CSVWriter<WriteTest>(WriteTest.class);

        writer.writeToCSV("test.csv", test);

        // read test.csv as text
        try (BufferedReader reader = new BufferedReader(new FileReader("test.csv"))) {
            String header = reader.readLine();
            assertEquals("\"a\",\"b\"", header);
            String line = reader.readLine();
            assertEquals("\"1\",\"A\"", line);
        }
        catch (Exception e) {
            assertTrue(false);
        }
        
        new File("test.csv").delete();    
    }
    
}
