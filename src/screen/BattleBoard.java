package screen;

import custom_texture.MoveButton;
import engine.BattleEngine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import object.Move;
import object.Pokemon;
import object.Trainer;

/**
 * @author Thomas
 */
public class BattleBoard extends javax.swing.JPanel {
    public String ROOT, SPRITE;
    public File KANTO, KANTO_MOVE;
    private final static int CURRENT_PKMN = 0;
    
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static Move switchMove = new Move(Pokemon.MOVES, "Switch");
    
    private final BattleEngine battleEngine;
    private Thread battleThread;
    private ArrayList<Pokemon> defeatPkmn;
    private final boolean isTrainer;
    
    public Trainer self;
    public Trainer enemy;
    private Pokemon selfPokemon;
    private Move playerMove;
    private final Pokemon otherPokemon;
    
    private Pokemon switchedPkmn1;
    private Pokemon switchedPkmn2;
    private Pokemon selfSwitchedPkmn;
    
    private boolean firstTurn;
    private boolean playBattle;
    private boolean pkmnChoose;
    private boolean endBattle;
    
    /**
     * Creates new form BattleBoard
     * @param self
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public BattleBoard(Trainer self, String ROOT, ArrayList<File> files) throws IOException {
        firstTurn = true;
        playBattle = false;
        pkmnChoose = false;
        endBattle = false;
        
        defeatPkmn = new ArrayList<>();
        switchedPkmn1 = null;
        switchedPkmn2 = null;
        selfSwitchedPkmn = null;
        
        initComponents();
        makeUI();
        this.ROOT = ROOT;
        setPath(files);
        this.self = self;
        isTrainer = false;
        
        otherPokemon = new Pokemon(KANTO, KANTO_MOVE, 129, 100, true, null, null);
        selfPokemon = self.getParty().getPkmn(CURRENT_PKMN);
        battleEngine = new BattleEngine(2, null, null, false);
        
        refresh();
        decleareThread();
    }
    /**
     * Creates new form BattleBoard
     * @param self
     * @param enemy
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public BattleBoard(Trainer self, Trainer enemy, String ROOT, ArrayList<File> files) throws IOException {
        firstTurn = true;
        playBattle = false;
        pkmnChoose = false;
        endBattle = false;
        
        defeatPkmn = new ArrayList<>();
        switchedPkmn1 = null;
        switchedPkmn2 = null;
        selfSwitchedPkmn = null;
        
        initComponents();
        makeUI();
        this.ROOT = ROOT;
        setPath(files);
        this.self = self;
        this.enemy = enemy;
        isTrainer = true;
        
        otherPokemon = enemy.getParty().getPkmn(CURRENT_PKMN);
        selfPokemon = self.getParty().getPkmn(CURRENT_PKMN);
        battleEngine = new BattleEngine(2, null, null, true);
        
        refresh();
        decleareThread();
    }
    
    private void switchPanel() {
        BattleTab.setSelectedIndex(1);
        BattleTab.setEnabledAt(0, false);
        BattleTab.revalidate();
        BattleTab.repaint();
        System.out.println("0");
    }
        
    private void switchPkmn() {
        battleEngine.switchPkmn(self, selfPokemon, selfSwitchedPkmn);
        if (battleEngine.getPokemonFromOrder(0) == selfPokemon) {
            switchedPkmn1 = selfSwitchedPkmn;
        } else {
            switchedPkmn2 = selfSwitchedPkmn;
        }
        selfPokemon = selfSwitchedPkmn;

        BattleTab.setSelectedIndex(0);
        BattleTab.setEnabledAt(0, true);
        BattleTab.revalidate();
        BattleTab.repaint();
        selfRefresh();
        
        pkmnChoose = false;
    }
    private void checkEnemyParty() {
        for (Pokemon pkmn: enemy.getParty().getArrayParty()) {
            if (pkmn.getStatus() != Pokemon.Status.KO) continue;
            else battleEnd(true); break;
        }
    }
    
    private boolean switchAction() {
        if (defeatPkmn != null) {
            if (defeatPkmn.contains(selfPokemon) && defeatPkmn.contains(otherPokemon)) { //all
                System.err.println("self KO");
                System.err.println("enemy KO");
                if (isTrainer) {
                    checkEnemyParty();
                    if (otherPokemon == battleEngine.getPokemonFromOrder(0)) {
                        switchedPkmn1 = otherPokemon;
                    } else if (otherPokemon == battleEngine.getPokemonFromOrder(1)) {
                        switchedPkmn2 = otherPokemon;
                    }
                } else {
                    System.err.println("stop battle");
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
                        checkEnemyParty();
                        if (otherPokemon == battleEngine.getPokemonFromOrder(0)) {
                            switchedPkmn1 = otherPokemon;
                        } else if (otherPokemon == battleEngine.getPokemonFromOrder(1)) {
                            switchedPkmn2 = otherPokemon;
                        }
                    } else {
                        System.err.println("stop battle");
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
                        try { this.sleep(50); } catch (InterruptedException ex) { } //sleep
                    }
                    if (firstTurn) {
                        battleEngine.setPriority(selfPokemon, playerMove, otherPokemon, otherPokemon.getRandomMove(false,1));
                        if (pkmnChoose) {
                            switchPkmn();
                        }
                        defeatPkmn = battleEngine.firstMove(switchedPkmn1, null, switchedPkmn2, null);
                    } else {
                        if (pkmnChoose) {
                            switchPkmn();
                        }
                        defeatPkmn = battleEngine.secondMove(switchedPkmn1, null, switchedPkmn2, null);
                    }
                    setBackgroundWeather();
                    printHPBar(selfPokemon, true, HealtBar, Healt, Status);
                    printHPBar(otherPokemon, true, eHealtBar, eHealt, eStatus);
                    
                    if (defeatPkmn != null) {
                        if (!defeatPkmn.isEmpty()) {
                            boolean temp = switchAction();
                            if (!endBattle) {
                                if (temp) {
                                    while (!pkmnChoose) {
                                        try { this.sleep(50); } catch (InterruptedException ex) { } //sleep
                                    }
                                }
                                if (battleEngine.getPokemonFromOrder(0) == selfPokemon) {
                                    battleEngine.changeOrder(null, playerMove, null, null);
                                } else {
                                    battleEngine.changeOrder(null, null, null, playerMove);
                                }
                                switchPkmn();
                                defeatPkmn.clear();
                            } else {
                               firstTurn = false;
                            }
                        }
                    }
                    
                    if (!firstTurn) {
                        battleEngine.setRoundFinish(selfPokemon, otherPokemon);
                        playBattle = false;
                        playerMove = null;
                        switchedPkmn1 = null;
                        switchedPkmn2 = null;
                        selfSwitchedPkmn = null;
                    } else {
                        firstTurn = false;
                    }
                }
            }
        };
        battleThread.start();
    }
    
    private void setPath(ArrayList<File> files) {
        if (OS.contains("win")) {
            SPRITE = ROOT + "\\sprite\\";
        } else {
            SPRITE = ROOT + "/sprite/";
        }
        for (File thisFile: files) {
            switch (thisFile.getName()) {
                case "kanto.csv": KANTO = thisFile;
                    break;
                case "kantoMove.csv": KANTO_MOVE = thisFile;
                    break;
                default:
                    break;
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
        this.setBackground(new Color(color.get(0), color.get(1), color.get(2)));
        this.refresh();
        this.revalidate();
        this.repaint();
    }
    
    /**
     *
     */
    public final void refresh() {
        try {
            printMove(selfPokemon);
            printParty(self);
            printInBattleStats(selfPokemon);
            printImage(selfPokemon, PkmnImage);
            printEnemyInBattleStats(otherPokemon);
            printImage(otherPokemon, ePkmnImage);
        } catch (IOException ex) {
        }
    }
    public final void selfRefresh() {
        try {
            printMove(selfPokemon);
            printParty(self);
            printInBattleStats(selfPokemon);
            printImage(selfPokemon, PkmnImage);
        } catch (IOException ex) {
        }
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
    
    private void removeMove() {
        MovePanel.removeAll();
    }
    private void printMove(final Pokemon pkmn) {
        final ArrayList<Move> moveSet = pkmn.getMoveSet();
        removeMove();
        try {
            for (int i = 0; i < moveSet.size(); ++i) {
                printMoveButton(pkmn, new JButton(), moveSet.get(i), i);
            }
        } catch (Exception e) {
        }
        MovePanel.revalidate();
        MovePanel.repaint();
    }
    private void printMoveButton(final Pokemon pkmn, JButton button, final Move move, final int index) {
        button = new MoveButton(printMoveText(move), getType(move));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!playBattle) {
                    if (pkmn.getStatus() != Pokemon.Status.KO && pkmn.getMoveSet().get(index).getPP() > 0) {
                        System.out.println("Press "+move.getName());
                        pkmn.getMoveSet().get(index).decreasePP(false, 1);
                        ((JButton) e.getSource()).setText(printMoveText(move));

                        firstTurn = true;
                        playerMove = move;
                        playBattle = true;
                    }
                }
            }
        });
        button.setPreferredSize(new Dimension(160, 60));
        MovePanel.add(button);
    }
    private String printMoveText(Move move) {
        return move.getName()+" "+move.getPP()+"/"+move.getMaxPP();
    }    
    private Pokemon.Type getType(Move move) {
        return move.getMoveType();
    }
    
    private void battleEnd(boolean close) {
        if (close == true) {
            this.setVisible(false);
            endBattle = true;
        }
    }
    
    private void removeParty() {
        PartyPanel.removeAll();
    }
    private void printParty(Trainer trn) {
        boolean close = true;
        final ArrayList<Pokemon> party = trn.getParty().getArrayParty();
        removeParty();
        try {
            for (int i = 0; i < party.size(); ++i) {
                printPartyButton(party.get(i), new JButton());
                if (party.get(i).getStatus() != Pokemon.Status.KO) {
                    close = false;
                }
            }
        } catch (Exception e) {
        }
        battleEnd(close);
        PartyPanel.revalidate();
        PartyPanel.repaint();        
    }
    private void printPartyButton(final Pokemon pkmn, JButton button) {
        String sex;
        if (pkmn.getIfMale()) { sex = "♂"; } else { sex = "♀"; }
        button = new JButton(pkmn.getSurname()+" "+sex);
        if (null != pkmn.getStatus()) switch (pkmn.getStatus()) {
            case KO:
                button.setBackground(Color.red);
                button.setEnabled(false);
                break;
            case Asleep:
                button.setBackground(Color.gray);
                break;
            case Poison:
                button.setBackground(Color.magenta);
                break;
            case BadPoison:
                button.setBackground(Color.darkGray);
                break;
            case Paralysis:
                button.setBackground(Color.yellow);
                break;
            case Burn:
                button.setBackground(Color.orange);
                break;
            case Freeze:
                button.setBackground(Color.cyan);
                break;
            default:
                button.setBackground(Color.blue);
                break;
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!playBattle) {
                    System.out.println("Press "+pkmn.getSurname());
                    int i = PartyPanel.getComponentZOrder(((JButton) e.getSource()));

                    selfSwitchedPkmn = self.getParty().getPkmn(i);
                    pkmnChoose = true;
                    
                    playerMove = switchMove;
                    firstTurn = true;
                    playBattle = true;
                } else {
                    System.out.println("Press "+pkmn.getSurname());
                    int i = PartyPanel.getComponentZOrder(((JButton) e.getSource()));

                    selfSwitchedPkmn = self.getParty().getPkmn(i);
                    pkmnChoose = true;
                    
                    playerMove = switchMove;
                }
            }
        });
        button.setPreferredSize(new Dimension(160, 60));
        if (selfPokemon == pkmn) {
            button.setEnabled(false);
        }
        PartyPanel.add(button);
    }
    
    private void printInBattleStats(Pokemon pkmn) throws IOException {
        Name.setText(pkmn.getSurname());
        printLevel(pkmn, Level);
        HealtBar.setMinimum(0);
        printHPBar(pkmn, true, HealtBar, Healt, Status);
        printExpBar(pkmn, true);
        printImage(pkmn, PkmnImage);
    }
    private void printEnemyInBattleStats(Pokemon pkmn) throws IOException {
        eName.setText(pkmn.getSurname());
        printLevel(pkmn, eLevel);
        eHealtBar.setMinimum(0);
        printHPBar(pkmn, true, eHealtBar, eHealt, eStatus);
        printImage(pkmn, ePkmnImage);
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
        jLbl2.setText(pkmn.getStatus().name());
    }
    private void printLevel(Pokemon pkmn, JLabel level) {
        String sex;
        if (pkmn.getIfMale()) { sex = "♂"; } else { sex = "♀"; }
        level.setText(sex+" "+pkmn.getLevel());
    }
    private void printImage(Pokemon pkmn, JLabel label) {
        if (label == PkmnImage) {
            label.setIcon(pkmn.getSpriteMirrored(SPRITE));
        } else {
            label.setIcon(pkmn.getSprite(SPRITE));
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
        PkmnImage = new javax.swing.JLabel();
        eBattlePanel = new javax.swing.JPanel();
        eName = new javax.swing.JLabel();
        eLevel = new javax.swing.JLabel();
        eHealt = new javax.swing.JLabel();
        eHPpanel = new javax.swing.JPanel();
        eHealtBar = new javax.swing.JProgressBar();
        jTextField7 = new javax.swing.JTextField();
        eStatus = new javax.swing.JLabel();
        eImagePanel = new javax.swing.JPanel();
        ePkmnImage = new javax.swing.JLabel();
        BattleTab = new javax.swing.JTabbedPane();
        MovePanel = new javax.swing.JPanel();
        move1 = new javax.swing.JButton();
        move2 = new javax.swing.JButton();
        move3 = new javax.swing.JButton();
        move4 = new javax.swing.JButton();
        move5 = new javax.swing.JButton();
        move6 = new javax.swing.JButton();
        PartyPanel = new javax.swing.JPanel();
        pkmn1 = new javax.swing.JButton();
        pkmn2 = new javax.swing.JButton();
        pkmn3 = new javax.swing.JButton();
        pkmn4 = new javax.swing.JButton();
        pkmn5 = new javax.swing.JButton();
        pkmn6 = new javax.swing.JButton();
        OptionPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        BagPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        TextPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();

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

        PkmnImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PkmnImage.setToolTipText("");
        PkmnImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout ImagePanelLayout = new javax.swing.GroupLayout(ImagePanel);
        ImagePanel.setLayout(ImagePanelLayout);
        ImagePanelLayout.setHorizontalGroup(
            ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PkmnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        );
        ImagePanelLayout.setVerticalGroup(
            ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PkmnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
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

        ePkmnImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ePkmnImage.setToolTipText("");
        ePkmnImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout eImagePanelLayout = new javax.swing.GroupLayout(eImagePanel);
        eImagePanel.setLayout(eImagePanelLayout);
        eImagePanelLayout.setHorizontalGroup(
            eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ePkmnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        );
        eImagePanelLayout.setVerticalGroup(
            eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
            .addGroup(eImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ePkmnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        );

        MovePanel.setOpaque(false);

        move1.setText("move1");
        move1.setToolTipText("");
        move1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move1.setName(""); // NOI18N
        move1.setOpaque(false);
        move1.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move1);

        move2.setText("move2");
        move2.setToolTipText("");
        move2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move2.setName(""); // NOI18N
        move2.setOpaque(false);
        move2.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move2);

        move3.setText("move3");
        move3.setToolTipText("");
        move3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move3.setName(""); // NOI18N
        move3.setOpaque(false);
        move3.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move3);

        move4.setText("move4");
        move4.setToolTipText("");
        move4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move4.setName(""); // NOI18N
        move4.setOpaque(false);
        move4.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move4);

        move5.setText("move5");
        move5.setToolTipText("");
        move5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move5.setName(""); // NOI18N
        move5.setOpaque(false);
        move5.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move5);

        move6.setText("move6");
        move6.setToolTipText("");
        move6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        move6.setName(""); // NOI18N
        move6.setOpaque(false);
        move6.setPreferredSize(new java.awt.Dimension(160, 60));
        MovePanel.add(move6);

        BattleTab.addTab("Moveset", MovePanel);

        PartyPanel.setOpaque(false);
        PartyPanel.setPreferredSize(new java.awt.Dimension(200, 607));

        pkmn1.setText("pkmn");
        pkmn1.setToolTipText("");
        pkmn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn1.setName(""); // NOI18N
        pkmn1.setOpaque(false);
        pkmn1.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn1);

        pkmn2.setText("pkmn");
        pkmn2.setToolTipText("");
        pkmn2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn2.setName(""); // NOI18N
        pkmn2.setOpaque(false);
        pkmn2.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn2);

        pkmn3.setText("pkmn");
        pkmn3.setToolTipText("");
        pkmn3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn3.setName(""); // NOI18N
        pkmn3.setOpaque(false);
        pkmn3.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn3);

        pkmn4.setText("pkmn");
        pkmn4.setToolTipText("");
        pkmn4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn4.setName(""); // NOI18N
        pkmn4.setOpaque(false);
        pkmn4.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn4);

        pkmn5.setText("pkmn");
        pkmn5.setToolTipText("");
        pkmn5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn5.setName(""); // NOI18N
        pkmn5.setOpaque(false);
        pkmn5.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn5);

        pkmn6.setText("pkmn");
        pkmn6.setToolTipText("");
        pkmn6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pkmn6.setName(""); // NOI18N
        pkmn6.setOpaque(false);
        pkmn6.setPreferredSize(new java.awt.Dimension(160, 60));
        PartyPanel.add(pkmn6);

        BattleTab.addTab("Party", PartyPanel);

        OptionPanel.setOpaque(false);

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout OptionPanelLayout = new javax.swing.GroupLayout(OptionPanel);
        OptionPanel.setLayout(OptionPanelLayout);
        OptionPanelLayout.setHorizontalGroup(
            OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
            .addGroup(OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(OptionPanelLayout.createSequentialGroup()
                    .addGap(0, 480, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 481, Short.MAX_VALUE)))
        );
        OptionPanelLayout.setVerticalGroup(
            OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
            .addGroup(OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(OptionPanelLayout.createSequentialGroup()
                    .addGap(0, 29, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 29, Short.MAX_VALUE)))
        );

        BattleTab.addTab("Option", OptionPanel);

        BagPanel.setOpaque(false);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
        );
        BagPanelLayout.setVerticalGroup(
            BagPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        );

        BattleTab.addTab("Bag", BagPanel);

        TextPanel.setOpaque(false);

        jButton1.setText("<SEND>");
        jButton1.setAutoscrolls(true);
        jButton1.setMinimumSize(null);
        jButton1.setOpaque(false);
        jButton1.setPreferredSize(new java.awt.Dimension(100, 60));
        TextPanel.add(jButton1);

        jTextField2.setFont(new java.awt.Font("Segoe UI Symbol", 0, 14)); // NOI18N
        jTextField2.setText("text");
        jTextField2.setOpaque(false);
        jTextField2.setPreferredSize(new java.awt.Dimension(720, 60));
        TextPanel.add(jTextField2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setAutoscrolls(true);
        jComboBox1.setMinimumSize(null);
        jComboBox1.setPreferredSize(new java.awt.Dimension(150, 60));
        TextPanel.add(jComboBox1);

        BattleTab.addTab("Text", TextPanel);

        javax.swing.GroupLayout BattleBoardLayout = new javax.swing.GroupLayout(BattleBoard);
        BattleBoard.setLayout(BattleBoardLayout);
        BattleBoardLayout.setHorizontalGroup(
            BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BattleBoardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BattleBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eBattlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BattleBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BagPanel;
    private javax.swing.JPanel BattleBoard;
    private javax.swing.JPanel BattlePanel;
    private javax.swing.JTabbedPane BattleTab;
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
    private javax.swing.JLabel PkmnImage;
    private javax.swing.JLabel Status;
    private javax.swing.JPanel TextPanel;
    private javax.swing.JPanel eBattlePanel;
    private javax.swing.JPanel eHPpanel;
    private javax.swing.JLabel eHealt;
    private javax.swing.JProgressBar eHealtBar;
    private javax.swing.JPanel eImagePanel;
    private javax.swing.JLabel eLevel;
    private javax.swing.JLabel eName;
    private javax.swing.JLabel ePkmnImage;
    private javax.swing.JLabel eStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JButton move1;
    private javax.swing.JButton move2;
    private javax.swing.JButton move3;
    private javax.swing.JButton move4;
    private javax.swing.JButton move5;
    private javax.swing.JButton move6;
    private javax.swing.JButton pkmn1;
    private javax.swing.JButton pkmn2;
    private javax.swing.JButton pkmn3;
    private javax.swing.JButton pkmn4;
    private javax.swing.JButton pkmn5;
    private javax.swing.JButton pkmn6;
    // End of variables declaration//GEN-END:variables
}
