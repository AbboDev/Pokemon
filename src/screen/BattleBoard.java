package screen;

import custom_texture.MovePanel;
import custom_texture.PkmnPartyPanel;
import engine.BattleEngine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import object.Move;
import object.Pokemon;
import static object.Pokemon.Status.*;
import object.Trainer;

/**
 * @author Thomas
 */
public class BattleBoard extends javax.swing.JPanel {
    private static final long serialVersionUID = -6244437515781556464L;
    public static final String SPRITE = "res/sprite";
    private final static int CURRENT_PKMN = 0;
    private final static int ANIMATION_DELAY = 150;
    private final static int BAR_DELAY = 30;
    
    private static Move switchMove = new Move("Switch");
    
    private final BattleEngine battleEngine;
    private Thread battleThread;
    private Timer selfHealtBarThread, otherHealtBarThread;
    private Timer selfSpriteTimer, otherSpriteTimer;
    private int selfX, selfY;
    private int otherX, otherY;
    private ArrayList<Pokemon> defeatPkmn;
    private final boolean isTrainer;
    
    public Trainer self;
    public Trainer enemy;
    private Pokemon selfPokemon;
    private Move selfMove;
    private Pokemon otherPokemon;
    private Move otherMove;
    
    private Pokemon selfSwitch;
    private Pokemon otherSwitch;
//    private Pokemon selfSwitchedPkmn;
    
    private boolean firstTurn;
    private boolean playBattle;
    private boolean pkmnChoose;
    private boolean endBattle;
    private boolean selfHit, otherHit;
    private int selfDamage, otherDamage;
    private int selfHP, otherHP;
    
