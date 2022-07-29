package NumberClassifier;

public class SigmoidActivationFunction implements IActivationFunction {
    
    public double value( double x ) {
        return 1.0 / ( 1.0 + Math.exp(-x) );
    }
    
    public double derivative( double x ) {
        return value(x) * (1 - value(x));
    }

}
