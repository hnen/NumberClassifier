package NumberClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Scanner;

import NumberClassifier.data.ImageSet;
import NumberClassifier.data.TrainingExample;
import NumberClassifier.gui.DrawFrame;
import NumberClassifier.gui.MainFrame;
import NumberClassifier.train.NeuralNetworkTrainer;
import NumberClassifier.train.TrainConfig;

import java.io.FileOutputStream;

/**
 * Entry point for the application.
 * <p>
 * This is a currently an automatically generated placeholder, waiting for implementation.
 * </p>
 */
public class App {

    public static void main(String[] args) throws Exception {        
        //DrawFrame frame = new DrawFrame();    
        MainFrame frame = new MainFrame();
        frame.openTraining(new File("test-conf.json"));
    }
    
}
