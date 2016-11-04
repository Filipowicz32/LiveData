package ViewFx;

import forex_livedata.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import static javafx.scene.control.DialogEvent.DIALOG_SHOWN;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.swing.GroupLayout;

/**
 *
 * @author John Filipowicz
 */
public class ForEx_LiveData extends Application {
    Scene mainScene, availableCurrency, comparisons;
    
    Button selectAll, clearSelected, runAsDefault, compare, viewHistorical, list;
    RadioButton usdRB, euroRB, poundRB, cadRB, chfRB, nzdRB, audRB, jpyRB;
    ToggleGroup group;
    CheckBox usdCB, euroCB, poundCB, cadCB, chfCB, nzdCB, audCB, jpyCB;
    Label sourceCurrency, quoteCurrencies, outputExplanation;
    
    VBox vbRadio, vbCheck, vbOutput;
    HBox hbCheck, hbRun;
    
    TextArea output;
    TextField sourceField, quoteField;
    
    Controller controller;
    
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Foreign Exchange Live Data");
        controller = new Controller();
        
        this.RadioButton();
        this.Checkboxes();
        this.Output();
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(vbRadio);
        mainLayout.setRight(vbCheck);
        mainLayout.setCenter(vbOutput);
        
        String css = "C:\\Users\\johnf\\OneDrive\\Documents\\NetBeansProjects\\ForEx_LiveData\\LiveData.css";
        mainScene = new Scene(mainLayout, 1000, 900);
        mainScene.getStylesheets().add(new File(css).toURI().toURL().toExternalForm());
        
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    /**
     * Radio Button Instantiation
     */
    private void RadioButton () {
        sourceCurrency = new Label("Select Source Currency\n(via RadioBox or TextField)");
        
        group = new ToggleGroup();
        usdRB = new RadioButton("US Dollars (USD)");
        usdRB.setToggleGroup(group);
        
        euroRB = new RadioButton("Euro (EUR)");
        euroRB.setToggleGroup(group);
        
        poundRB = new RadioButton("Great Britian Pound (GBP)");
        poundRB.setToggleGroup(group);
        
        cadRB = new RadioButton("Canadian Dollar (CAD)");
        cadRB.setToggleGroup(group);
        
        chfRB = new RadioButton("Swiss Franc (CHF)");
        chfRB.setToggleGroup(group);
        
        nzdRB = new RadioButton("New Zealand Dollar (NZD)");
        nzdRB.setToggleGroup(group);
        
        audRB = new RadioButton("Austrailian Dollar (AUS)");
        audRB.setToggleGroup(group);
        
        jpyRB = new RadioButton("Japanese Yen (JPY)");
        jpyRB.setToggleGroup(group);
        
        sourceField = new TextField("");
        
        usdRB.setSelected(true);
        vbRadio = new VBox(40, sourceCurrency, usdRB, euroRB, poundRB, cadRB, chfRB, nzdRB, audRB, jpyRB, sourceField);
        vbRadio.setPadding(new Insets(15));
        
        vbRadio.getStyleClass().add("vbox");
    }
    
    /**
     * Check Box Instantiation
     */
    private void Checkboxes () {
        quoteCurrencies = new Label("Select Quote Currencies\n(via CheckBox or/and TextField)");
        
        usdCB = new CheckBox("US Dollars (USD)");
        euroCB = new CheckBox("Euro (EUR)");
        poundCB = new CheckBox("Great Britain Pound (GBP)");
        cadCB = new CheckBox("Canadian Dollar (CAD)");
        chfCB = new CheckBox("Swiss Franc (CHF)");
        nzdCB = new CheckBox("New Zealand Dollar (NZD)");
        audCB = new CheckBox("Austrailian Dollar (AUD)");
        jpyCB = new CheckBox("Japanese Yen (JPY)");
        
        quoteField = new TextField("");
        
        selectAll = new Button("Select All");
        selectAll.setOnAction(e -> cbHandleButtonAction(e));
        clearSelected = new Button("Clear Selected");
        clearSelected.setOnAction(e -> cbHandleButtonAction(e));
        
        euroCB.setSelected(true);
        hbCheck = new HBox(15, selectAll, clearSelected);
        vbCheck = new VBox(35, quoteCurrencies, hbCheck, usdCB, euroCB, poundCB, cadCB, chfCB, nzdCB, audCB, jpyCB, quoteField);
        
        vbCheck.setPadding(new Insets(15));
        vbCheck.getStyleClass().add("vbox");
    }
    
    /**
     * Selects/deselects all check boxes
     * @param e ActionEvent
     */
    private void cbHandleButtonAction(ActionEvent e) {
        Button b = (Button) e.getSource();
        boolean selected = false;
        
        if(b.toString().equalsIgnoreCase(selectAll.toString())){
            selected = true;
        }
        
        for(Node node : vbCheck.getChildren()){
            if(node instanceof CheckBox){
                ((CheckBox) node).setSelected(selected);
            }  
        }
    }
    
