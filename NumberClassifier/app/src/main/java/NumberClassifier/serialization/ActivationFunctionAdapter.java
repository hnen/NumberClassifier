package NumberClassifier.serialization;

import java.lang.reflect.Type;
import com.google.gson.*;

import NumberClassifier.neuralnetwork.IActivationFunction;
import NumberClassifier.neuralnetwork.ReLUActivationFunction;
import NumberClassifier.neuralnetwork.SigmoidActivationFunction;

/**
 * Implements serialization for IActivationFunction object.
 */
public class ActivationFunctionAdapter implements JsonSerializer<IActivationFunction>, JsonDeserializer<IActivationFunction> {

    /**
     * Serializes IActivationFunction object to JSON.
     * @param src The IActivationFunction object to serialize.
     * @param typeOfSrc The type of the IActivationFunction object.
     * @return The JSON representation of the IActivationFunction object.
     */
    public JsonElement serialize(IActivationFunction f, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", f.getClass().getName());
        return wrapper;
    }

    /**
     * Deserialize an IActivationFunction object from JSON.
     */
    public IActivationFunction deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = (JsonObject) elem;
        String typeName = wrapper.get("type").getAsString();
        if ( typeName.equals( SigmoidActivationFunction.class.getName() ) ) {
            return new SigmoidActivationFunction();
        } else if ( typeName.equals( ReLUActivationFunction.class.getName() ) ) {
            return new ReLUActivationFunction();
        } else {
            throw new JsonParseException("Unknown activation function type: " + typeName);
        }
    }

}