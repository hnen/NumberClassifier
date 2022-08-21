package NumberClassifier.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingUtilities;

import NumberClassifier.stats.TrainingResult;
import NumberClassifier.neuralnetwork.ActivationFunctionFactory;
import NumberClassifier.neuralnetwork.IWeightInitMethod;
import NumberClassifier.neuralnetwork.UniformWeightInitMethod;
import NumberClassifier.neuralnetwork.WeightInitMethodFactory;
import NumberClassifier.stats.CSVWriter;
import NumberClassifier.train.NeuralNetworkTrainer;
import NumberClassifier.train.TrainConfig;
import NumberClassifier.train.NeuralNetworkTrainer.LossHistoryDatapoint;
import javafx.application.Platform;

/**
 * Frame where user can configure training parameters and train a network.
 */
public class TrainFrame extends JFrame {

    private TrainConfig conf;
    private JLabel trainingStatusLabel;
    private TrainingJob trainingJob;
    private JButton trainButton;
    private LossChart lossChart;

    /**
     * Constructs a new TrainFrame.
     * @param config Configuration to use.
     */
    public TrainFrame(File config) throws Exception {
        super("NumberClassifier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try (Scanner s = new Scanner(config)) {
            conf = TrainConfig.loadJSON(s.useDelimiter("\\Z").next());

            // set trainingData path to be relative to config file's path
            conf.trainingData = pathRelativeToConfigFileToAbsolute(config, conf.trainingData);
            conf.trainingLabels = pathRelativeToConfigFileToAbsolute(config, conf.trainingLabels);
            conf.testData = pathRelativeToConfigFileToAbsolute(config, conf.testData);
            conf.testLabels = pathRelativeToConfigFileToAbsolute(config, conf.testLabels);
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

        lossChart = new LossChart();
        c = createGbc(0, 2);
        c.gridwidth = 2;
        group.add(lossChart, c);

        JButton clearChartButon = new JButton();
        clearChartButon.setText("Clear");
        c = createGbc(0, 1);
        group.add(clearChartButon, c);
        clearChartButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lossChart.clear();
            }
        });

    }

    static String pathRelativeToConfigFileToAbsolute(File config, String path) throws IOException {
        String trainingDataDirectory = config.getAbsoluteFile().getParent();
        return new File(trainingDataDirectory + File.separator + path).getCanonicalPath();
    }

    private void trainingStopped() {
        trainButton.setText("Train");
        trainingJob = null;
    }

    private void startTraining() {
        trainingStatusLabel.setText("Training... (0%)");

        lossChart.addNewSeries();

        trainButton.setText("Cancel");

        trainingJob = new TrainingJob(conf);
        trainingJob.start();
        // update the status label repeatedly until the training is complete
        new Thread() {
            public void run() {
                try {
                    int lastAdded = 1;
                    while (trainingJob != null && trainingJob.isAlive()) {
                        trainingStatusLabel.setText(String.format("Training... (%.2f%%) ", trainingJob.getProgress() * 100.0));

                        NeuralNetworkTrainer.LossHistoryDatapoint[] lossHistory = trainingJob.getLossHistory();
                        if ( lossHistory.length > lastAdded ) {
                            LossHistoryDatapoint latest = lossHistory[lossHistory.length - 1];
                            lossChart.addData(latest);
                            lastAdded = lossHistory.length;
                        }

                        Thread.sleep(33);
                    }

                    if ( trainingJob != null ) {
                        trainingStatusLabel.setText(String.format("Saved to %s. Accuracy %.2f%%", conf.outFile, trainingJob.getAccuracy() * 100.0));
                        TrainingResult result = new TrainingResult(conf, trainingJob.getAccuracy(), trainingJob.getTrainDuration(), trainingJob.getAccuracyHistory(), trainingJob.getLossHistory());
                        try {                        
                            new CSVWriter<TrainingResult>(TrainingResult.class).writeToCSV("train-stats.csv", result);
                        } catch( Exception e ) {
                            e.printStackTrace();
                        }
                    }
                } catch(InterruptedException e) {
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
            new FileField(conf, "trainingData", ".idx3-ubyte", "MNIST image file"), 
            "Path to the MNIST image data file to use in training", 
            0
        );
        addField( 
            group,
            "Training Label File",
            new FileField(conf, "trainingLabels", ".idx1-ubyte", "MNIST label data file"),
            "Path to the MNIST label data file to use in training",
            1 
        );
        addField( 
            group, 
            "Test Image File", 
            new FileField(conf, "testData", ".idx3-ubyte", "MNIST image file"), 
            "Path to the MNIST image data file to use in testing", 
            2
        );
        addField( 
            group, 
            "Test Label File", 
            new FileField(conf, "testLabels", ".idx1-ubyte", "MNIST label data file"), 
            "Path to the MNIST label data file to use in testing", 
            3
        );

        group = addGroup( "Output" );
        addField( 
            group, 
            "Save to File", 
            new FileField(conf, "outFile", ".neuralnetwork.json", "Neural network file"), 
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
            "Weight initialization method",
            new WeightInitMethodField(conf, "initWeightsMethod"),
            "Algorithm to use to initialize the weights of the network",
            2
        );




        addField(
             group, 
             "Initial bias", 
             new DoubleField(conf, "initBiases"), 
             "Value to initialize the neuron biases to.", 
             4 
        );

        group = addGroup( "Training Strategy" );
        addField( 
            group, 
            "Learning rates per phase", 
            new DoubleArrayField(conf, "learningRate"), 
            "Determines how big steps parameters are updated on each epoch.", 
            0 
        );
        addField( 
            group, 
            "Number of epochs per phase", 
            new IntArrayField(conf, "epochs"), 
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
        addField( 
            group, 
            "RMSprop momentum", 
            new DoubleField(conf, "rmspropMomentum"), 
            "Value from 0 to 1 that determines how much of the previous gradient is kept when updating the weights.", 
            3 
        );
    }

    abstract class EditField {

        JTextField fieldTextField;

        abstract void setValue(String value) throws Exception;
        abstract String getValue() throws Exception;

        void createElement(JPanel group, int y) throws Exception {
            createTextField(group, y);
        }

        void refreshElements() throws Exception {
            if ( fieldTextField != null ) {
                fieldTextField.setText(getValue());
            }
        }

        protected JTextField createTextField(JPanel group, int y) throws Exception {
            fieldTextField = new JTextField(getValue());
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

        Field field;
        TrainConfig instance;

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
        String fileFilter;
        String fileDesc;

        public FileField(TrainConfig instance, String fieldName, String fileFilter, String fileDesc) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
            this.fileFilter = fileFilter;
            this.fileDesc = fileDesc;
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

                    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if ( f.isDirectory() )
                                return true;

                            return f.getName().endsWith(fileFilter);
                        }
                        @Override
                        public String getDescription() {
                            return fileDesc;
                        }
                    });
                    
                    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

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
            JComboBox<String> comboBox = new JComboBox<String>(ActivationFunctionFactory.getTypes());
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

       
    
    class WeightInitMethodField extends EditField {   
        Field field;
        TrainConfig instance;

        public WeightInitMethodField(TrainConfig instance, String fieldName) throws Exception {
            this.field = TrainConfig.class.getField(fieldName);
            this.instance = instance;
        }
        
        @Override
        public void setValue(String value) throws Exception {
            field.set(instance, WeightInitMethodFactory.create(value));
        }
        
        @Override
        public String getValue() throws Exception {
            return WeightInitMethodFactory.nameOf((IWeightInitMethod)field.get(instance));
        }

        @Override
        void createElement(JPanel group, int y) throws Exception {
            JComboBox<String> comboBox = new JComboBox<String>(WeightInitMethodFactory.getTypes());
            comboBox.setSelectedItem(field.get(instance));
            GridBagConstraints c = createGbc(1, y);
            group.add(comboBox, c);

            addLabel(group, "Uniform init range ", "Applies only when weight init is 'uniform'. Range to use for uniform init mmethod", y + 1);
            EditField rangeField = new EditField() {
                @Override
                public void setValue(String value) throws Exception {
                    String[] values = value.split(",");
                    double[] array = new double[values.length];
                    for( int i = 0; i < values.length; i++ ) {
                        array[i] = Double.parseDouble(values[i]);
                    }

                    IWeightInitMethod weightInitMethod = (IWeightInitMethod)field.get(instance);
                    if ( weightInitMethod instanceof UniformWeightInitMethod ) {
                        UniformWeightInitMethod uniformWeightInitMethod = (UniformWeightInitMethod)weightInitMethod;
                        uniformWeightInitMethod.setMin(array[0]);
                        uniformWeightInitMethod.setMax(array[1]);
                    }
                }

                @Override
                public String getValue() throws Exception {
                    IWeightInitMethod weightInitMethod = (IWeightInitMethod)field.get(instance);
                    if ( weightInitMethod instanceof UniformWeightInitMethod ) {
                        UniformWeightInitMethod initMethod = (UniformWeightInitMethod)field.get(instance);
                        return initMethod.getMin() + "," + initMethod.getMax();
                    }

                    return "N/A";
                }
            };
            rangeField.createElement(group, y + 1);

            comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        setValue((String)comboBox.getSelectedItem());
                        rangeField.refreshElements();
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
