package com.example.StickHeroApplication;

import javafx.event.ActionEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class soundManager {

    public static MediaPlayer gameBGM;
    public static MediaPlayer homescreenBGM;
    public static boolean isMuted;
    static AudioClip clickSound;
    private static soundManager instance;

    public static soundManager getInstance() {
        if (instance == null) {  // Singleton design pattern is used here
            instance = new soundManager();
        }
        return instance;
    }

    public void playHomeScreenBGM() {
        isMuted = false;
        String musicPath = Objects.requireNonNull(soundManager.class.getResource("sounds/bgm.mp3")).toString();
        Media media = new Media(musicPath);
        homescreenBGM = new MediaPlayer(media);
        homescreenBGM.setAutoPlay(true);
        homescreenBGM.setVolume(0.2);
        homescreenBGM.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void pauseHomeScreenBGM() {
        homescreenBGM.pause();
    }

    public boolean switchMusic(ActionEvent event) {
        if (isMuted) {
            isMuted = false;
            homescreenBGM.play();
            return true;
        } else {
            isMuted = true;
            homescreenBGM.pause();
            return false;
        }
    }

    public void playClickSound() {// flyweight design pattern is used here
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(soundManager.class.getResource("sounds/click.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();
    }

    public void playPlaySound() {// flyweight pattern is used here
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(soundManager.class.getResource("sounds/play.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();
    }

    public void stopPlaySound() {
        clickSound.stop();
    }

    public void playGameOverSound() {
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(getClass().getResource("sounds/gameOver.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();
    }

    public void playStickGrowSound() {
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(getClass().getResource("sounds/stickgrow.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();
    }

    public void stopStickGrowSound() {
        clickSound.stop();
    }

    public void playStickFallSound() {
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(getClass().getResource("sounds/stickfall.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();
    }

    public void playPlayerFallSound() {
        if (isMuted) {
            return;
        }
        String musicPath = Objects.requireNonNull(getClass().getResource("sounds/playerfall.mp3")).toString();
        clickSound = new AudioClip(musicPath);
        clickSound.play();

    }

    public void playgameplayBGM() {
        try{
            isMuted = false;
            String musicPath = Objects.requireNonNull(soundManager.class.getResource("sounds/playscreenbgm.mp3")).toString();
            Media media = new Media(musicPath);
            homescreenBGM = new MediaPlayer(media);
            homescreenBGM.setAutoPlay(true);
            homescreenBGM.setCycleCount(MediaPlayer.INDEFINITE);
        }catch (Exception e){
            System.out.println("an exception occured in loading gameplaybgmusic");
        }

    }
}