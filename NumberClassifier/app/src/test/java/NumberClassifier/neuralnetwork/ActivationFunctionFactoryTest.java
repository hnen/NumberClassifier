package NumberClassifier.neuralnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ActivationFunctionFactoryTest {
        
    @Test
    public void testCreate() {
        assertTrue(ActivationFunctionFactory.create("sigmoid") instanceof SigmoidActivationFunction);
        assertTrue(ActivationFunctionFactory.create("relu") instanceof ReLUActivationFunction);
        assertThrows(IllegalArgumentException.class, () -> ActivationFunctionFactory.create("asdassd"));
    }

    @Test
    public void testNameOf() {
        assertEquals("sigmoid", ActivationFunctionFactory.nameOf(new SigmoidActivationFunction()));
        assertEquals("relu", ActivationFunctionFactory.nameOf(new ReLUActivationFunction()));
        assertThrows(IllegalArgumentException.class, () -> ActivationFunctionFactory.nameOf(null));
    }

     @Test
     public void testTypes() {
        String[] types = ActivationFunctionFactory.getTypes();
        for (String type : types) {
            assertTrue(ActivationFunctionFactory.nameOf(ActivationFunctionFactory.create(type)).equals(type));
        }

        assertEquals(2, types.length);
     }
}
