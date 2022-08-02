package NumberClassifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class ImageSetTest {
    
    ImageSet createTestSet() throws Exception {

        byte imageFile[] = new byte[] {
            0x00, 0x00, 0x08, 0x03, // MNIST magic number
            0x00, 0x00, 0x00, 0x02, // number of images
            0x00, 0x00, 0x00, 0x04,    // number of rows
            0x00, 0x00, 0x00, 0x04,    // number of columns

            // test image 1
            11, 12, 13, 14,
            21, 22, 23, 24,
            31, 32, 33, 34,
            41, 42, 43, 44,
            
            // test image 2
            51, 52, 53, 54,
            61, 62, 63, 64,
            71, 72, 73, 74,
            81, 82, 83, 84,
        };

        byte labelFile[] = new byte[] {
            0x00, 0x00, 0x08, 0x01, // MNIST magic number
            0x00, 0x00, 0x00, 0x02, // number of images
            2,                      // label 1
            9                       // label 2
        };

        InputStream imageStream = new ByteArrayInputStream(imageFile);
        InputStream labelStream = new ByteArrayInputStream(labelFile);
        
        return ImageSet.loadFromMNIST(imageStream, labelStream, 10);
    }

    @Test void testLoadFromMNIST() throws Exception {
        ImageSet imageSet = createTestSet();

        assertEquals(2, imageSet.getNumImages());
        assertEquals(4, imageSet.getImageWidth());
        assertEquals(4, imageSet.getImageHeight());

        assertEquals(2, imageSet.getLabel(0));
        assertEquals(9, imageSet.getLabel(1));
        
        for ( int i = 0; i < 2; i++ ) {
            double[] image = imageSet.getImage(i);           
            assertEquals(4 * 4, image.length);
            int n = 0;
            for( int y = 0; y < 4; y++ ) {
                for ( int x = 0; x < 4; x++ ) {
                    int p = x + 1 + ((y + 1) * 10) + (i * 40);
                    assertEquals((double)p / 0xff, image[n], 0.00001);
                    n++;
                }
            }
        }
    }

    @Test void testCreateTrainingExamples() throws Exception {
        ImageSet imageSet = createTestSet();

        TrainingExample[] examples = imageSet.createTrainingExamples();
        assertEquals(2, examples.length);

        for ( int i = 0; i < 2; i++ ) {
            assertEquals(4 * 4, examples[i].input.length);
            assertEquals(10, examples[i].output.length);

            int n = 0;
            for( int y = 0; y < 4; y++ ) {
                for ( int x = 0; x < 4; x++ ) {
                    int p = x + 1 + ((y + 1) * 10) + (i * 40);
                    assertEquals((double)p / 0xff, examples[i].input[n], 0.00001);
                    n++;
                }
            }
        }

        for ( int i = 0; i < 10; i++ ) {
            assertEquals( i == 2 ? 1.0 : 0.0, examples[0].output[i] );
            assertEquals( i == 9 ? 1.0 : 0.0, examples[1].output[i] );
        }
    }

}
