package NumberClassifier.neuralnetwork;

/**
 * Rectified Linear Unit activation function.
 * <p>
 * ReLU function has gained popularity recent years as an activation function due to its simplicity
 * and its ability to perform well. When using to train a neural network, its biases should be initialized
 * to be slightly larger than 0.
 * </p>
 */
public class ReLUActivationFunction extends ActivationFunction {
    
    /**
     * Calculate value of the activation function.
     * @param x Activation function parameter.
     * @return Value of the activation function at {@code x}.
     */
    public double value( double x ) {
        return x > 0 ? x : 0.0;
    }
    
    /**
     * Calculate derivative of the activation function.
     * @param x Activation function parameter.
     * @return Derivative of the activation funcation at {@code x}.
     */
    public double derivative( double x ) {
        return x > 0 ? 1.0 : 0.0;
    }

}
