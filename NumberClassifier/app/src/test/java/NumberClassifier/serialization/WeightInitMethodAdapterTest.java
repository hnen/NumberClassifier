package NumberClassifier.serialization;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import NumberClassifier.neuralnetwork.HeWeightInitMethod;
import NumberClassifier.neuralnetwork.IWeightInitMethod;
import NumberClassifier.neuralnetwork.UniformWeightInitMethod;
import NumberClassifier.neuralnetwork.XavierWeightInitMethod;

import static org.junit.jupiter.api.Assertions.*;

public class WeightInitMethodAdapterTest {
 
    class TestClass {
        public IWeightInitMethod f;
    }

    @Test
    void testSerialize() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(IWeightInitMethod.class, new WeightInitMethodAdapter())
            .create();

        TestClass test = new TestClass();
        test.f = new UniformWeightInitMethod(-2, 5);
        String json = gson.toJson(test);

        TestClass deserialized = gson.fromJson(json, TestClass.class);
        assertTrue(deserialized.f instanceof UniformWeightInitMethod);
        assertEquals(-2, ((UniformWeightInitMethod)deserialized.f).getMin(), 0.00001);
        assertEquals(5, ((UniformWeightInitMethod)deserialized.f).getMax(), 0.00001);

        test.f = new XavierWeightInitMethod();
        json = gson.toJson(test);

        deserialized = gson.fromJson(json, TestClass.class);
        assertTrue(deserialized.f instanceof XavierWeightInitMethod);

        test.f = new HeWeightInitMethod();
        json = gson.toJson(test);

        deserialized = gson.fromJson(json, TestClass.class);
        assertTrue(deserialized.f instanceof HeWeightInitMethod);
    }

}
