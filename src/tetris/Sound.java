package tetris;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

    public boolean isPlay = false;
    private AudioClip clip;

    Sound(String nazwaPliku) {
        clip = Applet.newAudioClip(getClass().getResource("/resources/"+nazwaPliku));
    }

    public void play() {
        isPlay = true;
        new Thread() {
            public void run() {
                clip.play();
            }
        }.start();
    }

    public void stop() {
        isPlay = false;
        clip.stop();
    }

    public void petla() {
        isPlay = true;
        clip.loop();
    }
}
