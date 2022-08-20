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


    public static String getCSVHeader() {
        StringBuilder header = new StringBuilder();
        header.append("\"version\"");
        header.append(",");
        header.append("\"layers\"");
        header.append(",");
        header.append("\"activations\"");
        header.append(",");
        header.append("\"learningRate\"");
        header.append(",");
        header.append("\"initWeightsMin\"");
        header.append(",");
        header.append("\"initWeightsMax\"");
        header.append(",");
        header.append("\"initBiases\"");
        header.append(",");
        header.append("\"epochs\"");
        header.append(",");
        header.append("\"miniBatchSize\"");
        header.append(",");
        header.append("\"accuracy\"");
        header.append(",");
        header.append("\"trainDuration\"");
        return header.toString();
    }    

    public String toCSVRow() {
        StringBuilder row = new StringBuilder();
        row.append("\"");
        row.append(version);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(layers);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(activations);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(learningRate);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(initWeightsMin);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(initWeightsMax);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(initBiases);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(epochs);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(miniBatchSize);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(accuracy);
        row.append("\"");
        row.append(",");
        row.append("\"");
        row.append(trainDuration);
        row.append("\"");
        row.append("\n");
        return row.toString();
    }



}
