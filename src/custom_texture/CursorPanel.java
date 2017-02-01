package custom_texture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;
import screen.Board;

/**
 * @author Thomas
 */
public class CursorPanel extends ExpandPanel {
    private static final long serialVersionUID = 9612921993001222L;
    private static final String PATH = Board.ROOT+"/resources/image/Cursor.png";
    private static final int CLOCK = 300;
    public static final int MIN_SIZE = 32;
    
    private JLabel cursor;
    private boolean highlight;
    
    /**
     * Creates new form BattlePanel
     * 
     * @param light
     */
    public CursorPanel(boolean light) {
        setBackground(Color.yellow);
        setOpaque(true);
        this.highlight = light;
        initComponents();
        final int sizex = (int) (MIN_SIZE*(Board.xDIM));
        final int sizey = (int) (MIN_SIZE*(Board.yDIM));
        if (light) {
            cursor.setIcon(ImageConverter.getColorizedScaledImage(
                    ImageConverter.getImage(PATH), sizex, sizey, 255, 255, 255));
        } else {
            cursor.setIcon(ImageConverter.getScaledImage(
                    ImageConverter.getImage(PATH), sizex, sizey));
        }
        Timer timer = new Timer(CLOCK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (highlight) {
                    cursor.setIcon(ImageConverter.getColorizedScaledImage(
                            ImageConverter.getImage(PATH), sizex, sizey, 255, 255, 255));
                } else {
                    cursor.setIcon(ImageConverter.getScaledImage(
                            ImageConverter.getImage(PATH), sizex, sizey));
                }
                highlight = !highlight;
                repaint(); revalidate();
            }
        });
        timer.start();
    }
    
    /**
     * 
     * @return
     */
    public boolean getHighlight() {
        return highlight;
    }
    
    private void initComponents() {
        setMinimumSize(new Dimension(MIN_SIZE, MIN_SIZE));
        setOpaque(false);
        setLayout(new BorderLayout());
        
        cursor = new JLabel();
        cursor.setSize(getMinimumSize());
        
        add(cursor, BorderLayout.CENTER);
        
        setVisible(true);
    }
}