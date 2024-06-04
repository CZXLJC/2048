package org.example.game2048;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.util.Random;

public class GameManager {

    protected Label[][] labels;
    private int[][] grid;
    private boolean gameEnded;
    private PauseTransition pauseTransition;
    protected Random random;
    protected Label labelScore;
    protected Label labelBest;
    protected Label username;
    protected AudioPlayer audioPlayer;
    protected GridPane gridPane;
    protected AnchorPane gridGameOverPane;
    protected AnchorPane gridGameWinPane;
    protected LogInController logInController = new LogInController();
    protected SignUpController signUpController = new SignUpController();
    protected int count = 0;

    public GameManager(Label[][] labels, Label labelScore, Label labelBest, Label username, AudioPlayer audioPlayer, GridPane gridPane, AnchorPane gridGameWinPane, AnchorPane gridGameOverPane) {
        this.labels = labels;
        this.grid = new int[4][4];
        this.gameEnded = false;
        this.pauseTransition = new PauseTransition(Duration.seconds(2));
        this.random = new Random();
        this.labelScore = labelScore;
        this.labelBest = labelBest;
        this.audioPlayer = audioPlayer;
        this.username = username;
        this.gridPane = gridPane;
        this.gridGameOverPane = gridGameOverPane;
        this.gridGameWinPane = gridGameWinPane;
    }

    public void startGame() {
        gameEnded = false;
        // 游戏初始化逻辑
        clearGrid();
        int row1 = random.nextInt(labels.length);
        int col1 = random.nextInt(labels[0].length);
        int row2 = random.nextInt(labels.length);
        int col2 = random.nextInt(labels[0].length);
        while (row1 == row2 && col1 == col2) {
            row1 = random.nextInt(labels.length);
            col1 = random.nextInt(labels[0].length);
            row2 = random.nextInt(labels.length);
            col2 = random.nextInt(labels[0].length);
        }
        labels[row1][col1].setText("2");
        updateLabelStyle(labels[row1][col1]);
        labels[row2][col2].setText("4");
        updateLabelStyle(labels[row2][col2]);
        labelScore.setText("0");
        labelBest.setText("0");
    }

