package NumberClassifier;

import java.awt.*;
import javax.swing.*;

/**
 * Interactive App. User can draw the number here and the app will show which number the 
 * neural network thinks it is.
 */
public class InteractiveApp extends JFrame  {
    private DrawPanel drawPanel;

    public InteractiveApp() {
        super("NumberClassifier");

        drawPanel = new DrawPanel( 28, 28 );
        add(drawPanel, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);

    }

    public static void main(String[] args) {
        InteractiveApp app = new InteractiveApp();
    }

}
