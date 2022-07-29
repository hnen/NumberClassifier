package NumberClassifier;

public class ReLUActivationFunction implements IActivationFunction {
    
    public double value( double x ) {
        return x > 0 ? x : 0.0;
    }
    
    public double derivative( double x ) {
        return x > 0 ? 1.0 : 0.0;
    }

}
