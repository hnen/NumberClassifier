package NumberClassifier.gui;

import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import NumberClassifier.data.ImageSet;

import java.awt.BorderLayout;

public class ImageSetFrame extends JFrame {

    DrawPanel drawPanel;
    
    ImageSet imageSet;
    int imageIndex;
    JLabel imageLabel;

    public ImageSetFrame(InputStream imageSetFile, InputStream imageLabelsFile) throws Exception {
        super("NumberClassifier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel( 28, 28 );
        add(drawPanel, BorderLayout.CENTER);

        imageSet = ImageSet.loadFromMNIST(imageSetFile, imageLabelsFile, 10);

        imageLabel = new JLabel("?");
        imageLabel.setFont(imageLabel.getFont().deriveFont(50.0f));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.SOUTH);

        setImage(0);

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> {
            setImage(imageIndex - 1);
        });

        add(prevButton, BorderLayout.WEST);
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            setImage(imageIndex + 1);
        });
        add(nextButton, BorderLayout.EAST);

    }

    private void setImage(int imageIndex) {

        imageIndex = imageIndex % imageSet.getNumImages();
        if (imageIndex < 0) {
            imageIndex = imageSet.getNumImages() + imageIndex;
        }

        drawPanel.setImage( imageSet.getImage(imageIndex) );
        imageLabel.setText( Integer.toString(imageSet.getLabel(imageIndex)) );

        this.imageIndex = imageIndex;
    }
    
}
