package NumberClassifier.gui;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import javax.swing.*;

import NumberClassifier.neuralnetwork.FeedForwardNeuralNetwork;
import NumberClassifier.train.TrainConfig;

/**
 * Interactive App. User can draw the number here and the app will show which number the 
 * neural network thinks it is.
 */
public class InteractiveApp extends JFrame  {
    private DrawPanel drawPanel;
    private JLabel label;

    public InteractiveApp() throws Exception {
        super("NumberClassifier");

        // Print current working directory
        System.out.println("Current working directory : " + System.getProperty("user.dir"));

        drawPanel = new DrawPanel( 28, 28 );
        add(drawPanel, BorderLayout.CENTER);

        drawPanel.setOnChanged(() -> updateGuess());

        label = new JLabel("?");
        label.setFont(label.getFont().deriveFont(200.0f));
        add(label, BorderLayout.EAST);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);

        loadNeuralNetwork();
    }

    void updateGuess() {
        try {
            int guess = nn.getMaxActivation(drawPanel.getImage());
            label.setText(Integer.toString(guess));
        } catch(Exception e) {
            System.out.println(e);
            label.setText("?");
        }
    }

    void loadNeuralNetwork() throws Exception {            
        File file = new File("test-conf.json");
        TrainConfig conf = TrainConfig.loadJSON(new Scanner(file).useDelimiter("\\Z").next());

        nn = FeedForwardNeuralNetwork.load(new FileInputStream(new File(conf.outFile)));
    }

    public static void main(String[] args) throws Exception {
        InteractiveApp app = new InteractiveApp();
    }

    FeedForwardNeuralNetwork nn;

}
