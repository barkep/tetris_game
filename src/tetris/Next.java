package tetris;

import java.awt.Color;
import java.util.Random;

@SuppressWarnings("serial")
public class Next extends Label {

    byte klocek;
    private Random los = new Random();

    Next() {
        super((byte) 100, (byte) 100);
        losujKlocek();
    }

    @Override
    public void drawImage() {
        drukKlocek();
    }

    public void losujKlocek() {
        klocek = (byte) (los.nextInt(6) + 1);
    }

    private void drukKostka(byte x, byte y, byte k) {
        grafika.setColor(Klocki.KOLOR[k]);
        grafika.fillRect(x * Klocki.SIZE, y * Klocki.SIZE, Klocki.SIZE, Klocki.SIZE);
        grafika.setColor(Color.BLACK);
        grafika.drawRect(x * Klocki.SIZE, y * Klocki.SIZE, Klocki.SIZE - 1, Klocki.SIZE - 1);
    }

    private void drukKlocek() {
        grafika.setColor(Klocki.KOLOR[0]);
        grafika.fillRect(0, 0, 4 * Klocki.SIZE, 4 * Klocki.SIZE);
        for (byte tx = 0; tx < 4; tx++)
            for (byte ty = 0; ty < 4; ty++)
                if (Klocki.KLOCKI[klocek][ty][tx]) drukKostka(tx, ty, (byte) (klocek + 1));
    }
}