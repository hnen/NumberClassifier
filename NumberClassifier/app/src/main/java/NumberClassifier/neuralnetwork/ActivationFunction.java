package NumberClassifier.neuralnetwork;

/**
 * Interface for neural network activation function.
 */
public abstract class ActivationFunction {
    
    /**
     * Calculate value of the activation function.
     * @param x Activation function parameter.
     * @return Value of the activation function at {@code x}.
     */
    abstract double value(double x);

    /**
     * Calculate derivative of the activation function.
     * @param x Activation function parameter.
     * @return Derivative of the activation funcation at {@code x}.
     */
    abstract double derivative(double x);

    

}
