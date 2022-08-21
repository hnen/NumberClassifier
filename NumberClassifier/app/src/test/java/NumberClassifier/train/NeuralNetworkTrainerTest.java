package NumberClassifier.train;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import NumberClassifier.data.TrainingExample;
import NumberClassifier.neuralnetwork.SigmoidActivationFunction;
import NumberClassifier.neuralnetwork.UniformWeightInitMethod;

public class NeuralNetworkTrainerTest {
    
    @Test
    public void testTrain() throws Exception {

        TrainConfig testConf = new TrainConfig();

        testConf.trainingData = "";
        testConf.trainingLabels = "";
        testConf.testData = "";
        testConf.testLabels =  "";
        testConf.layers = new int[] { 2, 3, 2 };
        testConf.activation = "sigmoid";
        testConf.initWeightsMethod = new UniformWeightInitMethod(-1.0, 1.0);
        testConf.learningRate = new double[]{ 10.0 };
        testConf.initWeightsUniformRange = new double[] { -1.0, 1.0 };
        testConf.initBiases = 0.0;
        testConf.epochs = new int[]{ 5000 };
        testConf.miniBatchSize = 2;

        TrainingExample[] trainExamples = new TrainingExample[] {
            new TrainingExample( new double[] { 0.0, 0.0 }, new double[] { 1.0, 0.0 } ),
            new TrainingExample( new double[] { 1.0, 0.0 }, new double[] { 0.0, 1.0 } ),
            new TrainingExample( new double[] { 0.0, 1.0 }, new double[] { 0.0, 1.0 } ),
            new TrainingExample( new double[] { 1.0, 1.0 }, new double[] { 1.0, 0.0 } )
        };

        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer( testConf );
        trainer.train(trainExamples, trainExamples);

        double acc = trainer.testAccuracy(trainExamples);
        assertEquals(1.0, acc);
    }

}
