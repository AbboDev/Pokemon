package screen;

import custom_texture.ExpandPanel;
import custom_texture.PopupMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager.LookAndFeelInfo;
import objects.Pokemon;
import objects.Trainer;

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
    
    private boolean screenFunct = true;
    public static boolean isFull = false;
    public static double screenMaxDimx;
    public static double screenMaxDimy;
    public static double xDIM, yDIM;
    public static double c_xDIM, c_yDIM;
    public static double screenMaxDim;
    public static double screenWidth;
    public static double screenHeight;
    private boolean isHorizzontal;
    public Board board;
    private final JPanel panel = new JPanel();
    private final JPanel glass = new JPanel();
    private JPanel currentPanel = new JPanel();
    
    public enum Direction {
        NORTH, SOUTH, EAST, WEST,
        NORTH_EAST, NORTH_WEST,
        SOUTH_EAST, SOUTH_WEST
    }

    public Board(String name, Trainer main) throws IOException {
        xDIM = yDIM = 1;
        c_xDIM = c_yDIM = 1;
        putUI();
        
        Pokemon enemy = new Pokemon(43, 36, true, null, null);
        
        Trainer rival = new Trainer("Dumb");
        Pokemon gengar = new Pokemon(94, 70, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon ariados = new Pokemon(168, 40, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon persian = new Pokemon(53, 20, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        rival.getParty().addPkmnToParty(persian);
        rival.getParty().addPkmnToParty(gengar);
        rival.getParty().addPkmnToParty(ariados);
        
        final BattleBoard battleBoard = new BattleBoard(main, rival/*enemy*/);
        currentPanel = battleBoard;
        
        this.setTitle(name);
        this.setResizable(false);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        LayoutManager overlay = new OverlayLayout(panel);
        panel.setLayout(overlay);
        glass.setOpaque(false);
        glass.setSize((int) screenWidth, (int) screenHeight);
        FlowLayout layout = new FlowLayout();
        glass.setLayout(new FlowLayout());
        
        panel.add(glass);
        panel.add(currentPanel);
        
        this.add(panel);
        
        getScreenSettings();
        
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_F6: //Reset the board
                        setVisible(false);
                        dispose(); //Destroy the current board
                        splashScreen = new SplashScreen("reset_test", mainCharacter);
                        xDIM = yDIM = 1;
                        centerFrame((JFrame) event.getSource());
                        break;
                    case KeyEvent.VK_F10:
                        System.out.println(getCurrentBoardLock());
                        break;
                    case KeyEvent.VK_F11:
                        if (!getCurrentBoardLock()) {
                            setVisible(false);
                            dispose();
                            setUndecorated(!isFull);
                            if (!isFull) {
                                isFull = true;
                                c_xDIM = xDIM;
                                c_yDIM = yDIM;
                                xDIM = screenMaxDimx;
                                yDIM = screenMaxDimy;
                                getCurrentBoard().changeGraphic();
                                centerFullFrame((JFrame) event.getSource(), isHorizzontal);
                                setAlwaysOnTop(true);
                                setCursor(getToolkit().createCustomCursor(
                                    new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),"null"));
                            } else {
                                xDIM = c_xDIM;
                                yDIM = c_yDIM;
                                isFull = false;
                                getCurrentBoard().changeGraphic();
                                centerFrame((JFrame) event.getSource());
                                setAlwaysOnTop(false); pack();
                                setCursor(Cursor.getDefaultCursor());
                            }
                            setVisible(true);
                        }
                        break;
                    case KeyEvent.VK_F12:
                        if (!getCurrentBoardLock()) {
                            setVisible(false);
                            if (isFull) {
                                xDIM = c_xDIM;
                                yDIM = c_yDIM;
                                isFull = false; dispose();
                                setAlwaysOnTop(false);
                                setUndecorated(false);
                            }
                            if ((xDIM+0.25) <= screenMaxDim-0.25) {
                                xDIM += 0.25;
                                yDIM += 0.25;
                            } else {
                                xDIM = 1;
                                yDIM = 1;
                            }
                            getCurrentBoard().changeGraphic();
                            centerFrame((JFrame) event.getSource());
                            pack();
                            PopupMessage l = new PopupMessage("xDIM: "+xDIM+"xDIM: "+xDIM, "Dimension", 0, glass);
                            for (Component comp : glass.getComponents()) {
                                if (comp.getName().equals("Board:Dimension")) {
                                    glass.remove(comp);
                                }
                            }
                            glass.add(l);
                            setVisible(true);
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        //http://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                        dispatchEvent(new WindowEvent((Window) event.getSource(), WindowEvent.WINDOW_CLOSING));
                        setVisible(false);
                        dispose(); //Destroy the Board
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
                    getCurrentBoard().checkKey(event);
                    screenFunct = true;
                }
            }
        });
        
        getCurrentBoard().changeGraphic();
        centerFrame((JFrame) this);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    mainCharacter = new Trainer("Jesus");

                    Pokemon gyarados = new Pokemon(130, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon charizard = new Pokemon(6, 50, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon mewtwo = new Pokemon(150, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon umbreon = new Pokemon(196, 34, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon ampharos = new Pokemon(181, 20, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    Pokemon shuckle = new Pokemon(213, 62, false, mainCharacter.getHexID(), mainCharacter.getOctID());
                    mainCharacter.getParty().addPkmnToParty(gyarados);
                    mainCharacter.getParty().addPkmnToParty(charizard);
                    mainCharacter.getParty().addPkmnToParty(mewtwo);
                    mainCharacter.getParty().addPkmnToParty(umbreon);
                    mainCharacter.getParty().addPkmnToParty(ampharos);
                    mainCharacter.getParty().addPkmnToParty(shuckle);
                    
                    splashScreen = new SplashScreen("test", mainCharacter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void getScreenSettings() {
//        PopupMessage l = new PopupMessage(
//                (FRAME_WIDTH*xDIM)+" "+(FRAME_HEIGHT*yDIM), "CurrentDimension", 5, glass);
//        glass.add(l);
        System.out.println((FRAME_WIDTH*xDIM)+" "+(FRAME_HEIGHT*yDIM));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
//        PopupMessage l1 = new PopupMessage(
//                "widthMax: "+screenWidth+"; heightMax: "+screenHeight, "MaxSize", 5, glass);
//        glass.add(l1);
        System.out.println("widthMax: "+screenWidth+"; heightMax: "+screenHeight);
        
        double xDim = screenWidth/getCurrentBoard().getPreferredSize().width;
        double yDim = screenHeight/getCurrentBoard().getPreferredSize().height;
//        PopupMessage l2 = new PopupMessage(
//                "xDim: "+xDim+"; yDim: "+yDim, "xyDimension", 5, glass);
//        glass.add(l2);
        System.out.println("xDim: "+xDim+"; yDim: "+yDim);
        
        screenMaxDimx = xDim;
        screenMaxDimy = yDim;
        screenMaxDim = (xDim - yDim >= 0 ? yDim : xDim);
        isHorizzontal = xDim >= yDim;
//        PopupMessage l3 = new PopupMessage(
//                "screenMaxDimx: "+screenMaxDimx+"; screenMaxDimy: "+screenMaxDimy, "MaxDimension", 5, glass);
//        glass.add(l3);
        System.out.println("screenMaxDim: "+screenMaxDim);
    }
    
    private ExpandPanel getCurrentBoard() {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof ExpandPanel) {
                return (ExpandPanel) comp;
            }
        }
        return null;
    }
    
    private boolean getCurrentBoardLock() {
        ExpandPanel temp = getCurrentBoard();
        return temp.lock;
    }

    private void putUI() {
        try {
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
        int dx = (int) (centerPoint.x - (FRAME_WIDTH*xDIM)/2);
        int dy = (int) (centerPoint.y - (FRAME_HEIGHT*yDIM)/2);
        frame.setLocation(dx, dy);
    }
    
    private void centerFullFrame(JFrame frame, boolean h) {
        if (h) {
            int dx = (int) ((screenWidth-getCurrentBoard().getSize().width)/2);
            getCurrentBoard().setLocation(dx, 0);
        } else {
            int dy = (int) ((screenHeight-getCurrentBoard().getSize().height)/2);
            getCurrentBoard().setLocation(0, dy);
        }
        frame.setSize((int) screenWidth, (int) screenHeight);
        frame.setLocation(0, 0);
        System.out.println(frame.getSize().width+" "+frame.getSize().height);
    }
}