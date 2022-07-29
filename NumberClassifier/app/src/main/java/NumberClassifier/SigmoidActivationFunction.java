package NumberClassifier;

/**
 * Sigmoid activation function.
 * <p>
 * Can be passed as activation function for FeedForwardNeuralNetwork. Sigmoid activation function has classically been a standard choice for neural networks.
 * </p>
 */
public class SigmoidActivationFunction implements IActivationFunction {
    
    /**
     * Calculate value of the activation function.
     * @param x Activation function parameter.
     * @return Value of the activation function at {@code x}.
     */
    public double value( double x ) {
        return 1.0 / ( 1.0 + Math.exp(-x) );
    }
    
    /**
     * Calculate derivative of the activation function.
     * @param x Activation function parameter.
     * @return Derivative of the activation funcation at {@code x}.
     */
    public double derivative( double x ) {
        return value(x) * (1 - value(x));
    }

}
