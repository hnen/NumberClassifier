package NumberClassifier.serialization;

import java.lang.reflect.Type;
import com.google.gson.*;

import NumberClassifier.neuralnetwork.ActivationFunctionFactory;
import NumberClassifier.neuralnetwork.IActivationFunction;

/**
 * Implements serialization for IActivationFunction object.
 */
public class ActivationFunctionAdapter implements JsonSerializer<IActivationFunction>, JsonDeserializer<IActivationFunction> {

    /**
     * Serializes IActivationFunction object to JSON.
     * @param f The IActivationFunction object to serialize.
     * @param interfaceType The type of the IActivationFunction object.
     * @param context The GSON context.
     * @return The JSON representation of the IActivationFunction object.
     */
    public JsonElement serialize(IActivationFunction f, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", ActivationFunctionFactory.nameOf(f) );
        return wrapper;
    }

    /**
     * Deserialize an IActivationFunction object from JSON.
     * @param elem The JSON representation of the IActivationFunction object.
     * @param interfaceType The type of the IActivationFunction object.
     * @param context The GSON context.
     * @return The IActivationFunction object.
     */
    public IActivationFunction deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = (JsonObject) elem;
        String typeName = wrapper.get("type").getAsString();
        return ActivationFunctionFactory.create(typeName);
    }

}