    /**
     * Creates new form BattleBoard
     * @param self
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public BattleBoard(Trainer self, Pokemon pkmn) throws IOException {
        firstTurn = true;
        playBattle = false;
        pkmnChoose = false;
        endBattle = false;
        selfHit = otherHit = false;
        selfDamage = otherDamage = 0;
        
        defeatPkmn = new ArrayList<>();
        selfSwitch = null;
        otherSwitch = null;
        
        initComponents();
        this.self = self;
        isTrainer = false;
        
        otherPokemon = pkmn;
        selfPokemon = self.getParty().getPkmn(CURRENT_PKMN);
        battleEngine = new BattleEngine(2, null, null, false);
        otherHP = otherPokemon.getHP();
        selfHP = selfPokemon.getHP();
        
        printAll();
        decleareThread();
        
        BattleTab.revalidate();
        BattleTab.repaint();
    }
    /**
     * Creates new form BattleBoard
     * @param self
     * @param enemy
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public BattleBoard(Trainer self, Trainer enemy) throws IOException {
        firstTurn = true;
        playBattle = false;
        pkmnChoose = false;
        endBattle = false;
        selfHit = otherHit = false;
        selfDamage = otherDamage = 0;
        
        defeatPkmn = new ArrayList<>();
        selfSwitch = null;
        otherSwitch = null;
        
        initComponents();
        this.self = self;
        this.enemy = enemy;
        isTrainer = true;
        
        otherPokemon = enemy.getParty().getPkmn(CURRENT_PKMN);
        selfPokemon = self.getParty().getPkmn(CURRENT_PKMN);
        battleEngine = new BattleEngine(2, null, null, true);
        otherHP = otherPokemon.getHP();
        selfHP = selfPokemon.getHP();
        
        printAll();
        decleareThread();
        
        BattleTab.revalidate();
        BattleTab.repaint();
    }
    
    private void switchPanel() {
        BattleTab.setSelectedIndex(1);
        BattleTab.setEnabledAt(0, false);
        BattleTab.revalidate();
        BattleTab.repaint();
    }
        
    private void switchPkmn() {
        battleEngine.switchPkmn(self, selfPokemon, selfSwitch);
        selfPokemon = selfSwitch;

        BattleTab.setSelectedIndex(0);
        BattleTab.setEnabledAt(0, true);
        BattleTab.revalidate();
        BattleTab.repaint();
        selfRefresh();
        
        pkmnChoose = false;
    }
    
    private Pokemon checkEnemyParty() {
        boolean temp = true;
        for (Pokemon pkmn: enemy.getParty().getArrayParty()) {
            if (pkmn.getStatus() != Pokemon.Status.KO) {
                temp = false;
            }
        }
        if (temp) {
            battleEnd(temp);
            return null;
        } else {
            Pokemon newOPkmn = enemy.getParty().getRandomPkmn();
            battleEngine.switchPkmn(enemy, otherPokemon, newOPkmn);
            otherPokemon = newOPkmn;
            otherMove = switchMove;
            otherRefresh();
            return newOPkmn;
        }
    }
    
    private boolean switchAction() {
        if (defeatPkmn != null) {
            if (defeatPkmn.contains(selfPokemon) && defeatPkmn.contains(otherPokemon)) { //all
                System.err.println("self KO");
                System.err.println("enemy KO");
                if (isTrainer) {
                    Pokemon tempPkmn = checkEnemyParty();
                    otherSwitch = tempPkmn;
                } else {
                    battleEnd(true);
                }
                switchPanel();
            } else {
                if (defeatPkmn.contains(selfPokemon)) {
                    System.err.println("self KO");
                    switchPanel();
                } else if (defeatPkmn.contains(otherPokemon)) {
                    System.err.println("enemy KO");
                    if (isTrainer) {
                        Pokemon tempPkmn = checkEnemyParty();
                        otherSwitch = tempPkmn;
                    } else {
                        battleEnd(true);
                    }
                }
            }
        }
        return defeatPkmn.contains(selfPokemon);
    }
    
    private void decleareThread() {
        battleThread = new Thread("BattleThread") {
            @Override
            public synchronized void run() {
                while (true) {
                    while (!playBattle) {
                        try { Thread.sleep(50); } catch (InterruptedException ex) { } //sleep
                    }
                    if (firstTurn) {
                        otherMove = otherPokemon.getRandomMove(false,1);
                        battleEngine.setPriority(selfPokemon, selfMove, otherPokemon, otherMove);
                        
                        if (pkmnChoose) {
                            switchPkmn();
                        }
                        otherHP = otherPokemon.getHP();
                        selfHP = selfPokemon.getHP();
                        defeatPkmn = battleEngine.firstMove(selfSwitch, selfMove, otherSwitch, otherMove);
                        setDamage(0);
                        while (selfHit || otherHit) {
                            try { Thread.sleep(50); } catch (InterruptedException ex) { } //sleep
                        }
                    } else {
                        if (pkmnChoose) {
                            switchPkmn();
                        }
                        otherHP = otherPokemon.getHP();
                        selfHP = selfPokemon.getHP();
                        defeatPkmn = battleEngine.secondMove(selfSwitch, selfMove, otherSwitch, otherMove);
                        setDamage(1);
                        while (selfHit || otherHit) {
                            try { Thread.sleep(50); } catch (InterruptedException ex) { } //sleep
                        }
                    }
                    System.out.println("Finish Bar");
                    setBackgroundWeather();
                    printStat(selfPokemon, Status);
                    printStat(otherPokemon, eStatus);
                    
                    checkDefeatPokemon();
                    
                    if (!endBattle) {
                        if (!firstTurn) {
                            defeatPkmn = battleEngine.setRoundFinish(selfPokemon, otherPokemon);
                            setRecoil();
                            while (selfHit || otherHit) {
                                try { Thread.sleep(50); } catch (InterruptedException ex) { } //sleep
                            }
                            checkDefeatPokemon();
                            battleEngine.flush();
                            playBattle = false;
                            selfMove = null;
                            selfSwitch = null;
                            otherSwitch = null;
                            printParty(self);
                        } else {
                            firstTurn = false;
                        }
                    } else {
                        playBattle = false;
                    }
                }
            }
        };
        battleThread.start();
        
        selfX = PkmnSprite.getX();
        selfY = PkmnSprite.getY();
        otherX = ePkmnSprite.getX();
        otherY = ePkmnSprite.getY();
        
        selfSpriteTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selfPokemon.getStatus() != KO) {
                    if (selfPokemon.getStatus() == OK) {
                        if (PkmnSprite.getY() == selfY) {
                            PkmnSprite.setLocation(selfX, selfY + 3);
                        } else {
                            PkmnSprite.setLocation(selfX, selfY);
                        }
                        int delay = ((selfPokemon.getMaxHP() / selfPokemon.getHP()) > 50)
                                ? 50: selfPokemon.getMaxHP() / selfPokemon.getHP();
                        selfSpriteTimer.setDelay(ANIMATION_DELAY * (delay));
                    } else if (selfPokemon.getStatus() != Asleep) {
                        if (PkmnSprite.getX() == selfX) {
                            PkmnSprite.setLocation(selfX + 3, selfY);
                            selfSpriteTimer.setDelay(ANIMATION_DELAY);
                        } else {
                            PkmnSprite.setLocation(selfX, selfY);
                            selfSpriteTimer.setDelay(ANIMATION_DELAY*5);
                        }
                    }
                }
            }
        });
        otherSpriteTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (otherPokemon.getStatus() != KO) {
                    if (otherPokemon.getStatus() == OK) {
                        if (ePkmnSprite.getY() == otherY) {
                            ePkmnSprite.setLocation(otherX, otherY + 3);
                        } else {
                            ePkmnSprite.setLocation(otherX, otherY);
                        }
                        int delay = ((otherPokemon.getMaxHP() / otherPokemon.getHP()) > 50)
                                ? 50: otherPokemon.getMaxHP() / otherPokemon.getHP();
                        otherSpriteTimer.setDelay(ANIMATION_DELAY * (delay));
                    } else if (otherPokemon.getStatus() != Asleep) {
                        if (ePkmnSprite.getX() == otherX) {
                            ePkmnSprite.setLocation(otherX + 3, otherY);
                            otherSpriteTimer.setDelay(ANIMATION_DELAY);
                        } else {
                            ePkmnSprite.setLocation(otherX, otherY);
                            otherSpriteTimer.setDelay(ANIMATION_DELAY*5);
                        }
                    }
                }
            }
        });
        selfSpriteTimer.start();
        otherSpriteTimer.start();
        
        selfHealtBarThread = new Timer(BAR_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selfHit) {
//                    System.out.println("Start selfBar");
                    if (selfDamage > 0) {
                        if (selfHP > 0) {
                            --selfDamage;
                            HealtBar.setValue(HealtBar.getValue()-1);
                            --selfHP;
                            Healt.setText(selfHP+"/"+selfPokemon.getMaxHP());
                        } else {
                            selfHit = false;
//                            System.out.println("Stop selfBar");
                        }
                    } else {
                        selfHit = false;
//                        System.out.println("Stop selfBar");
                    }
                }
            }
        });
        otherHealtBarThread = new Timer(BAR_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (otherHit) {
//                    System.out.println("Start otherBar");
                    if (otherDamage > 0) {
                        if (otherHP > 0) {
                            --otherDamage;
                            eHealtBar.setValue(eHealtBar.getValue()-1);
                            --otherHP;
                            eHealt.setText(otherHP+"/"+otherPokemon.getMaxHP());
                        } else {
                            otherHit = false;
//                            System.out.println("Stop otherBar");
                        }
                    } else {
                        otherHit = false;
//                        System.out.println("Stop otherBar");
                    }
                }
            }
        });
        selfHealtBarThread.start();
        otherHealtBarThread.start();
    }
    
    private void setRecoil() {
        if (battleEngine.getEnemyPkmnRecoil() > 0) {
            otherDamage = battleEngine.getEnemyPkmnRecoil();
            otherHit = true;
        }
        if (battleEngine.getPlayerPkmnRecoil() > 0) {
            if (otherPokemon.getStatus() != KO) {
                
                selfDamage = battleEngine.getPlayerPkmnRecoil();
                selfHit = true;
            }
        }
    }
    private void setDamage(int index) {
        int damage = battleEngine.getDamage();
        if (battleEngine.getPokemonFromOrder(index) == otherPokemon) {
            selfDamage = damage;
            selfHit = true;
        } else if (battleEngine.getPokemonFromOrder(index) == selfPokemon) {
            otherDamage = damage;
            otherHit = true;
        }
    }
    private void checkDefeatPokemon() {
        if (defeatPkmn != null) {
            if (!defeatPkmn.isEmpty()) {
                PartyPanel.repaint();
                PartyPanel.revalidate();
                boolean temp = switchAction();
                if (!endBattle) {
                    if (temp) {
                        while (!pkmnChoose) {
                            try { Thread.sleep(50); } catch (InterruptedException ex) { } //sleep
                        }
                        switchPkmn();
                    }
                    battleEngine.changeOrder(selfSwitch, selfMove, otherSwitch, otherMove);
                    defeatPkmn.clear();
                } else {
                   firstTurn = false;
                }
            }
        }
    }
    /**
     * It return the trainer which its party's its modifies
     * @return
     */
    public Trainer returnTrainer() {
        return self;
    }

