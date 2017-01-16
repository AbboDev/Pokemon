package screen;

import custom_texture.BattlePanel;
import custom_texture.BattleTab;
import custom_texture.ExpandPanel;
import custom_texture.MovePanel;
import custom_texture.PkmnPartyPanel;
import custom_texture.SpritePanel;
import engine.BattleEngine;
import engine.BattleEngine.Key;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import objects.Move;
import objects.Pokemon;
import objects.Trainer;
import static objects.Pokemon.Status.*;
import static screen.Board.*;

/**
 * @author Thomas
 */
public class BattleBoard extends ExpandPanel {
    private static final long serialVersionUID = -6244437515781556464L;
    private static final Move SWITCH_MOVE = new Move("Switch");
    private static final int DEFAULT_CLOCK = 50;
    private static final int TEXT_CLOCK = 500;
    private static final int CURRENT_PKMN = 0;
    private static final int MIN_BOARD_WIDTH = 512;
    private static final int MIN_BOARD_HEIGHT = 280;
    
    private BattlePanel eBattlePanel, BattlePanel;
    private SpritePanel eSpritePanel, SpritePanel;
    private BattleTab BattleTabs;
    private int DIM = 1;
    
    private final BattleEngine battleEngine;
    private Thread battleThread;
    private ArrayList<Pokemon> defeatPkmn;
    private final boolean isTrainer;
    
    protected Trainer self;
    protected Trainer enemy;
    protected Pokemon selfPokemon;
    protected Move selfMove;
    protected Pokemon otherPokemon;
    protected Move otherMove;
    
    private Pokemon selfSwitch;
    private Pokemon otherSwitch;
    
    private boolean firstTurn;
    private boolean playBattle;
    private boolean pkmnChoose;
    private boolean endBattle;
    private boolean blockTab;
    private boolean selfHit, otherHit;
    private int selfDamage, otherDamage;
    private int selfHP, otherHP;
    
    /**
     * Creates new form BattleBoard
     * @param self
     * @param trn_pkmn
     * @param dim
     * @throws java.io.IOException
     */  
    public BattleBoard(Trainer self, Object trn_pkmn, int dim) throws IOException {
        this.self = self;
        if (trn_pkmn.getClass() == Pokemon.class) {
            otherPokemon = (Pokemon) trn_pkmn;
        } else if (trn_pkmn.getClass() == Trainer.class) {
            this.enemy = (Trainer) trn_pkmn;
            otherPokemon = enemy.getParty().getPkmn(CURRENT_PKMN);
        }
        DIM = dim;
        isTrainer = (enemy != null);
        battleEngine = new BattleEngine(2, null, null, isTrainer);
        setName("battleBoard");
        initBoard();
    }
    
    private void initBoard() {
        firstTurn = true;
        playBattle = false;
        pkmnChoose = false;
        endBattle = false;
        blockTab = false;
        selfHit = otherHit = false;
        selfDamage = otherDamage = 0;
        
        defeatPkmn = new ArrayList<>();
        selfSwitch = null;
        otherSwitch = null;
        
        selfPokemon = self.getParty().getPkmn(CURRENT_PKMN);
        otherHP = otherPokemon.getStat("HP");
        selfHP = selfPokemon.getStat("HP");
        
        createBoard();
    }
    
    private void createBoard() {
        initComponents();
        expandComponent(DIM);
        
        printBasicBoard();
        decleareThread();
    }
    
