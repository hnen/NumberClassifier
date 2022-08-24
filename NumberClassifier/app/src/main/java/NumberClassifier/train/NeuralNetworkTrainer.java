package NumberClassifier.train;

import NumberClassifier.data.TrainingExample;
import NumberClassifier.neuralnetwork.ActivationFunctionFactory;
import NumberClassifier.neuralnetwork.FeedForwardNeuralNetwork;
import NumberClassifier.neuralnetwork.FeedForwardNeuralNetworkParameters;

/**
 * Implements training strategy for FeedForwardNeuralNetwork. 
 * This includes dividing the training data into mini-batches and measuring the network accuracy.
 */
public class NeuralNetworkTrainer {

    public class LossHistoryDatapoint {
        public int epoch;
        public double loss;
        public boolean phaseChange;

        LossHistoryDatapoint(int epoch, double loss, boolean phaseChange) {
            this.epoch = epoch;
            this.loss = loss;
            this.phaseChange = phaseChange;
        }

    }

    private double[] accuracyHistory;
    private LossHistoryDatapoint[] lossHistory;

    /**
     * Construct NeuralNetworkTrainer with the given configuration.
     * @param trainConfig The configuration to use.
     * @throws Exception
     */
    public NeuralNetworkTrainer(TrainConfig trainConfig) throws Exception {
        this.trainConfig = trainConfig;

        this.nn = new FeedForwardNeuralNetwork( ActivationFunctionFactory.create(trainConfig.activation), trainConfig.layers );

        lossHistory = new LossHistoryDatapoint[0];

        trainConfig.initWeightsMethod.initializeWeights(nn.getParameters());
        nn.setBiases(trainConfig.initBiases);
    }
    
    public FeedForwardNeuralNetwork getNeuralNetwork() {
        return nn;
    }

    void addLossHistoryDatapoint(int epoch, double loss, boolean phaseChange) {
        LossHistoryDatapoint[] lossHistories = new LossHistoryDatapoint[lossHistory.length + 1];
        System.arraycopy(lossHistory, 0, lossHistories, 0, lossHistory.length);
        lossHistories[lossHistory.length] = new LossHistoryDatapoint(epoch, loss, phaseChange);
        lossHistory = lossHistories;     
    }

    /**
     * Train the neural network.
     * @param trainingExamples Training examples to use.
     * @throws Exception
     */
    public void train(TrainingExample[] trainingExamples, TrainingExample[] testExamples) throws Exception {
        int numBenchmarks = testExamples.length / 10;
        TrainingExample[] benchmarkBatch = new TrainingExample[numBenchmarks];
        for (int i = 0; i < numBenchmarks; i++) {
            benchmarkBatch[i] = testExamples[i];
        }
        trainingEpoch = 0;

        gradExpAvg = new FeedForwardNeuralNetworkParameters(nn.getParameters().getLayers());

        {
            double accuracy = testAccuracy(benchmarkBatch);
            accuracyHistory = new double[1];
            accuracyHistory[0] = accuracy;
        }

        for ( int p = 0; p < trainConfig.epochs.length; p++ ) {
            for ( int i = 0; i < trainConfig.epochs[p]; i++ ) {
                trainingEpoch++;
                TrainingExample[] batch = pickMiniBatch(trainingExamples, trainConfig.miniBatchSize);
                trainEpoch(nn, batch, trainConfig.learningRate[p]);
                if ( i % 100 == 0 ) {
                    loss = nn.calculateCost(benchmarkBatch);
                    addLossHistoryDatapoint(trainingEpoch, loss, false);
                }
            }

            double accuracy = testAccuracy(benchmarkBatch);
            double loss = nn.calculateCost(benchmarkBatch);

            double[] accuracyHistories = new double[accuracyHistory.length + 1];
            System.arraycopy(accuracyHistory, 0, accuracyHistories, 0, accuracyHistory.length);
            accuracyHistories[accuracyHistory.length] = accuracy;
            accuracyHistory = accuracyHistories;

            addLossHistoryDatapoint(trainingEpoch, loss, true);
        }
    }

    FeedForwardNeuralNetworkParameters gradExpAvg;

    public FeedForwardNeuralNetworkParameters trainEpoch( FeedForwardNeuralNetwork nn, TrainingExample[] examples, double learningRate ) throws Exception {
        FeedForwardNeuralNetworkParameters grad = new FeedForwardNeuralNetworkParameters(nn.getParameters().getLayers());

        for ( TrainingExample example : examples ) {
            FeedForwardNeuralNetworkParameters grad0 = nn.calculateCostGradient(example);
            grad.add( grad0 );
        }
        grad.multiply( 1.0 / examples.length );

        // RMSProp
        final double ro = this.trainConfig.rmspropMomentum;
        final double epsilon = 1e-8;
        FeedForwardNeuralNetworkParameters gradSq = grad.clone();
        gradSq.square();
        gradExpAvg.multiply( ro );
        gradSq.multiply( 1.0 - ro );
        gradExpAvg.add( gradSq );
        
        FeedForwardNeuralNetworkParameters gradExpAvg0 = gradExpAvg.clone();
        gradExpAvg0.add(epsilon);
        gradExpAvg0.squareRoot();
        
        grad.divide(gradExpAvg0);
        grad.multiply( -1.0 * learningRate );
        
        nn.getParameters().add( grad );

        return grad;
    }


    /**
     * Get the current training epoch. Useful for retrieving the progress of training from another thread.
     * @return The current training epoch.
     */
    public int getTrainingEpoch() {
        return trainingEpoch;
    }

    public double getLatestLossEstimate() {
        return loss;
    }

    public double[] getAccuracyHistory() {
        return accuracyHistory;
    }

    public LossHistoryDatapoint[] getLossHistory() {
        return lossHistory;
    }

    /**
     * Measure accuracy of the neural network. Assumes that the network has already been trained.
     * @param testExamples Test examples to use.
     * @return Accuracy of the network, a value between 0-1.
     * @throws Exception
     */
    public double testAccuracy(TrainingExample[] testExamples) throws Exception {
        int correct = 0;
        for ( int i = 0; i < testExamples.length; i++ ) {
            int result = nn.getMaxActivation(testExamples[i].input);

            int exampleResult = 0;
            double max = testExamples[i].output[0];
            for ( int j = 1; j < testExamples[i].output.length; j++ ) {
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

    int batchIndex = 0;

    private TrainingExample[] pickMiniBatch( TrainingExample[] trainingExamples, int batchSize ) {

        /* 
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
        */

        TrainingExample[] examples = new TrainingExample[trainConfig.miniBatchSize];
        for ( int j = 0; j < trainConfig.miniBatchSize; j++ ) {
            int i = batchIndex % trainingExamples.length;
            batchIndex++;
            examples[j] = trainingExamples[i];
        }

        return examples;
    }

    private FeedForwardNeuralNetwork nn;
    private TrainConfig trainConfig;
    private int trainingEpoch;
    private double loss;
}
