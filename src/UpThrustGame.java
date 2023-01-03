import java.util.*;

public class UpThrustGame extends NodeGameAB {

    public static final int BOARD_HEIGHT = 11;
    public static final int BOARD_WIDTH = 4;
    private static final int EMPTY_TILE = 0;
    private static final int COLOR_OFFSET = 3;
    private static final int NUMBER_OF_SCORING_ROWS = 5;
    private int myColor;
    private final int[][] board;

    public UpThrustGame(String rawBoard) {
        super(1);
        int[] values = getAllIntsFrom(rawBoard);
        this.board = createAndFill2dArray(BOARD_HEIGHT, BOARD_WIDTH, values);
        this.myColor = getTurn();
        System.out.println(Arrays.deepToString(this.board));
    }

    public UpThrustGame(int[][] board, int myColor, int depth) {
        super(depth);
        this.board = board;
        this.myColor = myColor;
    }

    public static int[] getAllIntsFrom(String string) {
        return string.chars()
                .filter(Character::isDigit)
                .map(Character::getNumericValue)
                .toArray();
    }

    // Maybe use arraycopy
    public static int[][] createAndFill2dArray(int height, int width, int[] values) {
        int[][] result = new int[height][width];
        int index = 0;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                result[row][column] = values[index++];
            }
        }
        return result;
    }

    public static int[][] copy(int[][] original) {
        int[][] result = new int[original.length][original[0].length];

        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }

    private static int newRowIdx(int pawnRow, int moveAmount) {
        return pawnRow - moveAmount;
    }

    private UpThrustGame copyWithMove(int pawnRow, int pawnColumn, int moveAmount) {
        int[][] copy = copy(board);
        copy[newRowIdx(pawnRow, moveAmount)][pawnColumn] = copy[pawnRow][pawnColumn];
        copy[pawnRow][pawnColumn] = EMPTY_TILE;
        return new UpThrustGame(copy, COLOR_OFFSET - myColor, getLvl() + 1);
    }

    private int numberOfPawnsInRow(int pawnRow) {
        return (int) Arrays.stream(board[pawnRow])
                .filter(pawn -> pawn != EMPTY_TILE)
                .count();
    }

    private boolean isHighestPawn(int pawnRow) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (board[row][column] != EMPTY_TILE) {
                    if (row != pawnRow) return false;
                    return numberOfPawnsInRow(pawnRow) == 1;
                }
            }
        }
        return false;
    }

    private static boolean isScoringRow(int row) {
        return row >= 0 && row < NUMBER_OF_SCORING_ROWS;
    }

    private boolean rowHasPieceWithColor(int row, int color) {
        return Arrays.stream(board[row]).anyMatch(piece -> piece == color);
    }

    private boolean isValidMove(int pawnRow, int pawnColumn, int moveAmount) {
        int newRow = newRowIdx(pawnRow, moveAmount);
        if (newRow < 0) return false;
        if (board[newRow][pawnColumn] != EMPTY_TILE) return false;
        if (isHighestPawn(pawnRow)) return false;
        return isScoringRow(newRow) || !rowHasPieceWithColor(newRow, board[pawnRow][pawnColumn]);
    }

    private int myOtherColor() {
        return myColor + 2;
    }

    @Override
    public ArrayList<Move> expandAB() {
        ArrayList<Move> result = new ArrayList<>();

        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int pawnColor = board[row][column];
                if (pawnColor == EMPTY_TILE) continue;
                int moveAmount = numberOfPawnsInRow(row);
                if ((pawnColor == myColor || pawnColor == myOtherColor()) && isValidMove(row, column, moveAmount)) {
                    //String action = pawnColor + " " + (row + 1) + " " + (column + 1);
                    String action = (column + 1) + " " + pawnColor;
                    UpThrustGame game = copyWithMove(row, column, moveAmount);
                    result.add(new Move(action, game));
                }
            }
        }
        return result;
    }

    // TODO change, largest value says which move will be played
    @Override
    public double getHeuristic() {
        if (board == null)
            return 0;
        double heuristic = 0;
        for (int l = 0; l < board.length; l++) {
            for (int c = 0; c < board[l].length; c++) {
                if (board[l][c] == 0)
                    continue;
                int v = Math.max(Math.abs(l - 3), Math.abs(c - 3));
                if (board[l][c] == getTurn())
                    heuristic += 9 - v;
                else
                    heuristic -= 9 - v;
            }
        }
        // testar final de jogo
        int n = 0;
        for (int[] ints : board) {
            for (int anInt : ints) {
                if (anInt == getTurn())
                    n++;
            }
        }
        if (n == 8)
            return WIN;
        n = 0;
        for (int[] ints : board) {
            for (int anInt : ints) {
                if (anInt == 3 - getTurn())
                    n++;
            }
        }
        if (n == 8)
            return LOSS;

        return heuristic;
    }

    public void setMyColor(int myColor) {
        this.myColor = myColor;
    }

    @Override
    public String toString() {
        return "UpThrustGame{" +
                "myColor=" + myColor +
                ", board=" + Arrays.deepToString(board) +
                '}';
    }
}