    /**
     *
     * @param trn
     */
    public void setTrainer(Trainer trn) {
        self = trn;
    }
    
    private void setBackgroundWeather() {
        String[] colorString = battleEngine.getWeather().split(",");
        ArrayList<Integer> color = new ArrayList<>();
        for(int i = 0; i < colorString.length; ++i) {
            color.add(Integer.parseInt(colorString[i]));
        }
        Color background = new Color(color.get(0), color.get(1), color.get(2));
        if (this.getBackground() == background) {
            this.setBackground(background);
            this.repaint();
            this.revalidate();
        }
    }
    
    /**
     *
     */
    public final void printAll() {
        try {
            printMove(selfPokemon);
            printParty(self);
            printInBattleStats(selfPokemon);
            printImage(selfPokemon, PkmnSprite);
            printEnemyInBattleStats(otherPokemon);
            printImage(otherPokemon, ePkmnSprite);
        } catch (IOException ex) {
        }
    }
    public final void selfRefresh() {
        try {
            printMove(selfPokemon);
            printParty(self);
            printInBattleStats(selfPokemon);
            printImage(selfPokemon, PkmnSprite);
        } catch (IOException ex) {
        }
    }
    public final void otherRefresh() {
        try {
            printEnemyInBattleStats(otherPokemon);
            printImage(otherPokemon, ePkmnSprite);
        } catch (IOException ex) {
        }
    }
    
