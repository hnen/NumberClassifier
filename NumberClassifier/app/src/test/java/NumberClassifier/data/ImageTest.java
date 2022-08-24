package NumberClassifier.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ImageTest {
    
    @Test
    public void testFitImage() {
        double pixels[] = new double[5*5];
        pixels[2 * 5 + 2] = 1.0;        
        Image image = new Image(pixels, 5, 5);
        assertEquals(1.0, image.getPixel(2, 2));
        
        Image.Rect bounds = image.calculateBounds(0.5);
        assertEquals(2, bounds.x);
        assertEquals(2, bounds.y);
        assertEquals(1, bounds.width);
        assertEquals(1, bounds.height);

        image.fitImage();
        bounds = image.calculateBounds(0.0);
        assertEquals(0, bounds.x);
        assertEquals(0, bounds.y);
        assertEquals(5, bounds.width);
        assertEquals(5, bounds.height);
    }

}
