package tetris;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tetris extends JPanel implements Runnable {


    static Tetris tetris = new Tetris();
    static JFrame okno = new JFrame("Tetris");
    static Thread watek = new Thread(tetris);

    static Plansza plansza = new Plansza();
    static Next next = new Next();

    boolean start = false;
    short op = 30;

    static int linie = 0, punkty = 0, poziom = 1;
    static JLabel lPunkty, lLinie, lPoziom, lPunkty2, lLinie2, lPoziom2;
    static Color kPunkty = new Color(255, 128, 255);
    static Color kLinie = new Color(255, 255, 0);
    static Color kPoziom = new Color(0, 255, 255);

    static Font cz1, cz2;

    Tetris() {
        super();
        setBackground(new Color(90, 90, 180));
        setLayout(null);
        start = true;
        cz1 = new Font("System", Font.BOLD, 20);
        cz2 = new Font("System", Font.BOLD, 10);
    }

    public static void main(String[] args) {
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.add(tetris);
        okno.setSize(388, 555);
        okno.setLocationRelativeTo(null);
        okno.setResizable(false);

        lPunkty = new JLabel("0", JLabel.RIGHT);
        lPunkty.setForeground(kPunkty);
        lPunkty.setFont(cz1);
        lPunkty.setBounds(270, 300, 100, 20);
        tetris.add(lPunkty);
        lPunkty2 = new JLabel("PUNKTY", JLabel.RIGHT);
        lPunkty2.setBounds(270, 290, 100, 10);
        lPunkty2.setForeground(kPunkty);
        lPunkty2.setFont(cz2);
        tetris.add(lPunkty2);

        lLinie = new JLabel("0", JLabel.RIGHT);
        lLinie.setForeground(kLinie);
        lLinie.setFont(cz1);
        lLinie.setBounds(270, 230, 100, 20);
        tetris.add(lLinie);
        lLinie2 = new JLabel("LINIE", JLabel.RIGHT);
        lLinie2.setBounds(270, 220, 100, 10);
        lLinie2.setForeground(kLinie);
        lLinie2.setFont(cz2);
        tetris.add(lLinie2);

        lPoziom = new JLabel("1", JLabel.RIGHT);
        lPoziom.setForeground(kPoziom);
        lPoziom.setFont(cz1);
        lPoziom.setBounds(270, 160, 100, 20);
        tetris.add(lPoziom);
        lPoziom2 = new JLabel("POZIOM", JLabel.RIGHT);
        lPoziom2.setBounds(270, 150, 100, 10);
        lPoziom2.setForeground(kPoziom);
        lPoziom2.setFont(cz2);
        tetris.add(lPoziom2);

        plansza.setLocation(10, 10);
        tetris.add(plansza);
        next.setLocation(270, 10);
        tetris.add(next);
        okno.setVisible(true);
        watek.start();
    }

    @Override
    public void run() {
        long wait, startCzas, cyklCzas;
        while (start) {
            startCzas = System.nanoTime();
            plansza.run();
            next.run();
            cyklCzas = System.nanoTime() - startCzas;
            wait = op - cyklCzas / 1000000;
            if (wait <= 0) wait = 5;
            try {
                watek.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
