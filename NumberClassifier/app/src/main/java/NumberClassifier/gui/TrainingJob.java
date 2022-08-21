package NumberClassifier.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import NumberClassifier.data.ImageSet;
import NumberClassifier.data.TrainingExample;
import NumberClassifier.train.NeuralNetworkTrainer;
import NumberClassifier.train.TrainConfig;

/**
 * Thread for running neural network training.
 */
public class TrainingJob extends Thread {

    private TrainConfig conf;
    private NeuralNetworkTrainer trainer;
    private double accuracy;
    private double duration;

    public TrainingJob(TrainConfig conf) {
        this.conf = conf;
    }

    public TrainingJob() {
    }
    
    public double getProgress() {
        if ( trainer == null ) {
            return 0.0;
        }

        return (double)trainer.getTrainingEpoch() / conf.getTotalEpochs();
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getTrainDuration() {
        return duration;
    }

    public double getLoss() {
        if ( trainer == null ) {
            return 0.0;
        }

        return trainer.getLatestLossEstimate();
    }

    public double[] getAccuracyHistory() {
        if ( trainer == null || trainer.getAccuracyHistory() == null ) {
            return new double[0];
        }

        return trainer.getAccuracyHistory();
    }

        public NeuralNetworkTrainer.LossHistoryDatapoint[] getLossHistory() {
        if ( trainer == null || trainer.getLossHistory() == null ) {
            return new NeuralNetworkTrainer.LossHistoryDatapoint[0];
        }

        return trainer.getLossHistory();
    }

    public void run() {
        try {
            trainer = new NeuralNetworkTrainer(conf);
            
            ImageSet imageSet = ImageSet.loadFromMNIST(
                    new FileInputStream(new File(conf.trainingData)),
                    new FileInputStream(new File(conf.trainingLabels)),
                    10, true);
            TrainingExample[] trainingExamples = imageSet.createTrainingExamples();
            
            ImageSet testSet = ImageSet.loadFromMNIST(
                    new FileInputStream(new File(conf.testData)),
                    new FileInputStream(new File(conf.testLabels)),
                    10, true);
            TrainingExample[] testExamples = testSet.createTrainingExamples();

            long start = System.currentTimeMillis();
            trainer.train(trainingExamples, testExamples);
            long end = System.currentTimeMillis();
            duration = (end - start) / 1000.0;
            

            accuracy = trainer.testAccuracy(testExamples);
            
            System.out.println("Accuracy: " + accuracy * 100.0 + "%" );
            
            File outFile = new File(conf.outFile);
            OutputStream stream = new FileOutputStream(outFile);
            trainer.getNeuralNetwork().serialize(stream);
            System.out.println( "Wrote to " + conf.outFile );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
