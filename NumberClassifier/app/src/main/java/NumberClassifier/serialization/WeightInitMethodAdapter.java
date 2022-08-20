package NumberClassifier.serialization;

import java.lang.reflect.Type;
import com.google.gson.*;

import NumberClassifier.neuralnetwork.IActivationFunction;
import NumberClassifier.neuralnetwork.IWeightInitMethod;
import NumberClassifier.neuralnetwork.ReLUActivationFunction;
import NumberClassifier.neuralnetwork.SigmoidActivationFunction;
import NumberClassifier.neuralnetwork.WeightInitMethodFactory;

/**
 * Implements serialization for IActivationFunction object.
 */
public class WeightInitMethodAdapter implements JsonSerializer<IWeightInitMethod>, JsonDeserializer<IWeightInitMethod> {

    /**
     * Serializes IWeightInitMethod object to JSON.
     * @param f The IWeightInitMethod object to serialize.
     * @param interfaceType The type of the IWeightInitMethod object.
     * @param context The GSON context.
     * @return The JSON representation of the IWeightInitMethod object.
     */
    public JsonElement serialize(IWeightInitMethod f, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", WeightInitMethodFactory.nameOf(f) );
        wrapper.add("data", context.serialize(f));
        return wrapper;
    }

    /**
     * Deserialize an IActivationFunction object from JSON.
     * @param elem The JSON representation of the IActivationFunction object.
     * @param interfaceType The type of the IActivationFunction object.
     * @param context The GSON context.
     * @return The IActivationFunction object.
     */
    public IWeightInitMethod deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = (JsonObject) elem;
        String typeName = wrapper.get("type").getAsString();
        IWeightInitMethod f = WeightInitMethodFactory.create(typeName);
        return context.deserialize(wrapper.get("data"), f.getClass());
    }

}