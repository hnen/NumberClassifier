package NumberClassifier;

/**
 * Sigmoid activation function.
 * <p>
 * Can be passed as activation function for FeedForwardNeuralNetwork. Sigmoid activation function has classically been a standard choice for neural networks.
 * </p>
 */
public class SigmoidActivationFunction implements IActivationFunction {
    
    public double value( double x ) {
        return 1.0 / ( 1.0 + Math.exp(-x) );
    }
    
    public double derivative( double x ) {
        return value(x) * (1 - value(x));
    }

}
