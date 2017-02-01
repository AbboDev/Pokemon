package custom_texture;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import objects.Move;
import objects.Move.TypeOfAttacks;
import objects.Pokemon.Type;
import screen.Board;

/**
 * @author Thomas
 */
public class MovePanel extends GradientPanel {
    private static final long serialVersionUID = -1853805251000791772L;
    private static final String IMAGE = "/resources/image/";
//    private static final int ICON_WIDTH = 45;
//    private static final int ICON_HEIGTH = 20;
    private static final int ICON_WIDTH = 70;
    private static final int ICON_HEIGTH = 25;
    
    private final Move move;

    /**
     * Creates new form PokemonPanel
     * @param move
     */
    public MovePanel(Move move) {
        initComponents();
        int borderx = (int) (10*Board.xDIM);
        Border padding = BorderFactory.createEmptyBorder(0, borderx, 0, 0);
        NamePanel.setBorder(padding);
        
        int bordery = (int) (10*Board.yDIM);
        padding = BorderFactory.createEmptyBorder(bordery, borderx, bordery, borderx);
        Type.setBorder(padding);
        Stat.setBorder(padding);
        expandComponent();
        
        this.move = move;
        
        setTextColor();
        name.setText(move.getName());
        changePP();
        changeStatus(move);
    }
    
    public final void changePP() {
        PP.setText(move.getPP() + "/" + move.getMaxPP());
        float movePP = move.getPP();
        float moveMaxPP = move.getMaxPP();
        float diffPP = movePP/moveMaxPP;
        if (movePP == 0) {
            PP.setForeground(Color.gray);
            name.setForeground(Color.gray);
        } else if (diffPP > 0.5 && diffPP <= 1) {
            PP.setForeground(Color.black);
            name.setForeground(Color.black);
        } else if (diffPP > 0.25 && diffPP <= 0.5) {
            PP.setForeground(Color.yellow);
            name.setForeground(Color.yellow);
        } else if (diffPP > 0.125 && diffPP <= 0.25) {
            PP.setForeground(Color.orange);
            name.setForeground(Color.orange);
        } else if (diffPP < 0.125) {
            PP.setForeground(Color.red);
            name.setForeground(Color.red);
        }
    }
    
    private void changeStatus(Move move) {
        Type typeMove = move.getMoveType();
        switch (typeMove) {
            case Normal:
                back1 = (new Color(232, 232, 216));
                back2 = (new Color(184, 184, 168));
                border1 = (new Color(152, 144, 120));
                border2 = (new Color(152, 144, 120));
                break;
            case Fighting:
                back1 = (new Color(248, 168, 168));
                back2 = (new Color(248, 112, 112));
                border1 = (new Color(208, 64, 64));
                border2 = (new Color(208, 64, 64));
                break;
            case Flying:
                back1 = (new Color(88, 200, 240));
                back2 = (new Color(184, 184, 168));
                border1 = (new Color(48, 160, 184));
                border2 = (new Color(152, 144, 120));
                break;
            case Rock:
                back1 = (new Color(216, 200, 244));
                back2 = (new Color(200, 160, 72));
                border1 = (new Color(144, 120, 56));
                border2 = (new Color(144, 120, 56));
                break;
            case Ground:
                back1 = (new Color(224, 224, 0));
                back2 = (new Color(200, 160, 72));
                border1 = (new Color(144, 120, 56));
                border2 = (new Color(144, 120, 56));
                break;
            case Steel:
                back1 = (new Color(232, 232, 232));
                back2 = (new Color(184, 184, 208));
                border1 = (new Color(144, 144, 176));
                border2 = (new Color(144, 144, 176));
                break;
            case Ghost:
                back1 = (new Color(208, 176, 248));
                back2 = (new Color(168, 112, 248));
                border1 = (new Color(120, 88, 176));
                border2 = (new Color(120, 88, 176));
                break;
            case Dark:
                back1 = (new Color(184, 176, 168));
                back2 = (new Color(144, 136, 136));
                border1 = (new Color(104, 104, 104));
                border2 = (new Color(104, 104, 104));
                break;
            case Psychic:
                back1 = (new Color(248, 152, 216));
                back2 = (new Color(248, 88, 136));
                border1 = (new Color(192, 40, 136));
                border2 = (new Color(192, 40, 136));
                break;
            case Bug:
                back1 = (new Color(216, 224, 200));
                back2 = (new Color(160, 200, 136));
                border1 = (new Color(120, 152, 80));
                border2 = (new Color(120, 152, 80));
                break;
            case Poison:
                back1 = (new Color(240, 208, 248));
                back2 = (new Color(224, 144, 248));
                border1 = (new Color(184, 72, 224));
                border2 = (new Color(184, 72, 224));
                break;
            case Grass:
                back1 = (new Color(216, 240, 192));
                back2 = (new Color(144, 232, 128));
                border1 = (new Color(88, 184, 72));
                border2 = (new Color(88, 184, 72));
                break;
            case Water:
                back1 = (new Color(200, 216, 248));
                back2 = (new Color(104, 152, 248));
                border1 = (new Color(48, 112, 192));
                border2 = (new Color(48, 112, 192));
                break;
            case Ice:
                back1 = (new Color(192, 248, 248));
                back2 = (new Color(48, 216, 208));
                border1 = (new Color(56, 152, 136));
                border2 = (new Color(56, 152, 136));
                break;
            case Fire:
                back1 = (new Color(248, 192, 144));
                back2 = (new Color(248, 144, 48));
                border1 = (new Color(192, 104, 32));
                border2 = (new Color(192, 104, 32));
                break;
            case Electric:
                back1 = (new Color(248, 240, 208));
                back2 = (new Color(224, 224, 0));
                border1 = (new Color(176, 168, 16));
                border2 = (new Color(176, 168, 16));
                break;
            case Fairy:
                back1 = (new Color(255, 197, 255));
                back2 = (new Color(255, 101, 213));
                border1 = (new Color(237, 85, 181));
                border2 = (new Color(237, 85, 181));
                break;
            case Dragon:
                back1 = (new Color(104, 152, 248));
                back2 = (new Color(248, 112, 112));
                border1 = (new Color(48, 112, 192));
                border2 = (new Color(208, 64, 64));
                break;
            case Dinosaur:
                back1 = (new Color(127, 51, 0));
                back2 = (new Color(0, 0, 0));
                break;
            case Light:
                back1 = (new Color(255, 236, 142));
                back2 = (new Color(255, 255, 255));
                break;
            default:
                back1 = (new Color(0, 0, 0));
                back2 = (new Color(100, 100, 100));
                border1 = (new Color(255, 255, 255));
                border2 = (new Color(155, 155, 155));
                break;
        }
        TypeOfAttacks typeOfAttack = move.getType();
        
        int width = (int) (ICON_WIDTH*(Board.xDIM)); int height = (int) (ICON_HEIGTH*(Board.yDIM));
        ImageIcon imageIcon = ImageConverter.getImage(Board.ROOT+IMAGE+typeOfAttack.name()+".png");
        Type.setIcon(ImageConverter.getScaledImage(imageIcon, width, height));
        
        imageIcon = ImageConverter.getImage(Board.ROOT+IMAGE+typeMove.name()+".png");
        Stat.setIcon(ImageConverter.getScaledImage(imageIcon, width, height));
        
        int borderx = (int) (BORDER*(Board.xDIM));
        int bordery = (int) (BORDER*(Board.yDIM));
        this.setBorder(new GradientBorder(borderx, bordery, border1, border2));
    }

