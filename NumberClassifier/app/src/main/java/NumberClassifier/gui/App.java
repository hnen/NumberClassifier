package NumberClassifier.gui;

import java.io.File;

/**
 * Entry point for the application.
 * <p>
 * This is a currently an automatically generated placeholder, waiting for implementation.
 * </p>
 */
public class App {

    public static void main(String[] args) throws Exception {        
        MainFrame frame = new MainFrame();
        frame.openTraining(new File("test.training.json"));
        frame.openNetwork(new File("test.neuralnetwork.json"));
        frame.openImageSet( new File("data/train-images.idx3-ubyte"), new File("data/train-labels.idx1-ubyte") );
    }
    
}
