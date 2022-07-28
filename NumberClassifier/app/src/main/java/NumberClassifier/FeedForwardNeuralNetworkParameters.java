package NumberClassifier;

public class FeedForwardNeuralNetworkParameters {

    public  FeedForwardNeuralNetworkParameters( int[] layers ) {
        this.weights = new double[layers.length - 1][];
        this.biases = new double[layers.length - 1][];
        
        for( int i = 0; i < layers.length - 1; i++ ) {
            weights[i] = new double[layers[i] * layers[i + 1]];
            biases[i] = new double[layers[i + 1]];
        }
    }

    public double [][] biases;
    public double [][] weights;    

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
    
            if ( another.biases[i].length != biases[i].length ) {
                throw new Exception( "Parameters have different number of neuron biases on a layer." );
            }
    
            for ( int j = 0; j < weights[i].length; j++ ) {
                weights[i][j] += another.weights[i][j];
                biases[i][j] += another.biases[i][j];
            }
        }
    }

    public void multiply( double x ) {
        // TODO
    }

}
