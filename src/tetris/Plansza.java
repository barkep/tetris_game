package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class Plansza extends Label implements ActionListener {

    final static short SZE = Klocki.SIZE * 10;
    final static short WYS = Klocki.SIZE * 20;

    byte[][] tab = new byte[12][22];
    Random los = new Random();

    Klocek klocek = new Klocek();
    byte klocekX, klocekY;

    boolean kLeft, kRight, kUp, kDown;

    short speed, speedMax;
    boolean speedKey;

    Sound sKlocek, sObrot, sPasek;

    boolean gamePlay, pause;
    Color sKolor;

    Plansza() {
        super(SZE, WYS);
        addMouseListener(new MAdapter());
        addKeyListener(new TAdapter());
        sKlocek = new Sound("klocek.wav");
        sObrot = new Sound("obrot.wav");
        sPasek = new Sound("pasek.wav");
        for (byte x = 0; x < 12; x++) {
            tab[x][0] = 1;
            tab[x][21] = 1;
        }
        for (byte y = 0; y < 22; y++) {
            tab[0][y] = 1;
            tab[11][y] = 1;
        }
        klocekX = 4;
        klocekY = 0;
        speedMax = (short) (21 - Tetris.poziom);
        gamePlay = false;
        pause = false;
        grafika.setFont(new Font("System", Font.BOLD, 18));
        sKolor = Color.WHITE;
    }

    @Override
    public void drawImage() {
        if (gamePlay) {
            key();
            cmpPlansza();
            drukPlansza();
            drukKlocek(klocekX, klocekY);
            if (!pause) {
                if (speed < speedMax) speed++;
                else {
                    speed = 0;
                    if (isKlocekPlansza(klocekX, (byte) (klocekY + 1))) klocekY++;
                    else {
                        klocekKoniec();
                        nowyKlocek();
                    }
                }
            } else {
                grafika.setColor(Color.BLACK);
                grafika.drawString("PAUZA", 91, 496);
                grafika.setColor(sKolor);
                grafika.drawString("PAUZA", 90, 495);
            }
        } else {
            grafika.setColor(Klocki.KOLOR[0]);
            grafika.fillRect(0, 0, SZE, WYS);
            grafika.setColor(Color.BLACK);
            grafika.drawString("TETRIS", 91, 51);
            grafika.drawString("Autor: Bartłomiej Kepka", 11, 71);
            grafika.drawString("Kliknij aby rozpoczac", 56, 496);
            grafika.setColor(sKolor);
            grafika.drawString("TETRIS", 90, 50);
            grafika.drawString("Autor: Bartłomiej Kepka", 10, 70);
            grafika.drawString("Kliknij aby rozpoczac", 57, 495);

        }
    }

    public void nowyKlocek() {
        sKlocek.play();
        klocekX = 4;
        klocekY = 0;
        speedMax = (short) (20 - Tetris.poziom);
        if (speedMax < 0) speedMax = 0;
        klocek.setKlocek(Tetris.next.klocek);
//        Tetris.next.losujKlocek();
        Tetris.punkty += klocek.akKlocek;
        Tetris.lPunkty.setText(String.valueOf(Tetris.punkty));
    }

    public void klocekKoniec() {
        for (byte xx = 0; xx < 4; xx++)
            for (byte yy = 0; yy < 4; yy++)
                if (klocek.tab[xx][yy]) tab[xx + klocekX][yy + klocekY] = (byte) (klocek.akKlocek + 1);
    }

    private void drukPlansza() {
        for (byte x = 1; x < 11; x++)
            for (byte y = 1; y < 21; y++) {
                grafika.setColor(Klocki.KOLOR[tab[x][y]]);
                grafika.fillRect((x * Klocki.SIZE) - Klocki.SIZE, (y * Klocki.SIZE) - Klocki.SIZE, Klocki.SIZE, Klocki.SIZE);
                grafika.setColor(Color.BLACK);
                if (tab[x][y] > 0)
                    grafika.drawRect((x * Klocki.SIZE) - Klocki.SIZE, (y * Klocki.SIZE) - Klocki.SIZE, Klocki.SIZE - 1, Klocki.SIZE - 1);
            }
    }

    private void drukKostka(byte x, byte y, byte k) {
        grafika.setColor(Klocki.KOLOR[k]);
        grafika.fillRect((x * Klocki.SIZE) - Klocki.SIZE, (y * Klocki.SIZE) - Klocki.SIZE, Klocki.SIZE, Klocki.SIZE);
        grafika.setColor(Color.BLACK);
        grafika.drawRect((x * Klocki.SIZE) - Klocki.SIZE, (y * Klocki.SIZE) - Klocki.SIZE, Klocki.SIZE - 1, Klocki.SIZE - 1);
    }

    private boolean isLinia(byte y) {
        for (byte x = 1; x < 11; x++) {
            if (tab[x][y] == 0) return false;
        }
        return true;
    }

    private boolean isFull() {
        for (byte x = 1; x < 11; x++) {
            if (tab[x][1] != 0) return true;
        }
        return false;
    }

    private void setLinia(byte y) {
        sPasek.play();
        for (byte x = 1; x < 11; x++) tab[x][y] = 8;
        Tetris.linie++;
        Tetris.lLinie.setText(String.valueOf(Tetris.linie));
        Tetris.punkty += (Tetris.poziom * 10);
        Tetris.lPunkty.setText(String.valueOf(Tetris.punkty));
        if (Tetris.linie == (Tetris.poziom * Tetris.poziom)) {
            Tetris.poziom++;
            Tetris.lPoziom.setText(String.valueOf(Tetris.poziom));
            if (speedMax > 0) speedMax--;
        }
    }

    private void downPlansza(byte y) {
        for (byte ty = y; ty > 1; ty--)
            for (byte x = 1; x < 11; x++) tab[x][ty] = tab[x][ty - 1];
        for (byte x = 1; x < 11; x++) tab[x][1] = 0;
    }

    private void cmpPlansza() {
        for (byte y = 1; y < 21; y++) {
            if (tab[1][y] == 8) downPlansza(y);
            if (isLinia(y)) setLinia(y);
        }
        if (isFull()) {
            gamePlay = false;
            Tetris.poziom = 1;
            Tetris.linie = 0;
            Tetris.punkty = 0;
            klocekX = 4;
            klocekY = 0;
            speedMax = (short) (21 - Tetris.poziom);
            for (byte x = 1; x < 11; x++) for (byte y = 1; y < 21; y++) tab[x][y] = 0;
        }
    }

    public void drukKlocek(byte x, byte y) {
        for (byte tx = 0; tx < 4; tx++)
            for (byte ty = 0; ty < 4; ty++)
                if (klocek.tab[tx][ty]) drukKostka((byte) (tx + x), (byte) (ty + y), (byte) (klocek.akKlocek + 1));
    }


    private boolean isKlocekPlansza(byte x, byte y) {
        for (byte xx = 0; xx < 4; xx++)
            for (byte yy = 0; yy < 4; yy++)
                if (klocek.tab[xx][yy] && tab[xx + x][yy + y] > 0) return false;
        return true;
    }

    private boolean moveLeft() {
        if (!isKlocekPlansza((byte) (klocekX - 1), klocekY)) return false;
        return true;
    }

    private boolean moveRight() {
        if (!isKlocekPlansza((byte) (klocekX + 1), klocekY)) return false;
        return true;
    }

    private void key() {
        speedKey = !speedKey;
        if (kUp && speedKey) {
            klocek.obrot();
            sObrot.play();
            if (!isKlocekPlansza(klocekX, klocekY)) klocek.cofnijObrot();
        }
        if (kLeft && moveLeft()) klocekX--;
        if (kRight && moveRight()) klocekX++;
        if (kDown && speedMax > 0) {
            speedMax = 0;
            Tetris.punkty += 5;
            Tetris.lPunkty.setText(String.valueOf(Tetris.punkty));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    class MAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!gamePlay) {
                gamePlay = true;
                Tetris.lPunkty.setText(String.valueOf(Tetris.punkty));
                Tetris.lLinie.setText(String.valueOf(Tetris.linie));
                Tetris.lPoziom.setText(String.valueOf(Tetris.poziom));
            } else
                pause = !pause;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int k = e.getKeyCode();
            if (k == e.VK_UP) kUp = true;
            if (k == e.VK_DOWN) kDown = true;
            if (k == e.VK_LEFT) kLeft = true;
            if (k == e.VK_RIGHT) kRight = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int k = e.getKeyCode();
            if (k == e.VK_UP) kUp = false;
            if (k == e.VK_DOWN) kDown = false;
            if (k == e.VK_LEFT) kLeft = false;
            if (k == e.VK_RIGHT) kRight = false;

        }


    }
}