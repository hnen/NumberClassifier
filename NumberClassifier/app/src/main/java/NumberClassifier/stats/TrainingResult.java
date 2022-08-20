package NumberClassifier.stats;

import NumberClassifier.neuralnetwork.UniformWeightInitMethod;
import NumberClassifier.neuralnetwork.WeightInitMethodFactory;
import NumberClassifier.train.TrainConfig;

public class TrainingResult {

    public int version;

    public String layers;
    public String activations;
    public double learningRate;
    public String initWeightsMethod;
    public double initWeightsMin;
    public double initWeightsMax;
    public double initBiases;

    public int epochs;
    public int miniBatchSize;

    public double accuracy;
    public double trainDuration;

    public TrainingResult(TrainConfig conf, double accuracy, double duration) {
        // 1 - initial implementation
        // 2 - xavier weights
        // 3&4 - he weights
        // 5 - parameterized weights
        version = 5;
        
        String[] strlayers = new String[conf.layers.length];
        for (int i = 0; i < conf.layers.length; i++) {
            strlayers[i] = Integer.toString(conf.layers[i]);
        }
        layers = String.join("-", strlayers);

        activations = conf.activation;
        learningRate = conf.learningRate;
        initWeightsMethod = WeightInitMethodFactory.nameOf(conf.initWeightsMethod);
        if ( conf.initWeightsMethod instanceof UniformWeightInitMethod ) {
            UniformWeightInitMethod init = (UniformWeightInitMethod) conf.initWeightsMethod;
            initWeightsMin = init.getMin();
            initWeightsMax = init.getMax();
        } else {
            initWeightsMin = 0.0;
            initWeightsMax = 0.0;
        }
        initBiases = conf.initBiases;
        epochs = conf.epochs;
        miniBatchSize = conf.miniBatchSize;
        this.accuracy = accuracy;
        trainDuration = duration;        
    }
}
