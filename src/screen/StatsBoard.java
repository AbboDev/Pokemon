package screen;

import custom_texture.MoveButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import object.Move;
import object.Pokemon;
import object.Trainer;

/**
 * @author Thomas
 */
public class StatsBoard extends javax.swing.JPanel {
    public String ROOT, SPRITE;
    public File KANTO, KANTO_MOVE;
    
    private Trainer self;
    private int pkmnIndex = 0;
    private Thread refreshThread;
    private Thread expThread;
    /**
     * Creates new form StatsBoard
     * @param self
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public StatsBoard(Trainer self, String ROOT, ArrayList<File> files) throws IOException {
        initComponents();
        this.ROOT = ROOT;
        setPath(files);
        this.self = self;
        
        refresh();
    }
    private void setPath(ArrayList<File> files) {
        SPRITE = "res/sprite";
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
     * It return the trainer which its party's stats modified
     * @return
     */
    public Trainer returnTrainer() {
        return self;
    }
    public void setTrainer(Trainer trn) {
        self = trn;
    }
    
    public final void refresh() {
        try {
            printMove(this.self.getParty().getPkmn(pkmnIndex));
            printInBattleStats(this.self.getParty().getPkmn(pkmnIndex));
            printStats(this.self.getParty().getPkmn(pkmnIndex));
        } catch (IOException ex) {
        }
    }
    
    private void removeMove() {
        MoveTab.removeAll();
    } 
    private void printMove(final Pokemon pkmn) {
        final ArrayList<Move> moveSet = pkmn.getMoveSet();
        removeMove();
        try {
            for (int i = 0; i < moveSet.size(); ++i) {
                printMoveButton(pkmn, new JButton(), moveSet.get(i));
            }
        } catch (Exception e) {
        }
        MoveTab.revalidate();
        MoveTab.repaint();
    }   
    private void printMoveButton(final Pokemon pkmn, JButton button, final Move move) {
        button = new MoveButton(printMoveText(move), getType(move));
        button.setPreferredSize(new Dimension(155, 45));
        MoveTab.add(button);
    }
    private String printMoveText(Move move) {
        return move.getName()+" "+move.getPP()+"/"+move.getMaxPP();
    }    
    private Pokemon.Type getType(Move move) {
        return move.getMoveType();
    }    
    
    private void printInBattleStats(Pokemon pkmn) throws IOException {
        Name.setText(pkmn.getName());
        printLevel(pkmn);
        HealtBar.setMinimum(0);
        printHPBar(pkmn, true);
        printExpBar(pkmn, true);
        printImage(pkmn, PkmnImage);
    }
    
    private void printStats(Pokemon pkmn) {
        hp.setText(pkmn.getHP()+"");
        atk.setText(pkmn.getAttack()+"");
        def.setText(pkmn.getDefense()+"");
        spatk.setText(pkmn.getSpAttack()+"");
        spdef.setText(pkmn.getSpDefense()+"");
        speed.setText(pkmn.getSpeed()+"");
        ivhp.setText(pkmn.getIVHP()+"");
        ivatk.setText(pkmn.getIVAttack()+"");
        ivdef.setText(pkmn.getIVDefense()+"");
        ivspatk.setText(pkmn.getIVSPAttack()+"");
        ivspdef.setText(pkmn.getIVSPDefense()+"");
        ivspeed.setText(pkmn.getIVSpeed()+"");
        evhp.setText(pkmn.getEVHP()+"");
        evatk.setText(pkmn.getEVAttack()+"");
        evdef.setText(pkmn.getEVDefense()+"");
        evspatk.setText(pkmn.getEVSPAttack()+"");
        evspdef.setText(pkmn.getEVSPDefense()+"");
        evspeed.setText(pkmn.getEVSpeed()+"");
        nature.setText(pkmn.getNature()+"");
        shiny.setText(pkmn.getIfShiny()+"");
        pokerus.setText(pkmn.getIfPokerus()+"");
        happiness.setText(pkmn.getHappiness()+"");
        status.setText(pkmn.getStatus()+"");
        maxExp.setText(pkmn.getMaxExp()+"");
        nextLevel.setText(pkmn.getNextLevelExperience()+"");
        exp.setText(pkmn.getExp()+"");
        item.setText(pkmn.getLevelExperience(pkmn.getLevel()+1)+"");
    }
    
