package org.example.game2048;

import java.io.*;
import java.util.ArrayList;

public class ReadAndLoad {
    public static void write(String s1,String s2) {
        try (FileWriter Writer = new FileWriter("APPData/User_id.txt", true);
             BufferedWriter bw = new BufferedWriter(Writer)
             // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            bw.write(s1 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter Writer = new FileWriter("APPData\\User_password.txt", true);
             BufferedWriter bw = new BufferedWriter(Writer)) {
            bw.write(s2 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readid() {
        ArrayList<String> lines = new ArrayList<String>();
        try (FileReader Reader = new FileReader("APPData/User_id.txt");
             BufferedReader br = new BufferedReader(Reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String s = br.readLine();
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public static ArrayList<String> readpassword() {
        ArrayList<String> lines = new ArrayList<String>();
        try (FileReader Reader = new FileReader("APPData/User_password.txt");
             BufferedReader br = new BufferedReader(Reader) // 建立一个对象，它把文件内容转成计算机能读��的语言
        ) {
            String s = br.readLine();
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public static ArrayList<String> myread(String path) {
        ArrayList<String> lines = new ArrayList<String>();
        try (FileReader Reader = new FileReader(path);
             BufferedReader br = new BufferedReader(Reader) // 建立一个对象，它把文件内容转成计算机能读��的语言
        ) {
            String s = br.readLine();
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
