package NumberClassifier.neuralnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class HeWeightInitMethodTest {

    @Test
    void testInitializeWeights() throws Exception {
        HeWeightInitMethod heWeightInitMethod = new HeWeightInitMethod();
        
        FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork( new SigmoidActivationFunction(), new int[] { 4000, 500, 5000, 500 });

        FeedForwardNeuralNetworkParameters params = nn.getParameters();
        heWeightInitMethod.initializeWeights(params);

        // test that weights are non-zero
        for (int i = 0; i < params.weights.length; i++) {
            for (int j = 0; j < params.weights[i].length; j++) {
                assertTrue(params.weights[i][j] != 0);
            }
        }

        // test that mean of weights is approx zero
        {
            double mean = 0;
            for (int i = 0; i < params.weights.length; i++) {
                for (int j = 0; j < params.weights[i].length; j++) {
                    mean += params.weights[i][j];
                }
            }
            mean /= params.weights.length * params.weights[0].length;
            assertTrue(mean < 0.01);
        }



    }

}

