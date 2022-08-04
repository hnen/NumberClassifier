package NumberClassifier;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TrainConfigTest {
    
    @Test
    void testLoadJSON() {
        String testConfigJSON = """ 
            {
                "trainingData": "test0",
                "trainingLabels": "test1",
                "testData": "test2",
                "testLabels": "test3",
            
                "layers": [ 1, 2, 3, 4, 5 ],
                
                "activation": "test5",
                "learningRate": 0.123,
                "initWeights": [-1.0, 1.0],
                "initBiases": [-1.0, 2.0],
            
                "epochs": 1000,
                "miniBatchSize": 10
            }
        """;

        TrainConfig config = TrainConfig.loadJSON(testConfigJSON);

        assertEquals("test0", config.trainingData);
        assertEquals("test1", config.trainingLabels);
        assertEquals("test2", config.testData);
        assertEquals("test3", config.testLabels);
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, config.layers);
        assertEquals("test5", config.activation);
        assertEquals(0.123, config.learningRate);
        assertArrayEquals(new double[] { -1.0, 1.0 }, config.initWeights);
        assertArrayEquals(new double[] { -1.0, 2.0 }, config.initBiases);
        assertEquals(1000, config.epochs);
        assertEquals(10, config.miniBatchSize);
    }



}
