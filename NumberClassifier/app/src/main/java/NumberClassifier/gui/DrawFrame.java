package NumberClassifier.gui;

import java.awt.*;
import java.io.InputStream;

import javax.swing.*;

import NumberClassifier.neuralnetwork.FeedForwardNeuralNetwork;

/**
 * Frame where user can draw the number and the neural network will classify it.
 */
public class DrawFrame extends JFrame  {
    private DrawPanel drawPanel;
    private JLabel label;

    public DrawFrame(InputStream neuralNetwork) throws Exception {
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

        setSize(800, 600);

        nn = FeedForwardNeuralNetwork.load(neuralNetwork);
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


    FeedForwardNeuralNetwork nn;

}
