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

    class WriteTest2 {
        public int a;
        public String b;
        public double c;
    }


    /**
     * @throws Exception
     */
    @Test
    public void testCSVWriter() throws Exception {

        WriteTest test = new WriteTest();
        test.a = 1;
        test.b = "A";

        // if test.csv exists, delete it
        File file = new File("test.csv");
        if (file.exists()) {
            file.delete();
        }

        CSVWriter<WriteTest> writer = new CSVWriter<WriteTest>(WriteTest.class);

        writer.writeToCSV("test.csv", test);

        try (BufferedReader reader = new BufferedReader(new FileReader("test.csv"))) {
            String header = reader.readLine();
            assertEquals("\"a\",\"b\"", header);
            String line = reader.readLine();
            assertEquals("\"1\",\"A\"", line);
        }
        catch (Exception e) {
            assertTrue(false);
        }

        WriteTest2 test2 = new WriteTest2();
        test2.a = 2;
        test2.b = "B";
        test2.c = 1.0;
        CSVWriter<WriteTest2> writer2 = new CSVWriter<WriteTest2>(WriteTest2.class);
        writer2.writeToCSV("test.csv", test2);

        try (BufferedReader reader = new BufferedReader(new FileReader("test.csv"))) {
            String header = reader.readLine();
            assertEquals("\"a\",\"b\",\"c\"", header);
            String line = reader.readLine();
            assertEquals("\"1\",\"A\",\"\"", line);
            String line2 = reader.readLine();
            assertEquals("\"2\",\"B\",\"1.0\"", line2);
        }
        catch (Exception e) {
            assertTrue(false);
        }

        new File("test.csv").delete();    
    }
    
}
