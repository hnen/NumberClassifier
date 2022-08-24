package NumberClassifier.neuralnetwork;

import java.util.Random;

public class UniformWeightInitMethod implements IWeightInitMethod {
    
    private double min;
    private double max;

    public UniformWeightInitMethod(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Initialize weights.
     * @param params Parameters of the neural network.
     */
    public void initializeWeights( FeedForwardNeuralNetworkParameters params ) {
        Random rnd = new Random(7457);
        for( int i = 0; i < params.getLayers().length - 1; i++ ) {
            for ( int j = 0; j < params.weights[i].length; j++ ) {
                params.weights[i][j] = rnd.nextDouble() * (max-min) + min;
            }
        }
    }
    

}
