package AI;

import model.GridNumber;
import view.GameFrame;
import view.GamePanel;
import view.GridComponent;

import java.util.Arrays;

public class AIplayer {
    private GridNumber grid;
    private GameFrame gameFrame;
    private GamePanel gamePanel;
    private GridNumber gridNumber;
    private int[][] Numbers;
    private boolean isOpen;
    private static final int FIXED_SCORE = 2; // 假设的固定分数
    private static final int EMPTY_SCORE = 2; // 空格分数
    private static final int MERGES_SCORE = 3; // 合并分数，用结果的，不用过程的，原因是对结果关注更合理
    private static final int MONOTONICITY_SCORE = 1; // 单调性分数
    private static final int SUM_SCORE = 0; // 行和分数

    public int[][] getNowNumbers() {
        int[][] Numbers = gridNumber.getNumbers();
        int[][] numbers = new int[Numbers.length][Numbers[0].length];
        for (int i = 0; i < Numbers.length; i++) {
            for (int j = 0; j < Numbers[0].length; j++) {
                numbers[i][j] = Numbers[i][j];
            }
        }
        return numbers;
    }

    public Move DetermineNextMove(int[][] board) {
        Move bestMove = null;
        int bestScore = 0;
        for (Move move : Move.values()) {
            int score = calculateScore(board, move);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int calculateScore(int[][] board, Move move) {
        int[][] newboard = DoMove(board, move);
        if (Arrays.deepEquals(newboard, board)) {
            return 0;
        } else {
            return GenerateScore(newboard, 0, 3);
        }
    }

    private int[][] DoMove(int[][] board, Move move) {
        int[][] newboard = new int[board.length][board[0].length];
        switch (move) {
            case UP -> newboard = doUP(board);
            case DOWN -> newboard = doDOWN(board);
            case LEFT -> newboard = doLEFT(board);
            case RIGHT -> newboard = doRIGHT(board);
        }
        return newboard;
    }

    private int GenerateScore(int[][] board, int Currentdepth, int Depthlimit) {
        int totalScore = 0;
        if (Currentdepth == Depthlimit) {
            return calculateFinalScore(board);
        } else {
            int[][] newboard2 = new int[board.length][board[0].length];
            for (int k = 0; k < board.length; k++) {
                for (int l = 0; l < board[0].length; l++) {
                    newboard2[k][l] = board[k][l];
                }
            }
            int[][] newboard4 = new int[board.length][board[0].length];
            for (int k = 0; k < board.length; k++) {
                for (int l = 0; l < board[0].length; l++) {
                    newboard4[k][l] = board[k][l];
                }
            }
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == 0) {
                        newboard2[i][j] = 2;
                        int totalScore2 = CalculteMoveScore(newboard2, Currentdepth, Depthlimit);
                        newboard4[i][j] = 4;
                        int totalScore4 = CalculteMoveScore(newboard4, Currentdepth, Depthlimit);
                        float TheScore = 0.25f * (float) totalScore4 + 0.75f * (float) totalScore2;
                        totalScore += (int) TheScore;
                    }
                }
            }
        }
        return totalScore;
    }

    private int CalculteMoveScore(int[][] board, int currentDepth, int depthlimit) {
        int bestScore = 0;
        for (Move move : Move.values()) {
            int[][] newBoard = DoMove(board, move);
            if (!Arrays.deepEquals(newBoard, board)) {
                int score = GenerateScore(newBoard, currentDepth + 1, depthlimit);
                if (score > bestScore) {
                    bestScore = score;
                }
            }
        }
        return bestScore;
    }

    private int calculateFinalScore(int[][] board) {
        int score = 0;
        score += FIXED_SCORE * board.length*board[0].length;
        score += EMPTY_SCORE * getEmpty(board);
        score += MERGES_SCORE * mergesInRow(board);
        //score -= MONOTONICITY_SCORE * Math.min(leftMonotonicityInRow(board), rightMonotonicityInRow(board));
        //score -= SUM_SCORE * sumInRow(board);
        score += FIXED_SCORE * board[0].length*board.length;
        score += EMPTY_SCORE * getEmpty(board);
        score += MERGES_SCORE * mergesInCol(board);
        //score -= MONOTONICITY_SCORE * Math.min(leftMonotonicityInCol(board), rightMonotonicityInCol(board));
        //score -= SUM_SCORE * sumInRow(board);
        return score;
    }

    private int sumInCol(int[][] board) {
        int score = 0;
        for (int i = 0; board.length > i; i++) {
            for (int j = 0; j < board[0].length; j++) {
                score += board[i][j];
            }
        }
        return score;
    }

    private int sumInRow(int[][] board) {
        int score = 0;
        for (int i = 0; board[0].length > i; i++) {
            for (int j = 0; j < board.length; j++) {
                score += board[j][i];
            }
        }
        return score;
    }

    private int mergesInCol(int[][] board) {
//       grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        return grid.mergesInCol();
        int count=0;
        int[][] newboard=deepcopy(board);
        for (int x = 0; x < newboard[0].length-2; x++) {
            for (int i = 0; i < newboard.length; i++) {
                int index = newboard[0].length - 1;
                int j = index - 1;
                boolean merged = false; // 添加一个标志变量,用于跟踪是否已经合并过
                while (j >= 0) { // 修改循环条件,确保不会越界
                    if (newboard[i][index] == 0) { // 如果当前位置为空
                        if (newboard[i][j] != 0) { // 如果前一个位置不为空
                            newboard[i][index] = newboard[i][j]; // 则将前一个位置的值移动到当前位置
                            newboard[i][j] = 0; // 并将前一个位置置为0
                        }
                        j--; // 继续向前检查
                    } else if (newboard[i][j] != 0 && newboard[i][index] == newboard[i][j] && !merged) { // 如果当前位置和前一个位置的值相同且未合并过
                        newboard[i][index] += newboard[i][j]; // 则将两个值相加
                        newboard[i][j] = 0; // 并将前一个位置置为0
                        count++;
                        merged = true; // 更新合并标志
                        j--; // 继续向前检查
                    } else if (newboard[i][j] != 0) { // 如果前一个位置不为0且和当前位置的值不同
                        index--; // 则将索引向左移动一位
                        j = index - 1; // 并更新j的值为新索引的前一个位置
                        merged = false; // 重置合并标志
                    } else { // 如果前一个位置为0
                        j--; // 继续向前检查
                    }
                }
            }
        }
        return count;
    }

    private int mergesInRow(int[][] board) {
//        grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        return grid.mergesInRow();
        int count=0;
        int[][] newboard=deepcopy(board);
        for (int y = 0;y < newboard.length-2; y++) {
            for (int j = 0; j < newboard[0].length; j++) {
                int index = newboard.length - 1;
                int i = index - 1;
                boolean merged = false;
                while (i >= 0) {
                    if (newboard[index][j] == 0) {
                        if (newboard[i][j] != 0) {
                            newboard[index][j] = newboard[i][j];
                            newboard[i][j] = 0;
                        }
                        i--;
                    } else if (newboard[i][j] != 0 && newboard[index][j] == newboard[i][j] && !merged) {
                        newboard[index][j] += newboard[i][j];
                        newboard[i][j] = 0;
                        count++;
                        merged = true;
                        i--;
                    } else if (newboard[i][j] != 0) {
                        index--;
                        i = index - 1;
                        merged = false;
                    } else {
                        i--;
                    }
                }
            }
        }
        return count++;
    }

    private int emptyCellsInCol(int[][] board) {
        grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
        geteverboard(board);
        grid.setNumbers(Numbers);
        return grid.getEmpty();
    }

    private int emptyCellsInRow(int[][] board) {
        grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
        geteverboard(board);
        grid.setNumbers(Numbers);
        return grid.getEmpty();
    }


    private int getEmpty(int[][] board) {
        return 0;
    }


    private int[][] doRIGHT(int[][] board) {
//        GridNumber grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        grid.moveRight();
//        return grid.getNumbers();
        int[][] newboard=deepcopy(board);
        for (int x = 0; x < newboard[0].length-1; x++) {
            for (int i = 0; i < newboard.length; i++) {
                int index = newboard[0].length - 1;
                int j = index - 1;
                boolean merged = false; // 添加一个标志变量,用于跟踪是否已经合并过
                while (j >= 0) { // 修改循环条件,确保不会越界
                    if (newboard[i][index] == 0) { // 如果当前位置为空
                        if (newboard[i][j] != 0) { // 如果前一个位置不为空
                            newboard[i][index] = newboard[i][j]; // 则将前一个位置的值移动到当前位置
                            newboard[i][j] = 0; // 并将前一个位置置为0
                        }
                        j--; // 继续向前检查
                    } else if (newboard[i][j] != 0 && newboard[i][index] == newboard[i][j] && !merged) { // 如果当前位置和前一个位置的值相同且未合并过
                        newboard[i][index] += newboard[i][j]; // 则将两个值相加
                        newboard[i][j] = 0; // 并将前一个位置置为0
                        merged = true; // 更新合并标志
                        j--; // 继续向前检查
                    } else if (newboard[i][j] != 0) { // 如果前一个位置不为0且和当前位置的值不同
                        index--; // 则将索引向左移动一位
                        j = index - 1; // 并更新j的值为新索引的前一个位置
                        merged = false; // 重置合并标志
                    } else { // 如果前一个位置为0
                        j--; // 继续向前检查
                    }
                }
            }
            if(!gridNumber.isMode2){
                break;
            }
        }
        return newboard;
    }

    public static int[][] deepcopy(int[][] board) {
        int[][] newboard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        return newboard;
    }

    private int[][] doLEFT(int[][] board) {
//        GridNumber grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        grid.moveLeft();
//        return grid.getNumbers();
        int[][] newboard=deepcopy(board);
        for (int x = 0; x < newboard[0].length-1; x++) {
            for (int i = 0; i < newboard.length; i++) {
                int index = 0;
                int j = 1;
                boolean merged = false;
                while (j < newboard[0].length) {
                    if (newboard[i][index] == 0) {
                        if (newboard[i][j] != 0) {
                            newboard[i][index] = newboard[i][j];
                            newboard[i][j] = 0;
                        }
                        j++;
                    } else if (newboard[i][j] != 0 && newboard[i][index] == newboard[i][j] && !merged) {
                        newboard[i][index] += newboard[i][j];
                        newboard[i][j] = 0;
                        merged = true;
                        j++;
                    } else if (newboard[i][j] != 0) {
                        index++;
                        j = index + 1;
                        merged = false;
                    } else {
                        j++;
                    }
                }
            }
            if(!gridNumber.isMode2){
                break;
            }
        }
        return newboard;
    }

    private int[][] doDOWN(int[][] board) {
//        GridNumber grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        grid.moveDown();
//        return grid.getNumbers();
        int[][] newboard=deepcopy(board);
        for (int y = 0;y < newboard.length-1; y++) {
            for (int j = 0; j < newboard[0].length; j++) {
                int index = newboard.length - 1;
                int i = index - 1;
                boolean merged = false;
                while (i >= 0) {
                    if (newboard[index][j] == 0) {
                        if (newboard[i][j] != 0) {
                            newboard[index][j] = newboard[i][j];
                            newboard[i][j] = 0;
                        }
                        i--;
                    } else if (newboard[i][j] != 0 && newboard[index][j] == newboard[i][j] && !merged) {
                        newboard[index][j] += newboard[i][j];
                        newboard[i][j] = 0;
                        merged = true;
                        i--;
                    } else if (newboard[i][j] != 0) {
                        index--;
                        i = index - 1;
                        merged = false;
                    } else {
                        i--;
                    }
                }
            }
            if(!gridNumber.isMode2){
                break;
            }
        }
        return newboard;
    }

    private int[][] doUP(int[][] board) {
//        GridNumber grid = new GridNumber(gridNumber.getX_COUNT(), gridNumber.getY_COUNT(),false);
//        geteverboard(board);
//        grid.setNumbers(Numbers);
//        grid.moveUp();
//        return grid.getNumbers();
        int[][] newboard=deepcopy(board);
        for (int y = 0; y < newboard.length-1; y++) {
            for (int j = 0; j < newboard[0].length; j++) {
                int index = 0;
                int i = 1;
                boolean merged = false;
                while (i < newboard.length) {
                    if (newboard[index][j] == 0) {
                        if (newboard[i][j] != 0) {
                            newboard[index][j] = newboard[i][j];
                            newboard[i][j] = 0;
                        }
                        i++;
                    } else if (newboard[i][j] != 0 && newboard[index][j] == newboard[i][j] && !merged) {
                        newboard[index][j] += newboard[i][j];
                        newboard[i][j] = 0;
                        merged = true;
                        i++;
                    } else if (newboard[i][j] != 0) {
                        index++;
                        i = index + 1;
                        merged = false;
                    } else {
                        i++;
                    }
                }
            }
            if(!gridNumber.isMode2){
                break;
            }
        }
        return newboard;
    }

    private void geteverboard(int[][] board) {
        board = new int[board.length][board[0].length];
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++){
                board[i][j]=board[i][j];
            }
        }
    }

    public void runAI(){
        while(gameFrame.isAIMODE()&&!gridNumber.checkwhetherGameOver()){
            int[][] numbers=getNowNumbers();
            Move bestMove=DetermineNextMove(numbers);
            switch (bestMove){
                case UP:
                    gamePanel.doMoveUp();
                    break;
                case DOWN:
                    gamePanel.doMoveDown();
                    break;
                case LEFT:
                    gamePanel.doMoveLeft();
                    break;
                case RIGHT:
                    gamePanel.doMoveRight();
                    break;
            }
        }
    }

    public void setGridNumber(GridNumber gridNumber) {
        this.gridNumber = gridNumber;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
