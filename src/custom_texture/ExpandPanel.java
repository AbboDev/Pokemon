package custom_texture;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Thomas
 */
public class ExpandPanel extends JPanel {
    public boolean lock = false;

    /**
     *
     * @param mult
     */
    public final void expandComponent(int mult) {
        setPreferredSize(new Dimension(getMinimumSize().width*mult, getMinimumSize().height*mult));
        for (Object obj : getComponents()) {
            if (obj instanceof Container) {
                Container panel = (Container) obj;
                containerIterator(mult, panel, true);
            } else {
                Component comp = (Component) obj;
                comp.setPreferredSize(new Dimension(comp.getMinimumSize().width*mult, comp.getMinimumSize().height*mult));
            }
        }
    }
    
    public static void containerIterator(int mult, Container cont, boolean font) {
        for (Component comp : cont.getComponents()) {
            comp.setPreferredSize(new Dimension(comp.getMinimumSize().width*mult, comp.getMinimumSize().height*mult));
            if (font) {
                comp.setFont(new Font("Trebuchet MS", 0, (11*mult)+(1*mult)));
            }
            if (comp instanceof Container) {
                Container panel = (Container) comp;
                containerIterator(mult, panel, font);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getIcon() != null) {
                    if (label.getWidth() != label.getHeight()) {
                        label.setIcon(SpriteImage.getScaledImage(
                                SpriteImage.iconToImageIcon(label.getIcon()),
                                label.getWidth()*mult, label.getHeight()*mult));
                    } else {
                        label.setIcon(SpriteImage.getScaledImage(
                                SpriteImage.iconToImageIcon(
                                label.getIcon()), label.getWidth()*mult));
                    }
                }
            }
        }
    }
}
