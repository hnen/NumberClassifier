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
            
            ImageSet imageSet = ImageSet.loadFromMNIST(
                    new FileInputStream(new File(conf.trainingData)),
                    new FileInputStream(new File(conf.trainingLabels)),
                    10);
            
            FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork( conf.activationFunction, conf.layers );
            nn.randomizeWeights(conf.initWeights[0], conf.initWeights[1]);
            nn.setBiases(conf.initBiases);
    
            TrainingExample[] trainingExamples = imageSet.createTrainingExamples();
            
            for ( int i = 0; i < conf.epochs; i++ ) {
                int[] batchIndices = new int[conf.miniBatchSize];
                for ( int j = 0; j < conf.miniBatchSize; j++ ) {                    
                    batchIndices[j] = (int) (Math.random() * trainingExamples.length);
                    boolean isDuplicate = false;
                    for( int k = 0; k < j; k++ ) {
                        if( batchIndices[k] == batchIndices[j] ) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if ( isDuplicate ) {
                        j--;
                    }
                }

                TrainingExample[] examples = new TrainingExample[conf.miniBatchSize];
                for ( int j = 0; j < conf.miniBatchSize; j++ ) {
                    examples[j] = trainingExamples[batchIndices[j]];
                }

                //double C0 = nn.calculateCost(examples);
                //System.out.println( "Epoch " + i + " before: " + C0 );

                nn.trainEpoch(examples, conf.learningRate);

                if ( i % 10 == 0 )
                {
                    double C1 = nn.calculateCost(examples);
                    System.out.println( "Epoch " + i + " after: " + C1 );
                }
            }

                
            ImageSet testSet = ImageSet.loadFromMNIST(
                new FileInputStream(new File(conf.testData)),
                new FileInputStream(new File(conf.testLabels)),
                10);
            TrainingExample[] testExamples = testSet.createTrainingExamples();

            int correct = 0;
            for ( int i = 0; i < testExamples.length; i++ ) {
                int result = nn.getMaxActivation(testExamples[i].input);

                int exampleResult = 0;
                double max = testExamples[i].output[0];
                for ( int j = 1; j < 10; j++ ) {
                    if ( testExamples[i].output[j] > max ) {
                        max = testExamples[i].output[j];
                        exampleResult = j;
                    }
                }

                if ( result == exampleResult )
                    correct++;
            }

            System.out.println("Accuracy: " + ((double)correct / testExamples.length) * 100.0 + "%" );
            

        } catch (Exception e) {
            e.printStackTrace();
        }        


    }
}