    private void decleareThread() {
        battleThread = new Thread() {
            @Override
            public synchronized void run() {
                while (true) {
                    while (!playBattle) {
                        try { Thread.sleep(DEFAULT_CLOCK); } catch (InterruptedException ex) { } //sleep
                    }
                    hideTabs(true);
                    if (firstTurn) {
                        otherMove = otherPokemon.getRandomMove(false,1);
                        battleEngine.setPriority(selfPokemon, selfMove, otherPokemon, otherMove);
                    }
                    if (pkmnChoose)
                        switchPkmn();
                    otherHP = otherPokemon.getStat("HP");
                    selfHP = selfPokemon.getStat("HP");
                    if (firstTurn) {
                        defeatPkmn = battleEngine.firstMove(selfSwitch, selfMove, otherSwitch, otherMove);
                        printText(Key.Attack);
                        try { setDamage(0); }
                        catch (InterruptedException ex) { }
                    } else {
                        defeatPkmn = battleEngine.secondMove(selfSwitch, selfMove, otherSwitch, otherMove);
                        printText(Key.Attack);
                        try { setDamage(1); }
                        catch (InterruptedException ex) { }
                    }
                    printText(Key.Nothing); printText(Key.Effect); printText(Key.WeatherStart);
                    printText(Key.Stats); printText(Key.Critical); printText(Key.CountHit);
                    refreshStatus();
                    BattleTabs.printModifiedPkmn(selfPokemon);
                    if (selfMove.getWeather() != null || otherMove.getWeather() != null) {
                        setBackgroundWeather();
                    }
                    checkDefeatPokemon();
                    if (!endBattle) {
                        if (!firstTurn) {
                            try {
                                defeatPkmn = battleEngine.setRoundFinish(selfPokemon, otherPokemon);
                                setRecoil(); printText(Key.Damage);
                                while (selfHit || otherHit) {
                                    try { Thread.sleep(DEFAULT_CLOCK); } catch (InterruptedException ex) { } //sleep
                                }
                                checkDefeatPokemon();
                                battleEngine.flush();
                                playBattle = false; selfMove = null;
                                selfSwitch = null; otherSwitch = null;
                                BattleTabs.printParty();
                                printText(Key.Weather);
                                setBackgroundWeather();
                                hideTabs(false); lock = false;
                            } catch (InterruptedException ex) {
                            }
                        } else {
                            firstTurn = false;
                        }
                    } else {
                        playBattle = false;
                    }
                    battleEngine.eraseAction();
                }
            }
        };
        battleThread.start();
    }
    
