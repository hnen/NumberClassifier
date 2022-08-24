package NumberClassifier.stats;

import java.util.Arrays;

import NumberClassifier.neuralnetwork.UniformWeightInitMethod;
import NumberClassifier.neuralnetwork.WeightInitMethodFactory;
import NumberClassifier.train.TrainConfig;
import NumberClassifier.train.NeuralNetworkTrainer.LossHistoryDatapoint;

public class TrainingResult {

    public int version;

    public String layers;
    public String activations;
    public String learningRate;
    public String initWeightsMethod;
    public double initWeightsMin;
    public double initWeightsMax;
    public double initBiases;

    public String epochs;
    public double rmspropMomentum;
    public int miniBatchSize;

    public double accuracy;
    public double trainDuration;


    public TrainingResult(TrainConfig conf, double accuracy, double duration, double[] accuracyHistory, LossHistoryDatapoint[] lossHistory) {
        // 1 - initial implementation
        // 2 - xavier weights
        // 3&4 - he weights
        // 5 - parameterized weights
        // 6 - scheduled learning rate and image transformation (centering)
        // 7 - image transformation (scaling)
        // 8 - RMSprop
        // 9 - static seed, rotating mini batches
        version = 9;
        
        String[] strlayers = new String[conf.layers.length];
        for (int i = 0; i < conf.layers.length; i++) {
            strlayers[i] = Integer.toString(conf.layers[i]);
        }
        layers = String.join("-", strlayers);

        activations = conf.activation;
        learningRate = String.join("-", Arrays.stream(conf.learningRate).mapToObj(Double::toString).toArray(String[]::new));

        initWeightsMethod = WeightInitMethodFactory.nameOf(conf.initWeightsMethod);
        if ( conf.initWeightsMethod instanceof UniformWeightInitMethod ) {
            UniformWeightInitMethod init = (UniformWeightInitMethod) conf.initWeightsMethod;
            initWeightsMin = init.getMin();
            initWeightsMax = init.getMax();
        } else {
            initWeightsMin = 0.0;
            initWeightsMax = 0.0;
        }
        this.initBiases = conf.initBiases;
        this.epochs = String.join("-", Arrays.stream(conf.epochs).mapToObj(Integer::toString).toArray(String[]::new));
        this.miniBatchSize = conf.miniBatchSize;
        this.rmspropMomentum = conf.rmspropMomentum;
        this.accuracy = accuracy;
        this.trainDuration = duration;
        //this.accuracyHistory = String.join("-", Arrays.stream(accuracyHistory).mapToObj(Double::toString).toArray(String[]::new));
        //this.lossHistory = String.join("-", Arrays.stream(lossHistory).mapToObj(Double::toString).toArray(String[]::new));
    }
}
