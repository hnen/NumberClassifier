package NumberClassifier;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  

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
    public IActivationFunction activationFunction;
    public double learningRate;
    public double[] initWeights;
    public double initBiases;
    public int epochs;
    public int miniBatchSize;

    /**
     * Load the training configuration from JSON data.
     * @param jsonData String containing the JSON data.
     * @return TrainConfig object
     */
    public static TrainConfig loadJSON( String jsonData ) throws Exception {
        GsonBuilder builder = new GsonBuilder(); 
        Gson gson = builder.create();

        TrainConfig conf = gson.fromJson(jsonData, TrainConfig.class);
        switch( conf.activation ) {
            case "sigmoid":
                conf.activationFunction = new SigmoidActivationFunction();
                break;
            case "relu":
                conf.activationFunction = new ReLUActivationFunction();
                break;
            default:
                throw new IllegalArgumentException("Unknown activation function: " + conf.activation);
        }

        return conf;
    }

}
