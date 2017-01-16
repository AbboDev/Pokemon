package custom_texture;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import objects.Move;
import objects.Pokemon;
import objects.Trainer;

/**
 * @author Thomas
 */
public class BattleTab extends ExpandPanel {
    private static final Color UNSELECT_COLOR = new Color(153, 153, 153);
    private static final Color SELECT_COLOR = new Color(91, 91, 91);
    private static final int MAX_TAB = 3;
    
    private int DIM = 1;
    
    private int currentTab;
    private int movesPos;
    private int partyPos;
    
    private boolean blockMoves;
    private boolean blockParty;
    private boolean blockBag;
    private boolean blockOption;
    
    private Trainer trainer;
    private Pokemon pokemon;

    /**
     * Creates new form BattleTab
     * @param trn
     * @param pkmn
     * @param tab
     * @param mult
     */
    public BattleTab(Trainer trn, Pokemon pkmn, int tab, int mult) {
        trainer = trn;
        pokemon = pkmn;
        DIM = mult;
        currentTab = tab;
        movesPos = 0;
        partyPos = 0;
        blockBag = false;
        blockMoves = false;
        blockParty = false;
        blockOption = false;
        initTab();
    }
    public BattleTab(BattleTab bt, int mult) {
        trainer = bt.getTrainer();
        pokemon = bt.getPokemon();
        DIM = mult;
        currentTab = bt.getCurrentTab();
        movesPos = bt.getMovesPos();
        partyPos = bt.getPartyPos();
        blockBag = bt.isBlockBag();
        blockMoves = bt.isBlockMoves();
        blockParty = bt.isBlockParty();
        blockOption = bt.isBlockOption();
        initTab();
    }
    private void initTab() {
        initComponents();
        MovesScroll.getViewport().setOpaque(false);
        PartyScroll.getViewport().setOpaque(false);
        BagScroll.getViewport().setOpaque(false);
        OptionScroll.getViewport().setOpaque(false);
        
        CardLayout cl = (CardLayout) (ScrollPanel.getLayout());
        if (currentTab == 0) {
            cl.first(ScrollPanel);
        } else {
            rapidShowTab(currentTab);
        }
        selectTab();
        
        printMoves();
        printParty();
        expandComponent(DIM);
        
        setScrollBarPosition(MovesScroll, movesPos);
        setScrollBarPosition(PartyScroll, partyPos);
    }
    
    public void rapidShowTab(int tab) {
        CardLayout cl = (CardLayout) (ScrollPanel.getLayout());
        boolean block = false;
        switch (tab) {
            case 0: block = blockMoves; break;
            case 1: block = blockParty; break;
            case 2: block = blockBag; break;
            case 3: block = blockOption; break;
            default: break;
        }
        if (!block) {
            cl.show(ScrollPanel, ScrollPanel.getComponent(tab).getName());
        }
        currentTab = tab;
        selectTab();
    }
    
    public final void showTab(int tab, boolean right) {
        checkBlock(tab, right);
        selectTab();
    }
    
    private void checkBlock(int firstTab, boolean right) {
        CardLayout cl = (CardLayout) (ScrollPanel.getLayout());
        if (right) {
            if (currentTab < MAX_TAB) {
                ++currentTab;
            } else {
                currentTab = 0;
            }
            cl.next(ScrollPanel);
        } else {
            if (currentTab > 0) {
                --currentTab;
            } else {
                currentTab = MAX_TAB;
            }
            cl.previous(ScrollPanel);
        }
        boolean check = false;
        if ((currentTab == 0 && !blockMoves) || (currentTab == 1 && !blockParty)
                || (currentTab == 2 && !blockBag) || (currentTab == 3 && !blockOption)) {
            check = true;
        }
        
        if (!check) {
            checkBlock(firstTab, right);
        }
    }
    
    private void selectTab() {
        for (int i = 0; i < NamePanel.getComponentCount(); ++i) {
            JLabel label = (JLabel) NamePanel.getComponent(i);
            if (i == currentTab) {
                label.setBackground(SELECT_COLOR);
            } else {
                label.setBackground(UNSELECT_COLOR);
            }
        }
    }
    
    public void printCursor(JPanel container, int index, JScrollPane scroll) {
        String name = "cursor";
        boolean light = false;
        int k = 5;
        for (Component comp: container.getComponents()) {
            if (comp.getName().equals(name)) {
                CursorPanel c = (CursorPanel) comp;
                light = c.getHighlight();
                container.remove(comp);
            }
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, k, 0, 0);
        CursorPanel cursor = new CursorPanel(light, DIM);
        cursor.setName(name);
        container.add(cursor, gbc, index);
        
        scroll.getHorizontalScrollBar().setValue((index*(160*DIM))-(cursor.getWidth()*DIM)+((k)*index));
        scroll.repaint();
        scroll.revalidate();
    }
    
