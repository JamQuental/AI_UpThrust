import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextField;


public abstract class NodeGameAB {
    private static int turn;
    private static int maxDepth;
    private static int LIMIT_TIME = 5;
    public static int VICTORY = 1000000;
    public static int DEFEAT = -1000000;

    private Move bestMove = null;
    private static Date startTime;

    private int depth;

    public NodeGameAB( int depth) {
        this.depth = depth;
    }

//    public abstract String getMove();

    public abstract ArrayList<Move> expandAB();

    public abstract double getH();

    public static int getMaxDepth() {
        return maxDepth;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public int getDepth() {
        return depth;
    }

    protected static void setTurn( String st) {
        turn = 0;
        try {
            turn = Integer.parseInt(st);
        }
        catch ( Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static int getTurn() {
        return turn;
    }


// --------------------------------------------------


    public int getSeconds() {
        return (int)(new Date().getTime()-startTime.getTime())/1000;
    }

    public String processAB( JTextField tf) {
        ArrayList<Move> suc = expandAB();
        double largest = DEFEAT - 1;
        bestMove = null;
        maxDepth = 5;       // no mínimo...
        startTime = new Date();
        while (getSeconds() < LIMIT_TIME && maxDepth < 50 && largest < VICTORY) {
            Move bestOfDepth = null;
            largest = DEFEAT - 1;
            for (Move candidate : suc) {
                double vMin = candidate.getState().minValue( -99999999, 99999999);
                if (vMin > largest || (vMin == largest && maybe())) {
                    largest = vMin;
                    bestOfDepth = candidate;
                    if (tf != null)
                        tf.setText("Depth:"+maxDepth+"  "+getSeconds()+"s  "+largest+" : "+bestOfDepth.getAction());
                    else
                        System.out.println("Depth:"+maxDepth+"  "+getSeconds()+"s  "+largest+" : "+bestOfDepth.getAction());
                }
            }
            maxDepth++;
            if (bestOfDepth != null) {
                bestMove = bestOfDepth;
                if (tf != null)
                    tf.setText("Depth:"+maxDepth+"  "+getSeconds()+"s  "+largest+" : "+bestMove.getAction());
                else
                    System.out.println("Depth:"+maxDepth+"  "+getSeconds()+"s  "+largest+" : "+bestMove.getAction());
            }
            tf.repaint();
        }
        if (bestMove != null)
            return bestMove.getAction();
        else
            return "passo";
    }

    public double maxValue( double alfa, double beta) {
        if (depth >= maxDepth || getSeconds() > LIMIT_TIME)
            return getH();
        ArrayList<Move> suc = expandAB();
        if (suc.size() == 0)
            return getH();
        for (Move cand : suc) {
            double vMin = cand.getState().minValue( alfa, beta);
            if (vMin > alfa) {
                alfa = vMin;
            }
            if (alfa >= beta)
                return beta;
        }
        return alfa;
    }

    public double minValue( double alfa, double beta) {
        if (depth >= maxDepth || getSeconds() > LIMIT_TIME)
            return getH();
        ArrayList<Move> suc = expandAB();
        if (suc.size() == 0)
            return getH();
        for (Move cand : suc) {
            double vMax = cand.getState().maxValue( alfa, beta);
            if (vMax < beta) {
                beta = vMax;
            }
            if (beta <= alfa)
                return alfa;
        }
        return beta;
    }

    private boolean maybe() {
        return Math.random() > 0.5;
    }

}
