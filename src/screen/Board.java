package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;
import object.Pokemon;
import object.Trainer;

/**
 * @author Thomas
 */
public class Board extends JFrame {

    private static final long serialVersionUID = 1L;
//    private static String OS = System.getProperty("os.name").toLowerCase();
//    private static String ROOT = System.getProperty("user.dir");
    
    public Board() throws IOException {
        Trainer mainCharacter = new Trainer();
        
        Pokemon gyarados = new Pokemon(130, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
//        Pokemon gyarados = new Pokemon(162, 18, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon charizard = new Pokemon(6, 50, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon mewtwo = new Pokemon(150, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon umbreon = new Pokemon(197, 34, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon ampharos = new Pokemon(181, 1, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon shuckle = new Pokemon(213, 62, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        mainCharacter.getParty().addPkmnToParty(gyarados);
        mainCharacter.getParty().addPkmnToParty(mewtwo);
        mainCharacter.getParty().addPkmnToParty(charizard);
        mainCharacter.getParty().addPkmnToParty(umbreon);
        mainCharacter.getParty().addPkmnToParty(ampharos);
        mainCharacter.getParty().addPkmnToParty(shuckle);
        
//        Pokemon enemy = new Pokemon(43, 36, true, null, null);
        Trainer rival = new Trainer();
        Pokemon gengar = new Pokemon(94, 70, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon ariados = new Pokemon(168, 40, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon persian = new Pokemon(53, 20, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        rival.getParty().addPkmnToParty(persian);
        rival.getParty().addPkmnToParty(gengar);
        rival.getParty().addPkmnToParty(ariados);
        
        putUI();
        
//        final BattleBoard battleBoard = new BattleBoard(mainCharacter, enemy);
        final BattleBoard battleBoard = new BattleBoard(mainCharacter, rival);
        final StatsBoard statsBoard = new StatsBoard(mainCharacter);
        
        final JFrame frame = new GameFrame("Test");
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                char ch = event.getKeyChar();
                System.out.println(ch);
                if (ch == '1') {
                    battleBoard.setTrainer(statsBoard.returnTrainer());
                    frame.remove(statsBoard);
                    frame.add(battleBoard);
                } else if (ch == '2') {
                    statsBoard.setTrainer(battleBoard.returnTrainer());
                    statsBoard.refresh();
                    frame.remove(battleBoard);
                    frame.add(statsBoard);
                }
                frame.revalidate();
                frame.repaint();
                frame.setFocusable(true);
                frame.requestFocusInWindow();
            }

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        frame.setSize(statsBoard.getWidth(), statsBoard.getHeight());
        frame.add(battleBoard);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Board board = new Board();
                } catch (IOException e) {
                }
            }
        });
    }
    private void putUI() {
        try {
            Color bgc = new Color(0, 255, 0);
            Color fgc = new Color(255, 0, 0);
            Font f = createFont();
//            setUIFont(new javax.swing.plaf.FontUIResource(f));
            
//            UIManager.put("Button.font", f);
//            UIManager.put("ToggleButton.font", f);
//            UIManager.put("RadioButton.font", f);
//            UIManager.put("CheckBox.font", f);
//            UIManager.put("ColorChooser.font", f);
//            UIManager.put("ComboBox.font", f);
//            UIManager.put("Label.font", f);
//            UIManager.put("List.font", f);
//            UIManager.put("MenuBar.font", f);
//            UIManager.put("MenuItem.font", f);
//            UIManager.put("RadioButtonMenuItem.font", f);
//            UIManager.put("CheckBoxMenuItem.font", f);
//            UIManager.put("Menu.font", f);
//            UIManager.put("PopupMenu.font", f);
//            UIManager.put("OptionPane.font", f);
//            UIManager.put("Panel.font", f);
//            UIManager.put("ProgressBar.font", f);
//            UIManager.put("ScrollPane.font", f);
//            UIManager.put("Viewport.font", f);
//            UIManager.put("TabbedPane.font", f);
//            UIManager.put("Table.font", f);
//            UIManager.put("TableHeader.font", f);
//            UIManager.put("TextField.font", f);
//            UIManager.put("PasswordField.font", f);
//            UIManager.put("TextArea.font", f);
//            UIManager.put("TextPane.font", f);
//            UIManager.put("EditorPane.font", f);
//            UIManager.put("TitledBorder.font", f);
//            UIManager.put("ToolBar.font", f);
//            UIManager.put("ToolTip.font", f);
//            UIManager.put("Tree.font", f);
            
//            UIManager.put("control", new Color(255, 255, 255, 0));
            UIManager.put("info", new Color(255, 255, 255, 0)); //remove
//            UIManager.put("nimbusAlertYellow", bgc);
            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("nimbusDisabledText", new Color(100, 100, 100));
//            UIManager.put("nimbusFocus", bgc);
            UIManager.put("nimbusGreen", new Color(51, 255, 51));
            UIManager.put("nimbusInfoBlue", new Color(0, 51, 255));
//            UIManager.put("nimbusLightBackground", bgc);
            UIManager.put("nimbusOrange	", new Color(255, 153, 0));
            UIManager.put("nimbusRed", new Color(255, 0, 0));
//            UIManager.put("nimbusSelectedText", bgc);
            UIManager.put("nimbusSelectionBackground", new Color(40, 40, 40));
//            UIManager.put("text", bgc);
            
            UIManager.put("activeCaption", bgc);
            UIManager.put("background", bgc);
            UIManager.put("controlDkShadow", bgc);
            UIManager.put("controlHighlight", bgc);
            UIManager.put("controlLHighlight", fgc);
//            UIManager.put("controlText", fgc);
//            UIManager.put("desktop", fgc);
//            UIManager.put("inactiveCaption", fgc);
//            UIManager.put("infoText", fgc);
//            UIManager.put("menu", fgc);
//            UIManager.put("menuText", fgc);
//            UIManager.put("nimbusBlueGrey", fgc);
//            UIManager.put("nimbusSelection", fgc);
//            UIManager.put("nimbusSelection", fgc);
//            UIManager.put("scrollbar", fgc);
//            UIManager.put("textBackground", fgc);
//            UIManager.put("textForeground", fgc);
//            UIManager.put("textHighlight", fgc);
//            UIManager.put("textHighlightText", fgc);
//            UIManager.put("textInactiveText", fgc);

//            UIManager.put("TabbedPane.shadow", fgc);
//            UIManager.put("TabbedPane.darkShadow", fgc);
//            UIManager.put("TabbedPane.light", fgc);
//            UIManager.put("TabbedPane.highlight", fgc);
//            UIManager.put("TabbedPane.tabAreaBackground", fgc);
//            UIManager.put("TabbedPane.unselectedBackground", fgc);
//            UIManager.put("TabbedPane.background", bgc);
//            UIManager.put("TabbedPane.foreground", bgc);
//            UIManager.put("TabbedPane.focus", fgc);
//            UIManager.put("TabbedPane.contentAreaColor", fgc);
//            UIManager.put("TabbedPane.selected", fgc);
//            UIManager.put("TabbedPane.selectHighlight", fgc);
//            UIManager.put("TabbedPane.borderHightlightColor", fgc);
            
//            UIManager.put("TabbedPane:TabbedPaneTabArea[Disabled].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTabArea[Enabled+Pressed].backgroundPainter", bgc);
//            UIManager.put("TabbedPane:TabbedPaneTabArea[Enabled].backgroundPainter", bgc);
//            
//            UIManager.put("TabbedPane:TabbedPaneTab[Disabled+Selected].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Disabled].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", bgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter", bgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter", bgc);
//            
//            UIManager.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].backgroundPainter", bgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Focused+Selected].backgroundPainter", fgc);
//            
//            UIManager.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", fgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter", bgc);
//            UIManager.put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", fgc);
            
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) { }
    }
    
    public Font createFont() {
        Font f = null;
        try {
            File file = new File(getClass().getClassLoader().getResource("res/font/font.ttf").getFile());
            f = Font.createFont(Font.TRUETYPE_FONT, file);
            f = f.deriveFont(Font.PLAIN, 16);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
        } catch (IOException | FontFormatException ex) { }
        return f;
    }
    
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}

class GameFrame extends JFrame {
    public GameFrame(String name) {
        this.setTitle(name);
        centerFrame(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
//        this.setCursor(this.getToolkit().createCustomCursor(
//            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),"null"));
    }
    
    private void centerFrame(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - 500;
        int dy = centerPoint.y - 300;
        frame.setLocation(dx, dy);
    }
}