    public void moveCursor(JPanel tab, boolean right) {
        JScrollPane scroll = (JScrollPane) tab.getParent().getParent();
        int pos;
        if (tab == MovesTab){
            pos = movesPos;
        } else if (tab == PartyTab){
            pos = partyPos;
        } else {
            pos = movesPos;
        }
        if (tab.getComponentCount() > 2) {
            if (right) {
                if (pos < tab.getComponentCount()-2) {
                    ++pos;
                } else {
                    pos = 0;
                }
            } else {
                if (pos > 0) {
                    --pos;
                } else {
                    pos = tab.getComponentCount()-2;
                }
            }
        } else if (tab.getComponentCount() == 2) {
            if (pos == 0) {
                pos = 1;
            } else {
                pos = 0;
            }
        }
        if (tab == MovesTab){
            movesPos = pos;
        } else if (tab == PartyTab){
            partyPos = pos;
        }
        printCursor(tab, pos, scroll);
    }
    
    public JScrollPane getMovesScroll() {
        return MovesScroll;
    }
    public JPanel getMovesTab() {
        return MovesTab;
    }
    
    public JScrollPane getPartyScroll() {
        return PartyScroll;
    }
    public JPanel getPartyTab() {
        return PartyTab;
    }
    
    public JScrollPane getBagScroll() {
        return BagScroll;
    }
    public JPanel getBagTab() {
        return BagTab;
    }
    
    public JScrollPane getOptionScroll() {
        return OptionScroll;
    }
    public JPanel getOptionTab() {
        return OptionTab;
    }

    public int getCurrentTab() {
        return currentTab;
    }
    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }
    public int getMovesPos() {
        return movesPos;
    }
    public void setMovesPos(int movesPos) {
        this.movesPos = movesPos;
    }
    public int getPartyPos() {
        return partyPos;
    }
    public void setPartyPos(int partyPos) {
        this.partyPos = partyPos;
    }
    
    public boolean isBlockMoves() {
        return blockMoves;
    }
    public void setBlockMoves(boolean blockMoves) {
        this.blockMoves = blockMoves;
    }
    public boolean isBlockParty() {
        return blockParty;
    }
    public void setBlockParty(boolean blockParty) {
        this.blockParty = blockParty;
    }
    public boolean isBlockBag() {
        return blockBag;
    }
    public void setBlockBag(boolean blockBag) {
        this.blockBag = blockBag;
    }
    public boolean isBlockOption() {
        return blockOption;
    }
    public void setBlockOption(boolean blockOption) {
        this.blockOption = blockOption;
    }

    public Trainer getTrainer() {
        return trainer;
    }
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
    public Pokemon getPokemon() {
        return pokemon;
    }
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
    
    public final void setScrollBarPosition(JScrollPane scroll, int pos) {
        scroll.getHorizontalScrollBar().setValue(
                (pos*(160*DIM))-(CursorPanel.SIZE*DIM)+((5*DIM)*pos));
    }
    
/******************************************************************************/
    //These methods populate the MovePanel
    private void clearMovesTab() {
        MovesTab.removeAll();
    }
    public final void printMoves() {
        final ArrayList<Move> moveSet = pokemon.getMoveSet();
        ArrayList<JPanel> buttonSet = new ArrayList<>();
        int k = 5;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, k, 0, 0);
        clearMovesTab();
        try {
            for (int i = 0; i < moveSet.size(); ++i) {
                buttonSet.add(printMovesButton(new JPanel(), moveSet.get(i)));
            }
        } catch (Exception e) {
        }
        for (JPanel button : buttonSet) {
            if (buttonSet.indexOf(button) == buttonSet.size()-1) {
                gbc.insets = new Insets(0, k, 0, k);
            }
            MovesTab.add(button, gbc);
        }
        printCursor(MovesTab, movesPos, MovesScroll);
    }
    private JPanel printMovesButton(JPanel button, final Move move) {
        button = new MovePanel(move, DIM);
        button.setName(move.getName());
        return button;
    }
    
/******************************************************************************/  
    //These methods populate the PartyPanel
    private void clearPartyTab() {
        PartyTab.removeAll();
    }
    public final void printParty() {
//        boolean close = true;
        final ArrayList<Pokemon> party = trainer.getParty().getArrayParty();
        ArrayList<JPanel> buttonSet = new ArrayList<>();
        int k = 5;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, k, 0, 0);
        clearPartyTab();
        try {
            for (int i = 0; i < party.size(); ++i) {
//                if (party.get(i).getStatus() != Pokemon.Status.KO) {
//                    close = false;
//                }
                buttonSet.add(printPartyButton(party.get(i), new JPanel(), i));
            }
        } catch (Exception e) {
        }
