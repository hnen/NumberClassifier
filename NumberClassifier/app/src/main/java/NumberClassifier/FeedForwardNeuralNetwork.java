package NumberClassifier;

/**
 * Basic feed forward neural network.
 */
public class FeedForwardNeuralNetwork {
    
    public FeedForwardNeuralNetwork( int[] layers ) throws Exception {
        if ( layers.length < 2 ) {
            throw new Exception( "There has to be at least 2 layers" );
        }

        this.layers = layers;
        this.params = new FeedForwardNeuralNetworkParameters(layers);

        this.activations = new double[layers.length][];
        for( int i = 0; i < layers.length; i++ ) {
            activations[i] = new double[layers[i]];
        }
    }

    public int getNumInputs() {
        return layers[0];
    }

    public int getNumOutputs() {
        return layers[layers.length - 1];
    }

    public void setInput( double[] input ) throws Exception {
        if( input.length != activations[0].length ) {
            throw new Exception( "Invalid number of inputs." );
        }
        activations[0] = input.clone();
    }

    public void setWeights( int layer, double[] weights ) throws Exception {
        if ( weights.length != params.weights[layer].length ) {
            throw new Exception( "Invalid number of weights." );
        }
        params.weights[layer] = weights.clone();
    }

    public void setBiases( int layer, double[] biases ) throws Exception {
        if ( biases.length != layers[layer] )  {
            throw new Exception( "Invalid number of biases." );
        }
        params.biases[layer - 1] = biases.clone();
    }

    public double[] getOutput() {
        return this.activations[this.activations.length - 1].clone();
    }
    
    public void feedForward() {
        for ( int i = 1; i < layers.length; i++ ) {
            for ( int j = 0; j < layers[i]; j++ ) {
                calculateSum( i, j );
            }        
        }
    }

    private void calculateSum( int layer, int neuron ) {
        double sum = 0;
        for ( int i = 0; i < layers[layer - 1]; i++ ) {
            sum += activations[layer - 1][i] * params.weights[layer - 1][i * layers[layer] + neuron];
        }
        sum += params.biases[layer - 1][neuron];
        activations[layer][neuron] = activationFunction( sum );
    }

    public static double activationFunction( double sum ) {
        return sum < 0 ? 0.0 : sum;
        //return sum <= 0.0 ? 0.0 : 1.0;
        //return 1.0 / ( 1.0 + Math.exp(-sum) );
    }
    
    FeedForwardNeuralNetworkParameters calculateCostGradient( TrainingExample example ) throws Exception {
        // TODO: Implement

        return null;
    }

    void trainEpoch( TrainingExample[] examples ) throws Exception {
        FeedForwardNeuralNetworkParameters paramsAcc = new FeedForwardNeuralNetworkParameters(layers);

        for ( TrainingExample example : examples ) {
            FeedForwardNeuralNetworkParameters params = calculateCostGradient(example);
            paramsAcc.add( params );
        }
        paramsAcc.multiply( 1.0 / examples.length );

        params.add( paramsAcc );
    }

    private int[] layers;

    private FeedForwardNeuralNetworkParameters params;
    private double [][] activations;

}
