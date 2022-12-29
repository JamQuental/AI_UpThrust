import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class NodeGameAB {
    private static int WIN = 1000000;
    private static int LOSS = -1000000;
    private static int TIMELIMIT = 5;
    private static int turn;
    private static int maxLevel;
    private int lvl;
    private Move bestMove;
    private static Date moveTime;

    public NodeGameAB(int lvl) {
        this.lvl = lvl;
    }

    public int getSeconds(){
        return (int)(new Date().getTime() - moveTime.getTime())/1000;
    }

    public String processAB(JTextField tf){
        ArrayList<Move> successor = expandAB();
        double biggest = LOSS-1;
        bestMove = null;
        maxLevel = 5;
        moveTime = new Date();
        while(getSeconds() < TIMELIMIT && maxLevel < 50 && biggest < WIN) {
            Move temporaryBestMove = null;
            biggest = LOSS - 1;
            for (Move move : successor) {
                double min = move.getState().minValue(-99999999, 99999999);
                if (min > biggest || (min == biggest && Math.random() > 0.5)) {
                    biggest = min;
                    temporaryBestMove = move;
                    if (tf != null) {
                        tf.setText("Nível:" + maxLevel + "  " + getSeconds() + "s  " + biggest + " : " + temporaryBestMove.getAction());
                    } else {
                        System.out.println("Nível:" + maxLevel + "  " + getSeconds() + "s  " + biggest + " : " + temporaryBestMove.getAction());
                    }
                }
                maxLevel++;
                if (temporaryBestMove != null) {
                    bestMove = temporaryBestMove;
                    if (tf != null) {
                        tf.setText("Nivel:" + maxLevel + "  " + getSeconds() + "s  " + biggest + " : " + bestMove.getAction());
                    } else {
                        System.out.println("Nivel:" + maxLevel + "  " + getSeconds() + "s  " + biggest + " : " + bestMove.getAction());
                    }
                }
                tf.repaint();
            }
        }
            if(bestMove != null){
                return bestMove.getAction();
            }
            else{
                return "passo";
            }

    }


    public double maxValue(double alfa, double beta){
        if(lvl >= maxLevel || getSeconds() > TIMELIMIT){
            return getH();
        }
        ArrayList<Move> successor = expandAB();
        if(successor.size() == 0){
            return getH();
        }
        for(Move move : successor){
            double min = move.getState().minValue(alfa, beta);
            if(min > alfa){
                alfa = min;
            }
            if( alfa >= beta){
                return beta;
            }
        }
        return alfa;
    }
    public double minValue(double alfa, double beta){
        if(lvl >= maxLevel || getSeconds() > TIMELIMIT){
            return getH();
        }
        ArrayList<Move> successor = expandAB();
        if(successor.size() == 0){
            return getH();
        }
        for(Move move: successor){
            double max = move.getState().maxValue(alfa, beta);
            if(max < beta){
                beta = max;
            }
            if(beta <= alfa){
                return alfa;
            }
        }
        return beta;
    }



     //public abstract ArrayList<Move> expandAB();

    //public abstract double getH();



    public static int getTurn() {
        return turn;
    }

    public static void setTurn(String turnInString) {
        int turn = 0;
        try{
            turn = Integer.parseInt(turnInString);
        }
        catch ( Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getWIN() {
        return WIN;
    }

    public static void setWIN(int WIN) {
        NodeGameAB.WIN = WIN;
    }

    public static int getLOSS() {
        return LOSS;
    }

    public static void setLOSS(int LOSS) {
        NodeGameAB.LOSS = LOSS;
    }

    public static int getTIMELIMIT() {
        return TIMELIMIT;
    }

    public static void setTIMELIMIT(int TIMELIMIT) {
        NodeGameAB.TIMELIMIT = TIMELIMIT;
    }

    public static int getMaxLevel() {
        return maxLevel;
    }

    public static void setMaxLevel(int maxLevel) {
        NodeGameAB.maxLevel = maxLevel;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public void setBestMove(Move bestMove) {
        this.bestMove = bestMove;
    }

    public static Date getMoveTime() {
        return moveTime;
    }

    public static void setMoveTime(Date moveTime) {
        NodeGameAB.moveTime = moveTime;
    }
}
