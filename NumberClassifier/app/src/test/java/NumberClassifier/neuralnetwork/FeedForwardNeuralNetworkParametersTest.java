package NumberClassifier.neuralnetwork;

import org.junit.jupiter.api.Test;

import NumberClassifier.neuralnetwork.FeedForwardNeuralNetworkParameters;

import static org.junit.jupiter.api.Assertions.*;

public class FeedForwardNeuralNetworkParametersTest {

    @Test void testWeightMatrix() throws Exception {
        FeedForwardNeuralNetworkParameters p = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );

        p.setWeights( 
            0, new double[] { 
            11.0, 12.0, 13.0, 
            21.0, 22.0, 23.0 
        });

        double[][] w = p.weightMatrix(0);

        assertEquals(11.0, w[0][0]);
        assertEquals(12.0, w[0][1]);
        assertEquals(13.0, w[0][2]);

        assertEquals(21.0, w[1][0]);
        assertEquals(22.0, w[1][1]);
        assertEquals(23.0, w[1][2]);
    }


    @Test void testAdd() throws Exception {
        FeedForwardNeuralNetworkParameters p0 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );
        FeedForwardNeuralNetworkParameters p1 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );

        p0.setWeights( 
            0, new double[] { 
            1.0, 2.0, 3.0, 
            4.0, 5.0, 6.0 
        });

        p1.setWeights( 
            0, new double[] { 
            6.0, 5.0, 4.0, 
            3.0, 2.0, 1.0 
        });

        p1.add(p0);

        assertEquals(7.0, p1.weights[0][0]);
        assertEquals(7.0, p1.weights[0][1]);
        assertEquals(7.0, p1.weights[0][2]);
        assertEquals(7.0, p1.weights[0][3]);
        assertEquals(7.0, p1.weights[0][4]);
        assertEquals(7.0, p1.weights[0][5]);
    }


    @Test void testAddWithInvalidParams() throws Exception {
        FeedForwardNeuralNetworkParameters p0 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );
        FeedForwardNeuralNetworkParameters p1 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 4 } );
        try {
            p1.add(p0);
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }

        FeedForwardNeuralNetworkParameters p2 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );
        FeedForwardNeuralNetworkParameters p3 = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3, 1 } );
        try {
            p2.add(p3);
            fail("Expected to throw exception.");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test void testMul() throws Exception {
        FeedForwardNeuralNetworkParameters p = new FeedForwardNeuralNetworkParameters( new int[] { 2, 3 } );

        p.setWeights( 
            0, new double[] { 
            1.0, 2.0, 3.0, 
            4.0, 5.0, 6.0 
        });

        p.multiply(2.0);

        assertEquals(2.0, p.weights[0][0]);
        assertEquals(4.0, p.weights[0][1]);
        assertEquals(6.0, p.weights[0][2]);
        assertEquals(8.0, p.weights[0][3]);
        assertEquals(10.0, p.weights[0][4]);
        assertEquals(12.0, p.weights[0][5]);
    }
    
    @Test void testToString() throws Exception {
        FeedForwardNeuralNetworkParameters p0 = new FeedForwardNeuralNetworkParameters( new int[]{2, 3, 1});
        assertNotNull(p0.toString());
        FeedForwardNeuralNetworkParameters p1 = new FeedForwardNeuralNetworkParameters( new int[]{1});
        assertNotNull(p1.toString());
    }

}
