package custom_texture;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import screen.Board;
import screen.Board.Direction;

/**
 * @author Thomas
 */
public class Particle extends JLabel {
    private final static String PATH = Board.ROOT+"/resources/particle/";
    private static final int MAX_STEPS = 99999;
    private static final int BASE_SPEED = 1;
    
    private ImageIcon particle;
    private Color color;
    private Color colorGrad;
    private int steps = MAX_STEPS;
    private double speedx = BASE_SPEED;
    private double speedy = BASE_SPEED;
    private Thread thread;
    private Direction direction = Direction.EAST;
    
    public Particle() {
        
    }
    public Particle(String file) {
        particle = ImageConverter.getImage(PATH+file);
    }
    public Particle(Color clr) {
        color = clr;
        colorGrad = clr;
    }
    public Particle(Color clr1, Color clr2) {
        color = clr1;
        colorGrad = clr2;
    }
    
    public void size(int size) {
        setSize(size, size);
    }
    public void size(int width, int height) {
        setSize(width, height);
    }
    public void size(Dimension dimension) {
        setSize(dimension);
    }
    
    public void color(Color c) {
        color = c;
    }
    public void gradientColor(Color c, Color g) {
        color = c; colorGrad = g;
    }
    public void gradient(Color g) {
        colorGrad = g;
    }
    public void alpha(boolean color) {
        if (color) {
            getBackground();
        }
        //https://tips4java.wordpress.com/2010/08/22/alpha-icons/
    }
    
    public void move(Direction d, int steps, double speedx, double speedy) {
        direction = d;
        this.steps = steps;
        switch (d) {
            case NORTH: break;
            case SOUTH: break;
            case EAST: break;
            case WEST: break;
            case NORTH_EAST: break;
            case NORTH_WEST: break;
            case SOUTH_EAST: break;
            case SOUTH_WEST: break;
            default: break;
        }
        setLocation((int) (getX()+speedx), (int) (getY()+speedy));
    }
    public void move(double speedx, double speedy) {
        move(direction, MAX_STEPS, speedx, speedy);
    }
    public void move(double speed, boolean x) {
        if (x) move(direction, MAX_STEPS, speed, speedy);
        else move(direction, MAX_STEPS, speedx, speed);
    }
    public void move(Direction d, int steps, double speed) {
        move(d, steps, speed, speed);
    }
    public void move(Direction d, double speed) {
        move(d, MAX_STEPS, speed, speed);
    }
    public void move(Direction d, int steps) {
        move(d, MAX_STEPS, speedx, speedy);
    }
    public void move(int steps) {
        move(direction, steps, speedx, speedy);
    }
    public void move(Direction d) {
        move(d, MAX_STEPS, speedx, speedy);
    }
    public void move() {
        move(direction, MAX_STEPS, speedx, speedy);
    }
    
    public void split(int number) {
        for (int i = 0; i < number; ++i) {
            Particle p = new Particle();
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Particles");
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Particle p = new Particle();
        
    }
}
