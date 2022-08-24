package NumberClassifier.neuralnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WeightInitMethodFactoryTest {
    
    @Test
    public void testCreate() {
        assertTrue(WeightInitMethodFactory.create("uniform") instanceof UniformWeightInitMethod);
        assertTrue(WeightInitMethodFactory.create("xavier") instanceof XavierWeightInitMethod);
        assertTrue(WeightInitMethodFactory.create("he") instanceof HeWeightInitMethod);
        assertThrows(IllegalArgumentException.class, () -> WeightInitMethodFactory.create("asdassd"));
    }

    @Test
    public void testNameOf() {
        assertEquals("uniform", WeightInitMethodFactory.nameOf(new UniformWeightInitMethod(0,0)));
        assertEquals("xavier", WeightInitMethodFactory.nameOf(new XavierWeightInitMethod()));
        assertEquals("he", WeightInitMethodFactory.nameOf(new HeWeightInitMethod()));
        assertThrows(IllegalArgumentException.class, () -> WeightInitMethodFactory.nameOf(null));
    }

     @Test
     public void testTypes() {
        String[] types = WeightInitMethodFactory.getTypes();
        for (String type : types) {
            assertTrue(WeightInitMethodFactory.nameOf(WeightInitMethodFactory.create(type)).equals(type));
        }

        assertEquals(3, types.length);
     }



}
