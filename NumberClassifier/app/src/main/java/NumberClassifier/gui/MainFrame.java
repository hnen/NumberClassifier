package NumberClassifier.gui;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    
    JTabbedPane tabbedPane;
    DrawFrame drawFrame;

    JMenuBar menuBar;

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
                } );
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
                } );
            } catch (Exception e) {
                System.out.println(e);
            }
        });        

        add(tabbedPane);

        setSize(800, 600);
        setVisible(true);
    }

    public void openTraining(File f) throws Exception {
        TrainFrame trainFrame = new TrainFrame(f);
        tabbedPane.addTab(f.getName(), trainFrame.getContentPane());
    }

    void openLoadDialog( Consumer<File> openAction ) throws Exception {
        // file chooser should show only .json files
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".json");
            }
            @Override
            public String getDescription() {
                return "JSON files";
            }
        });
        
        // when user selects a file
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openAction.accept(file);
            
        }
    }


    JPanel makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel label = new JLabel(text);
        panel.add(label);
        return panel;
    }

}
