package custom_texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.border.Border;

/**
 * @author Thomas
 */
public class GradientPanel extends ExpandPanel {
    private static final long serialVersionUID = -2870150416852080785L;
    public static final int BORDER = 2;
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, back1, 0, h, back2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

class GradientBorder implements Border {
    private final Insets margin;
    private final Color col1, col2;

    public GradientBorder (int top, int left, int bottom, int right, Color c1, Color c2) {
        super(); col1 = c1; col2 = c2;
        margin = new Insets(top, left, bottom, right);
    }
    
    public GradientBorder (int borderx, int bordery, Color c1, Color c2) {
        super(); col1 = c1; col2 = c2;
        margin = new Insets(bordery, borderx, bordery, borderx);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint (new GradientPaint(x, y, col1, x, y + height, col2));

        Area border = new Area (new Rectangle(x, y, width, height));
        border.subtract (new Area(new Rectangle(x + margin.left, y + margin.top,
                width - margin.left - margin.right, height - margin.top - margin.bottom)));
        g2d.fill(border);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return margin;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}

