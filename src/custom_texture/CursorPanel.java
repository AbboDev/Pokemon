package custom_texture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import screen.Board;

/**
 * @author Thomas
 */
public class CursorPanel extends JPanel {
    private static final long serialVersionUID = 9612921993001222L;
    private static final String PATH = Board.ROOT+"/resources/image/Cursor.png";
    private static final int CLOCK = 300;
    public static final int SIZE = 32;
    
    private JLabel cursor;
    private int DIM = 1;
    private boolean highlight;
    
    /**
     * Creates new form BattlePanel
     * 
     * @param light
     * @param dim
     */
    public CursorPanel(boolean light, int dim) {
        this.highlight = light;
        DIM = dim;
        initComponents();
        if (light) {
            cursor.setIcon(SpriteImage.getColorizedScaledImage(
                    SpriteImage.getImage(PATH), SIZE*DIM, 255, 255, 255));
        } else {
            cursor.setIcon(SpriteImage.getScaledImage(
                    SpriteImage.getImage(PATH), SIZE*DIM));
        }
        Timer timer = new Timer(CLOCK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (highlight) {
                    cursor.setIcon(SpriteImage.getColorizedScaledImage(
                            SpriteImage.getImage(PATH), SIZE*DIM, 255, 255, 255));
                } else {
                    cursor.setIcon(SpriteImage.getScaledImage(
                            SpriteImage.getImage(PATH), SIZE*DIM));
                }
                highlight = !highlight;
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
        setMinimumSize(new Dimension(SIZE, SIZE));
        setSize(SIZE*DIM, SIZE*DIM);
        setOpaque(false);
        setLayout(new BorderLayout());
        
        cursor = new JLabel();
        cursor.setSize(getWidth()*DIM, getHeight()*DIM);
        
        add(cursor, BorderLayout.CENTER);
        
        setVisible(true);
    }
}
