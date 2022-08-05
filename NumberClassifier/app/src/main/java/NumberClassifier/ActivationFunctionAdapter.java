package NumberClassifier;

import java.lang.reflect.Type;
import com.google.gson.*;

class ActivationFunctionAdapter implements JsonSerializer<IActivationFunction>, JsonDeserializer<IActivationFunction> {

    public JsonElement serialize(IActivationFunction f, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", f.getClass().getName());
        return wrapper;
    }

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