package NumberClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Entry point for the application.
 * <p>
 * This is a currently an automatically generated placeholder, waiting for implementation.
 * </p>
 */
public class App {

    public static void main(String[] args) {
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }        


    }
}
