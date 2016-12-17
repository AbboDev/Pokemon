package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager.LookAndFeelInfo;
import object.Pokemon;
import object.Trainer;

import static screen.Board.mainCharacter;

/**
 * @author Thomas
 */
public class Board extends JFrame {
    private static final long serialVersionUID = 800417746482024105L;
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String ROOT = System.getProperty("user.dir");
    public static Trainer mainCharacter;
    
    protected static final int FRAME_WIDTH = 512;
    protected static final int FRAME_HEIGHT = 384;
    protected static final int SPRITE_DIM = 144;
    public static final double INTEGER = 1;
    public static final double HALF_FLOAT = 0.5;
    public static final double QUARTER_FLOAT = 0.25;
    public static final double EIGHTH_FLOAT = 0.125;
    public static final double HALF = 2;
    public static final double QUARTER = 4;
    public static final double EIGHTH = 8;
    public static final double NULL = 0;
    protected static int FRAME_DIM = 1;
    
    protected static final int A_BTN = KeyEvent.VK_Z;
    protected static final int B_BTN = KeyEvent.VK_X;
    protected static final int L_BTN = KeyEvent.VK_A;
    protected static final int R_BTN = KeyEvent.VK_S;
    protected static final int START_BTN = KeyEvent.VK_SHIFT;
    protected static final int SELECT_BTN = KeyEvent.VK_CONTROL;
    protected static final int LEFT_BTN = KeyEvent.VK_LEFT;
    protected static final int RIGHT_BTN = KeyEvent.VK_RIGHT;
    protected static final int UP_BTN = KeyEvent.VK_UP;
    protected static final int DOWN_BTN = KeyEvent.VK_DOWN;
    
    private static SplashScreen splashScreen;
    private final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final GraphicsDevice device = env.getDefaultScreenDevice();
    
    private final boolean isFull = false;
    private boolean screenFunct = true;
    public Board board;

    public Board(String name, Trainer main) throws IOException {
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestFocus();
            }
        });
//        timer.start();
        putUI();
        centerFrame((JFrame) this);
        
        Pokemon enemy = new Pokemon(43, 36, true, null, null);
        
        Trainer rival = new Trainer("Dumb");
        Pokemon gengar = new Pokemon(94, 70, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon ariados = new Pokemon(168, 40, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon persian = new Pokemon(53, 20, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        rival.getParty().addPkmnToParty(persian);
        rival.getParty().addPkmnToParty(gengar);
        rival.getParty().addPkmnToParty(ariados);
        
//        final BattleBoard battleBoard = new BattleBoard(main, enemy, 1);
        final BattleBoard battleBoard = new BattleBoard(main, rival, 1);

//        final StatsBoard statsBoard = new StatsBoard(main);
        
        this.setSize(FRAME_WIDTH*FRAME_DIM, FRAME_HEIGHT*FRAME_DIM);
        System.out.println((FRAME_WIDTH*FRAME_DIM)+" "+(FRAME_HEIGHT*FRAME_DIM));
        this.setTitle(name);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);           
        this.add(battleBoard);
        this.pack();
        
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_F:
//                        if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
//                        if (!isFull) {
//                            device.setFullScreenWindow(board);
//                            setExtendedState(JFrame.MAXIMIZED_BOTH);
//                            setUndecorated(true);
//                            setVisible(true);
//                            isFull = true;
//                        } else {
//                            device.setFullScreenWindow(null);
//                            setExtendedState(0);
//                            setUndecorated(false);
//                            setVisible(true);
//                            isFull = false;
//                        }
                        break;
                    case KeyEvent.VK_F12:
                        setVisible(false);
                        if (FRAME_DIM != 1) {
                            FRAME_DIM = 1;
                        } else {
                            FRAME_DIM = 2;
                        }
                        battleBoard.changeGraphic(FRAME_DIM);
                        centerFrame((JFrame) event.getSource());
                        pack();
                        setVisible(true);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        //http://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                        dispatchEvent(new WindowEvent((Window) event.getSource(), WindowEvent.WINDOW_CLOSING));
                        setVisible(false); //you can't see me!
                        dispose(); //Destroy the JFrame object
                    default:
                        screenFunct = false;
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent event) {
            }

            @Override
            public void keyReleased(KeyEvent event) {
                if (!screenFunct) {
                    battleBoard.checkKey(event);
                    screenFunct = true;
                }
            }
        });
        this.setVisible(true);
        
        this.setCursor(this.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),"null"));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    mainCharacter = new Trainer("Fat_Ass");

                    Pokemon gyarados = new Pokemon(130, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon charizard = new Pokemon(6, 50, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon mewtwo = new Pokemon(150, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon umbreon = new Pokemon(196, 34, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon ampharos = new Pokemon(181, 10, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon shuckle = new Pokemon(213, 62, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    mainCharacter.getParty().addPkmnToParty(gyarados);
                    mainCharacter.getParty().addPkmnToParty(charizard);
                    mainCharacter.getParty().addPkmnToParty(mewtwo);
                    mainCharacter.getParty().addPkmnToParty(umbreon);
                    mainCharacter.getParty().addPkmnToParty(ampharos);
                    mainCharacter.getParty().addPkmnToParty(shuckle);
                    
                    splashScreen = new SplashScreen("test", mainCharacter);
                } catch (Exception e) {
                }
            }
        });
    }

    private void putUI() {
        try {
            Color bgc = new Color(0, 255, 0);
//            Color fgc = new Color(255, 0, 0);
//            Font f = createFont();
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

//            UIManager.put("activeCaption", bgc);
//            UIManager.put("background", bgc);
//            UIManager.put("controlDkShadow", bgc);
//            UIManager.put("controlHighlight", bgc);
//            UIManager.put("controlLHighlight", fgc);
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
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }

    public Font createFont() {
        Font f = null;
        try {
            File file = new File(Board.ROOT + "/resources/font/font.ttf");
            f = Font.createFont(Font.TRUETYPE_FONT, file);
            f = f.deriveFont(Font.PLAIN, 16);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
        } catch (IOException | FontFormatException ex) {
        }
        return f;
    }
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
    private void centerFrame(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - (FRAME_WIDTH*FRAME_DIM)/2;
        int dy = centerPoint.y - (FRAME_HEIGHT*FRAME_DIM)/2;
        frame.setLocation(dx, dy);
    }
}