package NumberClassifier.neuralnetwork;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import NumberClassifier.data.TrainingExample;
import NumberClassifier.serialization.ActivationFunctionAdapter;

/**
 * Basic feed forward neural network.
 * <p>
 * Represents a standard multilayer feed forward neural network. The activation function can be configured.
 * Cost function is hard coded mean squared error.
 * </p>
 */
public class FeedForwardNeuralNetwork {
    
    /**
     * Initializes an empty neural network with layer architecture defined by the parameters.
     * 
     * @param activationFunction Activation function to be used in the model.
     * @param layers Number of neurons on each layer. The size of the array defines the number of layers. First element defines number of input neurons, and last element defines number of output neurons.
     * @throws Exception
     */
    public FeedForwardNeuralNetwork( IActivationFunction activationFunction, int[] layers ) throws Exception {
        if ( layers.length < 2 ) {
            throw new Exception( "There has to be at least 2 layers" );
        }

        this.activationFunction = activationFunction;
        this.layers = layers;
        this.params = new FeedForwardNeuralNetworkParameters(layers);

        this.activations = new double[layers.length][];
        this.weightedInputs = new double[layers.length][];
        for( int i = 0; i < layers.length; i++ ) {
            activations[i] = new double[layers[i]];
            weightedInputs[i] = new double[layers[i]];
        }
    }

    public static FeedForwardNeuralNetwork load( InputStream stream ) throws Exception {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(IActivationFunction.class, new ActivationFunctionAdapter())
            .create();

        try (Scanner s = new Scanner(stream)) {
            String json = s.useDelimiter("\\Z").next();
            return gson.fromJson(json, FeedForwardNeuralNetwork.class);
        }
    }

    /**
     * Set each weight in the network to a uniformly distributed random number.
     * @param min Minimum value for the weight.
     * @param max Maximum value for the weight.
     */
    public void randomizeWeights( double min, double max ) {
        params.randomizeWeights( min, max );
    }

    /**
     * Set each bias in the network to a specified value.
     * @param value The value where bias should be set to.
     */
    public void setBiases( double value ) {
        params.setBiases( value );
    }

    /**
     * Get number of input neurons.
     * @return Number of input neurons.
     */
    public int getNumInputs() {
        return layers[0];
    }

    /**
     * Get number of output neurons.
     * @return Number of output neurons.
     */
    public int getNumOutputs() {
        return layers[layers.length - 1];
    }

    /**
     * Get number of layers in the neural network.
     * @return Number of layers, including input and output layers.
     */
    public int getNumLayers() {
        return layers.length;
    }

    /**
     * Get weights for connections from layer {@code layer} to {@code layer + 1}.
     * @param layer Layer index.
     * @return Array of connection weights. Index of connection from {@code i = neuron} on layer to {@code j = neuron on layer + 1} is {@code N * i + j}, where N is number of neurons on layer {@code layer}.
     */
    public double[] getWeights( int layer ) {
        return params.weights[layer];
    }

    /**
     * Get biases for neurons for layer {@code layer}.
     * @param layer Layer index. Note that input input layer ({@code layer = 0}) doesn't have biases.
     * @return Array of biases for each neuron.
     */
    public double[] getBiases( int layer ) {
        return params.biases[layer - 1];
    }

    /**
     * Get neuron activation values on a layer. The activation values will get filled when calling {@code feedforward()}
     * @param layer Layer number to get activations from. 0 is first layer (i.e. input layer) and layer count - 1 is the output layer.
     * @return Array of neuron activation values.
     */
    public double[] getActivations(int layer) {
        return activations[layer];
    }

    /**
     * Set input activation values. I.e, activation values on layer 0. 
     * @param input Input values. Size of the layer must be the same as {@code getNumInputs()}.
     * @throws Exception
     */
    public void setInput( double[] input ) throws Exception {
        if( input.length != activations[0].length ) {
            throw new Exception( "Invalid number of inputs." );
        }
        activations[0] = input.clone();
    }

    /**
     * Set weights for outgoing connections, from {@code layer} to {@code layer + 1}. 
     * @param layer Layer index.
     * @param weights Array of connection weights. Index of connection from {@code i = neuron} on layer to {@code j = neuron on layer + 1} is {@code N * i + j}, where N is number of neurons on layer {@code layer}.
     * @throws Exception
     */
    public void setWeights( int layer, double[] weights ) throws Exception {
        params.setWeights( layer, weights );
    }

    /**
     * Set neuron biases.
     * @param layer Layer index.
     * @param biases Array of biases for neurons on layer {@code layer}.
     * @throws Exception
     */
    public void setBiases( int layer, double[] biases ) throws Exception {
        if ( biases.length != layers[layer] )  {
            throw new Exception( "Invalid number of biases." );
        }
        params.biases[layer - 1] = biases.clone();
    }

    /**
     * Get output neuron activations. {@code feedforward()} should be called first to evaluate the values.
     * @return Output neuron activations.
     */
    public double[] getOutput() {
        return this.activations[this.activations.length - 1].clone();
    }
    
    /**
     * Calculate neuron activations. Usually you want to first set input activations by calling {@code setInput}, and after feedforwarding, you usually want to get the outpt values with {@code getOutput}.
     */
    public void feedForward() {
        for ( int i = 1; i < layers.length; i++ ) {
            for ( int j = 0; j < layers[i]; j++ ) {
                calculateSum( i, j );
            }        
        }
    }