    private void printExpBar(Pokemon pkmn, boolean change) {
        if (change) {
            ExpBar.setMinimum(pkmn.getLevelExperience(pkmn.getLevel()));
            ExpBar.setMaximum(pkmn.getLevelExperience(pkmn.getLevel()+1));
            printLevel(pkmn);
            printStats(pkmn);
            printHPBar(pkmn, change);
        }
        ExpBar.setValue(pkmn.getExp());
        ExpBar.setString(pkmn.getExp()+"");
        printExp(pkmn);
        ExpBar.repaint();
    }
    private void printHPBar(Pokemon pkmn, boolean change) {
        if (change) {
            HealtBar.setMaximum(pkmn.getMaxHP());
        }
        HealtBar.setValue(pkmn.getHP());
        Healt.setText(pkmn.getHP()+"/"+pkmn.getMaxHP());
    }
    private void printLevel(Pokemon pkmn) {
        String sex;
        if (pkmn.getIfMale()) { sex = "♂"; } else { sex = "♀"; }
        Level.setText(sex+" "+pkmn.getLevel());
    }
    private void printExp(Pokemon pkmn) {
        exp.setText(pkmn.getExp()+"");
        nextLevel.setText(pkmn.getNextLevelExperience()+"");
    }
    private void printImage(Pokemon pkmn, JLabel label) {
        boolean sexBoolean = !pkmn.getIfAsessual();
        label.setIcon(pkmn.getSprite(SPRITE, 256, 256, false, sexBoolean));
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StatsBoard = new javax.swing.JPanel();
        MoveTab = new javax.swing.JPanel();
        move1 = new javax.swing.JButton();
        move2 = new javax.swing.JButton();
        move3 = new javax.swing.JButton();
        move4 = new javax.swing.JButton();
        move5 = new javax.swing.JButton();
        move6 = new javax.swing.JButton();
        StatsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        hp = new javax.swing.JLabel();
        atk = new javax.swing.JLabel();
        def = new javax.swing.JLabel();
        spatk = new javax.swing.JLabel();
        spdef = new javax.swing.JLabel();
        speed = new javax.swing.JLabel();
        ivhp = new javax.swing.JLabel();
        ivatk = new javax.swing.JLabel();
        ivdef = new javax.swing.JLabel();
        ivspatk = new javax.swing.JLabel();
        ivspdef = new javax.swing.JLabel();
        ivspeed = new javax.swing.JLabel();
        evhp = new javax.swing.JLabel();
        evatk = new javax.swing.JLabel();
        evdef = new javax.swing.JLabel();
        evspatk = new javax.swing.JLabel();
        evspdef = new javax.swing.JLabel();
        evspeed = new javax.swing.JLabel();
        nature = new javax.swing.JLabel();
        shiny = new javax.swing.JLabel();
        pokerus = new javax.swing.JLabel();
        happiness = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        maxExp = new javax.swing.JLabel();
        nextLevel = new javax.swing.JLabel();
        exp = new javax.swing.JLabel();
        item = new javax.swing.JLabel();
        NamePanel = new javax.swing.JPanel();
        Name = new javax.swing.JLabel();
        Level = new javax.swing.JLabel();
        HPpanel = new javax.swing.JPanel();
        HealtBar = new javax.swing.JProgressBar();
        jTextField5 = new javax.swing.JTextField();
        Healt = new javax.swing.JLabel();
        ExpBar = new javax.swing.JProgressBar();
        ImagePanel = new javax.swing.JPanel();
        PkmnImage = new javax.swing.JLabel();
        New = new javax.swing.JButton();
        Index = new javax.swing.JLabel();
        Refresh = new javax.swing.JToggleButton();
        GiveExp = new javax.swing.JButton();
        TextExp = new javax.swing.JTextField();
        Previous = new javax.swing.JButton();
        Next = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));
        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        StatsBoard.setOpaque(false);

        MoveTab.setBackground(new java.awt.Color(102, 102, 102));
        MoveTab.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3, true));

        move1.setText("move1");
        move1.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move1);

        move2.setText("move2");
        move2.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move2);

        move3.setText("move3");
        move3.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move3);

        move4.setText("move4");
        move4.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move4);

        move5.setText("move5");
        move5.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move5);

        move6.setText("move6");
        move6.setPreferredSize(new java.awt.Dimension(250, 60));
        MoveTab.add(move6);

        StatsPanel.setBackground(new java.awt.Color(102, 102, 102));
        StatsPanel.setPreferredSize(new java.awt.Dimension(250, 430));

        jLabel1.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("HP");

        jLabel2.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("ATK");

        jLabel3.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("DEF");

        jLabel4.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("spATK");

        jLabel5.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("spDEF");

        jLabel6.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("SPEED");

        jLabel7.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("IV'S HP");

        jLabel8.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("IV'S ATK");

        jLabel9.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("IV'S DEF");

        jLabel10.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("IV'S spATK");

        jLabel11.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("IV'S spDEF");

        jLabel12.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("IV'S SPEED");

        jLabel13.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Nature");

        jLabel14.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Shiny");

        jLabel15.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Status");

        jLabel16.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Happiness");

        jLabel17.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Pokerùs");

        jLabel19.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("EV'S SPEED");

        jLabel20.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("EV'S spDEF");

        jLabel21.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("EV'S spATK");

        jLabel22.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("EV'S DEF");

        jLabel23.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("EV'S ATK");

        jLabel24.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("EV'S HP");

        jLabel27.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setText("Max Exp");

        jLabel30.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));

        jLabel18.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Exp");

        jLabel25.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));
        jLabel25.setText("Next Level Exp");

        jLabel26.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));
        jLabel26.setText("Hold Item");

        hp.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        hp.setForeground(new java.awt.Color(51, 51, 51));
        hp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hp.setText("data");

        atk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        atk.setForeground(new java.awt.Color(51, 51, 51));
        atk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        atk.setText("data");

        def.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        def.setForeground(new java.awt.Color(51, 51, 51));
        def.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        def.setText("data");

        spatk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        spatk.setForeground(new java.awt.Color(51, 51, 51));
        spatk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        spatk.setText("data");

        spdef.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        spdef.setForeground(new java.awt.Color(51, 51, 51));
        spdef.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        spdef.setText("data");

        speed.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        speed.setForeground(new java.awt.Color(51, 51, 51));
        speed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        speed.setText("data");

        ivhp.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivhp.setForeground(new java.awt.Color(51, 51, 51));
        ivhp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivhp.setText("data");

        ivatk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivatk.setForeground(new java.awt.Color(51, 51, 51));
        ivatk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivatk.setText("data");

        ivdef.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivdef.setForeground(new java.awt.Color(51, 51, 51));
        ivdef.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivdef.setText("data");

        ivspatk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivspatk.setForeground(new java.awt.Color(51, 51, 51));
        ivspatk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivspatk.setText("data");

        ivspdef.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivspdef.setForeground(new java.awt.Color(51, 51, 51));
        ivspdef.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivspdef.setText("data");

        ivspeed.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        ivspeed.setForeground(new java.awt.Color(51, 51, 51));
        ivspeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivspeed.setText("data");

        evhp.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evhp.setForeground(new java.awt.Color(51, 51, 51));
        evhp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evhp.setText("data");

        evatk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evatk.setForeground(new java.awt.Color(51, 51, 51));
        evatk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evatk.setText("data");

        evdef.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evdef.setForeground(new java.awt.Color(51, 51, 51));
        evdef.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evdef.setText("data");

        evspatk.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evspatk.setForeground(new java.awt.Color(51, 51, 51));
        evspatk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evspatk.setText("data");

        evspdef.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evspdef.setForeground(new java.awt.Color(51, 51, 51));
        evspdef.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evspdef.setText("data");

        evspeed.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        evspeed.setForeground(new java.awt.Color(51, 51, 51));
        evspeed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        evspeed.setText("data");

        nature.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        nature.setForeground(new java.awt.Color(51, 51, 51));
        nature.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nature.setText("data");

        shiny.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        shiny.setForeground(new java.awt.Color(51, 51, 51));
        shiny.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        shiny.setText("data");

        pokerus.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        pokerus.setForeground(new java.awt.Color(51, 51, 51));
        pokerus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pokerus.setText("data");

        happiness.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        happiness.setForeground(new java.awt.Color(51, 51, 51));
        happiness.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        happiness.setText("data");

        status.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        status.setForeground(new java.awt.Color(51, 51, 51));
        status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        status.setText("data");

        maxExp.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        maxExp.setForeground(new java.awt.Color(51, 51, 51));
        maxExp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        maxExp.setText("data");

        nextLevel.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        nextLevel.setForeground(new java.awt.Color(51, 51, 51));
        nextLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nextLevel.setText("data");

        exp.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        exp.setForeground(new java.awt.Color(51, 51, 51));
        exp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        exp.setText("data");

        item.setFont(new java.awt.Font("Gulim", 0, 11)); // NOI18N
        item.setForeground(new java.awt.Color(51, 51, 51));
        item.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        item.setText("data");

        javax.swing.GroupLayout StatsPanelLayout = new javax.swing.GroupLayout(StatsPanel);
        StatsPanel.setLayout(StatsPanelLayout);
        StatsPanelLayout.setHorizontalGroup(
            StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ivspeed, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(ivspdef, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(ivspatk, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(ivdef, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(ivatk, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(ivhp, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(speed, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(spdef, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(spatk, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(def, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(atk, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(hp, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(shiny, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nature, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pokerus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(happiness, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StatsPanelLayout.createSequentialGroup()
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(evspeed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(evspdef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(evspatk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(evdef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(evatk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(evhp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(maxExp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(exp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nextLevel, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(item, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        StatsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel11, jLabel12, jLabel13, jLabel14, jLabel15, jLabel16, jLabel17, jLabel18, jLabel19, jLabel2, jLabel20, jLabel21, jLabel22, jLabel23, jLabel24, jLabel25, jLabel26, jLabel27, jLabel3, jLabel30, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9});

        StatsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {atk, def, evatk, evdef, evhp, evspatk, evspdef, evspeed, exp, happiness, hp, item, ivatk, ivdef, ivhp, ivspatk, ivspdef, ivspeed, maxExp, nature, nextLevel, pokerus, shiny, spatk, spdef, speed, status});

        StatsPanelLayout.setVerticalGroup(
            StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addComponent(hp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(atk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(def)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spatk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spdef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(speed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivhp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivatk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivdef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivspatk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivspdef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ivspeed)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(StatsPanelLayout.createSequentialGroup()
                        .addComponent(evhp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evatk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evdef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evspatk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evspdef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evspeed)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(nature))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(shiny))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(pokerus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(happiness))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(maxExp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(nextLevel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(exp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(item))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        StatsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {exp, item});

        StatsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel18, jLabel25, jLabel26});

        StatsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel11, jLabel12, jLabel13, jLabel14, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9});

        NamePanel.setBackground(new java.awt.Color(102, 102, 102));

        Name.setText("Name");

        Level.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Level.setText("Level");

        HPpanel.setBackground(new java.awt.Color(51, 51, 51));
        HPpanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        HealtBar.setBackground(new java.awt.Color(0, 0, 0));
        HealtBar.setForeground(new java.awt.Color(51, 255, 51));
        HealtBar.setValue(30);
        HealtBar.setBorderPainted(false);
        HealtBar.setOpaque(true);
        HealtBar.setPreferredSize(new java.awt.Dimension(150, 15));
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

        Healt.setFont(new java.awt.Font("Tw Cen MT", 0, 11)); // NOI18N
        Healt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Healt.setText("Healt");

        ExpBar.setForeground(new java.awt.Color(0, 153, 255));
        ExpBar.setValue(50);
        ExpBar.setPreferredSize(new java.awt.Dimension(150, 15));
        ExpBar.setString("0");
        ExpBar.setStringPainted(true);

        javax.swing.GroupLayout NamePanelLayout = new javax.swing.GroupLayout(NamePanel);
        NamePanel.setLayout(NamePanelLayout);
        NamePanelLayout.setHorizontalGroup(
            NamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NamePanelLayout.createSequentialGroup()
                .addComponent(Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NamePanelLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(NamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HPpanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Healt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(ExpBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NamePanelLayout.setVerticalGroup(
            NamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NamePanelLayout.createSequentialGroup()
                .addGroup(NamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Healt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(HPpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExpBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        NamePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Level, Name});

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

        New.setText("New");
        New.setOpaque(false);
        New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewActionPerformed(evt);
            }
        });

        Index.setForeground(new java.awt.Color(153, 0, 255));
        Index.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Index.setText("INDEX = 0");

        Refresh.setText("Refresh");
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        GiveExp.setText("Exp");
        GiveExp.setOpaque(false);
        GiveExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GiveExpActionPerformed(evt);
            }
        });

        TextExp.setText("0");

        Previous.setText("← Previous");
        Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousActionPerformed(evt);
            }
        });

        Next.setText("Next →");
        Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout StatsBoardLayout = new javax.swing.GroupLayout(StatsBoard);
        StatsBoard.setLayout(StatsBoardLayout);
        StatsBoardLayout.setHorizontalGroup(
            StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatsBoardLayout.createSequentialGroup()
                .addGroup(StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatsBoardLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(New)
                            .addComponent(Index)
                            .addComponent(Refresh)
                            .addComponent(GiveExp)
                            .addComponent(TextExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(MoveTab, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(StatsBoardLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(NamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StatsBoardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Previous)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Next, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(StatsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        StatsBoardLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {GiveExp, Index, New, Refresh, TextExp});

        StatsBoardLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Next, Previous});

        StatsBoardLayout.setVerticalGroup(
            StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatsBoardLayout.createSequentialGroup()
                .addGroup(StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatsBoardLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(NamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addGroup(StatsBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Previous, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Next, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(StatsBoardLayout.createSequentialGroup()
                        .addComponent(MoveTab, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(New)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Index)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Refresh)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(GiveExp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(StatsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        StatsBoardLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {GiveExp, Index, New, Refresh, TextExp});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatsBoard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StatsBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextActionPerformed
        if (pkmnIndex < self.getParty().getMaxNumber()-1 && pkmnIndex < self.getParty().getSize()-1) {
            try {
                ++pkmnIndex;
                printMove(self.getParty().getPkmn(pkmnIndex));
                printInBattleStats(self.getParty().getPkmn(pkmnIndex));
                printStats(self.getParty().getPkmn(pkmnIndex));
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_NextActionPerformed

    private void PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousActionPerformed
        if (pkmnIndex > 0) {
            try {
                --pkmnIndex;
                printMove(self.getParty().getPkmn(pkmnIndex));
                printInBattleStats(self.getParty().getPkmn(pkmnIndex));
                printStats(self.getParty().getPkmn(pkmnIndex));
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_PreviousActionPerformed

    private void GiveExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GiveExpActionPerformed
        final int exp = Integer.parseInt(TextExp.getText());
        expThread = new Thread() {
            @Override
            public void run() {
                int thisExp, subExp = exp;
                while (subExp > 0) {
                    if (self.getParty().getPkmn(pkmnIndex).getLevel() < 100) {
                        if (subExp > self.getParty().getPkmn(pkmnIndex).getNextLevelExperience()) {
                            thisExp = self.getParty().getPkmn(pkmnIndex).getNextLevelExperience();
                            subExp -= thisExp;
                        } else {
                            thisExp = subExp;
                            subExp = 0;
                        }
                        final Timer t = new Timer(50, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (ExpBar.getValue() >= ExpBar.getValue()+exp || ExpBar.getValue() <= ExpBar.getMinimum()) {
                                    ((Timer) e.getSource()).stop();
                                }
                                ExpBar.setValue(Integer.parseInt(ExpBar.getString())+exp);
                            }
                        });
                        t.start();
                        boolean isLevelUp = self.getParty().getPkmn(pkmnIndex).obtainExp(thisExp);
                        printExpBar(self.getParty().getPkmn(pkmnIndex), isLevelUp);
                        System.out.println("Exp: "+self.getParty().getPkmn(pkmnIndex).getExp()+
                            " This level Exp: "+self.getParty().getPkmn(pkmnIndex).getLevelExperience(self.getParty().getPkmn(pkmnIndex).getLevel())+
                            " Next level Exp: "+self.getParty().getPkmn(pkmnIndex).getNextLevelExperience());
                    } else {
                        subExp = 0;
                    }
                }
                this.interrupt();
            }
        };
        expThread.start();
    }//GEN-LAST:event_GiveExpActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        Refresh.setEnabled(false);
        refreshThread = new Thread() {
            @Override
            public void run() {
                try {
                    boolean isShiny = false;
                    int index = 0;
                    Pokemon gyarados = null;
                    while (isShiny == false) {
                        gyarados = new Pokemon(KANTO, KANTO_MOVE, 130, 100, false, self.getHexID(), self.getOctID());
                        printMove(gyarados);
                        printInBattleStats(gyarados);
                        printStats(gyarados);
                        isShiny = gyarados.getIfShiny();
                        ++index;
                        Index.setText(index+"");
                    }
                    if (gyarados != null) {
                        self.getParty().addPkmnToParty(gyarados);
                    }
                    Refresh.setEnabled(true);
                } catch (IOException ex) {}
            }
        };
        refreshThread.start();
    }//GEN-LAST:event_RefreshActionPerformed

    private void NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewActionPerformed
        try {
            Pokemon gyarados = new Pokemon(KANTO, KANTO_MOVE, 130, 100, false, self.getHexID(), self.getOctID());
            self.getParty().addPkmnToParty(gyarados);
            pkmnIndex = self.getParty().getSize()-1;
            printMove(gyarados);
            printInBattleStats(self.getParty().getPkmn(pkmnIndex));
            printStats(gyarados);
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_NewActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar ExpBar;
    private javax.swing.JButton GiveExp;
    private javax.swing.JPanel HPpanel;
    private javax.swing.JLabel Healt;
    private javax.swing.JProgressBar HealtBar;
    private javax.swing.JPanel ImagePanel;
    private javax.swing.JLabel Index;
    private javax.swing.JLabel Level;
    private javax.swing.JPanel MoveTab;
    private javax.swing.JLabel Name;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JButton New;
    private javax.swing.JButton Next;
    private javax.swing.JLabel PkmnImage;
    private javax.swing.JButton Previous;
    private javax.swing.JToggleButton Refresh;
    private javax.swing.JPanel StatsBoard;
    private javax.swing.JPanel StatsPanel;
    private javax.swing.JTextField TextExp;
    private javax.swing.JLabel atk;
    private javax.swing.JLabel def;
    private javax.swing.JLabel evatk;
    private javax.swing.JLabel evdef;
    private javax.swing.JLabel evhp;
    private javax.swing.JLabel evspatk;
    private javax.swing.JLabel evspdef;
    private javax.swing.JLabel evspeed;
    private javax.swing.JLabel exp;
    private javax.swing.JLabel happiness;
    private javax.swing.JLabel hp;
    private javax.swing.JLabel item;
    private javax.swing.JLabel ivatk;
    private javax.swing.JLabel ivdef;
    private javax.swing.JLabel ivhp;
    private javax.swing.JLabel ivspatk;
    private javax.swing.JLabel ivspdef;
    private javax.swing.JLabel ivspeed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel maxExp;
    private javax.swing.JButton move1;
    private javax.swing.JButton move2;
    private javax.swing.JButton move3;
    private javax.swing.JButton move4;
    private javax.swing.JButton move5;
    private javax.swing.JButton move6;
    private javax.swing.JLabel nature;
    private javax.swing.JLabel nextLevel;
    private javax.swing.JLabel pokerus;
    private javax.swing.JLabel shiny;
    private javax.swing.JLabel spatk;
    private javax.swing.JLabel spdef;
    private javax.swing.JLabel speed;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables
}
