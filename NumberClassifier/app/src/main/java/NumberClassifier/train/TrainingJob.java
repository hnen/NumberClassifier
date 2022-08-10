package NumberClassifier.train;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import NumberClassifier.data.ImageSet;
import NumberClassifier.data.TrainingExample;

public class TrainingJob extends Thread {

    private TrainConfig conf;
    NeuralNetworkTrainer trainer;
    double accuracy;

    public TrainingJob(TrainConfig conf) {
        this.conf = conf;
    }
    
    public double getProgress() {
        if ( trainer == null ) {
            return 0.0;
        }

        return (double)trainer.getTrainingEpoch() / conf.epochs;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void run() {
        try {
            trainer = new NeuralNetworkTrainer(conf);
            
            ImageSet imageSet = ImageSet.loadFromMNIST(
                    new FileInputStream(new File(conf.trainingData)),
                    new FileInputStream(new File(conf.trainingLabels)),
                    10);
            TrainingExample[] trainingExamples = imageSet.createTrainingExamples();
            trainer.train(trainingExamples);
            
            ImageSet testSet = ImageSet.loadFromMNIST(
                    new FileInputStream(new File(conf.testData)),
                    new FileInputStream(new File(conf.testLabels)),
                    10);
            TrainingExample[] testExamples = testSet.createTrainingExamples();
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
