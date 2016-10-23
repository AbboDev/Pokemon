package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import object.Pokemon;
import object.Trainer;

/**
 * @author Thomas
 */
public class Board extends JFrame {

    private static final long serialVersionUID = 1L;
    private static String OS = System.getProperty("os.name").toLowerCase();
    
    private HashMap componentMap;
    
    public Board() throws IOException {
        Trainer mainCharacter = new Trainer();
        
        ClassLoader classLoader = getClass().getClassLoader();
        
        String ROOT = System.getProperty("user.dir");
        
        File KANTO, KANTO_MOVE;
        ArrayList<File> file = new ArrayList<>();
        
        KANTO = new File(classLoader.getResource("res/database/kanto.csv").getFile());
        KANTO_MOVE = new File(classLoader.getResource("res/database/kantoMove.csv").getFile());
            
        System.out.println(KANTO.getAbsolutePath());
        System.out.println(KANTO_MOVE.getAbsolutePath());

        file.add(KANTO);
        file.add(KANTO_MOVE);
        
        Pokemon gyarados = new Pokemon(KANTO, KANTO_MOVE, 130, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon charizard = new Pokemon(KANTO, KANTO_MOVE, 6, 40, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon mewtwo = new Pokemon(KANTO, KANTO_MOVE, 150, 100, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon bulbasaur = new Pokemon(KANTO, KANTO_MOVE, 1, 9, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        Pokemon squirtle = new Pokemon(KANTO, KANTO_MOVE, 7, 15, false, mainCharacter.getHexID(), mainCharacter.getOctID());
        mainCharacter.getParty().addPkmnToParty(gyarados);
        mainCharacter.getParty().addPkmnToParty(mewtwo);
        mainCharacter.getParty().addPkmnToParty(charizard);
        mainCharacter.getParty().addPkmnToParty(bulbasaur);
        mainCharacter.getParty().addPkmnToParty(squirtle);
        
        Pokemon enemy = new Pokemon(KANTO, KANTO_MOVE, 23, 50, true, null, null);
        
        final BattleBoard battleBoard = new BattleBoard(mainCharacter, enemy, ROOT, file);
        final StatsBoard statsBoard = new StatsBoard(mainCharacter, ROOT, file);
        
        final JFrame frame = new GameFrame("Test");
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                char ch = event.getKeyChar();
                System.out.println(ch);
                if (ch == '1') {
                    battleBoard.setTrainer(statsBoard.returnTrainer());
                    battleBoard.refresh();
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
    private void createComponentMap() {
        componentMap = new HashMap<>();
        Component[] components = this.getContentPane().getComponents();
        for (Component component : components) {
            componentMap.put(component.getName(), component);
        }
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        }
        else return null;
    }
    
//    public static List<Component> getAllComponents(final Container c) {
//        Component[] comps = c.getComponents();
//        List<Component> compList = new ArrayList<>();
//        for (Component comp : comps) {
//            compList.add(comp);
//            if (comp instanceof Container)
//                compList.addAll(getAllComponents((Container) comp));
//        }
//        return compList;
//    }
    public static StatsBoard getStatsBoardComponents(final Container c) {
        Component[] comps = c.getComponents();
        StatsBoard board = null;
        for (Component comp : comps) {
            if (comp instanceof StatsBoard) return board;
        }
        return board;
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
    }
    
    private void centerFrame(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - 500;
        int dy = centerPoint.y - 300;
        frame.setLocation(dx, dy);
    }
}