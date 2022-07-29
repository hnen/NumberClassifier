package NumberClassifier;

/**
 * Rectified Linear Unit activation function.
 * <p>
 * ReLU function has gained popularity recent years as an activation function due to its simplicity
 * and its ability to perform well. When using to train a neural network, its biases should be initialized
 * to be slightly larger than 0.
 * </p>
 */
public class ReLUActivationFunction implements IActivationFunction {
    
    public double value( double x ) {
        return x > 0 ? x : 0.0;
    }
    
    public double derivative( double x ) {
        return x > 0 ? 1.0 : 0.0;
    }

}
