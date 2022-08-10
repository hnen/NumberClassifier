package NumberClassifier.gui;

import java.util.Scanner;
import java.io.File;
import java.lang.reflect.Field;

import NumberClassifier.train.TrainConfig;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.GridBagConstraints;  
import java.awt.GridBagLayout;  
import java.awt.Insets;

import java.awt.GridLayout;


public class TrainFrame extends JPanel {

    TrainConfig conf;


    public TrainFrame(File config) throws Exception {
        //super("NumberClassifier");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        conf = TrainConfig.loadJSON(new Scanner(config).useDelimiter("\\Z").next());

        // set two-column layout
        //setLayout(new GridLayout(0, 3));
        // set layout where elements are added horizontally and line breaks can happen
        setLayout(new GridBagLayout());
        

        //public String outFile;
        //public int[] layers;
        //public String activation;
        //public IActivationFunction activationFunction;
        //public double learningRate;
        //public double[] initWeights;
        //public double initBiases;
        //public int epochs;
        //public int miniBatchSize;

        addGroup( "Dataset" );

        //public String trainingData;
        //public String trainingLabels;
        //public String testData;
        //public String testLabels;
        addField( "Training Image File", conf, "trainingData", "Path to the MNIST image data file to use in training", 0 );
        addField( "Training Label File", conf, "trainingLabels", "Path to the MNIST label data file to use in training", 1 );
        addField( "Test Image File", conf, "testData", "Path to the MNIST image data file to use in testing", 2 );
        addField( "Test Label File", conf, "testLabels", "Path to the MNIST label data file to use in testing", 3 );


    }

    void addGroup( String labelText ) {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Dataset"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    void addField(String name, TrainConfig instance, String fieldName, String tooltip, int y) throws Exception {
        Field field = TrainConfig.class.getField(fieldName);

        GridBagConstraints c = new GridBagConstraints();        
        c = createGbc(0, y);

        JLabel trainingDataLabel = new JLabel(name);
        trainingDataLabel.setToolTipText(tooltip);
        add(trainingDataLabel, c);

        JTextField trainingDataField = new JTextField((String)field.get(instance));
        c = createGbc(1, y);
        add(trainingDataField, c);

        // add listener when textfield text is changed, and set the value to field
        trainingDataField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }
            public void removeUpdate(DocumentEvent e) {
                update();
            }
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    field.set(instance, trainingDataField.getText());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        });


        JButton openFileButton = new JButton("...");
        openFileButton.setSize(new Dimension(10, 10));
        c = createGbc(2, y);
        add(openFileButton, c);

        openFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    trainingDataField.setText(file.getAbsolutePath());
                }
            }
        });

    }

    private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
  
        gbc.anchor = (x <= 1) ? GridBagConstraints.NORTHWEST : GridBagConstraints.NORTHEAST;
        gbc.fill = (x == 1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
  
        //gbc.insets = (x <= 2) ? WEST_INSETS : EAST_INSETS;
        gbc.weightx = (x == 0) ? 0.2 : ((x == 1) ? 1.0 : 0.1);
        gbc.weighty = 0.0;
        return gbc;
     }    

}
