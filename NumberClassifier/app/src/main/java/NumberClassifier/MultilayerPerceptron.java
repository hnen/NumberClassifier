package NumberClassifier;

/**
 * Basic feed forward neural network.
 */
public class MultilayerPerceptron {
    
    public MultilayerPerceptron( int[] layers ) {
        this.layers = layers;
        this.weights = new double[layers.length - 1][];
        this.biases = new double[layers.length - 1][];
        this.outputs = new double[layers.length][];

        for( int i = 0; i < layers.length - 1; i++ ) {
            weights[i] = new double[layers[i] * layers[i + 1]];
            biases[i] = new double[layers[i + 1]];
        }

        for( int i = 0; i < layers.length; i++ ) {
            outputs[i] = new double[layers[i]];
        }
    }

    public void setInput( double[] input ) throws Exception {
        if( input.length != outputs[0].length ) {
            throw new Exception( "Invalid number of inputs." );
        }
        outputs[0] = input.clone();
    }

    public void setWeights( int layer, double[] weights ) throws Exception {
        if ( weights.length != this.weights[layer].length ) {
            throw new Exception( "Invalid number of weights." );
        }
        this.weights[layer] = weights.clone();
    }

    public void setBiases( int layer, double[] biases ) throws Exception {
        if ( biases.length != this.layers[layer] )  {
            throw new Exception( "Invalid number of biases." );
        }
        this.biases[layer - 1] = biases.clone();
    }

    public double[] getOutput() {
        return this.outputs[this.outputs.length - 1].clone();
    }
    
    public void calculateOutputs() {
        for ( int i = 1; i < layers.length; i++ ) {
            for ( int j = 0; j < layers[i]; j++ ) {
                calculateSum( i, j );
            }        
        }
    }

    private void calculateSum( int layer, int neuron ) {
        double sum = 0;
        for ( int i = 0; i < layers[layer - 1]; i++ ) {
            sum += outputs[layer - 1][i] * weights[layer - 1][i * layers[layer] + neuron];
        }
        sum += biases[layer - 1][neuron];
        outputs[layer][neuron] = activationFunction( sum );
    }

    public static double activationFunction( double sum ) {
        return sum < 0 ? 0.0 : sum;
        //return sum <= 0.0 ? 0.0 : 1.0;
        //return 1.0 / ( 1.0 + Math.exp(-sum) );
    }

    private int[] layers;

    private double [][] biases;
    private double [][] weights;
    private double [][] outputs;

}
