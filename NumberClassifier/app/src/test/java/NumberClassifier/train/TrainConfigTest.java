package NumberClassifier.train;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import NumberClassifier.neuralnetwork.UniformWeightInitMethod;

public class TrainConfigTest {
    
    @Test
    void testLoadJSON() throws Exception {
        String testConfigJSON = """ 
            {
                "trainingData": "test0",
                "trainingLabels": "test1",
                "testData": "test2",
                "testLabels": "test3",
            
                "layers": [ 1, 2, 3, 4, 5 ],
                
                "activation": "sigmoid",
                "initWeightsUniformRange": [-1.0, 1.0],
                "initBiases": 0.1,

                "initWeightsMethod": {
                    "type": "uniform",
                    "data": {
                        "min": -5.0,
                        "max": 10.0
                    }
                },
            
                "learningRate": [0.5, 0.1, 0.01],
                "epochs": [500, 1000, 2000],
                "miniBatchSize": 10
            }
        """;

        TrainConfig config = TrainConfig.loadJSON(testConfigJSON);

        assertEquals("test0", config.trainingData);
        assertEquals("test1", config.trainingLabels);
        assertEquals("test2", config.testData);
        assertEquals("test3", config.testLabels);
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, config.layers);
        assertEquals("sigmoid", config.activation);
        assertArrayEquals(new double[] { -1.0, 1.0 }, config.initWeightsUniformRange);
        assertEquals(0.1, config.initBiases);
        assertArrayEquals(new double[]{ 0.5, 0.1, 0.01 }, config.learningRate);
        assertArrayEquals(new int[] {500, 1000, 2000}, config.epochs);
        assertEquals(10, config.miniBatchSize);
        assertEquals(UniformWeightInitMethod.class, config.initWeightsMethod.getClass());

        UniformWeightInitMethod uniformWeightInitMethod = (UniformWeightInitMethod)config.initWeightsMethod;
        assertEquals(-5.0, uniformWeightInitMethod.getMin());
        assertEquals(10.0, uniformWeightInitMethod.getMax());

    }



}
