package NumberClassifier;

/**
 * Interface for neural network activation function.
 */
public interface IActivationFunction {
    
    /**
     * Calculate value of the activation function.
     * @param x Activation function parameter.
     * @return Value of the activation function at {@code x}.
     */
    double value(double x);

    /**
     * Calculate derivative of the activation function.
     * @param x Activation function parameter.
     * @return Derivative of the activation funcation at {@code x}.
     */
    double derivative(double x);
}
