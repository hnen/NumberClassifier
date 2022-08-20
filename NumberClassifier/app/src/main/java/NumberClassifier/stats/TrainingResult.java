package NumberClassifier.stats;

import NumberClassifier.train.TrainConfig;

public class TrainingResult {

    public int version;

    public String layers;
    public String activations;
    public double learningRate;
    public double initWeightsMin;
    public double initWeightsMax;
    public double initBiases;

    public int epochs;
    public int miniBatchSize;

    public double accuracy;
    public double trainDuration;

    public TrainingResult(TrainConfig conf, double accuracy, double duration) {
        version = 1;
        
        String[] strlayers = new String[conf.layers.length];
        for (int i = 0; i < conf.layers.length; i++) {
            strlayers[i] = Integer.toString(conf.layers[i]);
        }
        layers = String.join("-", strlayers);

        activations = conf.activation;
        learningRate = conf.learningRate;
        initWeightsMin = conf.initWeights[0];
        initWeightsMax = conf.initWeights[1];
        initBiases = conf.initBiases;
        epochs = conf.epochs;
        miniBatchSize = conf.miniBatchSize;
        this.accuracy = accuracy;
        trainDuration = duration;        
    }
}
