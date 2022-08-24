package NumberClassifier.serialization;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import NumberClassifier.neuralnetwork.IActivationFunction;
import NumberClassifier.neuralnetwork.ReLUActivationFunction;
import NumberClassifier.neuralnetwork.SigmoidActivationFunction;

import static org.junit.jupiter.api.Assertions.*;

public class ActivationFunctionAdapterTest {
 
    class TestClass {
        public IActivationFunction f;
    }

    @Test
    void testSerialize() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(IActivationFunction.class, new ActivationFunctionAdapter())
            .create();

        TestClass test = new TestClass();
        test.f = new ReLUActivationFunction();
        String json = gson.toJson(test);
        System.out.println(json);
        assertTrue(json.contains("\"type\": \"relu\""));

        TestClass deserialized = gson.fromJson(json, TestClass.class);
        assertTrue(deserialized.f instanceof ReLUActivationFunction);

        test.f = new SigmoidActivationFunction();
        json = gson.toJson(test);
        System.out.println(json);
        assertTrue(json.contains("\"type\": \"sigmoid\""));

        deserialized = gson.fromJson(json, TestClass.class);
        assertTrue(deserialized.f instanceof SigmoidActivationFunction);
    }

}