    private void setTextColor() {
        name.setForeground(Color.black);
        PP.setForeground(Color.black);
    }
    
    public JLabel getPP() {
        return PP;
    }
    public JLabel getMoveName() {
        return name;
    }
    public JLabel getStat() {
        return Stat;
    }
    public JLabel getType() {
        return Type;
    }
    public Move getMove() {
        return move;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        NamePanel = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        PP = new javax.swing.JLabel();
        TypePanel = new javax.swing.JPanel();
        Stat = new javax.swing.JLabel();
        Type = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(80, 30));
        setName(""); // NOI18N
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(160, 60));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridBagLayout());

        NamePanel.setMinimumSize(new java.awt.Dimension(120, 30));
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(120, 30));
        NamePanel.setLayout(new java.awt.BorderLayout());

        name.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        name.setText("NAME");
        name.setToolTipText("");
        name.setDoubleBuffered(true);
        name.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        name.setMaximumSize(null);
        name.setMinimumSize(new java.awt.Dimension(110, 30));
        name.setPreferredSize(new java.awt.Dimension(110, 30));
        NamePanel.add(name, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(NamePanel, gridBagConstraints);

        jPanel1.setMinimumSize(new java.awt.Dimension(40, 30));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(40, 30));
        jPanel1.setLayout(new java.awt.BorderLayout());

        PP.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        PP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PP.setText("PP/MAX");
        PP.setToolTipText("");
        PP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PP.setInheritsPopupMenu(false);
        PP.setMaximumSize(null);
        PP.setMinimumSize(new java.awt.Dimension(40, 30));
        PP.setName(""); // NOI18N
        PP.setPreferredSize(new java.awt.Dimension(40, 30));
        jPanel1.add(PP, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        TypePanel.setOpaque(false);
        TypePanel.setLayout(new java.awt.GridLayout(1, 0));

        Stat.setBackground(new java.awt.Color(51, 255, 51));
        Stat.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Stat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Stat.setToolTipText("");
        Stat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Stat.setMaximumSize(null);
        Stat.setMinimumSize(new java.awt.Dimension(80, 30));
        Stat.setPreferredSize(new java.awt.Dimension(80, 30));
        TypePanel.add(Stat);

        Type.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Type.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Type.setMaximumSize(null);
        Type.setMinimumSize(new java.awt.Dimension(80, 30));
        Type.setPreferredSize(new java.awt.Dimension(80, 30));
        TypePanel.add(Type);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(TypePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NamePanel;
    private javax.swing.JLabel PP;
    private javax.swing.JLabel Stat;
    private javax.swing.JLabel Type;
    private javax.swing.JPanel TypePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}