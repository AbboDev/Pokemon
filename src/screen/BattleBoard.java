package screen;

import custom_texture.MoveButton;
import engine.BattleEngine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
    
    public Trainer self;
    public final Trainer enemy = new Trainer();
    private final BattleEngine battleEngine;
    private final Pokemon wild;
    /**
     * Creates new form BattleBoard
     * @param self
     * @param ROOT
     * @param files
     * @throws java.io.IOException
     */    
    public BattleBoard(Trainer self, String ROOT, ArrayList<File> files) throws IOException {
        initComponents();
        makeUI();
        this.ROOT = ROOT;
        setPath(files);
        this.self = self;
        
        wild = new Pokemon(KANTO, KANTO_MOVE, 129, 102, true, null, null);
        battleEngine = new BattleEngine(2, null, null);
        
        refresh();
        
        jLabel1.setText(ROOT + " " + KANTO.getPath()+ " " + KANTO_MOVE.getName() );
    }
    private void setPath(ArrayList<File> files) {
        SPRITE = ROOT + "\\sprite\\";
        for (File thisFile: files) {
            switch (thisFile.getName()) {
                case "kanto.csv": KANTO = thisFile;
                    System.out.println(thisFile.getName());
                    jLabel1.setText(thisFile.getName());
                    break;
                case "kantoMove.csv": KANTO_MOVE = thisFile;
                    break;
                default:
                    break;
            }
//            KANTO = new File(ROOT + "src\\pokemon\\kanto.csv");
//            KANTO_MOVE = new File(ROOT + "src\\pokemon\\kantoMove.csv");
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
            printMove(this.self.getParty().getPkmn(CURRENT_PKMN));
            printInBattleStats(this.self.getParty().getPkmn(CURRENT_PKMN));
            printEnemyInBattleStats(wild);
            printParty(this.self);
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
            public void actionPerformed(ActionEvent e) {
                if (pkmn.getStatus() != Pokemon.Status.KO) {
                    if (pkmn.getMoveSet().get(index).getPP() > 0) {
                        System.out.println("Press "+move.getName());
                        pkmn.getMoveSet().get(index).decreasePP(false, 1);
                        ((JButton) e.getSource()).setText(printMoveText(move));
                        battleEngine.round(self.getParty().getPkmn(CURRENT_PKMN), move, wild, wild.getRandomMove(false,1));
                        printHPBar(pkmn, true, HealtBar, Healt);
                        printHPBar(wild, true, eHealtBar, eHealt);
                        if (pkmn.getStatus() == Pokemon.Status.KO) {
                            BattleTab.setSelectedIndex(1);
                            BattleTab.setEnabledAt(0, false);
                            printParty(self);
                        }
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
    
    private void removeParty() {
        PartyPanel.removeAll();
    }
    private void printParty(Trainer trn) {
        boolean close = true;
        final ArrayList<Pokemon> party = trn.getParty().getArrayParty();
        removeParty();
        try {
            for (int i = 0; i < party.size(); ++i) {
                printPartyButton(party.get(i), new JButton(), i);
                if (party.get(i).getStatus() != Pokemon.Status.KO) {
                    close = false;
                }
            }
        } catch (Exception e) {
        }
        if (close == true) {
            this.setVisible(false);
        }
        PartyPanel.revalidate();
        PartyPanel.repaint();        
    }
    private void printPartyButton(final Pokemon pkmn, JButton button, final int index) {
        String sex;
        if (pkmn.getIfMale()) { sex = "♂"; } else { sex = "♀"; }
        button = new JButton(pkmn.getName()+" "+sex);
        if (pkmn.getStatus() == Pokemon.Status.KO) {
            button.setBackground(Color.red);
            button.setEnabled(false);
        } else {
            button.setBackground(Color.blue);
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int i = PartyPanel.getComponentZOrder(((JButton) e.getSource()));
                    battleEngine.switchPkmn(self, self.getParty().getPkmn(CURRENT_PKMN), self.getParty().getPkmn(i));
                    printImage(self.getParty().getPkmn(CURRENT_PKMN), PkmnImage);
                    printMove(self.getParty().getPkmn(CURRENT_PKMN));
                    printInBattleStats(self.getParty().getPkmn(CURRENT_PKMN));
                    BattleTab.setEnabledAt(0, true);
                    BattleTab.setSelectedIndex(0);
                    printParty(self);
                } catch (IOException ex) {
                }
            }            
        });
        button.setPreferredSize(new Dimension(160, 60));
        if (self.getParty().getPkmn(CURRENT_PKMN) == pkmn) {
            button.setEnabled(false);
        }
        PartyPanel.add(button);
    }
    
    private void printInBattleStats(Pokemon pkmn) throws IOException {
        Name.setText(pkmn.getName());
        printLevel(pkmn, Level);
        HealtBar.setMinimum(0);
        printHPBar(pkmn, true, HealtBar, Healt);
        printExpBar(pkmn, true);
        printImage(pkmn, PkmnImage);
    }
    private void printEnemyInBattleStats(Pokemon pkmn) throws IOException {
        eName.setText(pkmn.getName());
        printLevel(pkmn, eLevel);
        eHealtBar.setMinimum(0);
        printHPBar(pkmn, true, eHealtBar, eHealt);
        printImage(pkmn, ePkmnImage);
    }
    
    private void printExpBar(Pokemon pkmn, boolean change) {
        if (change) {
            ExpBar.setMinimum(pkmn.getLevelExperience(pkmn.getLevel()));
            ExpBar.setMaximum(pkmn.getLevelExperience(pkmn.getLevel()+1));
            printLevel(pkmn, Level);
            printHPBar(pkmn, change, HealtBar, Healt);
        }
        ExpBar.setValue(pkmn.getExp());
        ExpBar.repaint();
    }
    private void printHPBar(Pokemon pkmn, boolean change, JProgressBar jPrBar, JLabel jLbl) {
        if (change) {
            jPrBar.setMaximum(pkmn.getMaxHP());
        }
        jPrBar.setValue(pkmn.getHP());
        jLbl.setText(pkmn.getHP()+"/"+pkmn.getMaxHP());
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
            String image = ("D:\\Project\\SPRITE\\xy-animated-shiny\\001.gif");
            System.out.println(image);
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(image).getImage());
            imageIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
            label.setIcon(imageIcon);
//            label.setIcon(pkmn.getImage(SPRITE));
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

        BackgroundLabel = new javax.swing.JLabel();
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

        setBackground(new java.awt.Color(51, 51, 51));
        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        BackgroundLabel.setBackground(new java.awt.Color(255, 102, 102));
        BackgroundLabel.setMaximumSize(new java.awt.Dimension(980, 578));
        BackgroundLabel.setMinimumSize(new java.awt.Dimension(980, 578));
        BackgroundLabel.setOpaque(true);
        BackgroundLabel.setPreferredSize(new java.awt.Dimension(980, 578));

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
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        OptionPanelLayout.setVerticalGroup(
            OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
            .addGroup(OptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(OptionPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(BackgroundLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BattleBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(BackgroundLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BackgroundLabel;
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
