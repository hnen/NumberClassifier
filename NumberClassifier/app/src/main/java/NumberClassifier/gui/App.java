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
        frame.openTraining(new File("test-conf.json"));
        frame.openNetwork(new File("nn.json"));
    }
    
}
