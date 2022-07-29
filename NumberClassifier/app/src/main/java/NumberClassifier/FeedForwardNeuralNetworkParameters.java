package NumberClassifier;

import java.util.Random;

/**
 * Stores weights and biases for a Feed Forward Neural Network.
 * <p>
 * In addition to representing the neural network parameters, it can represent gradient of the parameters.
 * </p>
 */
public class FeedForwardNeuralNetworkParameters {

    private int[] layers;
    public double [][] biases;
    public double [][] weights;    

    public  FeedForwardNeuralNetworkParameters( int[] layers ) {
        this.layers = layers;
        this.weights = new double[layers.length - 1][];
        this.biases = new double[layers.length - 1][];
        
        for( int i = 0; i < layers.length - 1; i++ ) {
            weights[i] = new double[layers[i] * layers[i + 1]];
            biases[i] = new double[layers[i + 1]];
        }
    }

    public void randomizeWeights( double min, double max ) {
        Random rnd = new Random();
        for( int i = 0; i < layers.length - 1; i++ ) {
            for ( int j = 0; j < weights[i].length; j++ ) {
                weights[i][j] = rnd.nextDouble() * (max-min) + min;
            }
        }        
    }

    public void setBiases( double value ) {
        for( int i = 0; i < layers.length - 1; i++ ) {
            for ( int j = 0; j < biases[i].length; j++ ) {
                biases[i][j] = value;
            }
        }
    }

    public void setWeights( int layer, double[] weights ) throws Exception {
        if ( weights.length != this.weights[layer].length ) {
            throw new Exception( "Invalid number of weights." );
        }

        this.weights[layer] = weights.clone();
    }

    public double[][] weightMatrix( int L ) {
        double[][] ret = new double[layers[L]][];
        for ( int i = 0; i < layers[L]; i++) {
            ret[i] = new double[layers[L+1]];

            for ( int j = 0; j < layers[L+1]; j++) {
                ret[i][j] = weights[L][layers[L + 1] * i + j];
            }
        }
        return ret;
    }

    public void add( FeedForwardNeuralNetworkParameters another ) throws Exception {
        if ( another.weights.length != weights.length ) {
            throw new Exception( "Parameters have different number of weight layers." );
        }

        if ( another.biases.length != biases.length ) {
            throw new Exception( "Parameters have different number of bias layers." );
        }

        for ( int i = 0; i < weights.length; i++ ) {   
            if ( another.weights[i].length != weights[i].length ) {
                throw new Exception( "Parameters have different number of neuron weights on a layer." );
            }
    
            for ( int j = 0; j < weights[i].length; j++ ) {
                weights[i][j] += another.weights[i][j];
            }
        }
        
        for ( int i = 0; i < biases.length; i++ ) {   
            if ( another.biases[i].length != biases[i].length ) {
                throw new Exception( "Parameters have different number of neuron biases on a layer." );
            }

            for ( int j = 0; j < biases[i].length; j++ ) {
                biases[i][j] += another.biases[i][j];
            }
        }        
    }

    public void multiply( double x ) {
        for ( int i = 0; i < weights.length; i++ ) {   
            for ( int j = 0; j < weights[i].length; j++ ) {
                weights[i][j] *= x;
            }
        }
        
        for ( int i = 0; i < biases.length; i++ ) {   
            for ( int j = 0; j < biases[i].length; j++ ) {
                biases[i][j] *= x;
            }
        }  
    }

}
