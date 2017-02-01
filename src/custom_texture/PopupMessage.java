package custom_texture;

import java.awt.Color;
import javax.swing.JPanel;

/**
 * @author Thomas
 */
public class PopupMessage extends FadingText {
    public PopupMessage(String string, String name, Integer type, final JPanel frame) {
        super(true, frame);
        setText(string);
        switch (type) {
            case 0: //board popup
                r = Color.darkGray.getRed();
                g = Color.darkGray.getGreen();
                b = Color.darkGray.getBlue();
                setName("Board:"+name);
                break;
            case 1: //error popup
                r = Color.red.getRed();
                g = Color.red.getGreen();
                b = Color.red.getBlue();
                setName("Error:"+name);
                break;
            case 2: //warning popup
                r = Color.yellow.getRed();
                g = Color.yellow.getGreen();
                b = Color.yellow.getBlue();
                setName("Warning:"+name);
                break;
            case 3: //cheat popup
                r = Color.cyan.getRed();
                g = Color.cyan.getGreen();
                b = Color.cyan.getBlue();
                setName("Cheat:"+name);
                break;
            case 4: //text popup
                r = Color.orange.getRed();
                g = Color.orange.getGreen();
                b = Color.orange.getBlue();
                setName("Text: "+name);
                break;
            default: //board popup
                r = g = b = 255;
                setName("System:"+name);
                break;
        }
        setForeground(new Color(r, g, b, 255));
        setVisible(true);
    }
}