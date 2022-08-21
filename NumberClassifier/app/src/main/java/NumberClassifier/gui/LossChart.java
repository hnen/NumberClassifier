package NumberClassifier.gui;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class LossChart extends JFXPanel {

    final LineChart<Number,Number> lineChart;
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();    
    XYChart.Series<Number, Number> lossSeries = new XYChart.Series<Number, Number>();


    public LossChart() {
        //stage.setTitle("Line Chart Sample");
        //defining the axes
    
        xAxis.setLabel("Epoch");
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        //creating the chart
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Training statistics");
        lossSeries.setName("Loss");
        //populating the series with data
        Scene scene  = new Scene(lineChart,300,250);
        lineChart.getData().add(lossSeries);

        /*
        // Create a debug JavaFX scene with red box
        Scene scene = new Scene(new javafx.scene.layout.VBox(), 300, 250);
        scene.setFill(javafx.scene.paint.Color.RED);
        */

        setScene(scene);

        Platform.runLater(() -> {

        });

    }

    public void addData(int epoch, double loss) {
        Platform.runLater(() -> {
            lossSeries.getData().add(new XYChart.Data<Number, Number>(epoch, loss));
        });

    }

}
