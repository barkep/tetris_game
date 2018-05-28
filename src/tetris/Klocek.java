package tetris;

public class Klocek {

    public boolean[][] tab = new boolean[4][4];
    private boolean[][] tabE = new boolean[4][4];
    byte akKlocek;

    Klocek() {
        setKlocek((byte) 0);
    }

    public void setKlocek(byte k) {
        akKlocek = k;
        for (byte x = 0; x < 4; x++)
            for (byte y = 0; y < 4; y++)
                tab[y][x] = Klocki.KLOCKI[akKlocek][x][y];
    }

    public void obrot() {
        for (byte x = 0; x < 4; x++) for (byte y = 0; y < 4; y++) tabE[x][y] = tab[x][y];
        for (byte x = 0; x < 4; x++) for (byte y = 0; y < 4; y++) tab[3 - y][x] = tabE[x][y];
    }

    public void cofnijObrot() {
        for (byte x = 0; x < 4; x++) for (byte y = 0; y < 4; y++) tab[x][y] = tabE[x][y];
    }
}