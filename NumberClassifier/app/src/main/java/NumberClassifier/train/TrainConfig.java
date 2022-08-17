package NumberClassifier.train;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

import NumberClassifier.neuralnetwork.IActivationFunction;
import NumberClassifier.neuralnetwork.ReLUActivationFunction;
import NumberClassifier.neuralnetwork.SigmoidActivationFunction;  

/**
 * Configuration for training and testing a neural network model. 
 */
public class TrainConfig {
    public String trainingData;
    public String trainingLabels;
    public String testData;
    public String testLabels;
    public String outFile;
    public int[] layers;
    public String activation;
    public double learningRate;
    public double[] initWeights;
    public double initBiases;
    public int epochs;
    public int miniBatchSize;

    public IActivationFunction getActivationFunction() {
        switch( activation ) {
            case "sigmoid":
                return new SigmoidActivationFunction();
            case "relu":
                return new ReLUActivationFunction();
            default:
                throw new IllegalArgumentException("Unknown activation function: " + activation);
        }
    }

    /**
     * Load the training configuration from JSON data.
     * @param jsonData String containing the JSON data.
     * @return TrainConfig object
     */
    public static TrainConfig loadJSON( String jsonData ) throws Exception {
        GsonBuilder builder = new GsonBuilder(); 
        Gson gson = builder.create();
        TrainConfig conf = gson.fromJson(jsonData, TrainConfig.class);
        return conf;
    }

}
