package NumberClassifier.train;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

import NumberClassifier.neuralnetwork.IWeightInitMethod;
import NumberClassifier.serialization.WeightInitMethodAdapter;

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
    public IWeightInitMethod initWeightsMethod;
    public double[] initWeightsUniformRange;
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
        Gson gson = builder
            .registerTypeAdapter(IWeightInitMethod.class, new WeightInitMethodAdapter())
            .create();
        TrainConfig conf = gson.fromJson(jsonData, TrainConfig.class);
        return conf;
    }

}
