package NumberClassifier.neuralnetwork;

import java.util.Random;

/**
 * Stores weights and biases for a Feed Forward Neural Network.
 * <p>
 * In addition to representing the neural network parameters, it can represent gradient of the parameters.
 * </p>
 */
public class FeedForwardNeuralNetworkParameters {

    private int[] layers;

    /**
     * Biases of each neuron. Array's first index maps to layer index, and second index maps to neuron index.
     */
    public double [][] biases;

    /**
     * Weights of each neuron connection. Array's first index maps to layer index, and connection weights are for the outgoing connections. 
     * For second index, index for connection from {@code i = neuron on layer} to {@code j = neuron on layer + 1} is {@code N * i + j}, where N is number of neurons on layer {@code layer}.
     */
    public double [][] weights;    

    /**
     * Initialize empty parameteres for feedforward neural network with architecture defined by the parameter.
     * @param layers Number of neurons on each layer. The size of the array defines the number of layers. First element defines number of input neurons, and last element defines number of output neurons.
     */
    public FeedForwardNeuralNetworkParameters( int[] layers ) {
        this.layers = layers;
        this.weights = new double[layers.length - 1][];
        this.biases = new double[layers.length - 1][];
        
        for( int i = 0; i < layers.length - 1; i++ ) {
            weights[i] = new double[layers[i] * layers[i + 1]];
            biases[i] = new double[layers[i + 1]];
        }
    }

    /**
     * Set each weight in the network to a uniformly distributed random number.
     * @param min Minimum value for the weight.
     * @param max Maximum value for the weight.
     */
    public void randomizeWeights( double min, double max ) {
        Random rnd = new Random();
        for( int i = 0; i < layers.length - 1; i++ ) {
            for ( int j = 0; j < weights[i].length; j++ ) {
                weights[i][j] = rnd.nextDouble() * (max-min) + min;
            }
        }        
    }

    /**
     * Set each bias in the network to a specified value.
     * @param value The value where bias should be set to.
     */
    public void setBiases( double value ) {
        for( int i = 0; i < layers.length - 1; i++ ) {
            for ( int j = 0; j < biases[i].length; j++ ) {
                biases[i][j] = value;
            }
        }
    }

    /**
     * Set weights for outgoing connections, from {@code layer} to {@code layer + 1}. 
     * @param layer Layer index.
     * @param weights Array of conection weights. Index of connection from {@code i = neuron} on layer to {@code j = neuron on layer + 1} is {@code N * i + j}, where N is number of neurons on layer {@code layer}.
     * @throws Exception
     */
    public void setWeights( int layer, double[] weights ) throws Exception {
        if ( weights.length != this.weights[layer].length ) {
            throw new Exception( "Invalid number of weights." );
        }

        this.weights[layer] = weights.clone();
    }

    /**
     * Generate a matrix for connections from layer {@code L} to layer {@code L + 1}, so that first index represents outgoing node and second index incoming node.
     * @param L Outgoing layer index for connections
     * @return 2-dimensional double array representing the matrix.
     */
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

    /**
     * Add weights and biases of another network parameters to weights and biases of this.
     * @param another Network to add the parameters with. The network must have exactly same layer architecture as this has.
     * @throws Exception
     */
    public void add( FeedForwardNeuralNetworkParameters another ) throws Exception {
        if ( layers.length != another.layers.length ) {
            throw new Exception( "Parameters have different number of layers." );
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


    @Override 
    public String toString() {
        String ret = "";
        for ( int i = 0; i < layers.length; i++ ) {
            ret += "Layer " + i + " ------------------------------ \n";

            if ( i < layers.length - 1) {
                double[] w = weights[i];
                int numL0 = layers[i];
                int numL1 = layers[i + 1];
                for ( int j = 0; j < numL0; j++) {
                    for ( int k = 0; k < numL1; k++) {
                        ret += "w " + j + " -> " + k + ": " + w[j * numL1 + k] + "\n";
                    }
                }
            }

            if ( i > 0 ) {
                double[] b = biases[i - 1];
                for ( int j = 0; j < b.length; j++ ) {
                    ret += "b_" + j + " = " + b[j] + "\n";
                }
            }
        }
        return ret;
    }

}
