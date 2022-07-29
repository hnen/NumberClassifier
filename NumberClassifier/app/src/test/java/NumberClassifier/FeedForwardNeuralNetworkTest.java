package NumberClassifier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FeedForwardNeuralNetworkTest {

    @Test void testCalculateOutputError() throws Exception {
        FeedForwardNeuralNetwork ffn0 = new FeedForwardNeuralNetwork( new ReLUActivationFunction(), new int[] { 2, 3, 1 } );

        for ( int i = 0; i < 100; i++) {
            ffn0.randomizeWeights(0.0, 1.0);
            ffn0.setBiases(0.1);
            testCalculateOutputError(ffn0, new double[] { 0.0, 0.0 }, new double[] { 0.0 });
            testCalculateOutputError(ffn0, new double[] { 1.0, 0.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn0, new double[] { 0.0, 1.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn0, new double[] { 1.0, 1.0 }, new double[] { 0.0 });
        }

        FeedForwardNeuralNetwork ffn1 = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );
        for ( int i = 0; i < 100; i++) {
            ffn1.randomizeWeights(-1.0, 1.0);
            ffn1.setBiases(0.0);
            testCalculateOutputError(ffn1, new double[] { 0.0, 0.0 }, new double[] { 0.0 });
            testCalculateOutputError(ffn1, new double[] { 1.0, 0.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn1, new double[] { 0.0, 1.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn1, new double[] { 1.0, 1.0 }, new double[] { 0.0 });
        }
    }

    void testCalculateOutputError( FeedForwardNeuralNetwork ffn, double[] input, double[] expectedOutput ) throws Exception {
        TrainingExample ex = new TrainingExample();
        ex.input = input;
        ex.output = expectedOutput;
        
        ffn.setInput(ex.input);
        ffn.feedForward();
        double output = ffn.getOutput()[0];
        double[] err = ffn.calculateOutputError(ex.output);

        assertEquals(1, err.length);
        
        if ( output < ex.output[0] )
            assertTrue( err[0] < 0.0 );

        if ( output > ex.output[0] )
            assertTrue( err[0] > 0.0 );

        if ( output == ex.output[0] )
            assertEquals(0.0, err[0]);        
    }

    @Test void testFeedForward() throws Exception {
        // Setup ---
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );
        ffn.setWeights( 
            0, new double[] { 
            1.0, 1.0, 0.0, 
            0.0, 1.0, 1.0 
        });
        ffn.setBiases( 1, new double[] { 0.0, 0.0, 0.0 });
        ffn.setWeights( 1, new double[] { 1.0, -1.5, 1.0 } );

        ffn.setInput( new double[] { 1.0, 0.0 } );
        ffn.feedForward();
        assertTrue( ffn.getActivations(1)[0] > 0.5 );
        assertTrue( ffn.getActivations(1)[1] > 0.5 );
        assertEquals(0.5, ffn.getActivations(1)[2]);

        ffn.setInput( new double[] { 0.0, 1.0 } );
        ffn.feedForward();
        assertEquals(0.5, ffn.getActivations(1)[0]);
        assertTrue( ffn.getActivations(1)[2] > 0.5 );
        assertTrue( ffn.getActivations(1)[1] > 0.5 );

    }
    

    @Test void setInputDoesntAcceptInvalidInput() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );

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
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );

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
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );

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
