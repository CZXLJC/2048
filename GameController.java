package org.example.game2048;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.util.Random;


public class GameController {
    @FXML
    protected JFXButton buttonTryAgain, buttonPlayAgain, buttonSave, buttonContinue, buttonAI, buttonFight,
            buttonUp, buttonDown, buttonLeft, buttonRight, buttonBack, buttonTools, buttonNewGame;
    @FXML
    protected Label labelUsername1, labelUsername2, labelUsername3, scoreNumber1, scoreNumber2, scoreNumber3, labelUsername, labelScore, labelBest;
    @FXML
    protected ImageView Sea, shan, NKD, huoshan;
    @FXML
    protected MenuBar menu;
    @FXML
    protected MenuItem menuSound1, menuSound2, menuSea, menuSUSTech, menuMountain, menuOrig, menuLKWG, menuAQCS, menuVoice, menuStop, menuClose;
    @FXML
    public AnchorPane gridGameOverPane, gridGameWinPane, sliderVoice;
    @FXML
    protected GuiApplication application;
    @FXML
    public GridPane gridPane;
    @FXML
    public Slider volumeSlider;
    //其他变量声明
    protected Label[][] labels;
    protected Random random;

    private AIPlayer aiPlayer = new AIPlayer();
    private Thread AIThread;
    private boolean isAIMode = false;
    protected static final long serialVersionUID = 1L;
    protected int[][] grid;
    protected AudioPlayer audioPlayer = new AudioPlayer();
    protected GameManager gameManager;
    protected GameData gameData;
    protected LogInController logInController = new LogInController();

