package NumberClassifier.neuralnetwork;

public class ActivationFunctionFactory {
    
    /**
     * Create activation function.
     * @param type Type of activation function.
     * @return Activation function.
     */
    public static IActivationFunction create( String type ) {
        switch (type) {
            case "relu":
                return new ReLUActivationFunction();
            case "sigmoid":
                return new SigmoidActivationFunction();
            default:
                throw new IllegalArgumentException("Unknown activation function type: " + type);
        }
    }
    
    public static String[] getTypes() {
        return new String[] { "relu", "sigmoid" };
    }

    public static String nameOf(IActivationFunction method) {
        if (method instanceof ReLUActivationFunction) {
            return "relu";
        } else if (method instanceof SigmoidActivationFunction) {
            return "sigmoid";
        } else {
            throw new IllegalArgumentException("Unknown activation function type: " + method.getClass().getSimpleName());
        }
    }
    
}
