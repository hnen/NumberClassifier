package NumberClassifier.gui;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import NumberClassifier.train.NeuralNetworkTrainer.LossHistoryDatapoint;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class LossChart extends JFXPanel {

    final LineChart<Number,Number> lineChart;
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();    
    XYChart.Series<Number, Number> lossSeries;


    public LossChart() {
        //stage.setTitle("Line Chart Sample");
        //defining the axes
    
        xAxis.setLabel("Epoch");
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        //creating the chart
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setAnimated(false);
        
        lineChart.setLegendVisible(false);

        lineChart.setTitle("Loss Function History (log scale)");
        
        //populating the series with data
        Scene scene  = new Scene(lineChart,400,280);
        lineChart.setCreateSymbols(false);

        /*
        // Create a debug JavaFX scene with red box
        Scene scene = new Scene(new javafx.scene.layout.VBox(), 300, 250);
        scene.setFill(javafx.scene.paint.Color.RED);
        */

        setScene(scene);

        Platform.runLater(() -> {

        });

    }

    public void addNewSeries() {
        Platform.runLater(() -> {
            lossSeries = new XYChart.Series<Number, Number>();
            lineChart.getData().add(lossSeries);
        });
    }

    public void clear() {
        Platform.runLater(() -> {
            lineChart.getData().clear();
        });
    }

    public void addData(LossHistoryDatapoint datapoint) {
        Platform.runLater(() -> {
            lossSeries.getData().add(new XYChart.Data<Number, Number>(datapoint.epoch, Math.log10(datapoint.loss)));
        });

    }

}
