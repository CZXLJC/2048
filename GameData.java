package org.example.game2048;

import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameData implements Serializable {
    protected int score;
    protected int bestScore;
    protected String username;
    protected List<int[][]> moveHistory;
    protected List<Label[][]> labelHistory;

    public GameData(Label[][] labels, int score, int bestScore, String username) {
        this.score = score;
        this.bestScore = bestScore;
        this.username = username;
        this.moveHistory = new ArrayList<>();
        this.labelHistory = new ArrayList<>();
        addMoveAndLabels(labels);
    }

    public void addMoveAndLabels(Label[][] labels) {
        moveHistory.add(labelsToInt(labels));
        labelHistory.add(labels);
    }

    public void undoMove(Label[][] labels) {
        if (moveHistory.size() > 1) {
            moveHistory.remove(moveHistory.size() - 1);
            int[][] prevBoard = moveHistory.get(moveHistory.size() - 1);
            updateLabels(labels, prevBoard);
            labelHistory.remove(labelHistory.size() - 1);
        }
    }

    private int[][] labelsToInt(Label[][] labels) {
        int[][] result = new int[labels.length][labels[0].length];
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                String text = labels[i][j].getText();
                result[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return result;
    }

    private void updateLabels(Label[][] labels, int[][] board) {
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                labels[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                // You can add more logic here to update styles or other properties of the labels
            }
        }
    }
    // 使用序列化保存游戏数据
    public void saveData(String folderPath) {
        try (FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "gameData.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 使用序列化加载游戏数据
    public static GameData loadData(String folderPath) {
        try (FileInputStream fis = new FileInputStream(folderPath + File.separator + "gameData.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public String getUsername() {
        return username;
    }

    public List<int[][]> getMoveHistory() {
        return moveHistory;
    }

    public List<Label[][]> getLabelHistory() {
        return labelHistory;
    }
}