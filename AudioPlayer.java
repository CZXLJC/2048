package org.example.game2048;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class AudioPlayer {
    protected MediaPlayer mediaPlayer;
    protected void adjustVolume(Slider slider){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(slider.getValue() * 0.01);
            }
        });
    }
    public void play(String musicFile) {
        stop(); // 停止之前的播放
        try {
            URL resource = getClass().getResource(musicFile);
            if (resource == null) {
                System.out.println("无法找到音频文件：" + musicFile);
                return;
            }
            String mediaPath=resource.toExternalForm();
            //System.out.println("播放音频文件：" + mediaPath);

            Media media = new Media(mediaPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() -> System.out.println("MediaPlayer错误：" + mediaPlayer.getError().getMessage()));  // 捕获错误
            mediaPlayer.play();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