    /**
     * Display Quotes and Button to view term graph (This months ratios)
     */
    private void Output () {
        compare = new Button("View Quotes");
        compare.setOnAction(e -> outputHandleButtonAction(e));
        runAsDefault = new Button("View Default Quotes");
        runAsDefault.setOnAction(e -> outputHandleButtonAction(e));
        viewHistorical = new Button("View Historical Quote");
        viewHistorical.setOnAction(e -> historicalHandleButtonAction(e));
        list = new Button("List Supported Currencies");
        list.setOnAction(e -> listHandleButtonAction(e));
        
        hbRun = new HBox(compare, runAsDefault, viewHistorical, list);
        hbRun.getStyleClass().add("hbox");
        hbRun.setPadding(new Insets(3));
        hbRun.setSpacing(10);
        hbRun.setAlignment(Pos.CENTER);
        
        outputExplanation = new Label("  (e.g. USDEUR: .8 means .8Euros is equivalent to 1USDollar)");
        outputExplanation.setAlignment(Pos.CENTER);
        outputExplanation.setPadding(new Insets(5));
        
        output = new TextArea("Output will be displayed here");
        output.setPrefHeight(1080);
        output.setEditable(false);
        output.setPrefColumnCount(2);
        output.setPadding(new Insets(10));
        output.setWrapText(true);
        
        vbOutput = new VBox(hbRun, outputExplanation, output);
    }
    
    /**
     * Display quotes and other output
     * @param e ActionEvent
     */
    private void outputHandleButtonAction (ActionEvent e){
        Button b = (Button) e.getSource();
        String polishedOutput = "";
        Alert alert = new Alert(AlertType.ERROR);
        
        if(b.toString().equals(runAsDefault.toString())){
            usdRB.setSelected(true);
            
            for(Node node : vbCheck.getChildren()){
                if(node instanceof CheckBox){
                    ((CheckBox) node).setSelected(false);
                }
                if(node instanceof TextField){
                    ((TextField) node).setText("");
                }
            }
            
            euroCB.setSelected(true);
            poundCB.setSelected(true);
        }
        try{
            controller.FormatOutput(this.FormRequest());
        } catch (Exception noneSelectedException){
            alert.setContentText("There must be a currency selected to compare the source currency to.");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
        
        
        polishedOutput = controller.getFinalQuotes();
        output.setText(polishedOutput);
    }
    
    /**
     * Prompt for currency pair and produce a line graph pop-up with this months ratios
     * @param e 
     */
    private void historicalHandleButtonAction (ActionEvent e) {
        String input = "";
        Dialog<String> dialog = new Dialog();
        Alert alert = new Alert(AlertType.ERROR);
        
        dialog.setHeaderText("Enter the desired source and quote currencies. (e.g. 'USD || EUR')");
        dialog.setResizable(false);
        
        Label sourceLabel = new Label("Source: ");
        Label quoteLabel = new Label("Quote: ");
        TextField sourceTField = new TextField();
        TextField quoteTField = new TextField();
        
        GridPane grid = new GridPane();
        grid.add(sourceLabel, 1, 1);
        grid.add(sourceTField, 2, 1);
        grid.add(quoteLabel, 1, 2);
        grid.add(quoteTField, 2, 2);
        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        
        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType param) {
                if(param == buttonTypeOk)
                    return "" + sourceTField.getText() + "," + quoteTField.getText();
                return null;
            }
        
        });
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()){
            input = result.get();
        }
        
        try{
            controller.Validate(input);
            LineGraphScene.display(input);
            
        } catch(Exception error){
            alert.setContentText("Invalid Format of Input. Only use a currency's three character code (e.g. USD)\n" + input);
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
    }
    
    /**
     * Produce the accepted currency list to the output
     * @param e 
     */
    private void listHandleButtonAction (ActionEvent e) {
        String supported = "";
        
        try{
            supported = controller.CreateAllowedCurrenciesList();
        } catch (Exception er){
            System.out.println("Invalid endpoint in request");
            supported = "Attempt to get list failed";
        }
        
        output.setText(supported);
    }
    
    /**
     * Form source and quote strings to be passed for a json request
     * @return raw quotes from the json request
     */
    private String FormRequest () throws Exception {
        String source = "&source=", quotes = "&currencies=", temp = "";
        Exception noneSelectedException = null;
        int cbAdded = 0;
        
        for(Node node : vbCheck.getChildren()){
            if(node instanceof CheckBox){
                if(((CheckBox) node).isSelected()){
                    temp = ((CheckBox) node).getText();
                    temp = temp.substring(temp.length() - 4, temp.length() - 1);
                    
                    if(cbAdded != 0)
                        temp = "," + temp;
                    
                    quotes += temp;
                    cbAdded++;
                }
            }  
        }
        
        for(Node node : vbRadio.getChildren()){
            if(node instanceof RadioButton){
                if(((RadioButton) node).isSelected()){
                    temp = ((RadioButton) node).getText();
                    temp = temp.substring(temp.length() - 4, temp.length() - 1);
                    source += temp;
                    break;
                }
            }
        }
        
        if(cbAdded == 0){
            throw noneSelectedException;
        }
        
        controller.CreateRawQuotes(source, quotes);
        return controller.getRawQuotes();
    }
}
