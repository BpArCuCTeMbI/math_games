import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class GameModel {
    private ObservableList<GameLogEntry> gameLog = FXCollections.observableArrayList();
    private double[][] gameMatrix;
    private int gamesAmount;

    private Double[] aOptStrat;
    private Double[] bOptStrat;

    private double[] statSumValueOfGame;
    private double[] statAverageValueOfGame;

    private double[] anOptStratA;
    private double[] anOptStratB;
    private double anGameValue;

    private int winsAmountA;
    private int winsAmountB;

    public int getWinsAmountA() {
        return winsAmountA;
    }

    public int getWinsAmountB() {
        return winsAmountB;
    }

    GameModel(){
        gameMatrix = new double[2][2];
        //gameLog = new ArrayList<>();

        aOptStrat = new Double[2];
        bOptStrat = new Double[2];

        for(int i = 0; i < 2; ++i) {
            aOptStrat[i] = 0.0;
            bOptStrat[i] = 0.0;
        }
        winsAmountA = 0;
        winsAmountB = 0;

    }

    public void runBrownRobinson(){
        for(int iter = 0; iter < gamesAmount; ++iter){
            GameLogEntry ent = new GameLogEntry();
            ent.setGameNumber(iter + 1);
            int stratA;

            if(iter < 1){
                Random rnd = new Random();
                stratA = rnd.nextBoolean() ? 2 : 1;
            }else{
                stratA = gameLog.get(iter-1).getMaxIndex() + 1;
            }
            ent.setChosenStratA(stratA);

            Double[] scoresByB = new Double[2];
            for(int i = 0; i < 2; ++i){
                if(iter == 0){
                    scoresByB[i] = gameMatrix[stratA-1][i];
                }
                else{
                    scoresByB[i] = gameLog.get(iter-1).getScoresByB()[i] + gameMatrix[stratA-1][i];
                }
            }
            ent.setScoresByB(scoresByB);

            int minIndex = IntStream.range(0, Arrays.asList(scoresByB).size()).boxed().min(Comparator.comparingDouble(Arrays.asList(scoresByB)::get)).get();
            Double lowerScore = scoresByB[minIndex] / ent.getGameNumber();

            int stratB = minIndex + 1;
            ent.setChosenStratB(stratB);
            ent.setLowerPrice(lowerScore);


            Double[] scoresByA = new Double[2];
            for(int i = 0; i < 2; ++i){
                if(iter == 0){
                    scoresByA[i] = gameMatrix[i][stratB-1];
                }
                else{
                    scoresByA[i] = gameLog.get(iter-1).getScoresByA()[i] + gameMatrix[i][stratB-1];
                }
            }
            ent.setScoresByA(scoresByA);

            int maxIndex = IntStream.range(0, Arrays.asList(scoresByA).size()).boxed().max(Comparator.comparingDouble(Arrays.asList(scoresByA)::get)).get();
            Double higherScore = scoresByA[maxIndex] / ent.getGameNumber();
            ent.setHigherPrice(higherScore);

            Double averageScore = (ent.getHigherPrice() + ent.getLowerPrice()) / 2;
            ent.setAveragePrice(averageScore);

            ent.setBz(scoresByB[0]);
            ent.setBo(scoresByB[1]);
            ent.setAz(scoresByA[0]);
            ent.setAo(scoresByA[1]);

            ent.setMaxIndex(maxIndex);

            gameLog.add(ent);

        }
    }

    public void setGameMatrix(double[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    public void setGameLog(ObservableList<GameLogEntry> gameLog) {
        this.gameLog = gameLog;
    }

    public void setGamesAmount(int gamesAmount) {
        this.gamesAmount = gamesAmount;
    }

    public int getGamesAmount(){
        return gamesAmount;
    }

    public ObservableList<GameLogEntry> getGameLog() {
        return gameLog;
    }

    public double[][] getGameMatrix() {
        return gameMatrix;
    }

    public Double[] getaOptStrat() {
        return aOptStrat;
    }

    public Double[] getbOptStrat() {
        return bOptStrat;
    }

    public void findProbs(){

        for(int i = 0; i < gamesAmount; ++i){
            int stratNum = gameLog.get(i).getChosenStratA();
            aOptStrat[stratNum-1]++;
            stratNum = gameLog.get(i).getChosenStratB();
            bOptStrat[stratNum-1]++;
        }

        for(int i = 0; i < 2; ++i){
            aOptStrat[i] /= gamesAmount;
            bOptStrat[i] /= gamesAmount;
        }

    }

    public void printOptStrats(){
        System.out.printf("Optimal strategy for A: (");
        for(int i = 0; i < aOptStrat.length; ++i){
            System.out.printf("%f", aOptStrat[i]);
            if(i != aOptStrat.length - 1){
                System.out.printf("; ");
            }
        }
        System.out.printf(")\n");

        System.out.printf("Optimal strategy for B: (");
        for(int i = 0; i < bOptStrat.length; ++i){
            System.out.printf("%f", bOptStrat[i]);
            if(i != bOptStrat.length - 1){
                System.out.printf("; ");
            }
        }

        System.out.printf(")\n");

    }

    public void debugDummy(){

    }

    public void initStatLogArrays(){
        statSumValueOfGame = new double[gamesAmount];
        statAverageValueOfGame = new double[gamesAmount];
    }

    private int pickStratIndexRandomly (Double[] weights) {
        // calculate the total weight
        double cumulativeSum = 0;
        for(int i = 0; i < weights.length; i++){
            cumulativeSum += weights[i];
        }

        double randChoice = ThreadLocalRandom.current().nextDouble((double) 0, cumulativeSum);
        //System.out.println(randChoice);
        for(int i = 0; i < weights.length; i++){
            randChoice -= weights[i];
            if(randChoice < 0){
                return i;
            }
        }
        return weights.length - 1;
    }

    public double[] getStatAverageValueOfGame() {
        return statAverageValueOfGame;
    }

    public double[] getStatSumValueOfGame() {
        return statSumValueOfGame;
    }

    public void calcAnalytically(){

        anOptStratA = new double[2];
        anOptStratB = new double[2];
        anOptStratA[0] = (gameMatrix[1][1] - gameMatrix[1][0]) / (gameMatrix[0][0] + gameMatrix[1][1] - gameMatrix[1][0] - gameMatrix[0][1]) ;
        anOptStratA[1] = 1 - anOptStratA[0];
        anOptStratB[0] = (gameMatrix[1][1] - gameMatrix[0][1]) / (gameMatrix[0][0] + gameMatrix[1][1] - gameMatrix[1][0] - gameMatrix[0][1]) ;
        anOptStratB[1] = 1 - anOptStratB[0];
        anGameValue = (gameMatrix[0][0] * gameMatrix[1][1] - gameMatrix[0][1] * gameMatrix[1][0]) / (gameMatrix[0][0] + gameMatrix[1][1] - gameMatrix[1][0] - gameMatrix[0][1]);
    }

    public double getAnGameValue() {
        return anGameValue;
    }

    public double[] getAnOptStratA() {
        return anOptStratA;
    }

    public double[] getAnOptStratB() {
        return anOptStratB;
    }

    public void playWithOptStrats(){
        //0 index is processed separately
        int stratA = pickStratIndexRandomly(aOptStrat);
        int stratB = pickStratIndexRandomly(bOptStrat);

        statSumValueOfGame[0] = gameMatrix[stratA][stratB];
        statAverageValueOfGame[0] = statSumValueOfGame[0];
        if(gameMatrix[stratA][stratB] > 0){
            winsAmountA++;
        }else if(gameMatrix[stratA][stratB] < 0){
            winsAmountB++;
        }

        for(int i = 1; i < gamesAmount; i++){
            //choose strategies for both players
            stratA = pickStratIndexRandomly(aOptStrat);
            stratB = pickStratIndexRandomly(bOptStrat);
            //System.out.println("A: " + stratA + "; B: " + stratB);

            statSumValueOfGame[i] = statSumValueOfGame[i - 1] + gameMatrix[stratA][stratB];
            statAverageValueOfGame[i] = statSumValueOfGame[i] / ( i + 1 ) ;
            if(gameMatrix[stratA][stratB] > 0){
                winsAmountA++;
            }else if(gameMatrix[stratA][stratB] < 0){
                winsAmountB++;
            }

        }


        debugDummy();
    }

}
