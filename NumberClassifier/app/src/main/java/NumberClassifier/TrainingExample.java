package NumberClassifier;

/**
 * Represents an input-output pair for training neural networks.
 */
public class TrainingExample {

    /**
     * Initialize data structure with {@code input} and {@code output} being null.
     */
    public TrainingExample() {}

    /**
     * Initialize data structure with input and output.
     * @param input Input data.
     * @param output Output data.
     */
    public TrainingExample( double[] input, double[] output ) {
        this.input = input;
        this.output = output;
    }

    /**
     * Input values for the training example. Length of the array should be the same as number of input neurons at the neural network's input layer.
     */
    public double [] input;

    /**
     * Output values for the training example. Length of the array should be the same as number of output neurons at the neural network's output layer.
     */
    public double [] output;

}
