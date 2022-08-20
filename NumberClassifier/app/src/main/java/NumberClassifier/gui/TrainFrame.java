package NumberClassifier.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import NumberClassifier.stats.TrainingResult;
import NumberClassifier.stats.CSVWriter;
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
        addFields();

        JPanel group = addGroup( "Training" );

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
                    TrainingResult result = new TrainingResult(conf, trainingJob.getAccuracy(), trainingJob.getTrainDuration());
                    try {                        
                        new CSVWriter<TrainingResult>(TrainingResult.class).writeToCSV("train-stats.csv", result);
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }
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

    void addFields() throws Exception {
       
        JPanel group = null;

        group = addGroup( "Dataset" );
        addField( 
            group, 
            "Training Image File", 
            new FileField(conf, "trainingData"), 
            "Path to the MNIST image data file to use in training", 
            0
        );
        addField( 
            group,
            "Training Label File",
            new FileField(conf, "trainingLabels"),
            "Path to the MNIST label data file to use in training",
            1 
        );
        addField( 
            group, 
            "Test Image File", 
            new FileField(conf, "testData"), 
            "Path to the MNIST image data file to use in testing", 
            2
        );
        addField( 
            group, 
            "Test Label File", 
            new FileField(conf, "testLabels"), 
            "Path to the MNIST label data file to use in testing", 
            3
        );

        group = addGroup( "Output" );
        addField( 
            group, 
            "Save to File", 
            new FileField(conf, "outFile"), 
            "File to write the trained network to.", 
            0 
        );

        group = addGroup( "Neural Network" );
        addField( 
            group, 
            "Layers", 
            new IntArrayField(conf, "layers"),
            "Number of neurons on each layer", 
            0
        );

        addField( 
            group, 
            "Activation function",
            new ActivationFunctionField(conf, "activation"),
            "Activation function determines the shape of the neuron output function",
            1 
        );

        addField( 
            group, 
            "Initial weights", 
            new DoubleArrayField(conf, "initWeights"),
            "Range of weights to initialize neuron connections to. Weights are randomized uniformly between values.",
             2 );
        addField(
             group, 
             "Initial bias", 
             new DoubleField(conf, "initBiases"), 
             "Value to initialize the neuron biases to.", 
             3 
        );

        group = addGroup( "Training Strategy" );
        addField( 
            group, 
            "Learning rate", 
            new DoubleField(conf, "learningRate"), 
            "Determines how big steps parameters are updated on each epoch.", 
            0 
        );
        addField( 
            group, 
            "Number of epochs", 
            new IntField(conf, "epochs"), 
            "How many iterations are run during the training.", 
            1 
        );
        addField( 
            group, 
            "Mini batch size", 
            new IntField(conf, "miniBatchSize"), 
            "On each epoch a sub sample is selected from training data. This value detemrines the size of the sample.", 
            2
        );
    }

    abstract class EditField {

        Field field;
        TrainConfig instance;

        abstract void setValue(String value) throws Exception;
        abstract String getValue() throws Exception;

        void createElement(JPanel group, int y) throws Exception {
            createTextField(group, y);
        }

        protected JTextField createTextField(JPanel group, int y) throws Exception {
            JTextField fieldTextField = new JTextField(getValue());
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
                        setValue(fieldTextField.getText());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });          

            return fieldTextField;
        }

    }

    class IntField extends EditField {

        public IntField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }

        @Override
        public void setValue(String value) throws Exception {
            field.setInt(instance, Integer.parseInt(value));
        }

        @Override
        public String getValue() throws Exception {
            return Integer.toString(field.getInt(instance));
        }

    }

    class DoubleField extends EditField {   
        Field field;
        TrainConfig instance;

        public DoubleField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }
        
        @Override
        public void setValue(String value) throws Exception {
            field.setDouble(conf, Double.parseDouble(value));
        }
        
        @Override
        public String getValue() throws Exception {
            return Double.toString(field.getDouble(conf));
        }
    }

    
    class FileField extends EditField {   
        Field field;
        TrainConfig instance;

        public FileField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }
        
        @Override
        public void setValue(String value) throws Exception {
            field.set(instance, value);
        }
        
        @Override
        public String getValue() throws Exception {
            return (String)field.get(instance);
        }

        @Override
        void createElement(JPanel group, int y) throws Exception {
            JTextField fieldTextField = createTextField(group, y);

            JButton openFileButton = new JButton("...");
            openFileButton.setSize(new Dimension(10, 10));
            GridBagConstraints c = createGbc(2, y);
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
    }

    class IntArrayField extends EditField {
        Field field;
        TrainConfig instance;
        
        public IntArrayField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }


        @Override
        public void setValue(String value) throws Exception {
            String[] values = value.split(",");
            int[] array = new int[values.length];
            for( int i = 0; i < values.length; i++ ) {
                array[i] = Integer.parseInt(values[i]);
            }
            field.set(conf, array);
        }

        @Override
        public String getValue() throws Exception {
            int[] array = (int[])field.get(conf);
            StringBuilder sb = new StringBuilder();

            for ( int i = 0; i < array.length; i++ ) {
                sb.append(array[i]);
                if ( i < array.length - 1 ) {
                    sb.append(",");
                }
            }

            return sb.toString();
        }
    }

    
    class DoubleArrayField extends EditField {
        Field field;
        TrainConfig instance;
        
        public DoubleArrayField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }


        @Override
        public void setValue(String value) throws Exception {
            String[] values = value.split(",");
            double[] array = new double[values.length];
            for( int i = 0; i < values.length; i++ ) {
                array[i] = Double.parseDouble(values[i]);
            }
            field.set(conf, array);
        }

        @Override
        public String getValue() throws Exception {
            double[] array = (double[])field.get(conf);
            StringBuilder sb = new StringBuilder();

            for ( int i = 0; i < array.length; i++ ) {
                sb.append(array[i]);
                if ( i < array.length - 1 ) {
                    sb.append(",");
                }
            }

            return sb.toString();
        }
    }


    
    
    class ActivationFunctionField extends EditField {   
        Field field;
        TrainConfig instance;

        public ActivationFunctionField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }
        
        @Override
        public void setValue(String value) throws Exception {
            field.set(instance, value);
        }
        
        @Override
        public String getValue() throws Exception {
            return (String)field.get(instance);
        }

        @Override
        void createElement(JPanel group, int y) throws Exception {
            // add a dropdown field with two choices: "sigmoid" and "relu"
            JComboBox<String> comboBox = new JComboBox<String>(new String[] {"sigmoid", "relu"});
            comboBox.setSelectedItem(field.get(instance));
            GridBagConstraints c = createGbc(1, y);
            group.add(comboBox, c);

            comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        field.set(instance, comboBox.getSelectedItem());
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });

        }
    }


    private void addField(JPanel group, String name, EditField field, String tooltip, int y) throws Exception {
        addLabel(group, name, tooltip, y);
        field.createElement(group, y);
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
