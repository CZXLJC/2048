package org.example.game2048;

import java.util.Arrays;

public class AIPlayer {
    private GameController gameController;
    private static final int FIXED_SCORE = 100; // 假设的固定分数
    private static final int EMPTY_SCORE = 3; // 空格分数
    private static final int MERGES_SCORE = 5; // 合并分数，用结果的，不用过程的，原因是对结果关注更合理
    private static final int MONOTONICITY_SCORE = 10; // 单调性分数
    private static final int SUM_SCORE = 1; // 行和分数

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
        int score = 10000;
        for (int row = 0; row < board.length; row++) {
            score += FIXED_SCORE * board[0].length;
            score += EMPTY_SCORE * emptyCellsInRow(board, row);
            score += MERGES_SCORE * mergesInRow(board, row);
            score -= MONOTONICITY_SCORE * Math.min(leftMonotonicityInRow(board, row), rightMonotonicityInRow(board, row));
            score -= SUM_SCORE * sumInRow(board, row);
        }
        for (int col = 0; col < board[0].length; col++) {
            score += FIXED_SCORE * board.length;
            score += EMPTY_SCORE * emptyCellsInCol(board, col);
            score += MERGES_SCORE * mergesInCol(board, col);
            score -= MONOTONICITY_SCORE * Math.min(leftMonotonicityInCol(board, col), rightMonotonicityInCol(board, col));
            score -= SUM_SCORE * sumInCol(board, col);
        }
        return score;
    }

    private int rightMonotonicityInCol(int[][] board, int col) {
        int score = 0;
        for (int i = 1; i < board.length; i++) {
            if (board[i][col] > board[i - 1][col]) {
                score++;
            }
        }
        return score;
    }

    private int rightMonotonicityInRow(int[][] board, int row) {
        int score = 0;
        for (int col = board[0].length - 2; col >= 0; col--) {
            if (board[row][col] < board[row][col + 1]) {
                score++; // 发现一个升序对，增加得分
            }
        }
        return score;
    }

    private int leftMonotonicityInCol(int[][] board, int col) {
        int score = 0;
        for (int i = board.length - 2; i >= 0; i--) {
            if (board[i][col] > board[i + 1][col]) {
                score++;
            }
        }
        return score;
    }

    private int leftMonotonicityInRow(int[][] board, int row) {
        int score = 0;
        for (int col = 1; col < board[0].length; col++) {
            if (board[row][col] < board[row][col - 1]) {
                score++;
            }
        }
        return score;
    }

    private int sumInCol(int[][] board, int col) {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            score += board[i][col];
        }
        return score;
    }

    private int sumInRow(int[][] board, int row) {
        int score = 0;
        for (int j = 0; j < board[0].length; j++) {
            score += board[row][j];
        }
        return score;
    }

    private int mergesInCol(int[][] board, int Col) {
        int count = 0;
        for (int row = 0; row < board.length - 1; row++) {
            if (board[row][Col] == board[row + 1][Col]) {
                count++;
            }
        }
        return count;
    }

    private int mergesInRow(int[][] board, int Row) {
        int count = 0;
        for (int col = 0; col < board[0].length - 1; col++) {
            if (board[Row][col] == board[Row][col]) {
                count++;
            }
        }
        return count;
    }

    private int emptyCellsInCol(int[][] board, int Col) {
        int count = 0;
        for (int row = 0; row < board[0].length; row++) {
            if (board[row][Col] == 0) {
                count++;
            }
        }
        return count;
    }

    private int emptyCellsInRow(int[][] board, int Row) {
        int count = 0;
        for (int col = 0; col < board.length; col++) {
            if (board[Row][col] == 0) {
                count++;
            }
        }
        return count;
    }

    private int[][] doRIGHT(int[][] board) {
        int[][] newboard = deepcopy(board);
        for (int i = 0; i < newboard.length; i++) {
            // Step 1: Move all non-zero elements to the right
            int[] newRow = new int[newboard[i].length];
            int index = newRow.length - 1;
            for (int j = newboard[i].length - 1; j >= 0; j--) {
                if (newboard[i][j] != 0) {
                    newRow[index--] = newboard[i][j];
                }
            }

            // Step 2: Merge adjacent equal elements
            for (int j = newRow.length - 1; j > 0; j--) {
                if (newRow[j] != 0 && newRow[j] == newRow[j - 1]) {
                    newRow[j] *= 2;
                    newRow[j - 1] = 0;
                    j--; // Skip the next element
                }
            }

            // Step 3: Move all non-zero elements to the right again
            index = newRow.length - 1;
            for (int j = newRow.length - 1; j >= 0; j--) {
                if (newRow[j] != 0) {
                    newboard[i][index--] = newRow[j];
                }
            }
            // Fill the remaining elements with zero
            while (index >= 0) {
                newboard[i][index--] = 0;
            }

        }
        return newboard;
    }

    private int[][] doLEFT(int[][] board) {
        int[][] newboard = deepcopy(board);
        for (int i = 0; i < newboard.length; i++) {
            // Step 1: Move all non-zero elements to the left
            int[] newRow = new int[newboard[i].length];
            int index = 0;
            for (int j = 0; j < newboard[i].length; j++) {
                if (newboard[i][j] != 0) {
                    newRow[index++] = newboard[i][j];
                }
            }

            // Step 2: Merge adjacent equal elements
            for (int j = 0; j < newRow.length - 1; j++) {
                if (newRow[j] != 0 && newRow[j] == newRow[j + 1]) {
                    newRow[j] *= 2;
                    newRow[j + 1] = 0;
                    j++; // Skip the next element
                }
            }

            // Step 3: Move all non-zero elements to the left again
            index = 0;
            for (int j = 0; j < newRow.length; j++) {
                if (newRow[j] != 0) {
                    newboard[i][index++] = newRow[j];
                }
            }
            // Fill the remaining elements with zero
            while (index < newRow.length) {
                newboard[i][index++] = 0;
            }


        }
        return newboard;
    }


    private int[][] doDOWN(int[][] board) {
        int[][] newboard = deepcopy(board);
        for (int j = 0; j < newboard[0].length; j++) {
            // Step 1: Move all non-zero elements down
            int[] newCol = new int[newboard.length];
            int index = newCol.length - 1;
            for (int i = newboard.length - 1; i >= 0; i--) {
                if (newboard[i][j] != 0) {
                    newCol[index--] = newboard[i][j];
                }
            }

            // Step 2: Merge adjacent equal elements
            for (int i = newCol.length - 1; i > 0; i--) {
                if (newCol[i] != 0 && newCol[i] == newCol[i - 1]) {
                    newCol[i] *= 2;
                    newCol[i - 1] = 0;
                    i--; // Skip the next element
                }
            }

            // Step 3: Move all non-zero elements down again
            index = newCol.length - 1;
            for (int i = newCol.length - 1; i >= 0; i--) {
                if (newCol[i] != 0) {
                    newboard[index--][j] = newCol[i];
                }
            }
            // Fill the remaining elements with zero
            while (index >= 0) {
                newboard[index--][j] = 0;
            }


        }
        return newboard;
    }


    private int[][] doUP(int[][] board) {
        int[][] newboard = deepcopy(board);
        for (int j = 0; j < newboard[0].length; j++) {
            // Step 1: Move all non-zero elements up
            int[] newCol = new int[newboard.length];
            int index = 0;
            for (int i = 0; i < newboard.length; i++) {
                if (newboard[i][j] != 0) {
                    newCol[index++] = newboard[i][j];
                }
            }

            // Step 2: Merge adjacent equal elements
            for (int i = 0; i < newCol.length - 1; i++) {
                if (newCol[i] != 0 && newCol[i] == newCol[i + 1]) {
                    newCol[i] *= 2;
                    newCol[i + 1] = 0;
                    i++; // Skip the next element
                }
            }

            // Step 3: Move all non-zero elements up again
            index = 0;
            for (int i = 0; i < newCol.length; i++) {
                if (newCol[i] != 0) {
                    newboard[index++][j] = newCol[i];
                }
            }
            // Fill the remaining elements with zero
            while (index < newCol.length) {
                newboard[index++][j] = 0;
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


    private void geteverboard(int[][] board) {
        board = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = board[i][j];
            }
        }
    }



}