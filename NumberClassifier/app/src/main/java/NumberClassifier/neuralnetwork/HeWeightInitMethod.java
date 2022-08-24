package NumberClassifier.neuralnetwork;

import java.util.Random;

public class HeWeightInitMethod implements IWeightInitMethod {
    
    public void initializeWeights( FeedForwardNeuralNetworkParameters params ) {
        Random rnd = new Random(7457);
        for( int i = 0; i < params.getLayers().length - 1; i++ ) {
            for ( int j = 0; j < params.weights[i].length; j++ ) {
                params.weights[i][j] = rnd.nextGaussian() * Math.sqrt(2.0 / (double)params.getLayers()[i]);
            }
        }
    }


}
