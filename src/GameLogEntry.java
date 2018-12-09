public class GameLogEntry {
    private int gameNumber;

    private int chosenStratA;
    private int chosenStratB;

    private Double[] scoresByB;
    private Double[] scoresByA;

    //bz, bo - накопленный выигрыш для игрока A при выборе первой либо второй стратегии игроком B соответственно
    private double bz;
    private double bo;
    private double az;
    private double ao;

    private double higherPrice;
    private double lowerPrice;
    private double averagePrice;

    private int maxIndex;

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public GameLogEntry(){
        scoresByA = new Double[2];
        scoresByB = new Double[2];
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public int getChosenStratA() {
        return chosenStratA;
    }

    public int getChosenStratB() {
        return chosenStratB;
    }

    public double getHigherPrice() {
        return higherPrice;
    }

    public double getLowerPrice() {
        return lowerPrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public Double[] getScoresByA() {
        return scoresByA;
    }

    public Double[] getScoresByB() {
        return scoresByB;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public void setScoresByA(Double[] scoresByA) {
        this.scoresByA = scoresByA;
    }

    public void setScoresByB(Double[] scoresByB) {
        this.scoresByB = scoresByB;
    }

    public void setChosenStratA(int chosenStratA) {
        this.chosenStratA = chosenStratA;
    }

    public void setChosenStratB(int chosenStratB) {
        this.chosenStratB = chosenStratB;
    }

    public void setLowerPrice(double lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public void setHigherPrice(double higherPrice) {
        this.higherPrice = higherPrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getAo() {
        return ao;
    }

    public double getAz() {
        return az;
    }

    public double getBo() {
        return bo;
    }

    public double getBz() {
        return bz;
    }

    public void setAo(double ao) {
        this.ao = ao;
    }

    public void setAz(double az) {
        this.az = az;
    }

    public void setBo(double bo) {
        this.bo = bo;
    }

    public void setBz(double bz) {
        this.bz = bz;
    }
}