    public int getMaxActivation( double[] input ) throws Exception {
        setInput(input);
        feedForward();
        int maxIndex = 0;
        double maxValue = activations[activations.length - 1][0];
        for ( int i = 1; i < activations[activations.length - 1].length; i++ ) {
            if ( activations[activations.length - 1][i] > maxValue ) {
                maxIndex = i;
                maxValue = activations[activations.length - 1][i];
            }
        }
        //System.out.println( maxValue );
        return maxIndex;
    }

    private void calculateSum( int layer, int neuron ) {
        double sum = 0;
        for ( int i = 0; i < layers[layer - 1]; i++ ) {
            sum += activations[layer - 1][i] * params.weights[layer - 1][i * layers[layer] + neuron];
        }
        sum += params.biases[layer - 1][neuron];
        weightedInputs[layer][neuron] = sum;
        activations[layer][neuron] = activationFunction.value( sum );
    }

    /**
     * Calculate value of the cost function. The method assumes that feedforward has been executed. The cost function is mean squared error.
     * @param targetOutput Target output values for output neuron activations.
     * @return Value of the cost function.
     */
    public double calculateCost( double[] targetOutput ) {
        double C = 0.0;
        for ( int i = 0; i < layers[layers.length - 1]; i++ ) {
            double delta = (activations[layers.length - 1][i] - targetOutput[i]);
            C += delta * delta;
        }
        C /= layers[layers.length - 1];

        return C;
    }

    /**
     * Calculate average for the cost function over training examples.
     * @param examples Array oft training examples.
     * @return Average of cost function.
     * @throws Exception
     */
    public double calculateCost( TrainingExample[] examples ) throws Exception {
        double C = 0.0;
        for ( TrainingExample ex : examples ) {
            setInput(ex.input);
            feedForward();
            C += calculateCost( ex.output );
        }
        return C / examples.length;
    }

    public FeedForwardNeuralNetworkParameters trainEpoch( TrainingExample[] examples, double learningRate ) throws Exception {
        FeedForwardNeuralNetworkParameters paramsAcc = new FeedForwardNeuralNetworkParameters(layers);

        for ( TrainingExample example : examples ) {
            FeedForwardNeuralNetworkParameters params = calculateCostGradient(example);
            params.multiply( -1.0 * learningRate );
            paramsAcc.add( params );
        }
        paramsAcc.multiply( 1.0 / examples.length );

        params.add( paramsAcc );

        return paramsAcc;
    }


    /**
     * Calculate gradient of the cost function related to the neural network parameters.
     * @param example Training example to calculate the cost gradient against.
     * @return Parameters containing the gradient.
     * @throws Exception
     */
    public FeedForwardNeuralNetworkParameters calculateCostGradient( TrainingExample example ) throws Exception {
        setInput( example.input );
        feedForward();

        double[][] errors = calculateErrors(example.output);

        FeedForwardNeuralNetworkParameters gradient = new FeedForwardNeuralNetworkParameters(layers);
        for ( int i = 0; i < layers.length; i++ ) {
            if ( i > 0 ) {
                for ( int j = 0; j < layers[i]; j++) {
                    gradient.biases[i - 1][j] = errors[i][j];
                }
            }

            if ( i < layers.length - 1 ) {
                for ( int j = 0; j < layers[i]; j++) {
                    for ( int k = 0; k < layers[i + 1]; k++) {
                        gradient.weights[i][j * layers[i + 1] + k] = errors[i + 1][k] * activations[i][j];                    
                    }
                }
            }
        }

        return gradient;
    }

    /**
     * Calculate gradient of the cost function related to the weighted input for each of the hidden and output layer neurons. These values are used in backpropagation algorithm.
     * @param targetOutput Target values for output neurons, where cost function minimum should be.
     * @return Error as 2-dimensional array, where first index represents layer and second index a neuron on that layer.
     * @throws Exception
     */
    double[][] calculateErrors(double[] targetOutput) throws Exception {
        double[][] errors = new double[layers.length][];
        errors[layers.length - 1] = calculateOutputError(targetOutput);

        for ( int L = layers.length - 2; L >= 0; L-- ) {
            double[][] w = params.weightMatrix(L);
            errors[L] = new double[layers[L]];
            for ( int i = 0; i < layers[L]; i++ ) {
                for ( int j = 0; j < layers[L+1]; j++) {
                    errors[L][i] += errors[L+1][j] * w[i][j];
                }
                errors[L][i] *= activationFunction.derivative( weightedInputs[L][i] );
            }
        }

        return errors;
    }

    /**
     * Calculate gradient of the cost function related to the weighted input for each of the output neurons. These values are used in backpropagation algorithm.
     * @param targetOutput Target values for output neurons, where cost function minimum should be.
     * @return Cost function gradients for each output neuron.
     */
    public double[] calculateOutputError(double[] targetOutput) {
        double[] currentOutput = getOutput();
        double[] currentSum = weightedInputs[layers.length-1];
        double[] ret = new double[currentOutput.length];
        for ( int i = 0; i < currentOutput.length; i++ ) {
            ret[i] = (currentOutput[i] - targetOutput[i]) * activationFunction.derivative(currentSum[i]);
        }
        return ret;
    }

    /**
     * Serialize the network to a JSON file.
     * @param out Output stream to write the network as JSON text.
     * @throws Exception
     */
    public void serialize(OutputStream out) throws Exception {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(IActivationFunction.class, new ActivationFunctionAdapter())
            .create();

        String json = gson.toJson(this);
        out.write(json.getBytes());
    }

    @Override 
    public String toString() {
        return params.toString();
    }

    private IActivationFunction activationFunction;
    private int[] layers;

    private FeedForwardNeuralNetworkParameters params;
    private double [][] activations;
    private double [][] weightedInputs;

    

}