    private void removeMove() {
        MovePanel.removeAll();
    }
    private void printMove(final Pokemon pkmn) {
        final ArrayList<Move> moveSet = pkmn.getMoveSet();
        ArrayList<JPanel> buttonSet = new ArrayList<>();
        removeMove();
        try {
            for (int i = 0; i < moveSet.size(); ++i) {
                buttonSet.add(printMoveButton(pkmn, new JPanel(), moveSet.get(i), i));
            }
        } catch (Exception e) {
        }
        for (JPanel button : buttonSet) {
            MovePanel.add(button);
        }
        MovePanel.repaint();
        MovePanel.revalidate();
    }
    private JPanel printMoveButton(final Pokemon pkmn, JPanel button, final Move move, final int index) {
        button = new MovePanel(move);
        if (pkmn.getStatus() != Pokemon.Status.KO) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!playBattle) {
                        if (pkmn.getMoveSet().get(index).getPP() > 0) {
                            System.out.println("Press "+move.getName());
                            pkmn.getMoveSet().get(index).decreasePP(false, 1);
                            MovePanel mvp = (MovePanel)e.getSource();
                            mvp.getPP().setText(move.getPP()+"/"+move.getMaxPP());

                            firstTurn = true;
                            selfMove = move;
                            playBattle = true;
                            repaint();
                            revalidate();
                        }
                    }
                }
            });
        } else {
            button.setEnabled(false);
        }
        button.setPreferredSize(new Dimension(160, 60));
        return button;
    }
    
    private void battleEnd(boolean close) {
        if (close == true) {
            this.setVisible(false);
            System.err.println("stop battle");
            endBattle = true;
        }
    }
    
    private void removeParty() {
        PartyPanel.removeAll();
    }
    private void printParty(Trainer trn) {
        boolean close = true;
        final ArrayList<Pokemon> party = trn.getParty().getArrayParty();
        ArrayList<JPanel> buttonSet = new ArrayList<>();
        removeParty();
        try {
            for (int i = 0; i < party.size(); ++i) {
                if (party.get(i).getStatus() != Pokemon.Status.KO) {
                    close = false;
                }
                buttonSet.add(printPartyButton(party.get(i), new JPanel()));
            }
        } catch (Exception e) {
        }
        battleEnd(close);
        for (JPanel button : buttonSet) {
            PartyPanel.add(button);
        }
        PartyPanel.repaint();
        PartyPanel.revalidate();        
    }
    private JPanel printPartyButton(final Pokemon pkmn, JPanel button) {
        button = new PkmnPartyPanel(pkmn);
        if (pkmn.getStatus() != Pokemon.Status.KO) {
            if (pkmn != selfPokemon) {
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!playBattle) {
                            System.out.println("Press "+pkmn.getSurname());
                            int i = PartyPanel.getComponentZOrder(((JPanel) e.getSource()));

                            selfSwitch = self.getParty().getPkmn(i);
                            pkmnChoose = true;

                            selfMove = switchMove;
                            firstTurn = true;
                            playBattle = true;
                        } else {
                            System.out.println("Press "+pkmn.getSurname());
                            int i = PartyPanel.getComponentZOrder(((JPanel) e.getSource()));

                            selfSwitch = self.getParty().getPkmn(i);
                            pkmnChoose = true;

                            selfMove = switchMove;
                        }
                    }
                });
            }
        } else {
            button.setEnabled(false);
        }
        button.setPreferredSize(new Dimension(160, 60));
        return button;
    }
    
    private void printInBattleStats(Pokemon pkmn) throws IOException {
        Name.setText(pkmn.getSurname());
        printLevel(pkmn, Level);
        HealtBar.setMinimum(0);
        printHPBar(pkmn, true, HealtBar, Healt, Status);
        printExpBar(pkmn, true);
    }
    private void printEnemyInBattleStats(Pokemon pkmn) throws IOException {
        eName.setText(pkmn.getSurname());
        printLevel(pkmn, eLevel);
        eHealtBar.setMinimum(0);
        printHPBar(pkmn, true, eHealtBar, eHealt, eStatus);
    }
    
    private void printExpBar(Pokemon pkmn, boolean change) {
        if (change) {
            ExpBar.setMinimum(pkmn.getLevelExperience(pkmn.getLevel()));
            ExpBar.setMaximum(pkmn.getLevelExperience(pkmn.getLevel()+1));
            printLevel(pkmn, Level);
            printHPBar(pkmn, change, HealtBar, Healt, Status);
        }
        ExpBar.setValue(pkmn.getExp());
        ExpBar.repaint();
    }
    private void printHPBar(Pokemon pkmn, boolean change, JProgressBar jPrBar, JLabel jLbl1, JLabel jLbl2) {
        if (change) {
            jPrBar.setMaximum(pkmn.getMaxHP());
        }
        jPrBar.setValue(pkmn.getHP());
        jLbl1.setText(pkmn.getHP()+"/"+pkmn.getMaxHP());
        printStat(pkmn, jLbl2);
    }
    private void printStat(Pokemon pkmn, JLabel stat) {
        stat.setForeground(Color.black);
        switch (pkmn.getStatus()) {
            case OK:
                stat.setText("OK");
                stat.setBackground(Color.white);
                break;
            case Asleep:
                stat.setText("SLP");
                stat.setBackground(Color.gray);
                break;
            case BadPoison:
                stat.setText("BPSN");
                stat.setBackground(Color.magenta);
                break;
            case Burn:
                stat.setText("BRN");
                stat.setBackground(Color.orange);
                break;
            case Freeze:
                stat.setText("FRZ");
                stat.setBackground(Color.cyan);
                break;
            case Paralysis:
                stat.setText("PAR");
                stat.setBackground(Color.yellow);
                break;
            case Poison:
                stat.setText("PSN");
                stat.setBackground(Color.pink);
                break;
            default:
                break;
        }
    }
    private void printLevel(Pokemon pkmn, JLabel level) {
        if (!pkmn.getIfAsessual()) {
            String sex;
            if (pkmn.getIfMale()) { sex = "♂"; } else { sex = "♀"; }
            level.setText(sex+" Lv. "+pkmn.getLevel());
        } else {
            level.setText("Lv. "+pkmn.getLevel()+"");
        }
    }
    private void printImage(Pokemon pkmn, JLabel label) {
        boolean sexBoolean = !pkmn.getIfAsessual();
        if (label == PkmnSprite) {
            label.setIcon(pkmn.getSprite(SPRITE, 256, 256, true, sexBoolean));
        } else if (label == ePkmnSprite) {
            label.setIcon(pkmn.getSprite(SPRITE, 256, 256, false, sexBoolean));
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BattleBoard = new javax.swing.JPanel();
        BattlePanel = new javax.swing.JPanel();
        Name = new javax.swing.JLabel();
        Level = new javax.swing.JLabel();
        Healt = new javax.swing.JLabel();
        ExpBar = new javax.swing.JProgressBar();
        HPpanel = new javax.swing.JPanel();
        HealtBar = new javax.swing.JProgressBar();
        jTextField5 = new javax.swing.JTextField();
        Status = new javax.swing.JLabel();
        ImagePanel = new javax.swing.JPanel();
        PkmnSprite = new javax.swing.JLabel();
        eBattlePanel = new javax.swing.JPanel();
        eName = new javax.swing.JLabel();
        eLevel = new javax.swing.JLabel();
        eHealt = new javax.swing.JLabel();
        eHPpanel = new javax.swing.JPanel();
        eHealtBar = new javax.swing.JProgressBar();
        jTextField7 = new javax.swing.JTextField();
        eStatus = new javax.swing.JLabel();
        eImagePanel = new javax.swing.JPanel();
        ePkmnSprite = new javax.swing.JLabel();
        BattleTab = new javax.swing.JTabbedPane();
        MovePanel = new AlphaPanel();
        PartyPanel = new AlphaPanel();
        OptionPanel = new AlphaPanel();
        BagPanel = new AlphaPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        TextPanel = new AlphaPanel();
        SendButton = new javax.swing.JButton();
        TextControl = new javax.swing.JTextField();
        CommandList = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(102, 204, 0));
        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        BattleBoard.setMaximumSize(new java.awt.Dimension(980, 578));
        BattleBoard.setMinimumSize(new java.awt.Dimension(980, 578));
        BattleBoard.setOpaque(false);
        BattleBoard.setPreferredSize(new java.awt.Dimension(980, 578));

        BattlePanel.setBackground(new java.awt.Color(102, 102, 102));
        BattlePanel.setMaximumSize(new java.awt.Dimension(260, 90));
        BattlePanel.setMinimumSize(new java.awt.Dimension(260, 90));

        Name.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        Name.setText("Name");

        Level.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        Level.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Level.setText("Level");

        Healt.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        Healt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Healt.setText("Healt");

        ExpBar.setForeground(new java.awt.Color(0, 153, 255));
        ExpBar.setValue(50);
        ExpBar.setString("");
        ExpBar.setStringPainted(true);

        HPpanel.setBackground(new java.awt.Color(51, 51, 51));
        HPpanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        HealtBar.setBackground(new java.awt.Color(51, 51, 51));
        HealtBar.setForeground(new java.awt.Color(51, 255, 51));
        HealtBar.setValue(30);
        HealtBar.setBorderPainted(false);
        HealtBar.setPreferredSize(new java.awt.Dimension(150, 14));
        HealtBar.setString("");

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(51, 51, 51));
        jTextField5.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 11)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(255, 153, 0));
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setText("HP");
        jTextField5.setBorder(null);
        jTextField5.setFocusable(false);
        jTextField5.setHighlighter(null);
        jTextField5.setPreferredSize(new java.awt.Dimension(20, 20));
        jTextField5.setSelectedTextColor(new java.awt.Color(255, 102, 0));
        jTextField5.setSelectionColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout HPpanelLayout = new javax.swing.GroupLayout(HPpanel);
        HPpanel.setLayout(HPpanelLayout);
        HPpanelLayout.setHorizontalGroup(
            HPpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HPpanelLayout.createSequentialGroup()
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HealtBar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        HPpanelLayout.setVerticalGroup(
            HPpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HealtBar, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status.setText("OK");
        Status.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout BattlePanelLayout = new javax.swing.GroupLayout(BattlePanel);
        BattlePanel.setLayout(BattlePanelLayout);
        BattlePanelLayout.setHorizontalGroup(
            BattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BattlePanelLayout.createSequentialGroup()
                .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Level, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(ExpBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BattlePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(HPpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(BattlePanelLayout.createSequentialGroup()
                .addComponent(Status, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Healt, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        BattlePanelLayout.setVerticalGroup(
            BattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BattlePanelLayout.createSequentialGroup()
                .addGroup(BattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HPpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Healt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(ExpBar, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        BattlePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Healt, Status});

        Status.getAccessibleContext().setAccessibleName("Status");

        ImagePanel.setOpaque(false);

        PkmnSprite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PkmnSprite.setFocusable(false);
        PkmnSprite.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PkmnSprite.setMaximumSize(new java.awt.Dimension(256, 256));
        PkmnSprite.setMinimumSize(new java.awt.Dimension(256, 256));
        PkmnSprite.setPreferredSize(new java.awt.Dimension(256, 256));

        javax.swing.GroupLayout ImagePanelLayout = new javax.swing.GroupLayout(ImagePanel);
        ImagePanel.setLayout(ImagePanelLayout);
        ImagePanelLayout.setHorizontalGroup(
            ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PkmnSprite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ImagePanelLayout.setVerticalGroup(
            ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PkmnSprite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        eBattlePanel.setBackground(new java.awt.Color(102, 102, 102));
        eBattlePanel.setPreferredSize(new java.awt.Dimension(260, 80));

        eName.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        eName.setText("Name");

        eLevel.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        eLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        eLevel.setText("Level");

        eHealt.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        eHealt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eHealt.setText("Healt");

        eHPpanel.setBackground(new java.awt.Color(51, 51, 51));
        eHPpanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        eHealtBar.setBackground(new java.awt.Color(51, 51, 51));
        eHealtBar.setForeground(new java.awt.Color(51, 255, 51));
        eHealtBar.setValue(30);
        eHealtBar.setBorderPainted(false);
        eHealtBar.setPreferredSize(new java.awt.Dimension(150, 14));
        eHealtBar.setString("");

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(51, 51, 51));
        jTextField7.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 11)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(255, 153, 0));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setText("HP");
        jTextField7.setBorder(null);
        jTextField7.setFocusable(false);
        jTextField7.setHighlighter(null);
        jTextField7.setPreferredSize(new java.awt.Dimension(20, 20));
        jTextField7.setSelectedTextColor(new java.awt.Color(255, 102, 0));
        jTextField7.setSelectionColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout eHPpanelLayout = new javax.swing.GroupLayout(eHPpanel);
        eHPpanel.setLayout(eHPpanelLayout);
        eHPpanelLayout.setHorizontalGroup(
            eHPpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eHPpanelLayout.createSequentialGroup()
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eHealtBar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        eHPpanelLayout.setVerticalGroup(
            eHPpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(eHealtBar, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        eStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eStatus.setText("OK");
        eStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout eBattlePanelLayout = new javax.swing.GroupLayout(eBattlePanel);
        eBattlePanel.setLayout(eBattlePanelLayout);
        eBattlePanelLayout.setHorizontalGroup(
            eBattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eBattlePanelLayout.createSequentialGroup()
                .addComponent(eName, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eLevel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eBattlePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(eHPpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(eBattlePanelLayout.createSequentialGroup()
                .addComponent(eStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eHealt, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        eBattlePanelLayout.setVerticalGroup(
            eBattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eBattlePanelLayout.createSequentialGroup()
                .addGroup(eBattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eHPpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(eBattlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eHealt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        eImagePanel.setOpaque(false);

        ePkmnSprite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ePkmnSprite.setFocusable(false);
        ePkmnSprite.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ePkmnSprite.setMaximumSize(new java.awt.Dimension(256, 256));
        ePkmnSprite.setMinimumSize(new java.awt.Dimension(256, 256));
        ePkmnSprite.setPreferredSize(new java.awt.Dimension(256, 256));

        javax.swing.GroupLayout eImagePanelLayout = new javax.swing.GroupLayout(eImagePanel);
        eImagePanel.setLayout(eImagePanelLayout);
        eImagePanelLayout.setHorizontalGroup(
            eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ePkmnSprite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        eImagePanelLayout.setVerticalGroup(
            eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ePkmnSprite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        BattleTab.addTab("Moveset", MovePanel);

        PartyPanel.setOpaque(true);
        PartyPanel.setPreferredSize(new java.awt.Dimension(200, 607));
        BattleTab.addTab("Party", PartyPanel);

        OptionPanel.setOpaque(true);
        BattleTab.addTab("Option", OptionPanel);

        BagPanel.setOpaque(true);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout BagPanelLayout = new javax.swing.GroupLayout(BagPanel);
        BagPanel.setLayout(BagPanelLayout);
        BagPanelLayout.setHorizontalGroup(
            BagPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
        );
        BagPanelLayout.setVerticalGroup(
            BagPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        );

        BattleTab.addTab("Bag", BagPanel);

        TextPanel.setOpaque(true);

        SendButton.setText("<SEND>");
        SendButton.setAutoscrolls(true);
        SendButton.setMinimumSize(null);
        SendButton.setPreferredSize(new java.awt.Dimension(100, 60));
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });
        TextPanel.add(SendButton);

        TextControl.setFont(new java.awt.Font("Segoe UI Symbol", 0, 14)); // NOI18N
        TextControl.setText("text");
        TextControl.setOpaque(false);
        TextControl.setPreferredSize(new java.awt.Dimension(720, 60));
        TextPanel.add(TextControl);

        CommandList.setAutoscrolls(true);
        CommandList.setEnabled(false);
        CommandList.setPreferredSize(new java.awt.Dimension(150, 60));
        TextPanel.add(CommandList);

        BattleTab.addTab("Text", TextPanel);

        javax.swing.GroupLayout BattleBoardLayout = new javax.swing.GroupLayout(BattleBoard);
        BattleBoard.setLayout(BattleBoardLayout);
        BattleBoardLayout.setHorizontalGroup(
            BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BattleBoardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eBattlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BattlePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eImagePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(BattleTab)
        );
        BattleBoardLayout.setVerticalGroup(
            BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BattleBoardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BattleBoardLayout.createSequentialGroup()
                        .addComponent(eImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                        .addComponent(BattlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BattleBoardLayout.createSequentialGroup()
                        .addComponent(eBattlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BattleTab, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BattleBoard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BattleBoard, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
        String command = TextControl.getText();
        switch (command) {
            case "SLP":
                selfPokemon.setStatus(Asleep);
                CommandList.addItem(command);
                printStat(selfPokemon, Status);
                break;
            default: break;
        }
        if (CommandList.getItemCount() > 0) {
            CommandList.setEnabled(true);
        }
    }//GEN-LAST:event_SendButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BagPanel;
    private javax.swing.JPanel BattleBoard;
    private javax.swing.JPanel BattlePanel;
    private javax.swing.JTabbedPane BattleTab;
    private javax.swing.JComboBox CommandList;
    private javax.swing.JProgressBar ExpBar;
    private javax.swing.JPanel HPpanel;
    private javax.swing.JLabel Healt;
    private javax.swing.JProgressBar HealtBar;
    private javax.swing.JPanel ImagePanel;
    private javax.swing.JLabel Level;
    private javax.swing.JPanel MovePanel;
    private javax.swing.JLabel Name;
    private javax.swing.JPanel OptionPanel;
    private javax.swing.JPanel PartyPanel;
    private javax.swing.JLabel PkmnSprite;
    private javax.swing.JButton SendButton;
    private javax.swing.JLabel Status;
    private javax.swing.JTextField TextControl;
    private javax.swing.JPanel TextPanel;
    private javax.swing.JPanel eBattlePanel;
    private javax.swing.JPanel eHPpanel;
    private javax.swing.JLabel eHealt;
    private javax.swing.JProgressBar eHealtBar;
    private javax.swing.JPanel eImagePanel;
    private javax.swing.JLabel eLevel;
    private javax.swing.JLabel eName;
    private javax.swing.JLabel ePkmnSprite;
    private javax.swing.JLabel eStatus;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}

class AlphaPanel extends JPanel {
    public AlphaPanel() {
        this.setBackground(new Color(102, 102, 102, 190));
    }
}