package NumberClassifier.gui;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

/**
 * Main window for the application. Contains a tabbed pane where different functions can be opened to.
 */
public class MainFrame extends JFrame {
    
    private JTabbedPane tabbedPane;
    private DrawFrame drawFrame;

    /**
     * Constructs a new MainFrame.
     * @throws Exception if there is an error creating the frame.
     */
    public MainFrame() throws Exception {
        super("NumberClassifier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu loadSubMenu = new JMenu("Open");
        JMenuItem loadTrainingConfig = new JMenuItem("Training");
        JMenuItem loadNeuralNetwork = new JMenuItem("Neural Network");
        JMenuItem saveItem = new JMenuItem("Save...");

        loadSubMenu.add(loadTrainingConfig);
        loadSubMenu.add(loadNeuralNetwork);
        fileMenu.add(loadSubMenu);
        menuBar.add(fileMenu);
        fileMenu.add(saveItem);
        setJMenuBar(menuBar);

        loadNeuralNetwork.addActionListener(ev -> {
            try {
                openLoadDialog( f -> {
                    try {
                        drawFrame = new DrawFrame(new FileInputStream(f));
                        tabbedPane.addTab(f.getName(), drawFrame.getContentPane());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }, ".neuralnetwork.json", "Neural network file (*.neuralnetwork.json)" );
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        loadTrainingConfig.addActionListener(ev -> {
            try {
                openLoadDialog( f -> {
                    try {
                        openTraining(f);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }, ".training.json", "Training configuration file (*.training.json)" );
            } catch (Exception e) {
                System.out.println(e);
            }
        });        

        add(tabbedPane);

        setSize(800, 600);
        setVisible(true);
    }

    /**
     * Open training configuration file.
     * @param f File to open.
     */
    public void openTraining(File f) throws Exception {
        TrainFrame trainFrame = new TrainFrame(f);
        tabbedPane.addTab(f.getName(), trainFrame.getContentPane());
    }
    
    /**
     * Open neural network for drawing.
     * @param f File to open.
     */
    public void openNetwork(File f) throws Exception {
        DrawFrame drawFrame = new DrawFrame(new FileInputStream(f));
        tabbedPane.addTab(f.getName(), drawFrame.getContentPane());
    }

    public void openImageSet(File images, File labels) throws Exception {
        ImageSetFrame imageSetFrame = new ImageSetFrame(new FileInputStream(images), new FileInputStream(labels));
        tabbedPane.addTab(images.getName(), imageSetFrame.getContentPane());
    }

    private void openLoadDialog( Consumer<File> openAction, String fileExtension, String fileDescription ) throws Exception {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if ( f.isDirectory() )
                    return true;
                
                return f.getName().endsWith(fileExtension);
            }
            @Override
            public String getDescription() {
                return fileDescription;
            }
        });

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openAction.accept(file);
            
        }
    }

}
