package NumberClassifier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MultilayerPerceptronTest {

    @Test void evaluatesXOR() {
        double output;
        int layers[] = { 2, 3, 1 };
        MultilayerPerceptron ffn = new MultilayerPerceptron( layers );

        ffn.setWeights( 
            0, new double[] { 
            1.0, 1.0, 0.0, 
            0.0, 1.0, 1.0 
        });
        ffn.setBiases( 1, new double[] { -0.4, -1.4, -0.4 });

        ffn.setWeights( 1, new double[] { 1.0, -2.0, 1.0 } );

        ffn.setInput( new double[] { 1.0, 0.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output > 0.5, "1 XOR 0 should be 1" );

        ffn.setInput( new double[] { 0.0, 1.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output > 0.5, "0 XOR 1 should be 1" );

        ffn.setInput( new double[] { 1.0, 1.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output < 0.5, "1 XOR 1 should be 0" );

        ffn.setInput( new double[] { 0.0, 0.0 } );
        ffn.calculateOutputs();
        output = ffn.getOutput()[0];
        assertTrue( output < 0.5, "0 XOR 0 should be 0" );
    }
    
}
