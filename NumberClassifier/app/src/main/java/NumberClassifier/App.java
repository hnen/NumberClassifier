package NumberClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Scanner;

import NumberClassifier.data.ImageSet;
import NumberClassifier.data.TrainingExample;
import NumberClassifier.gui.DrawFrame;
import NumberClassifier.gui.MainFrame;
import NumberClassifier.train.NeuralNetworkTrainer;
import NumberClassifier.train.TrainConfig;

import java.io.FileOutputStream;

/**
 * Entry point for the application.
 * <p>
 * This is a currently an automatically generated placeholder, waiting for implementation.
 * </p>
 */
public class App {

    public static void main(String[] args) throws Exception {        
        //DrawFrame frame = new DrawFrame();    
        MainFrame frame = new MainFrame();
        frame.openTraining(new File("test-conf.json"));
    }

    void train() {
        try {
            
            File file = new File("test-conf.json");
            TrainConfig conf = TrainConfig.loadJSON(new Scanner(file).useDelimiter("\\Z").next());
            NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(conf);
            
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
            double accuracy = trainer.testAccuracy(testExamples);

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
