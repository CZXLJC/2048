package model;

import AI.Move;
import view.GamePanel;
import view.ListenerPanel;

import java.util.Arrays;
import java.util.Random;

public class GridNumber {
    private final int X_COUNT;
    private final int Y_COUNT;
    public boolean isMode2 = false;
    public boolean isMode3 = false;

    private int[][] numbers;

    static Random random = new Random();

    public GridNumber(int xCount, int yCount) {
        this.X_COUNT = xCount;
        this.Y_COUNT = yCount;
        this.numbers = new int[this.X_COUNT][this.Y_COUNT];
        this.initialNumbers();
    }

    public GridNumber(int x, int y, boolean isInitial) {
        this.X_COUNT = x;
        this.Y_COUNT = y;
        this.numbers = new int[this.X_COUNT][this.Y_COUNT];
    }

    public boolean checkwhetherGameOver() {
        if (getEmpty() == 0 && !canMove()) {
            return true;
        }
        return false;

    }

    public int getEmpty() {
        int count = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[0].length; j++) {
                if (numbers[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean canMove() {
        int first = 0;
        int second = 0;
        int count = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[0].length - 1; j++) {
                first = numbers[i][j];
                second = numbers[i][j + 1];
                if (first == second) {
                    count++;
                    break;
                }
            }
        }
        for (int j = 0; j < numbers[0].length; j++) {
            for (int i = 0; i < numbers.length - 1; i++) {
                first = numbers[i][j];
                second = numbers[i + 1][j];
                if (first == second) {
                    count++;
                    break;
                }
            }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean newcheckwhethergameover() {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[0].length; j++) {
                if (numbers[i][j] == 0) {
                    return false;
                }
                if (j < numbers[0].length - 1 && numbers[i][j] == numbers[i][j + 1]) {
                    return false;
                }
                if (i < numbers.length - 1 && numbers[i][j] == numbers[i + 1][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addnewGrid() {
        if (getEmpty() != 0) {
            Random rand = new Random();
            Random rand2 = new Random();
            int index = rand.nextInt(getEmpty());
            int count = 0;
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[0].length; j++) {
                    if (numbers[i][j] == 0) {
                        count++;
                        if (count == index + 1) {
                            numbers[i][j] = rand.nextInt(4) == 0 ? 4 : 2;
                        }
                    }
                }
            }
        }
    }

    public void initialNumbers() {
        if (isMode2) {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    numbers[i][j] = random.nextInt(2) == 0 ? 2 : 0;
                }
            }
        } else {
            for (int i = 0; i < numbers.length; i++) {
                for (int j = 0; j < numbers[i].length; j++) {
                    numbers[i][j] = 0;
                }
            }
            addnewGrid();
            addnewGrid();
        }
    }

    public int moveRight() {
        int count = 0;
        for (int x = 0; x < X_COUNT - 1; x++) {
            for (int i = 0; i < numbers.length; i++) {
                int index = numbers[0].length - 1;
                int j = index - 1;
                boolean merged = false; // 添加一个标志变量,用于跟踪是否已经合并过
                while (j >= 0) { // 修改循环条件,确保不会越界
                    if (numbers[i][index] == 0) { // 如果当前位置为空
                        if (numbers[i][j] != 0) { // 如果前一个位置不为空
                            numbers[i][index] = numbers[i][j]; // 则将前一个位置的值移动到当前位置
                            numbers[i][j] = 0; // 并将前一个位置置为0
                        }
                        j--; // 继续向前检查
                    } else if (numbers[i][j] != 0 && numbers[i][index] == numbers[i][j] && !merged) { // 如果当前位置和前一个位置的值相同且未合并过
                        //doRightAnimation(i,index,j);
                        numbers[i][index] += numbers[i][j]; // 则将两个值相加
                        numbers[i][j] = 0; // 并将前一个位置置为0
                        count++;
                        merged = true; // 更新合并标志
                        j--; // 继续向前检查
                    } else if (numbers[i][j] != 0) { // 如果前一个位置不为0且和当前位置的值不同
                        index--; // 则将索引向左移动一位
                        j = index - 1; // 并更新j的值为新索引的前一个位置
                        merged = false; // 重置合并标志
                    } else { // 如果前一个位置为0
                        j--; // 继续向前检查
                    }
                }
            }
            if (!isMode2) {
                break;

            }
        }

        return count;

    }

//    private void doRightAnimation(int Row, int Right, int Left) {
//        long start = System.currentTimeMillis();
//        while(Right-Left>1){
//            long end = System.currentTimeMillis();
//            if(end-start>100){
//                numbers[Row][Right-1]=numbers[Row][Right];
//                numbers[Row][Right]=0;
//                Right--;
//                start=end;
//            }
//        }
//    }

    public int moveLeft() {
        int count = 0;
        for (int x = 0; x < X_COUNT - 1; x++) {
            for (int i = 0; i < numbers.length; i++) {
                int index = 0;
                int j = 1;
                boolean merged = false;
                while (j < numbers[0].length) {
                    if (numbers[i][index] == 0) {
                        if (numbers[i][j] != 0) {
                            numbers[i][index] = numbers[i][j];
                            numbers[i][j] = 0;
                        }
                        j++;
                    } else if (numbers[i][j] != 0 && numbers[i][index] == numbers[i][j] && !merged) {
                        numbers[i][index] += numbers[i][j];
                        numbers[i][j] = 0;
                        count++;
                        merged = true;
                        j++;
                    } else if (numbers[i][j] != 0) {
                        index++;
                        j = index + 1;
                        merged = false;
                    } else {
                        j++;
                    }
                }
            }
            if (!isMode2) {
                break;

            }
        }
        return count;

    }

    public int moveUp() {
        int count = 0;
        for (int y = 0; y < Y_COUNT - 1; y++) {
            for (int j = 0; j < numbers[0].length; j++) {
                int index = 0;
                int i = 1;
                boolean merged = false;
                while (i < numbers.length) {
                    if (numbers[index][j] == 0) {
                        if (numbers[i][j] != 0) {
                            numbers[index][j] = numbers[i][j];
                            numbers[i][j] = 0;
                        }
                        i++;
                    } else if (numbers[i][j] != 0 && numbers[index][j] == numbers[i][j] && !merged) {
                        numbers[index][j] += numbers[i][j];
                        numbers[i][j] = 0;
                        count++;
                        merged = true;
                        i++;
                    } else if (numbers[i][j] != 0) {
                        index++;
                        i = index + 1;
                        merged = false;
                    } else {
                        i++;
                    }
                }
            }
            if (!isMode2) {
                break;

            }
        }
        return count;
    }

    public int moveDown() {
        int count = 0;
        for (int y = 0; y < Y_COUNT - 1; y++) {
            for (int j = 0; j < numbers[0].length; j++) {
                int index = numbers.length - 1;
                int i = index - 1;
                boolean merged = false;
                while (i >= 0) {
                    if (numbers[index][j] == 0) {
                        if (numbers[i][j] != 0) {
                            numbers[index][j] = numbers[i][j];
                            numbers[i][j] = 0;
                        }
                        i--;
                    } else if (numbers[i][j] != 0 && numbers[index][j] == numbers[i][j] && !merged) {
                        numbers[index][j] += numbers[i][j];
                        numbers[i][j] = 0;
                        count++;
                        merged = true;
                        i--;
                    } else if (numbers[i][j] != 0) {
                        index--;
                        i = index - 1;
                        merged = false;
                    } else {
                        i--;
                    }
                }
            }
            if (!isMode2) {
                break;

            }
        }
        return count;
    }

    public int mergesInRow() {
        GridNumber gridNumber = new GridNumber(getX_COUNT(), getY_COUNT());
        int count = 0;
        gridNumber.setNumbers(this.getNumbers());
        count = gridNumber.moveLeft();
        return count;
    }

    public int mergesInCol() {
        GridNumber gridNumber = new GridNumber(getX_COUNT(), getY_COUNT());
        int count = 0;
        gridNumber.setNumbers(this.getNumbers());
        count = gridNumber.moveUp();
        return count;
    }

    //public boolean weathercanmoveleft(){}
    public int getNumber(int i, int j) {
        return numbers[i][j];
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }

    public void printNumber() {
        for (int[] line : numbers) {
            System.out.println(Arrays.toString(line));
        }
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public int getmaxScore() {
        int max = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[0].length; j++) {
                if (numbers[i][j] > max) {
                    max = numbers[i][j];
                }
            }
        }
        return max;
    }

    public int getX_COUNT() {
        return X_COUNT;
    }

    public int getY_COUNT() {
        return Y_COUNT;
    }

}
