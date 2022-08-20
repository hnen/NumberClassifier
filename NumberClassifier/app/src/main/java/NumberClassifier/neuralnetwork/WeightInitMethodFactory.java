package NumberClassifier.neuralnetwork;

public class WeightInitMethodFactory {

    public static IWeightInitMethod create( String type ) {
        switch (type) {
            case "uniform":
                return new UniformWeightInitMethod(-1.0, 1.0);
            case "xavier":
                return new XavierWeightInitMethod();
            case "he":
                return new HeWeightInitMethod();
            default:
                throw new IllegalArgumentException("Unknown activation function type: " + type);
        }
    }

    public static String nameOf(IWeightInitMethod method) {
        if (method instanceof UniformWeightInitMethod) {
            return "uniform";
        } else if (method instanceof XavierWeightInitMethod) {
            return "xavier";
        } else if (method instanceof HeWeightInitMethod) {
            return "he";
        } else {
            throw new IllegalArgumentException("Unknown weight init method: " + method);
        }
    }
    
    public static String[] getTypes() {
        return new String[] { "uniform", "xavier", "he" };
    }

}
