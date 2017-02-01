package custom_texture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import objects.Pokemon;
import objects.Pokemon.Status;
import screen.Board;

import static objects.Pokemon.Status.*;

/**
 * @author Thomas
 */
public class SpritePanel extends ExpandPanel {
    private static final long serialVersionUID = 9612921993001222L;
    private static final int CLOCK = 150;
    private static final int MIN_SIZE = 144;
    
    private final boolean mirror;
    private boolean hit;
    private int thisX, thisY;
    private String path;
    private Pokemon pokemon;
    private Timer timer;
    private JLabel sprite;
    
    /**
     * Creates new form BattlePanel
     * @param pkmn
     * @param mirror
     */
    public SpritePanel(Pokemon pkmn, boolean mirror) {
//        super();
        setOpaque(true);
        hit = false;
        this.mirror = mirror;
        pokemon = pkmn;
        initComponents();
        path = getPkmnPath(pokemon);
        printImage();
        thisX = sprite.getX();
        thisY = sprite.getY();
        timer = new Timer(CLOCK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (pokemon.getStatus() != KO) {
                    if (!hit) {
                        double diff = pokemon.getStat("MaxHP") / pokemon.getStat("HP");
                        double delay = 1;
                        if (diff >= Board.INTEGER && diff < Board.HALF) {
                            delay = Board.INTEGER;
                        } else if (diff >= Board.HALF && diff < Board.QUARTER) {
                            delay = Board.HALF_FLOAT;
                        } else if (diff >= Board.QUARTER && diff < Board.EIGHTH) {
                            delay = Board.QUARTER_FLOAT;
                        } else if (diff >= Board.EIGHTH) {
                            delay = Board.EIGHTH_FLOAT;
                        }
                        if (pokemon.getStatus() == OK) {
                            if (sprite.getY() == thisY) {
                                sprite.setLocation(thisX, (int) (thisY + (3*(Board.yDIM))));
                            } else {
                                sprite.setLocation(thisX, thisY);
                            }
                        } else if (pokemon.getStatus() != Asleep && pokemon.getStatus() != Freeze) {
                            if (sprite.getX() == thisX) {
                                sprite.setLocation((int) (thisX + (3*(Board.yDIM))), thisY);
                            } else {
                                sprite.setLocation(thisX, thisY);
                                delay /= 3;
                            }
                        }
                        timer.setDelay((int) (CLOCK / delay));
                    }
                }
            }
        });
        timer.start();
    }
    
    public final void printImage() {
        ImageIcon image = ImageConverter.getImage(path);
        if (mirror) {
            sprite.setIcon(ImageConverter.getScaledMirrorImage(image,
                    (int) (MIN_SIZE*(Board.xDIM)), (int) (MIN_SIZE*(Board.yDIM))));
        } else {
            sprite.setIcon(ImageConverter.getScaledImage(image,
                    (int)(MIN_SIZE*(Board.xDIM)), (int) (MIN_SIZE*(Board.yDIM))));
        }
        colorImage(pokemon.getStatus());
    }
    
    public void colorImage(Status status) {
        int r = 0, g = 0, b = 0;
        switch (status) {
            case Paralysis:
                r = 255; g = 204; b = 0; break;
            case Poison:
                r = 204; g = 0; b = 153; break;
            case BadPoison:
                r = 204; g = 0; b = 255; break;
            case Burn:
                r = 255; g = 51; b = 0; break;
            case Freeze:
                r = 51; g = 204; b = 255; break;
            default: break;
        }
        if (status != KO) {
            if (status != pokemon.getStatus()) {
                Image image = ImageConverter.getScaledImage(ImageConverter.getImage(path),
                        (int) (MIN_SIZE*(Board.xDIM)), (int) (MIN_SIZE*(Board.yDIM))).getImage();
                if (!mirror) {
                    sprite.setIcon(new ImageIcon(ImageConverter.colorImage(
                            ImageConverter.imageToBufferedImage(image), r, g, b)));
                } else {
                    sprite.setIcon(ImageConverter.getMirrorImage(new ImageIcon(ImageConverter.colorImage(
                            ImageConverter.imageToBufferedImage(image), r, g, b))));
                }
            } else {
                if (status != OK) {
                    Image image = ImageConverter.getScaledImage(ImageConverter.getImage(path),
                            (int) (MIN_SIZE*(Board.xDIM)), (int) (MIN_SIZE*(Board.yDIM))).getImage();
                    if (!mirror) {
                        sprite.setIcon(new ImageIcon(ImageConverter.colorImage(
                                ImageConverter.imageToBufferedImage(image), r, g, b)));
                    } else {
                        sprite.setIcon(ImageConverter.getMirrorImage(new ImageIcon(ImageConverter.colorImage(
                                ImageConverter.imageToBufferedImage(image), r, g, b))));
                    }
                }
            }
        }
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        path = getPkmnPath(pokemon);
        printImage();
    }
    public boolean getHit() {
        return hit;
    }
    public void setHit(boolean hit) {
        this.hit = hit;
    }
    
    private String getPkmnPath(Pokemon pkmn) {
        boolean shinyBln = pkmn.getIfShiny();
        boolean asexBln = pkmn.getIfAsessual();
        boolean femaleBln = !pkmn.getIfMale();
        ArrayList<Boolean> bool = new ArrayList<>();
        bool.add(shinyBln);
        bool.add(asexBln);
        bool.add(femaleBln);
        return Pokemon.getImagePath(bool, pkmn.getID(), "sprite");
    }
    
    private void initComponents() {
        setMinimumSize(new Dimension(MIN_SIZE, MIN_SIZE));
        setOpaque(false);
        setLayout(new BorderLayout());
        
        sprite = new JLabel();
        sprite.setSize(getMinimumSize());
        
        add(sprite, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g); 
        int width = (int) (getMinimumSize().width*(Board.xDIM));
        int height = (int) (getMinimumSize().height*(Board.yDIM));
        int size = (int) (100*(Board.yDIM));
        g.setColor(Color.blue);
        g.drawOval(0, size, width, height-(size)); 
        g.setColor(Color.green); 
        g.fillOval(0, size, width, height-(size));
    }
}
