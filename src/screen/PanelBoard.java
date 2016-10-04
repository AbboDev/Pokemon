package screen;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.UIManager;
import object.Trainer;
import static screen.PanelBoard.root;

/**
 * @author Thomas
 */
public class PanelBoard extends JPanel {
    public static String root = System.getProperty("user.dir")+"\\src\\pokemon\\";
    public final static File kanto = new File(root + "kanto.csv");
    public final static File kantoMove = new File(root + "kantoMove.csv");
    
    public Trainer self = new Trainer();
    private boolean isOpen;
    /**
     * Creates new form BattleBoard
     * @throws java.io.IOException
     */    
    public PanelBoard() {
        makeUI();
        isOpen = true;
    }
    
    /**
     * It return the trainer which its party's stats modified
     * @return
     */
    public Trainer returnTrainer() {
        return self;
    }
    
    private static void makeUI() {
        Color bgc = new Color(51, 51, 51, 100);
        Color fgc = new Color(102, 102, 102, 100);
                
        UIManager.put("TabbedPane.shadow", fgc);
        UIManager.put("TabbedPane.darkShadow", fgc);
        UIManager.put("TabbedPane.light", fgc);
        UIManager.put("TabbedPane.highlight", fgc);
        UIManager.put("TabbedPane.tabAreaBackground", fgc);
        UIManager.put("TabbedPane.unselectedBackground", fgc);
        UIManager.put("TabbedPane.background", bgc);
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.focus", fgc);
        UIManager.put("TabbedPane.contentAreaColor", fgc);
        UIManager.put("TabbedPane.selected", fgc);
        UIManager.put("TabbedPane.selectHighlight", fgc);
        UIManager.put("TabbedPane.borderHightlightColor", fgc);
    }
}
