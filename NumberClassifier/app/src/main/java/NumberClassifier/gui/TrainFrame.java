package NumberClassifier.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import NumberClassifier.train.TrainConfig;

/**
 * Frame where user can configure training parameters and train a network.
 */
public class TrainFrame extends JFrame {

    private TrainConfig conf;
    private JLabel trainingStatusLabel;
    private TrainingJob trainingJob;
    private JButton trainButton;

    /**
     * Constructs a new TrainFrame.
     * @param config Configuration to use.
     */
    public TrainFrame(File config) throws Exception {
        super("NumberClassifier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try (Scanner s = new Scanner(config)) {
            conf = TrainConfig.loadJSON(s.useDelimiter("\\Z").next());
        }

        setLayout(new GridBagLayout());
        
        JPanel group = null;

        group = addGroup( "Dataset" );
        addFileField( group, "Training Image File", conf, "trainingData", "Path to the MNIST image data file to use in training", 0 );
        addFileField( group, "Training Label File", conf, "trainingLabels", "Path to the MNIST label data file to use in training", 1 );
        addFileField( group, "Test Image File", conf, "testData", "Path to the MNIST image data file to use in testing", 2 );
        addFileField( group, "Test Label File", conf, "testLabels", "Path to the MNIST label data file to use in testing", 3 );

        group = addGroup( "Output" );
        addFileField( group, "Save to File", conf, "outFile", "File to write the trained network to.", 0 );

        group = addGroup( "Neural Network" );
        //public int[] layers;
        addLabel( group, "Layers (TODO - Edit json manually)", "Number of neurons on each layer", 0 );
        //public String activation;
        addLabel( group, "Activation function (TODO - Edit json manually)", "Activation function determines the shape of the neuron output function", 1 );
        //public double[] initWeights;
        addLabel( group, "Initial weights (TODO - Edit json manually)", "Range of weights to initialize neuron connections to. Weights are randomized uniformly between values.", 2 );
        addDoubleField( group, "Initial bias", conf, "initBiases", "Value to initialize the neuron biases to.", 3 );

        group = addGroup( "Training Strategy" );
        addDoubleField( group, "Learning rate", conf, "learningRate", "Determines how big steps parameters are updated on each epoch.", 0 );
        addIntField( group, "Number of epochs", conf, "epochs", "How many iterations are run during the training.", 1 );
        addIntField( group, "Mini batch size", conf, "miniBatchSize", "On each epoch a sub sample is selected from training data. This value detemrines the size of the sample.", 2 );

        group = addGroup( "Training" );

        trainButton = new JButton("Train");
        GridBagConstraints c = createGbc(0, 0);
        group.add(trainButton, c);

        trainButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if( trainingJob == null ) {
                    startTraining();
                }
                else {
                    trainingJob.interrupt();
                    trainingStatusLabel.setText("Stopped");
                    trainingStopped();                    
                }
            }
        });

        trainingStatusLabel = new JLabel("");
        c = createGbc(1, 0);
        group.add(trainingStatusLabel, c);

    }

    private void trainingStopped() {
        trainButton.setText("Train");
        trainingJob = null;
    }

    private void startTraining() {
        trainingStatusLabel.setText("Training... (0%)");

        trainButton.setText("Cancel");

        trainingJob = new TrainingJob(conf);
        trainingJob.start();
        // update the status label repeatedly until the training is complete
        new Thread() {
            public void run() {
                while (trainingJob != null && trainingJob.isAlive()) {
                    trainingStatusLabel.setText(String.format("Training... (%.2f%%)", trainingJob.getProgress() * 100.0));
                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if ( trainingJob != null ) {
                    trainingStatusLabel.setText(String.format("Saved to %s. Accuracy %.2f%%", conf.outFile, trainingJob.getAccuracy() * 100.0));
                }
                trainingStopped();
            }
        }.start();
    }

    private JPanel addGroup( String labelText ) {
        JPanel group = new JPanel();
        group.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(labelText),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        group.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 10);
        add(group, c);
        return group;
    }

    private void addLabel(JPanel group, String label, String tooltip, int y) {
        GridBagConstraints c = new GridBagConstraints();        
        c = createGbc(0, y);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setToolTipText(tooltip);
        group.add(fieldLabel, c);
    }


    private void addIntField(JPanel group, String name, TrainConfig instance, String fieldName, String tooltip, int y) throws Exception {
        addLabel(group, name, tooltip, y);

        Field field = TrainConfig.class.getField(fieldName);
        JTextField fieldTextField = new JTextField(field.get(instance).toString());
        GridBagConstraints c = createGbc(1, y);
        group.add(fieldTextField, c);

        // add listener when textfield text is changed, and set the value to field
        fieldTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                    field.set(instance, Integer.valueOf(fieldTextField.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        });

    }

    private void addDoubleField(JPanel group, String name, TrainConfig instance, String fieldName, String tooltip, int y) throws Exception {
        addLabel(group, name, tooltip, y);

        Field field = TrainConfig.class.getField(fieldName);

        JTextField fieldTextField = new JTextField(field.get(instance).toString());
        GridBagConstraints c = createGbc(1, y);
        group.add(fieldTextField, c);

        // add listener when textfield text is changed, and set the value to field
        fieldTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                    field.set(instance, Double.valueOf(fieldTextField.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        });

    }

    private void addFileField(JPanel group, String name, TrainConfig instance, String fieldName, String tooltip, int y) throws Exception {
        addLabel(group, name, tooltip, y);

        Field field = TrainConfig.class.getField(fieldName);

        addLabel(group, name, tooltip, y);

        JTextField fieldTextField = new JTextField((String)field.get(instance));
        GridBagConstraints c = createGbc(1, y);
        group.add(fieldTextField, c);

        // add listener when textfield text is changed, and set the value to field
        fieldTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                    field.set(instance, fieldTextField.getText());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        });


        JButton openFileButton = new JButton("...");
        openFileButton.setSize(new Dimension(10, 10));
        c = createGbc(2, y);
        group.add(openFileButton, c);

        openFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    fieldTextField.setText(file.getAbsolutePath());
                }
            }
        });

    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
  
        gbc.anchor = (x <= 1) ? GridBagConstraints.NORTHWEST : GridBagConstraints.NORTHEAST;
        gbc.fill = (x == 1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
  
        gbc.weightx = (x == 0) ? 0.2 : ((x == 1) ? 1.0 : 0.1);
        gbc.weighty = 0.0;
        return gbc;
     }    

}
