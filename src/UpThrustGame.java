import java.util.ArrayList;
import java.util.Arrays;

public class UpThrustGame extends NodeGameAB{
    String[][] gameMatrix;
    private boolean isPlayer1;
    public UpThrustGame(String[][] gameMatrix) {
        super(1);
        this.gameMatrix = gameMatrix;
        if(getTurn() == 1){
            isPlayer1 = true;
        }
        else{
            isPlayer1 = false;
        }
    }

    public void addNewState(UpThrustGame newState){
        printMatrix(newState.gameMatrix);
    }

    public String[][] copy(String[][] original){
        String[][] newArr = new String[original.length][original[0].length];
        for (int i = 0; i < newArr.length; i++) {
            for (int k = 0; k < newArr[i].length; k++) {
                newArr[i][k] = original[i][k];
            }
        }
        return newArr;
    }

    public boolean biggestPosition(int colorLinePos, int colorColumnPos, String color){
        int line = 0;
        int column = 0;
        for(int i = 0; i < gameMatrix.length; i++){
            for(int k = 0; k < gameMatrix[i].length; k++){
                if(gameMatrix[i][k].equalsIgnoreCase(color)){
                    line = i;
                    column = k;
                    if(line == colorLinePos && column == colorColumnPos && numberOfJumps(gameMatrix[i]) == 1){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
       for(int i = 0; i < gameMatrix.length; i++){
           System.out.println(Arrays.toString(gameMatrix[i]));
       }
       return "\n";
    }



    public boolean color(int lineColorPos, int colorPos, String color){
        if(lineColorPos >= 5){
            for(int i = 0; i < gameMatrix[lineColorPos].length; i++){
                if(i == colorPos){
                    continue;
                }
                else if(gameMatrix[lineColorPos][i].equalsIgnoreCase(color)){
                    return false;
                }
            }
        }
        return true;
    }

    public void printMatrix(String[][] matrix){
        for(int i = 0; i < gameMatrix.length; i++){
            System.out.println(Arrays.toString(gameMatrix[i]) + " line - " + i);
        }
        System.out.println("\n");
    }
    public int numberOfJumps(String[] jumps){
        int numberOfJumps = 0;
        for(int i = 0; i < jumps.length; i++){
            if(!jumps[i].equalsIgnoreCase("0")){
                numberOfJumps++;
            }
        }
        return numberOfJumps;
    }
    @Override
    public ArrayList<Move> expandAB() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int i = 0; i < gameMatrix.length; i++) {
            int num = numberOfJumps(gameMatrix[i]);
            for (int k = 0; k < gameMatrix[i].length; k++) {
                if ((isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("1"))
                        || (isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("3"))) {
                    if (i - num >= 0 && gameMatrix[i - num][k].equalsIgnoreCase("0")
                            && biggestPosition(i, k, gameMatrix[i][k])) {// esqueci me de contar quantos estavam na
                        // linha: Funciona

                        String[][] tempArray = copy(gameMatrix);
                        String temp = tempArray[i - num][k];
                        tempArray[i - num][k] = tempArray[i][k];
                        tempArray[i][k] = temp;

                        if (color(i - num, k, tempArray[i - num][k])) {
                            printMatrix(tempArray);
                            possibleMoves.add(new Move("" + (k+1) + " " +gameMatrix[i][k], new UpThrustGame(tempArray)));
                        }
                    }
                }
                if ((!isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("2"))
                        || (!isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("4"))) {
                    if (i - num >= 0 && gameMatrix[i - num][k].equalsIgnoreCase("0")
                            && biggestPosition(i, k, gameMatrix[i][k])) {

                        String[][] tempArray = copy(gameMatrix);
                        String temp = tempArray[i - num][k];
                        tempArray[i - num][k] = tempArray[i][k];
                        tempArray[i][k] = temp;

                        if (color(i - num, k, tempArray[i - num][k])) {
                            possibleMoves.add(new Move("" + (k+1) + " " +gameMatrix[i][k], new UpThrustGame(tempArray)));
                            printMatrix(tempArray);
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public double getHeuristic() {
        if (gameMatrix == null)
            return 0;
        double heuristic = 0;
        for (int l = 0; l < gameMatrix.length; l++) {
            for (int c = 0; c < gameMatrix[l].length; c++) {
                if (gameMatrix[l][c].equals("0"))
                    continue;
                int v = Math.max(Math.abs(l - 3), Math.abs(c - 3));
                if (gameMatrix[l][c].equals(Integer.toString(getTurn())))
                    heuristic += 9 - v;
                else
                    heuristic -= 9 - v;
            }
        }
        // testar final de jogo
        int n = 0;
        for (int l = 0; l < gameMatrix.length; l++) {
            for (int c = 0; c <  gameMatrix[l].length; c++) {
                if (gameMatrix[l][c].equals(Integer.toString(getTurn())))
                    n++;
            }
        }
        if (n == 8)
            return WIN;
        n = 0;
        for (int l = 0; l < gameMatrix.length; l++) {
            for (int c = 0; c < gameMatrix[l].length; c++) {
                if (gameMatrix[l][c].equals(Integer.toString(3-getTurn())))
                    n++;
            }
        }
        if (n == 8)
            return LOSS;

        return heuristic;
    }

    public ArrayList<UpThrustGame> successor(){
        ArrayList<UpThrustGame> possibleSuccessors = new ArrayList<>();
        for (int i = 0; i < gameMatrix.length; i++) {
            int n = numberOfJumps(gameMatrix[i]);
            for (int k = 0; k < gameMatrix[i].length; k++) {
                if ((isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("1"))
                        || (isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("3"))) {
                    if (i - n >= 0 && gameMatrix[i - n][k].equalsIgnoreCase("0")
                            && biggestPosition(i, k, gameMatrix[i][k])) {// esqueci me de contar quantos estavam na
                        // linha: Funciona

                        String[][] tempArray = copy(gameMatrix);
                        String temp = tempArray[i - n][k];
                        tempArray[i - n][k] = tempArray[i][k];
                        tempArray[i][k] = temp;
                        //printMatrizInv(tempArray);
                        if (color(i - n, k, tempArray[i - n][k])) {// simples erro de logica: corrigido
                            printMatrix(tempArray);
                            possibleSuccessors.add(new UpThrustGame(tempArray));
                        }
                        //para fazer todos verificar antes se está ou não ativo;
                    }
                }
                if ((isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("2"))
                        || (isPlayer1 && gameMatrix[i][k].equalsIgnoreCase("4"))) {
                    if (i - n >= 0 && gameMatrix[i - n][k].equalsIgnoreCase("0")
                            && biggestPosition(i, k, gameMatrix[i][k])) {

                        String[][] tempArray = copy(gameMatrix);

                        String temp = tempArray[i - n][k];
                        tempArray[i - n][k] = tempArray[i][k];
                        tempArray[i][k] = temp;

                        if (color(i - n, k, tempArray[i - n][k])) {
                            possibleSuccessors.add(new UpThrustGame(tempArray));
                            printMatrix(tempArray);
                        }
                    }
                }
            }
        }
        return possibleSuccessors;
    }


    public String[][] getGameMatrix() {
        return gameMatrix;
    }

    public void setGameMatrix(String[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public void setPlayer1(boolean player1) {
        isPlayer1 = player1;
    }
}
