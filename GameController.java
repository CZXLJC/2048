package controller;

import model.GridNumber;
import view.GamePanel;
import view.GridComponent;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;


/**
 * This class is used for interactive with JButton in GameFrame.
 */
public class GameController {
    private GamePanel view;
    private GridNumber model;

    public GameController(GamePanel view, GridNumber model) {
        this.view = view;
        this.model = model;
    }

    public void restartGame() {
        model = view.getModel();
        model.initialNumbers();
        view.updateGridsNumber();
        view.setSteps(-1);
        view.afterMove();
        view.restarthistory();
    }

    public void saveGame(String name) {
        model = view.getModel();
        String id = view.getId();
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
            for (int[] i : model.getNumbers()) {
                for (int j : i) {
                    writer.write(j + " ");
                }
                writer.write("\n");
            }
            //还要再写入步数和分数；
            int steps = view.getSteps();
            int scores = view.getScores();
            String stepNscores = String.valueOf(steps) + " " + String.valueOf(scores);
            writer.write(stepNscores);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存历史走棋记录，方便悔棋，但放弃了
//        String path2 = "APPData\\Save\\id" + id + "\\history\\" + name + ".txt";
//        try {
//            File file2 = new File(path2);
//            if (!file2.getParentFile().exists()) {
//                file2.getParentFile().mkdirs();
//            }
//            if (!file2.exists()) {
//                file2.createNewFile();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try (
//                FileWriter file2 = new FileWriter(path2);
//                BufferedWriter writer2 = new BufferedWriter(file2)) {
//            for (int[] i : view.getHistory().get(0)) {
//                for (int j : i) {
//                    writer2.write(j + " ");
//                }
//                writer2.write("\n");
//            }
//            //还要再写入步数和分数；
//            int steps = view.getSteps();
//            int scores = view.getScores();
//            String stepNscores = String.valueOf(steps) + " " + String.valueOf(scores);
//            writer2.write(stepNscores);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//
//        }
    }

    public boolean loadGame(String name) {
        model = view.getModel();
        String id = view.getId();
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
        int[][] temp = new int[model.getX_COUNT()][model.getY_COUNT()];
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
                    if (numbers.length != model.getY_COUNT()) {
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
        model.setNumbers(temp);
        view.setScores(scores);
        view.setSteps(steps - 1);
        view.updateGridsNumber();
        view.afterMove();
        return true;
    }
}
