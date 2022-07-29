package NumberClassifier;

/**
 * Interface for neural network activation function.
 */
public interface IActivationFunction {
    double value(double x);
    double derivative(double x);
}
