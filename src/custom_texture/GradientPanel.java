package custom_texture;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * @author Thomas
 */
public class GradientPanel extends JPanel {
    private static final long serialVersionUID = -2870150416852080785L;
    private final Color color1, color2;
    
    public GradientPanel(Color c1, Color c2) {
        color1 = c1;
        color2 = c2;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(
            0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