    protected void clearGrid() {
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                labels[i][j].setText("");
                clearLabelStyle(labels[i][j]);
            }
        }
    }

    protected void updateLabelStyle(Label label) {
        label.getStyleClass().clear();
        label.getStyleClass().add(getClass().getResource("style.css").toExternalForm());
        label.getStyleClass().add("label");
        label.getStyleClass().add("label" + label.getText());
    }

    protected void clearLabelStyle(Label label) {
        label.getStyleClass().clear();
        label.getStyleClass().add(getClass().getResource("style.css").toExternalForm());
        label.getStyleClass().add("label");
    }

    protected Label[][] createLabels() {
        labels = new Label[4][4];
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                Label label = new Label();
                labels[i][j] = label;
                label.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                gridPane.add(label, j, i);
            }
        }
        return labels;
    }

    public void actionTryAgain() {
        gridGameOverPane.setVisible(false);
        gameEnded = false;
        startGame();
    }

    public void actionPlayAgain() {
        gridGameWinPane.setVisible(false);
        gameEnded = false;
        startGame();
    }

    protected void back() {

    }


    public void moveUp() {
        // 处理向上移动逻辑
        if (!gameEnded) {
            count++;
            for (int j = 0; j < labels[0].length; j++) {
                boolean[] merged = new boolean[labels.length];//用来记录这个标签知否已经合并过一次了
                for (int i = 1; i < labels.length; i++) {
                    if (!labels[i][j].getText().isEmpty()) {
                        int k = i;
                        while (k > 0) {
                            if (labels[k - 1][j].getText().isEmpty()) {
                                labels[k - 1][j].setText(labels[k][j].getText());
                                updateLabelStyle(labels[k - 1][j]);
                                labels[k][j].setText("");
                                updateLabelStyle(labels[k][j]);
                                k--;
                            } else if (labels[k - 1][j].getText().equals(labels[k][j].getText()) && !merged[k - 1] && !merged[k]) {
                                int n = 2 * Integer.parseInt(labels[k][j].getText());
                                String s = String.valueOf(n);
                                labels[k - 1][j].setText(s);
                                //更新分数
                                int intNewScore = Integer.parseInt(labelScore.getText()) + Integer.parseInt(s);
                                String newScore = String.valueOf(intNewScore);
                                labelScore.setText(newScore);
                                //更新最高值
                                if (Integer.parseInt(labelScore.getText()) >= Integer.parseInt(labelBest.getText())) {
                                    labelBest.setText(labelScore.getText());
                                }
                                updateLabelStyle(labels[k - 1][j]);
                                labels[k][j].setText("");
                                updateLabelStyle(labels[k][j]);
                                merged[k - 1] = true;
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            generateNewBlock();
            checkGameWinOrLose();
            audioPlayer.play("popSound.mp3");
        }
    }

    public void moveDown() {
        if (!gameEnded) {
            count++;
            for (int j = labels[0].length - 1; j >= 0; j--) {
                boolean[] merged = new boolean[labels.length];
                for (int i = labels.length - 2; i >= 0; i--) {
                    if (!labels[i][j].getText().isEmpty()) {
                        int k = i;
                        while (k < labels[0].length - 1) {
                            if (labels[k + 1][j].getText().isEmpty()) {
                                labels[k + 1][j].setText(labels[k][j].getText());
                                updateLabelStyle(labels[k + 1][j]);
                                labels[k][j].setText("");
                                updateLabelStyle(labels[k][j]);
                                k++;
                            } else if (labels[k + 1][j].getText().equals(labels[k][j].getText()) && !merged[k] && !merged[k + 1]) {
                                int n = 2 * Integer.parseInt(labels[k][j].getText());
                                String s = String.valueOf(n);
                                labels[k + 1][j].setText(s);
                                int intNewScore = Integer.parseInt(labelScore.getText()) + Integer.parseInt(s);
                                String newScore = String.valueOf(intNewScore);
                                labelScore.setText(newScore);
                                //更新最高值
                                if (Integer.parseInt(labelScore.getText()) >= Integer.parseInt(labelBest.getText())) {
                                    labelBest.setText(labelScore.getText());
                                }
                                updateLabelStyle(labels[k + 1][j]);
                                labels[k][j].setText("");
                                updateLabelStyle(labels[k][j]);
                                merged[k + 1] = true;
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            generateNewBlock();
            checkGameWinOrLose();
            audioPlayer.play("popSound.mp3");
        }
        // 处理向下移动逻辑
    }

    public void moveLeft() {
        if (!gameEnded) {
            count++;
            for (int i = 0; i < labels.length; i++) {
                boolean[] merged = new boolean[labels[0].length];
                for (int j = 1; j < labels[0].length; j++) {
                    if (!labels[i][j].getText().isEmpty()) {
                        int k = j;
                        while (k > 0) {
                            if (labels[i][k - 1].getText().isEmpty()) {
                                labels[i][k - 1].setText(labels[i][k].getText());
                                updateLabelStyle(labels[i][k - 1]);
                                labels[i][k].setText("");
                                updateLabelStyle(labels[i][k]);
                                k--;
                            } else if (labels[i][k - 1].getText().equals(labels[i][k].getText()) && !merged[k] && !merged[k - 1]) {
                                int n = 2 * Integer.parseInt(labels[i][k].getText());
                                String s = String.valueOf(n);
                                labels[i][k - 1].setText(s);
                                int intNewScore = Integer.parseInt(labelScore.getText()) + Integer.parseInt(s);
                                String newScore = String.valueOf(intNewScore);
                                labelScore.setText(newScore);
                                //更新最高值
                                if (Integer.parseInt(labelScore.getText()) >= Integer.parseInt(labelBest.getText())) {
                                    labelBest.setText(labelScore.getText());
                                }
                                updateLabelStyle(labels[i][k - 1]);
                                merged[k - 1] = true;
                                labels[i][k].setText("");
                                updateLabelStyle(labels[i][k]);
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            generateNewBlock();
            checkGameWinOrLose();
            audioPlayer.play("popSound.mp3");
        }
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void moveRight() {
        if (!gameEnded) {
            count++;
            for (int i = 0; i < labels.length; i++) {
                boolean[] merged = new boolean[labels.length];
                for (int j = labels[0].length - 2; j >= 0; j--) {
                    if (!labels[i][j].getText().isEmpty()) {
                        int k = j;
                        while (k < labels[0].length - 1) {
                            if (labels[i][k + 1].getText().isEmpty()) {
                                labels[i][k + 1].setText(labels[i][k].getText());
                                updateLabelStyle(this.labels[i][k + 1]);
                                labels[i][k].setText("");
                                updateLabelStyle(this.labels[i][k]);
                                k++;
                            } else if (labels[i][k + 1].getText().equals(labels[i][k].getText()) && !merged[k] && !merged[k + 1]) {
                                int n = 2 * Integer.parseInt(labels[i][k].getText());
                                String s = String.valueOf(n);
                                labels[i][k + 1].setText(s);
                                int intNewScore = Integer.parseInt(labelScore.getText()) + Integer.parseInt(s);
                                String newScore = String.valueOf(intNewScore);
                                labelScore.setText(newScore);
                                //更新最高值
                                if (Integer.parseInt(labelScore.getText()) >= Integer.parseInt(labelBest.getText())) {
                                    labelBest.setText(labelScore.getText());
                                }
                                updateLabelStyle(labels[i][k + 1]);
                                merged[k + 1] = true;
                                labels[i][k].setText("");
                                updateLabelStyle(labels[i][k]);
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            generateNewBlock();
            checkGameWinOrLose();
            audioPlayer.play("popSound.mp3");
        }
    }

    public void generateNewBlock() {
        int row = random.nextInt(labels.length);
        int col = random.nextInt(labels[0].length);
        while (!labels[row][col].getText().isEmpty()) {
            row = random.nextInt(labels.length);
            col = random.nextInt(labels[0].length);
        }
        int newValue = (random.nextInt(2) == 0) ? 2 : 4; // 随机生成 2 或 4
        labels[row][col].setText(String.valueOf(newValue));
        updateLabelStyle(labels[row][col]);
        // 生成新方块逻辑
    }

    public void checkGameWinOrLose() {
        boolean gameLose = true;
        boolean gridIsFull = true;
        int n = Integer.parseInt(labelScore.getText());
        if (n >= 2048) {
            gameEnded = true;
            pauseTransition.setOnFinished(event -> {
                gridGameWinPane.setVisible(true);
                audioPlayer.play("gameWin.mp3");
            }); // 当延迟结束后显示游戏结束界面
            //pauseTransition.setOnFinished(event -> playMusic("gameWin.mp3")); // 当延迟结束后显示游戏结束界面
            pauseTransition.play(); // 启动延迟计时器
        } else {
            for (int i = 0; i < labels.length; i++) {
                for (int j = 0; j < labels[0].length; j++) {
                    if (labels[i][j].getText().isEmpty()) {
                        gridIsFull = false;
                        gameLose = false;
                        gameEnded = false;
                        break;
                    }
                }
            }
            if (gridIsFull) {
                for (int i = 0; i < labels.length - 1; i++) {
                    for (int j = 0; j < labels[0].length - 1; j++) {
                        if (labels[i][j].getText().equals(labels[i][j + 1].getText()) || labels[i][j].getText().equals(labels[i + 1][j].getText())) {
                            gameLose = false;
                            gameEnded = false;
                            break;
                        }
                    }
                }
                for (int j = 0; j < labels[0].length - 1; j++) {
                    if (labels[labels.length - 1][j].equals(labels[labels.length - 1][j + 1])) {
                        gameLose = false;
                        gameEnded = false;
                        break;
                    }
                }
                for (int i = 0; i < labels.length - 1; i++) {
                    if (labels[i][labels.length - 1].equals(labels[i + 1][labels.length - 1])) {
                        gameLose = false;
                        gameEnded = false;
                        break;
                    }
                }
            } else {
                gameLose = false; // 游戏面板未满，不判定为失败
                gameEnded = false;
            }
            if (gameLose) {
                gameEnded = true; // 游戏失败，游戏结束
                pauseTransition.setOnFinished(event -> {
                    gridGameOverPane.setVisible(true);
                    audioPlayer.play("gameLose.mp3");
                }); // 当延迟结束后显示游戏结束界面
                pauseTransition.play(); // 启动延迟计时
            }
        }
        // 检查游戏胜利或失败逻辑
    }

    // 其他游戏逻辑方法...
    // 可以根据需要添加其他方法和成员变量
}
