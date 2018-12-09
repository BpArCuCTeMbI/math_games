import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.event.WindowEvent;


public class BrownRobinsonGameController {

    private GameModel game;
    private JFrame fr1;
    private JFrame fr2;

    @FXML
    private TableView tableViewLog;

    @FXML
    private TableColumn<GameLogEntry, Integer> colGameNum;

    @FXML
    private TableColumn<GameLogEntry, Integer> colChosenStratA;

    @FXML
    private TableColumn<GameLogEntry, Double> colScoreByBO;

    @FXML
    private TableColumn<GameLogEntry, Double> colScoreByBT;

    @FXML
    private TableColumn<GameLogEntry, Integer> colChosenStratB;

    @FXML
    private TableColumn<GameLogEntry, Double> colScoreByAO;

    @FXML
    private TableColumn<GameLogEntry, Double> colScoreByAT;

    @FXML
    private TableColumn<GameLogEntry, Double> colLowerPrice;

    @FXML
    private TableColumn<GameLogEntry, Double> colHigherPrice;

    @FXML
    private TableColumn<GameLogEntry, Double> colAveragePrice;

    @FXML
    private TextField ZZTextField;

    @FXML
    private TextField ZOTextField;

    @FXML
    private TextField OZTextField;

    @FXML
    private TextField OOTextField;

    @FXML
    private TextField gamesAmountTextField;

    @FXML
    private TextArea optimalStratTextArea;

    @FXML
    void calculateButtonPressed(ActionEvent event) {

        try{
            double[][] gameMtr = new double[2][2];
            int gamesAmount = Integer.parseInt(gamesAmountTextField.getText());
            gameMtr[0][0] = Double.parseDouble(ZZTextField.getText());
            gameMtr[0][1] = Double.parseDouble(ZOTextField.getText());
            gameMtr[1][0] = Double.parseDouble(OZTextField.getText());
            gameMtr[1][1] = Double.parseDouble(OOTextField.getText());
            game.setGameMatrix(gameMtr);
            game.setGamesAmount(gamesAmount);
            game.initStatLogArrays();

            game.runBrownRobinson();

            game.findProbs();

            //game.printOptStrats();

            //generate output to textarea
            StringBuilder bld = new StringBuilder();
            bld.append("Optimal strategy for player A: (");
            for(int i = 0; i < game.getaOptStrat().length; ++i){
                bld.append(game.getaOptStrat()[i]);
                if(i != game.getaOptStrat().length - 1){
                    bld.append("; ");
                }
            }
            bld.append(")\nOptimal strategy for player B: (");

            for(int i = 0; i < game.getbOptStrat().length; ++i){
                bld.append(game.getbOptStrat()[i]);
                if(i != game.getbOptStrat().length - 1){
                    bld.append("; ");
                }
            }
            bld.append(")\n");

            optimalStratTextArea.setText(bld.toString());

        }
        catch (NumberFormatException ex){
            Alert alertWin = new Alert(Alert.AlertType.WARNING);
            alertWin.setTitle("Wrong input!");
            alertWin.setHeaderText("Wrong input");
            alertWin.setContentText("Game matrix elements or amount of iterations are in wrong format or not a numbers at all.");

            alertWin.showAndWait();
        }
    }

    @FXML
    void playOptimalButtonPressed(ActionEvent event) {

        game.calcAnalytically();

        StringBuilder bld = new StringBuilder(optimalStratTextArea.getText());
        bld.append("\nAnalytically calculated optimal strategy A: (" + game.getAnOptStratA()[0] + "; " + game.getAnOptStratA()[1] + ")\n");
        bld.append("Analytically calculated optimal strategy B: (" + game.getAnOptStratB()[0] + "; " + game.getAnOptStratB()[1] + ")\n");
        bld.append("Analytically calculated game value: " + game.getAnGameValue() + "\n");
        optimalStratTextArea.setText(bld.toString());

        game.playWithOptStrats();
        bld = new StringBuilder(optimalStratTextArea.getText());
        bld.append("\nFirst player won: " + game.getWinsAmountA() + " games.\n");
        bld.append("Second player won: " + game.getWinsAmountB() + " games.\n");
        optimalStratTextArea.setText(bld.toString());

        int len = game.getStatSumValueOfGame().length;
        double[] xGameAmountData = new double[len];
        for(int i = 0; i < len; i++){
            xGameAmountData[i] = i + 1;
            xGameAmountData[i] = i + 1;
        }

        XYChart sumValOfGameChart = QuickChart.getChart("Total Value of game", "Number of games", "Total Value", "y(x)", xGameAmountData, game.getStatSumValueOfGame());
        XYChart AverageValOfGameChart = QuickChart.getChart("Average Value of game", "Number of games", "Average Value", "y(x)", xGameAmountData, game.getStatAverageValueOfGame());

        // Show it
        fr1 = new SwingWrapper(sumValOfGameChart).displayChart();
        fr1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr2 = new SwingWrapper(AverageValOfGameChart).displayChart();
        fr2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    @FXML
    void resetButtonPressed(ActionEvent event) {
        fr1.dispatchEvent(new WindowEvent(fr1, WindowEvent.WINDOW_CLOSING));
        fr2.dispatchEvent(new WindowEvent(fr2, WindowEvent.WINDOW_CLOSING));

        game = new GameModel();
        tableViewLog.getItems().clear();
        ZZTextField.clear();
        ZOTextField.clear();
        OZTextField.clear();
        OOTextField.clear();
        gamesAmountTextField.clear();
        optimalStratTextArea.clear();

        colGameNum.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("gameNumber"));
        colChosenStratA.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("chosenStratA"));
        colChosenStratB.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("chosenStratB"));
        colHigherPrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("higherPrice"));
        colLowerPrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("lowerPrice"));
        colAveragePrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("averagePrice"));

        colScoreByBO.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("bz"));
        colScoreByBT.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("bo"));
        colScoreByAO.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("az"));
        colScoreByAT.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("ao"));


        tableViewLog.setItems(game.getGameLog());

    }


    @FXML
    private void initialize(){
        game = new GameModel();

        colGameNum.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("gameNumber"));
        colChosenStratA.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("chosenStratA"));
        colChosenStratB.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Integer>("chosenStratB"));
        colHigherPrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("higherPrice"));
        colLowerPrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("lowerPrice"));
        colAveragePrice.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("averagePrice"));

        colScoreByBO.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("bz"));
        colScoreByBT.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("bo"));
        colScoreByAO.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("az"));
        colScoreByAT.setCellValueFactory(new PropertyValueFactory<GameLogEntry, Double>("ao"));


        tableViewLog.setItems(game.getGameLog());

    }

}
