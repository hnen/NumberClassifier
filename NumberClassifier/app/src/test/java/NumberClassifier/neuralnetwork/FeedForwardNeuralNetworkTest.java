package NumberClassifier.neuralnetwork;

import org.junit.jupiter.api.Test;

import NumberClassifier.data.TrainingExample;

import static org.junit.jupiter.api.Assertions.*;

public class FeedForwardNeuralNetworkTest {



    @Test void testCalculateCostGradient() throws Exception {
        FeedForwardNeuralNetwork ffn0 = new FeedForwardNeuralNetwork( new ReLUActivationFunction(), new int[] { 2, 3, 1 } );

        UniformWeightInitMethod uniformWeightInitMethod0 = new UniformWeightInitMethod( 0.0, 1.0 );
        for ( int i = 0; i < 1000; i++) {
            uniformWeightInitMethod0.initializeWeights( ffn0.getParameters() );    
            ffn0.setBiases(0.1);
            testCalculateCostGradient(ffn0, new double[] { 0.0, 0.0 }, new double[] { 0.0 });
            testCalculateCostGradient(ffn0, new double[] { 1.0, 0.0 }, new double[] { 1.0 });
            testCalculateCostGradient(ffn0, new double[] { 0.0, 1.0 }, new double[] { 1.0 });
            testCalculateCostGradient(ffn0, new double[] { 1.0, 1.0 }, new double[] { 0.0 });
        }

        UniformWeightInitMethod uniformWeightInitMethod1 = new UniformWeightInitMethod( -1.0, 1.0 );
        FeedForwardNeuralNetwork ffn1 = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );
        for ( int i = 0; i < 1000; i++) {
            uniformWeightInitMethod0.initializeWeights( ffn1.getParameters() );    
            ffn1.setBiases(0.0);
            testCalculateCostGradient(ffn1, new double[] { 0.0, 0.0 }, new double[] { 0.0 });
            testCalculateCostGradient(ffn1, new double[] { 1.0, 0.0 }, new double[] { 1.0 });
            testCalculateCostGradient(ffn1, new double[] { 0.0, 1.0 }, new double[] { 1.0 });
            testCalculateCostGradient(ffn1, new double[] { 1.0, 1.0 }, new double[] { 0.0 });
        }
    }

    void testCalculateCostGradient( FeedForwardNeuralNetwork ffn, double[] input, double[] expectedOutput ) throws Exception {
        TrainingExample ex = new TrainingExample();
        ex.input = input;
        ex.output = expectedOutput;
        
        ffn.setInput(ex.input);
        ffn.feedForward();
        FeedForwardNeuralNetworkParameters grad = ffn.calculateCostGradient(ex);
        double C0 = ffn.calculateCost( expectedOutput );

        for ( int i = 0; i < ffn.getNumLayers() - 1; i++) {
            double[] weights = ffn.getWeights(i);
            for ( int j = 0; j < weights.length; j++ ) {
                double gradient = grad.weights[i][j];
                //weights[j] -= gradient * 0.0625;
                //ffn.feedForward();
                //double C1 = ffn.calculateCost( expectedOutput );
                //assertTrue( grad.weights[i][j] == 0.0 || (C0 == 0.0 && C1 == 0.0) || C1 < C0 );
                //C0 = C1;
                weights[j] -= gradient * 0.125;
            }         

            double[] biases = ffn.getBiases(i + 1);
            for ( int j = 0; j < biases.length; j++ ) {
                double gradient = grad.biases[i][j];
                biases[j] -= gradient * 0.125;
            }         
        }

        ffn.feedForward();
        double C1 = ffn.calculateCost( expectedOutput );
        assertTrue( (C0 == 0.0 && C1 == 0.0) || C1 < C0 );

    }    

    @Test void testCalculateOutputError() throws Exception {
        FeedForwardNeuralNetwork ffn0 = new FeedForwardNeuralNetwork( new ReLUActivationFunction(), new int[] { 2, 3, 1 } );

        UniformWeightInitMethod uniformWeightInitMethod0 = new UniformWeightInitMethod( 0.0, 1.0 );
        for ( int i = 0; i < 100; i++) {
            uniformWeightInitMethod0.initializeWeights( ffn0.getParameters() );
            ffn0.setBiases(0.1);
            testCalculateOutputError(ffn0, new double[] { 0.0, 0.0 }, new double[] { 0.0 });
            testCalculateOutputError(ffn0, new double[] { 1.0, 0.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn0, new double[] { 0.0, 1.0 }, new double[] { 1.0 });
            testCalculateOutputError(ffn0, new double[] { 1.0, 1.0 }, new double[] { 0.0 });
        }

        FeedForwardNeuralNetwork ffn1 = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );
        UniformWeightInitMethod uniformWeightInitMethod1 = new UniformWeightInitMethod( -1.0, 1.0 );
        for ( int i = 0; i < 100; i++) {
            uniformWeightInitMethod1.initializeWeights( ffn1.getParameters() );
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

    @Test void testNumInputsOutputs() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3, 1 } );
        assertEquals(2, ffn.getNumInputs());
        assertEquals(1, ffn.getNumOutputs());
    }

    @Test void testInvalidLayerCount() throws Exception {
        try {
            new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2 } );
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test void testToString() throws Exception {
        FeedForwardNeuralNetwork ffn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 2, 3 } );
        assertNotNull(ffn.toString());
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
