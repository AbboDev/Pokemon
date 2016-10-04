/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_texture;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author Thomas
 */
public class StatsPanel extends JPanel {
    public StatsPanel() {
        this.setBackground(new Color(102, 102, 102, 100));
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3, true));
    }

    public static ComponentUI createUI(JComponent c) {
        return ComponentUI.createUI(c);
    }
    
}
