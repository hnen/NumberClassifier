package NumberClassifier;

/**
 * Implements training strategy for FeedForwardNeuralNetwork. This includes dividing the training data into mini-batches and measuring the network accuracy.
 */
public class NeuralNetworkTrainer {

    /**
     * Construct NeuralNetworkTrainer with the given configuration.
     * @param trainConfig The configuration to use.
     * @throws Exception
     */
    public NeuralNetworkTrainer(TrainConfig trainConfig) throws Exception {
        this.trainConfig = trainConfig;

        this.nn = new FeedForwardNeuralNetwork( trainConfig.activationFunction, trainConfig.layers );
        nn.randomizeWeights(trainConfig.initWeights[0], trainConfig.initWeights[1]);
        nn.setBiases(trainConfig.initBiases);
    }

    /**
     * Train the neural network.
     * @param trainingExamples Training examples to use.
     * @throws Exception
     */
    public void train(TrainingExample[] trainingExamples) throws Exception {
       
        for ( int i = 0; i < trainConfig.epochs; i++ ) {
            TrainingExample[] batch = pickMiniBatch(trainingExamples, trainConfig.miniBatchSize);

            nn.trainEpoch(batch, trainConfig.learningRate);

            if ( i % 100 == 0 ) {
                //double C1 = nn.calculateCost(trainingExamples);
                System.out.println( "Epoch " + i  );
            }
        }        
    }

    /**
     * Mesaure accuracy of the neural network. Assumes that the network has already been trained.
     * @param testExamples Test examples to use.
     * @return Accuracy of the network, a value between 0-1.
     * @throws Exception
     */
    double testAccuracy(TrainingExample[] testExamples) throws Exception {
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

        return (double)correct / testExamples.length;
    }

    private TrainingExample[] pickMiniBatch( TrainingExample[] trainingExamples, int batchSize ) {
        int[] batchIndices = new int[trainConfig.miniBatchSize];
        for ( int j = 0; j < trainConfig.miniBatchSize; j++ ) {                    
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

        TrainingExample[] examples = new TrainingExample[trainConfig.miniBatchSize];
        for ( int j = 0; j < trainConfig.miniBatchSize; j++ ) {
            examples[j] = trainingExamples[batchIndices[j]];
        }

        return examples;
    }

    private FeedForwardNeuralNetwork nn;
    private TrainConfig trainConfig;
}