//        battleEnd(close);
        for (JPanel button : buttonSet) {
            if (buttonSet.indexOf(button) == buttonSet.size()-1) {
                gbc.insets = new Insets(0, k, 0, k);
            }
            PartyTab.add(button, gbc);
        }
        printCursor(PartyTab, partyPos, PartyScroll);
    }
    private JPanel printPartyButton(final Pokemon pkmn, JPanel button, int index) {
        button = new PkmnPartyPanel(pkmn, DIM);
        if (pkmn.getStatus() == Pokemon.Status.KO) {
            button.setEnabled(false);
        }
        button.setName(pkmn.getName()+index);
        return button;
    }
    public void printModifiedPkmn(Pokemon pokemon) {
        for (int i = 0; i < PartyTab.getComponents().length; ++i) {
            try {
                PkmnPartyPanel comp = (PkmnPartyPanel) PartyTab.getComponent(i);
                if (comp.getPokemon().equals(pokemon)) {
                    comp.refreshAll(DIM);
                }
            } catch (Exception e) {}
        }
    }
    
/******************************************************************************/  
    //These methods populate the TextTab
    /**
     * 
     * @param text 
     */
    public void echo(String text) {
        TextLbl.setText("");
        TextLbl.setText("<html>");
        char[] singleLetter = text.toCharArray();
        for (int i = 0; i < text.length(); ++i) {
            try {
                TextLbl.setText(TextLbl.getText()+""+singleLetter[i]);
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    public void stopEcho() {
        CardLayout cl = (CardLayout) (getLayout());
        cl.show(this, "ChooseTab");
        TextLbl.setText("");
    }
    
    public void startEcho() {
        CardLayout cl = (CardLayout) (getLayout());
        cl.show(this, "TextTab");
        TextLbl.setFont(new java.awt.Font("Trebuchet MS", 0, 24*DIM));
        TextLbl.setText("");
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

        ChooseTab = new javax.swing.JPanel();
        NamePanel = new javax.swing.JPanel();
        MovesLabel = new javax.swing.JLabel();
        PartyLabel = new javax.swing.JLabel();
        BagLabel = new javax.swing.JLabel();
        OptionLabel = new javax.swing.JLabel();
        ScrollPanel = new javax.swing.JPanel();
        MovesScroll = new javax.swing.JScrollPane(MovesTab);
        MovesTab = new javax.swing.JPanel();
        PartyScroll = new javax.swing.JScrollPane();
        PartyTab = new javax.swing.JPanel();
        BagScroll = new javax.swing.JScrollPane();
        BagTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        OptionScroll = new javax.swing.JScrollPane();
        OptionTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TextTab = new javax.swing.JPanel();
        TextLbl = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setMaximumSize(new java.awt.Dimension(1024, 200));
        setMinimumSize(new java.awt.Dimension(512, 100));
        setPreferredSize(new java.awt.Dimension(512, 100));
        setLayout(new java.awt.CardLayout());

        ChooseTab.setOpaque(false);
        ChooseTab.setLayout(new java.awt.GridBagLayout());

        NamePanel.setMaximumSize(new java.awt.Dimension(1024, 40));
        NamePanel.setMinimumSize(new java.awt.Dimension(512, 20));
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(512, 20));
        NamePanel.setLayout(new java.awt.GridLayout(1, 0));

        MovesLabel.setBackground(new java.awt.Color(153, 153, 153));
        MovesLabel.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11)); // NOI18N
        MovesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MovesLabel.setText("MOVES");
        MovesLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MovesLabel.setMaximumSize(new java.awt.Dimension(256, 40));
        MovesLabel.setMinimumSize(new java.awt.Dimension(128, 20));
        MovesLabel.setOpaque(true);
        MovesLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        NamePanel.add(MovesLabel);

        PartyLabel.setBackground(new java.awt.Color(153, 153, 153));
        PartyLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        PartyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PartyLabel.setText("PARTY");
        PartyLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PartyLabel.setMaximumSize(new java.awt.Dimension(256, 40));
        PartyLabel.setMinimumSize(new java.awt.Dimension(128, 20));
        PartyLabel.setOpaque(true);
        PartyLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        NamePanel.add(PartyLabel);

        BagLabel.setBackground(new java.awt.Color(153, 153, 153));
        BagLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        BagLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BagLabel.setText("BAG");
        BagLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BagLabel.setMaximumSize(new java.awt.Dimension(256, 40));
        BagLabel.setMinimumSize(new java.awt.Dimension(128, 20));
        BagLabel.setOpaque(true);
        BagLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        NamePanel.add(BagLabel);

        OptionLabel.setBackground(new java.awt.Color(153, 153, 153));
        OptionLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        OptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        OptionLabel.setText("OPTION");
        OptionLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        OptionLabel.setMaximumSize(new java.awt.Dimension(256, 40));
        OptionLabel.setMinimumSize(new java.awt.Dimension(128, 20));
        OptionLabel.setOpaque(true);
        OptionLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        NamePanel.add(OptionLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        ChooseTab.add(NamePanel, gridBagConstraints);

        ScrollPanel.setMaximumSize(new java.awt.Dimension(1024, 160));
        ScrollPanel.setMinimumSize(new java.awt.Dimension(512, 80));
        ScrollPanel.setOpaque(false);
        ScrollPanel.setPreferredSize(new java.awt.Dimension(512, 80));
        ScrollPanel.setLayout(new java.awt.CardLayout());

        MovesScroll.setBorder(null);
        MovesScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        MovesScroll.setToolTipText("");
        MovesScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        MovesScroll.setAutoscrolls(true);
        MovesScroll.setMaximumSize(null);
        MovesScroll.setMinimumSize(null);
        MovesScroll.setName("MovesScroll"); // NOI18N
        MovesScroll.setOpaque(false);
        MovesScroll.setPreferredSize(null);

        MovesTab.setOpaque(false);
        MovesTab.setLayout(new java.awt.GridBagLayout());
        MovesScroll.setViewportView(MovesTab);

        ScrollPanel.add(MovesScroll, "MovesScroll");

        PartyScroll.setBorder(null);
        PartyScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PartyScroll.setToolTipText("");
        PartyScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        PartyScroll.setAutoscrolls(true);
        PartyScroll.setMaximumSize(null);
        PartyScroll.setMinimumSize(null);
        PartyScroll.setName("PartyScroll"); // NOI18N
        PartyScroll.setOpaque(false);
        PartyScroll.setPreferredSize(null);

        PartyTab.setOpaque(false);
        PartyTab.setLayout(new java.awt.GridBagLayout());
        PartyScroll.setViewportView(PartyTab);

        ScrollPanel.add(PartyScroll, "PartyScroll");

        BagScroll.setBorder(null);
        BagScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        BagScroll.setToolTipText("");
        BagScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        BagScroll.setAutoscrolls(true);
        BagScroll.setMaximumSize(null);
        BagScroll.setMinimumSize(null);
        BagScroll.setName("BagScroll"); // NOI18N
        BagScroll.setOpaque(false);

        BagTab.setMaximumSize(null);
        BagTab.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel1.setText("WORK IN PROGRESS...");
        BagTab.add(jLabel1);

        BagScroll.setViewportView(BagTab);

        ScrollPanel.add(BagScroll, "BagScroll");

        OptionScroll.setBorder(null);
        OptionScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        OptionScroll.setToolTipText("");
        OptionScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        OptionScroll.setAutoscrolls(true);
        OptionScroll.setMaximumSize(null);
        OptionScroll.setMinimumSize(null);
        OptionScroll.setName("OptionScroll"); // NOI18N
        OptionScroll.setOpaque(false);

        OptionTab.setMaximumSize(null);
        OptionTab.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel2.setText("WORK IN PROGRESS...");
        OptionTab.add(jLabel2);

        OptionScroll.setViewportView(OptionTab);

        ScrollPanel.add(OptionScroll, "OptionScroll");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        ChooseTab.add(ScrollPanel, gridBagConstraints);

        add(ChooseTab, "ChooseTab");

        TextTab.setBackground(new java.awt.Color(153, 153, 153));
        TextTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TextTab.setOpaque(false);
        TextTab.setLayout(new java.awt.BorderLayout());

        TextLbl.setBackground(new java.awt.Color(51, 51, 51));
        TextLbl.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        TextLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        TextLbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        TextTab.add(TextLbl, java.awt.BorderLayout.CENTER);

        add(TextTab, "TextTab");
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BagLabel;
    private javax.swing.JScrollPane BagScroll;
    private javax.swing.JPanel BagTab;
    private javax.swing.JPanel ChooseTab;
    private javax.swing.JLabel MovesLabel;
    private javax.swing.JScrollPane MovesScroll;
    private javax.swing.JPanel MovesTab;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JLabel OptionLabel;
    private javax.swing.JScrollPane OptionScroll;
    private javax.swing.JPanel OptionTab;
    private javax.swing.JLabel PartyLabel;
    private javax.swing.JScrollPane PartyScroll;
    private javax.swing.JPanel PartyTab;
    private javax.swing.JPanel ScrollPanel;
    private javax.swing.JLabel TextLbl;
    private javax.swing.JPanel TextTab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
