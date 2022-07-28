package NumberClassifier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FeedForwardNeuralNetworkTest {

    /**
     * Test a simple network that calculates XOR operation.
     */
    @Test void evaluatesXOR() throws Exception {
        // Setup ---
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new int[] { 2, 3, 1 } );
        ffn.setWeights( 
            0, new double[] { 
            1.0, 1.0, 0.0, 
            0.0, 1.0, 1.0 
        });
        ffn.setBiases( 1, new double[] { -0.4, -1.4, -0.4 });
        ffn.setWeights( 1, new double[] { 1.0, -2.0, 1.0 } );

        // Calculate ---
        double output;

        ffn.setInput( new double[] { 1.0, 0.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output > 0.5, "1 XOR 0 should be 1" );

        ffn.setInput( new double[] { 0.0, 1.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output > 0.5, "0 XOR 1 should be 1" );

        ffn.setInput( new double[] { 1.0, 1.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output < 0.5, "1 XOR 1 should be 0" );

        ffn.setInput( new double[] { 0.0, 0.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output < 0.5, "0 XOR 0 should be 0" );
    }
    

    @Test void setInputDoesntAcceptInvalidInput() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new int[] { 2, 3, 1 } );

        // Too little inputs
        try {
            ffn.setInput(new double[] {0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Too many inputs
        try {
            ffn.setInput(new double[] {0.0, 0.0, 0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test void setWeightsDoesntAcceptInvalidInput() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new int[] { 2, 3, 1 } );

        // Layer id out of bounds
        try {
            ffn.setWeights(-1, new double[] {});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer id out of bounds
        try {
            ffn.setWeights(3, new double[] {});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer has too little biases
        try {
            ffn.setWeights(0, new double[] {0.0, 0.0, 0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer has too many biases
        try {
            ffn.setWeights(0, new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
    
    @Test void setBiasesDoesntAcceptInvalidInput() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new int[] { 2, 3, 1 } );

        // Layer id out of bounds
        try {
            ffn.setBiases(-1, new double[] {});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer id out of bounds
        try {
            ffn.setBiases(3, new double[] {});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer has too little weights
        try {
            ffn.setBiases(0, new double[] {0.0, 0.0, 0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Layer has too many weights
        try {
            ffn.setBiases(0, new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

}