    /**
     * It return the trainer which its party's its modifies
     * @return
     */
    public Trainer returnTrainer() {
        return self;
    }
    /**
     * It set the current trainer with a modified trainer
     * @param trn
     */
    public void setTrainer(Trainer trn) {
        self = trn;
//        printAll();
    }
    
/******************************************************************************/
    //These methods manage the switch action
    private void switchPanel() {
        boolean temp = checkParty(enemy);
        if (temp) {
            battleEnd(temp);
        } else {
            BattleTabs.rapidShowTab(1);
            BattleTabs.setBlockMoves(true);
            BattleTabs.printModifiedPkmn(selfPokemon);
            hideTabs(false);
        }
    }
    private void switchPkmn() {
        hideTabs(true);
        BattleTabs.setBlockMoves(false);
        battleEngine.switchPkmn(self, selfPokemon, selfSwitch);
        printText(Key.Switch);
        selfPokemon = selfSwitch;
        SpritePanel.setPokemon(selfPokemon);
        BattlePanel.setPokemon(selfPokemon);
        
        BattleTabs.setPokemon(selfPokemon);
        BattleTabs.setMovesPos(0);
        BattleTabs.setPartyPos(0);
        BattleTabs.rapidShowTab(0);
        selfRefresh();
        
        pkmnChoose = false;
    }
    private Pokemon checkEnemyParty() {
        boolean temp = checkParty(enemy);
        if (temp) {
            battleEnd(temp);
            return null;
        } else {
            Pokemon newOPkmn = enemy.getParty().getRandomPkmn();
            battleEngine.switchPkmn(enemy, otherPokemon, newOPkmn);
            printText(Key.Switch);
            otherPokemon = newOPkmn;
            eSpritePanel.setPokemon(otherPokemon);
            eBattlePanel.setPokemon(otherPokemon);
            otherMove = SWITCH_MOVE;
            otherRefresh();
            return newOPkmn;
        }
    }
    private boolean checkParty(Trainer trn) {
        boolean temp = true;
        for (Pokemon pkmn: trn.getParty().getArrayParty()) {
            if (pkmn.getStatus() != Pokemon.Status.KO) {
                temp = false;
            }
        }
        return temp;
    }
    private boolean switchAction() {
        if (defeatPkmn != null) {
            if (defeatPkmn.contains(selfPokemon) && defeatPkmn.contains(otherPokemon)) { //all
                printText(Key.KO);
                printText(Key.KO);
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
                    printText(Key.KO);
                    System.err.println("self KO");
                    switchPanel();
                } else if (defeatPkmn.contains(otherPokemon)) {
                    printText(Key.KO);
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
    

/******************************************************************************/
    //These methods manage the damage calculation with bars update
    private void setRecoil() throws InterruptedException {
        if (battleEngine.getEnemyPkmnRecoil() > 0) {
            otherDamage = battleEngine.getEnemyPkmnRecoil();
            moveBar(otherPokemon);
        }
        if (battleEngine.getPlayerPkmnRecoil() > 0) {
            if (otherPokemon.getStatus() != KO) {
                selfDamage = battleEngine.getPlayerPkmnRecoil();
                moveBar(selfPokemon);
            }
        }
    }
    private void setDamage(int index) throws InterruptedException {
        ArrayList<Integer> damages = battleEngine.getDamageArray();
        if (battleEngine.getPokemonFromOrder(index) == otherPokemon) {
            for (Integer damage : damages) {
                selfDamage = damage;
                moveBar(selfPokemon);
            }
        } else if (battleEngine.getPokemonFromOrder(index) == selfPokemon) {
            for (Integer damage : damages) {
                otherDamage = damage;
                moveBar(otherPokemon);
            }
        }
    }
    private void moveBar(Pokemon pkmn) throws InterruptedException {
        if (pkmn == selfPokemon) {
            selfHit = true;
            BattlePanel.setTimer(selfHP, selfPokemon.getStat("MaxHP"), selfDamage);
            BattlePanel.setHit(true);
            SpritePanel.setHit(true);
            eSpritePanel.setHit(true);
            while (selfHit) {
                try {
                    if (BattlePanel.getHit()) {
                        Thread.sleep(DEFAULT_CLOCK);
                    } else {
                        selfHit = false;
                        SpritePanel.setHit(false);
                        eSpritePanel.setHit(false);
                    }
                } catch (InterruptedException ex) { } //sleep
            }
            Thread.sleep(150);
        } else if (pkmn == otherPokemon) {
            otherHit = true;
            eBattlePanel.setTimer(otherHP, otherPokemon.getStat("MaxHP"), otherDamage);
            eBattlePanel.setHit(true);
            SpritePanel.setHit(true);
            eSpritePanel.setHit(true);
            while (otherHit) {
                try {
                    if (eBattlePanel.getHit()) {
                        Thread.sleep(DEFAULT_CLOCK);
                    } else {
                        otherHit = false;
                        SpritePanel.setHit(false);
                        eSpritePanel.setHit(false);
                    }
                } catch (InterruptedException ex) { } //sleep
            }
            Thread.sleep(150);
        }
    }
    private void checkDefeatPokemon() {
        if (defeatPkmn != null) {
            if (!defeatPkmn.isEmpty()) {
                BattleTabs.getPartyTab().repaint();
                BattleTabs.getPartyTab().revalidate();
                boolean temp = switchAction();
                if (!endBattle) {
                    if (temp) {
                        while (!pkmnChoose) {
                            try { Thread.sleep(DEFAULT_CLOCK); } catch (InterruptedException ex) { } //sleep
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
    private void battleEnd(boolean close) {
        if (close == true) {
            this.setVisible(false);
            System.err.println("stop battle");
            endBattle = true;
        }
    }

/******************************************************************************/  
    //These methods refresh the GUI
    private void hideTabs(boolean hide) {
        blockTab = hide;
        if (!hide) {
            try {
                Thread.sleep(TEXT_CLOCK);
                BattleTabs.stopEcho();
            } catch (InterruptedException ex) {
            }
        } else {
            BattleTabs.startEcho();
        }
    }
    
    private void selfRefresh() {
        try {
            BattleTabs.printMoves();
            BattleTabs.printParty();
            printInBattleStats(selfPokemon);
            SpritePanel.printImage();
        } catch (IOException ex) {
        }
    }
    
    private void otherRefresh() {
        try {
            printEnemyInBattleStats(otherPokemon);
            eSpritePanel.printImage();
        } catch (IOException ex) {
        }
    }
    
    private void refreshStatus() {
        SpritePanel.colorImage(SpritePanel.getPokemon().getStatus());
        BattlePanel.printStat();
        eSpritePanel.colorImage(eSpritePanel.getPokemon().getStatus());
        eBattlePanel.printStat();
        printText(Key.Status);
        repaint();
        revalidate();
    }
    
    private void printInBattleStats(Pokemon pkmn) throws IOException {
        BattlePanel.printName(pkmn.getSurname());
        BattlePanel.printLevel();
        BattlePanel.getHealtBar().setMinimum(0);
        BattlePanel.printHPBar(true);
        BattlePanel.printExpBar(true);
    }
    
    private void printEnemyInBattleStats(Pokemon pkmn) throws IOException {
        eBattlePanel.printName(pkmn.getSurname());
        eBattlePanel.printLevel();
        eBattlePanel.getHealtBar().setMinimum(0);
        eBattlePanel.printHPBar(true);
    }
    
    private void setBackgroundWeather() {
        String[] colorString = battleEngine.getWeather().split(",");
        ArrayList<Integer> color = new ArrayList<>();
        for(int i = 0; i < colorString.length; ++i) {
            color.add(Integer.parseInt(colorString[i]));
        }
        Color background = new Color(color.get(0), color.get(1), color.get(2));
        if (BattleBoard.getBackground() != background) {
            BattleBoard.setBackground(background);
            BattleBoard.repaint();
            BattleBoard.revalidate();
            printText(Key.Weather);
        }
    }
    
    private void printBasicBoard() {
        eBattlePanel = new BattlePanel(otherPokemon, false, DIM);
        BattlePanel = new BattlePanel(selfPokemon, true, DIM);
        eSpritePanel = new SpritePanel(otherPokemon, false, DIM);
        SpritePanel = new SpritePanel(selfPokemon, true, DIM);

        BattleBoard.add(eBattlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(
                10*DIM, 10*DIM, -1, -1));
        BattleBoard.add(BattlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(
                (MIN_BOARD_WIDTH*DIM)-((BattlePanel.getMinimumSize().width*DIM)+10*DIM),
                (MIN_BOARD_HEIGHT*DIM)-((BattlePanel.getMinimumSize().height*DIM)+10*DIM), -1, -1));

        BattleBoard.add(eSpritePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(
                (MIN_BOARD_WIDTH*DIM)-((eSpritePanel.getMinimumSize().width*DIM)+10*DIM), 10*DIM, -1, -1));
        BattleBoard.add(SpritePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(
                10*DIM, (MIN_BOARD_HEIGHT*DIM)-((SpritePanel.getMinimumSize().height*DIM)+10*DIM), -1, -1));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        if (BattleTabs == null) {
            BattleTabs = new BattleTab(self, selfPokemon, 0, DIM);
        } else {
            BattleTab bt = BattleTabs;
            BattleTabs = new BattleTab(bt, DIM);
        }
        add(BattleTabs, gridBagConstraints);
    }
    
    private void printText(Key key) {
        if (battleEngine.isKeyContain(key)) {
            try {
                BattleTabs.echo(battleEngine.getActionText(key));
                Thread.sleep(TEXT_CLOCK);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    public void changeGraphic(int mult) {
        DIM = mult;
        BattleBoard.remove(BattlePanel);
        BattleBoard.remove(eBattlePanel);
        BattleBoard.remove(SpritePanel);
        BattleBoard.remove(eSpritePanel);
        remove(BattleTabs);
        expandComponent(DIM);
        
        printBasicBoard();
        revalidate();
        repaint();
    }
    
/******************************************************************************/    
    /**
     * Take the event from Parent an applies it to the current tab
     * @param e 
     */
    protected void checkKey(KeyEvent e) {
        if (!blockTab) {
            lock = true;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    BattleTabs.rapidShowTab(0);
                    revalidate();
                    break;
                case KeyEvent.VK_2:
                    BattleTabs.rapidShowTab(1);
                    revalidate();
                    break;
                case KeyEvent.VK_3:
                    BattleTabs.rapidShowTab(2);
                    revalidate();
                    break;
                case KeyEvent.VK_4:
                    BattleTabs.rapidShowTab(3);
                    revalidate();
                    break;
                case L_BTN:
                    BattleTabs.showTab(BattleTabs.getCurrentTab(), false);
                    break;
                case R_BTN:
                    BattleTabs.showTab(BattleTabs.getCurrentTab(), true);
                    break;
                case UP_BTN:
                    break;
                case DOWN_BTN:
                    break;
                case RIGHT_BTN:
                    switch (BattleTabs.getCurrentTab()) {
                        case 0:
                            BattleTabs.moveCursor(BattleTabs.getMovesTab(), true);
                            break;
                        case 1:
                            BattleTabs.moveCursor(BattleTabs.getPartyTab(), true);
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    } break;
                case LEFT_BTN:
                    switch (BattleTabs.getCurrentTab()) {
                        case 0:
                            BattleTabs.moveCursor(BattleTabs.getMovesTab(), false);
                            break;
                        case 1:
                            BattleTabs.moveCursor(BattleTabs.getPartyTab(), false);
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    } break;
                case A_BTN:
                    switch (BattleTabs.getCurrentTab()) {
                        case 0:
                            if (selfPokemon.getStatus() != Pokemon.Status.KO) {
                                if (!playBattle) {
                                    if (selfPokemon.getMoveSet().get(BattleTabs.getMovesPos()).getPP() > 0) {
                                        MovePanel mvp =
                                                (MovePanel) BattleTabs.getMovesTab()
                                                .getComponent(BattleTabs.getMovesPos()+1);
                                        System.out.println("Press "+mvp.getName());
                                        selfPokemon.getMoveSet().get(BattleTabs.getMovesPos())
                                                .decreasePP(false, 1);
                                        mvp.changePP();
                                        mvp.recolor();
                                        firstTurn = true;
                                        selfMove = mvp.getMove();
                                        playBattle = true;
                                        repaint();
                                        revalidate();
                                    }
                                }
                            }
                            break;
                        case 1:
                            PkmnPartyPanel prp =
                                    (PkmnPartyPanel) BattleTabs.getPartyTab().
                                    getComponent(BattleTabs.getPartyPos()+1);
                            if (prp.getPokemon().getStatus() != KO) {
                                if (prp.getPokemon() != selfPokemon) {
                                    System.out.println("Press "+prp.getPokemon().getSurname());
                                    if (!playBattle) {
                                        selfSwitch = self.getParty().getPkmn(BattleTabs.getPartyPos());
                                        pkmnChoose = true;
                                        selfMove = SWITCH_MOVE;
                                        firstTurn = true;
                                        playBattle = true;
                                    } else {
                                        selfSwitch = self.getParty().getPkmn(BattleTabs.getPartyPos());
                                        pkmnChoose = true;
                                        selfMove = SWITCH_MOVE;
                                    }
                                }
                            }
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }
                    break;
                case B_BTN:
                    break;
                case KeyEvent.VK_NUMPAD1:
                    if (selfPokemon.getStatus() != Paralysis) {
                        SpritePanel.colorImage(Paralysis);
                        BattleTabs.printModifiedPkmn(selfPokemon);
                        selfPokemon.setStatus(Paralysis);
                        BattlePanel.printStat();
                        eBattlePanel.printStat();
                    }
                    break;
                case KeyEvent.VK_NUMPAD2:
                    if (selfPokemon.getStatus() != Poison) {
                        SpritePanel.colorImage(Poison);
                        BattleTabs.printModifiedPkmn(selfPokemon);
                        selfPokemon.setStatus(Poison);
                        BattlePanel.printStat();
                        eBattlePanel.printStat();
                    }
                    break;
                case KeyEvent.VK_NUMPAD3:
                    if (selfPokemon.getStatus() != Burn) {
                        SpritePanel.colorImage(Burn);
                        BattleTabs.printModifiedPkmn(selfPokemon);
                        selfPokemon.setStatus(Burn);
                        BattlePanel.printStat();
                        eBattlePanel.printStat();
                    }
                    break;
                case KeyEvent.VK_NUMPAD4:
                    if (selfPokemon.getStatus() != Freeze) {
                        SpritePanel.colorImage(Freeze);
                        BattleTabs.printModifiedPkmn(selfPokemon);
                        selfPokemon.setStatus(Freeze);
                        BattlePanel.printStat();
                        eBattlePanel.printStat();
                    }
                    break;
                case KeyEvent.VK_NUMPAD5:
                    if (selfPokemon.getStatus() != BadPoison) {
                        SpritePanel.colorImage(BadPoison);
                        BattleTabs.printModifiedPkmn(selfPokemon);
                        selfPokemon.setStatus(BadPoison);
                        BattlePanel.printStat();
                        eBattlePanel.printStat();
                    }
                    break;
                default:
                    break;
            }
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
        java.awt.GridBagConstraints gridBagConstraints;

        BattleBoard = new javax.swing.JPanel();

        setBackground(new java.awt.Color(51, 51, 51));
        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(512, 384));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(512, 384));
        setLayout(new java.awt.GridBagLayout());

        BattleBoard.setBackground(new java.awt.Color(102, 204, 0));
        BattleBoard.setMaximumSize(new java.awt.Dimension(1024, 560));
        BattleBoard.setMinimumSize(new java.awt.Dimension(512, 280));
        BattleBoard.setPreferredSize(new java.awt.Dimension(512, 280));
        BattleBoard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(BattleBoard, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BattleBoard;
    // End of variables declaration//GEN-END:variables
}