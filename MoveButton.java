package custom_texture;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import object.Pokemon.Type;

/**
 * @author Thomas
 */
public class MoveButton extends JButton {
    
    public MoveButton(String text) {
        this.setText(text);
    }
        
    public MoveButton(String text, Type type) {
        this.setHorizontalTextPosition(CENTER);
        this.setForeground(Color.WHITE);
        this.setText(text);
        Color internalColor;
        Border border = null;
        switch (type) {
            case Normal:
                internalColor = (new Color(168, 168, 120, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Fighting:
                internalColor = (new Color(192, 48, 40, 255));
                border = (BorderFactory.createLineBorder(Color.BLACK, 3, true));
                break;
            case Flying:
                internalColor = (new Color(168, 144, 240, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Rock:
                internalColor = (new Color(184, 160, 56, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Ground:
                internalColor = (new Color(224, 192, 104, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Steel:
                internalColor = (new Color(184, 184, 208, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Ghost:
                internalColor = (new Color(112, 88, 152, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Dark:
                internalColor = (new Color(112, 88, 72, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Psychic:
                internalColor = (new Color(248, 88, 136, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Bug:
                internalColor = (new Color(168, 184, 32, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Poison:
                internalColor = (new Color(160, 64, 160, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Grass:
                internalColor = (new Color(120, 200, 80, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Water:
                internalColor = (new Color(104, 144, 240, 255));
                border = (BorderFactory.createLineBorder(Color.BLUE, 3, true));
                break;
            case Ice:
                internalColor = (new Color(152, 216, 216, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Fire:
                internalColor = (new Color(240, 128, 48, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Electric:
                internalColor = (new Color(248, 208, 48, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Fairy:
                internalColor = (new Color(255, 101, 213, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Dragon:
                internalColor = (new Color(112, 56, 248, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Dinosaur:
                internalColor = (new Color(127, 51, 0, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            case Light:
                internalColor = (new Color(255, 236, 142, 255));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
            default:
                internalColor = (new Color(168, 168, 120));
                border = (BorderFactory.createLineBorder(Color.GRAY, 3, true));
                break;
        }
        this.setBackground(internalColor);
        this.setBorder(border);
    }
    
    
}