    public GameController() {
    }
    // 方法用于将标签传递给GameManager
    public void saveGame(String name) {
        String id = labelUsername.getText();
        String path = "APPData\\Save\\id" + id + "\\" + name + ".txt";
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (
                FileWriter file = new FileWriter(path);
                BufferedWriter writer = new BufferedWriter(file)) {
            for (int[] i : LabelToInt(labels)) {
                for (int j : i) {
                    writer.write(j + " ");
                }
                writer.write("\n");
            }
            //还要再写入步数和分数；
            int steps = gameManager.count;
            int scores = gameData.score;
            String stepNscores = String.valueOf(steps) + " " + String.valueOf(scores);
            writer.write(stepNscores);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean loadGame(String name) {
        String id = labelUsername.getText();
        //先不赋值，若出现nopointer，就说明有问题，这样才好！！---编译上就过不了。。。。
        int scores = 0;
        int steps = 0;
        String path = "APPData\\Save\\id" + id + "\\" + name + ".txt";
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                JOptionPane.showOptionDialog(null, "No such file", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "系统内部错误", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int[][] temp = new int[labels.length][labels.length];
        try (
                FileReader file = new FileReader(path);
                BufferedReader reader = new BufferedReader(file)
        ) {
            String line;
            int i = 0;
            int z = 0;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                row++;
                if (numbers.length != 2) {
                    if (numbers.length != labels.length) {
                        JOptionPane.showMessageDialog(null, "102错误，棋盘格式错误:最早在第" + row + "行出错", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    for (int j = 0; j < temp[0].length; j++) {
                        try {
                            int tempInt = Integer.parseInt(numbers[j]);
                            if (tempInt < 0 || !(tempInt == 0 || tempInt == 2 || tempInt == 4 || tempInt == 8 || tempInt == 16
                                    || tempInt == 32 || tempInt == 64 || tempInt == 128 || tempInt == 256
                                    || tempInt == 512 || tempInt == 1024 || tempInt == 2048 || tempInt == 4096
                                    || tempInt == 8192 || tempInt == 16384 || tempInt == 32768 || tempInt == 65536)) {
                                j++;
                                JOptionPane.showMessageDialog(null, "103错误，数字错误:最早在第" + row + "行,第" + j + "列出错", "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        } catch (Exception e) {
                            j++;
                            JOptionPane.showMessageDialog(null, "103错误，字符错误:最早在第" + row + "行,第" + j + "列出错", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        temp[i][j] = Integer.parseInt(numbers[j]);
                    }
                    i++;
                } else {
                    steps = Integer.parseInt(numbers[0]);
                    scores = Integer.parseInt(numbers[1]);
                }
            }
        } catch (Exception e) {
            return false;
        }
        labelScore.setText(String.valueOf(scores));
        gameManager.setCount(steps-1);
        //model.setNumbers(temp);
        //view.setScores(scores);
        //view.setSteps(steps - 1);
        //view.updateGridsNumber();
        //view.afterMove();
        return true;
    }
    // 使用序列化保存游戏数据
    public void saveGame() {
        //int[][] gridData = GameData.labelToInt(labels);
        int score = Integer.parseInt(labelScore.getText());
        int bestScore = Integer.parseInt(labelBest.getText());
        String username = labelUsername.getText();
        //GameData data = new GameData(gridData, score, bestScore, username);
        //GameData.saveData(data, "gameData.ser");
    }

    // 使用序列化加载游戏数据
    public void loadGame() {
        GameData data = GameData.loadData("gameData.ser");
        if (data != null) {
            // GameData.intToLabel(data.getBoard(), labels);
            labelScore.setText(String.valueOf(data.getScore()));
            labelBest.setText(String.valueOf(data.getBestScore()));
            labelUsername.setText(data.getUsername());
        }
    }

    protected void updateGameStage(GameData gameData) {

        labelUsername.setText(gameData.getUsername());
        labelScore.setText(String.valueOf(gameData.getScore()));
        labelBest.setText(String.valueOf(gameData.getBestScore()));
        //labels = intToLabel(gameData.getBoard());
    }

    public void setApplication(GuiApplication application) {
        this.application = application;
        this.labels = createLabels();
        this.random = new Random();
        //updateGameStage(GameData.loadData(labelUsername.getText()));
        //传递给gameManager
        gameManager = new GameManager(labels, labelScore, labelBest, labelUsername, audioPlayer, gridPane, gridGameWinPane, gridGameOverPane);
        handelKeyPressed();
        //slider
        audioPlayer.adjustVolume(volumeSlider);
        gameManager.startGame();
        initAIThread();
        //Runtime.getRuntime().addShutdownHook(new Thread(this::saveGameData));
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

    public void actionNewGame(ActionEvent event) {
        gameManager.startGame();
    }

    public void actionTryAgain(ActionEvent event) {
        gameManager.actionTryAgain();
    }

    public void actionPlayAgain(ActionEvent event) {
        gameManager.actionPlayAgain();
    }

    public void actionModelFight(ActionEvent event) {

    }

    public void actionSave(ActionEvent event) {

    }

    public void actionContinue(ActionEvent event) {

    }

    public void actionBack(ActionEvent event) {
        gameManager.back();
    }

    public void actionUp() {
        gameManager.moveUp();
    }

    public void actionLeft() {
        gameManager.moveLeft();
    }

    public void actionDown() {
        gameManager.moveDown();
    }

    public void actionRight() {
        gameManager.moveRight();
    }

    public void actionModelAI(ActionEvent event) {
        if (isAIMode) {
            buttonAI.setText("AI Mode");
            isAIMode = false;
            AIThread.stop();
        } else {
            buttonAI.setText("Manual Mode");
            isAIMode = true;
            AIThread.start();
        }
    }

    void initAIThread() {
        AIThread = new Thread(() -> {
            while (true) {
                Move move = aiPlayer.DetermineNextMove(LabelToInt(labels));
                switch (move) {
                    case UP:
                        actionUp();
                        break;
                    case DOWN:
                        actionDown();
                        break;
                    case LEFT:
                        actionLeft();
                        break;
                    case RIGHT:
                        actionRight();
                        break;
                }
            }
        });
    }
    public static int[][] LabelToInt(Label[][] labels) {
        int[][] result = new int[4][4];
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                if (labels[i][j].getText() != "") {
                    result[i][j] = Integer.parseInt(labels[i][j].getText());
                } else {
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    // 方法用于将int数组转换为Label数组
    public static Label[][] intToLabel(int[][] gridData) {
        Label[][] labels = new Label[gridData.length][gridData[0].length];
        for (int i = 0; i < gridData.length; i++) {
            for (int j = 0; j < gridData[i].length; j++) {
                Label label = new Label(Integer.toString(gridData[i][j]));
                labels[i][j] = label;
            }
        }
        return labels;
    }

    public void actionMenuSound1(ActionEvent event) {
    }

    public void actionSound2(ActionEvent event) {
    }

    // 菜单项的事件处理方法
    @FXML
    private void actionMenuSea(ActionEvent event) {
        NKD.setVisible(false);
        huoshan.setVisible(false);
        Sea.setVisible(true);
    }

    @FXML
    public void actionMenuMountain(ActionEvent event) {
        huoshan.setVisible(true);
        Sea.setVisible(false);
        NKD.setVisible(false);
    }

    public void menuOrigion(ActionEvent event) {
        Sea.setVisible(false);
        NKD.setVisible(false);
        huoshan.setVisible(false);
    }

    public void actionMenuSUSTech(ActionEvent event) {
        Sea.setVisible(false);
        huoshan.setVisible(false);
        NKD.setVisible(true);
    }

    //音乐
    public void actionAQCS(ActionEvent event) {
        audioPlayer.play("AQCS.mp3");
    }

    public void actionMenuLKWG(ActionEvent event) {
        audioPlayer.play("LKWG.mp3");
    }

    public void actionMenuStop(ActionEvent event) {
        audioPlayer.stop();
    }

    public void actionMenuVoice(ActionEvent event) {
        volumeSlider.setVisible(true);
    }

    public void actionCloseSlider(ActionEvent event) {
        volumeSlider.setVisible(false);
    }

    protected void handelKeyPressed() {
        buttonTools.setFocusTraversable(false);
        buttonSave.setFocusTraversable(false);
        buttonContinue.setFocusTraversable(false);
        buttonAI.setFocusTraversable(false);
        buttonFight.setFocusTraversable(false);
        buttonBack.setFocusTraversable(false);
        buttonNewGame.setFocusTraversable(false);
        buttonPlayAgain.setFocusTraversable(false);
        buttonTryAgain.setFocusTraversable(false);

        //gameController.labels=labels;
        //gameManager.labelBest = labelBest;
        //gameManager.username = labelUsername;

        Scene sceneButtonUp = buttonUp.getScene(); // 获取任意一个控件的场景
        if (sceneButtonUp != null) {
            sceneButtonUp.setOnKeyPressed(this::handleGameRunKeyPress); // 设置按键事件处理器
        }
        Scene sceneButtonLeft = buttonLeft.getScene(); // 获取任意一个控件的场景
        if (sceneButtonLeft != null) {
            sceneButtonLeft.setOnKeyPressed(this::handleGameRunKeyPress); // 设置按键事件处理器
        }
        Scene sceneButtonDown = buttonDown.getScene(); // 获取任意一个控件的场景
        if (sceneButtonDown != null) {
            sceneButtonDown.setOnKeyPressed(this::handleGameRunKeyPress); // 设置按键事件处理器
        }
        Scene sceneButtonRight = buttonRight.getScene(); // 获取任意一个控件的场景
        if (sceneButtonRight != null) {
            sceneButtonRight.setOnKeyPressed(this::handleGameRunKeyPress); // 设置按键事件处理器
        }
        buttonUp.setOnKeyPressed(this::handleGameRunKeyPress);
        buttonLeft.setOnKeyPressed(this::handleGameRunKeyPress);
        buttonDown.setOnKeyPressed(this::handleGameRunKeyPress);
        buttonRight.setOnKeyPressed(this::handleGameRunKeyPress);
    }

    public void handleGameRunKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case UP:
                gameManager.moveUp();
                break;
            case DOWN:
                gameManager.moveDown();
                break;
            case LEFT:
                gameManager.moveLeft();
                break;
            case RIGHT:
                gameManager.moveRight();
                break;
        }
    }

}
