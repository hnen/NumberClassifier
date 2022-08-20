package NumberClassifier.neuralnetwork;

import java.util.Random;

public class XavierWeightInitMethod implements IWeightInitMethod {
    
    /**
     * Initialize weights.
     * @param params Parameters of the neural network.
     */
    public void initializeWeights( FeedForwardNeuralNetworkParameters params ) {
        Random rnd = new Random();
        for( int i = 0; i < params.getLayers().length - 1; i++ ) {
            for ( int j = 0; j < params.weights[i].length; j++ ) {
                params.weights[i][j] = (rnd.nextDouble() - 0.5) * 2.0 / Math.sqrt((double)params.getLayers()[i]);
            }
        }        
    }


}
