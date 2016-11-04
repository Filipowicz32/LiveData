package ViewFx;

import forex_livedata.Controller;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Line Graph pop-up for javafx forex project
 * @author John Filipowicz
 */
public class LineGraphScene {
    
    /**
     * Produces a line graph pop-up of one quote per day this month for the indicated currency pair
     * @param sourceQuote 
     */
    public static void display(String sourceQuote) {
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        
        String dateStr = "";
        int historical = 10;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        
        dateStr = dateFormat.format(date);
        historical = Integer.parseInt(dateStr.substring(8));
        
        Controller controller = new Controller();
        String source = sourceQuote.substring(0, 3);
        String quote = sourceQuote.substring(sourceQuote.length() - 3);
        
        stage.setTitle("Multi Comparison of " + source + " and " + quote + " (" + source + quote + ")");
        
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ratio (" + source + quote + ")");
        final LineChart<Number, Number> linechart = new LineChart<>(xAxis, yAxis);
        linechart.setTitle("Data from the past " + historical + " days. (One tick per day)");
        
        XYChart.Series series = new XYChart.Series();
        series.setName(quote + " per " + source);
        
        int i = 0;
        int tempDay = 0;
        while(Integer.parseInt(dateStr.substring(8)) > 0){
            tempDay = Integer.parseInt(dateStr.substring(8));
            tempDay--;
            
            dateStr = dateStr.substring(0, 8);
            
            if(tempDay < 10)
                dateStr += "0";
            
            dateStr += tempDay;
        
            series.getData().add(new XYChart.Data(i, controller.CreateHistoricComparison(source, quote)));
            i++;
        }
        
        Scene scene = new Scene(linechart, 800, 600);
        linechart.getData().add(series);
        
        stage.setScene(scene);
        stage.show();
    }
    
}
