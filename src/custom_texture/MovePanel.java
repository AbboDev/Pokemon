package custom_texture;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
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
public class MovePanel extends ExpandPanel {
    private static final long serialVersionUID = -1853805251000791772L;
    private static final String IMAGE = "/resources/image/";
    private static final int BORDER = 2;
    private static final int ICON_WIDTH = 45;
    private static final int ICON_HEIGTH = 20;
    
    private final Move move;
    private Color original1, original2;
    private Color color1, color2;
    private Color border1, border2;
    private MouseAdapter ma;

    /**
     * Creates new form PokemonPanel
     * @param move
     * @param mult
     */
    public MovePanel(Move move, int mult) {
        initComponents();
        expandComponent(mult);

        this.move = move;

        setTextColor();
        refreshAll(mult);
    }

    private void refreshAll(int mult) {
        name.setText(move.getName());
        changePP();

        changeStatus(move, mult);
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
    
    private void changeStatus(Move move, int mult) {
        Type typeMove = move.getMoveType();
        switch (typeMove) {
            case Normal:
                original1 = (new Color(232, 232, 216));
                original2 = (new Color(184, 184, 168));
                border1 = (new Color(152, 144, 120));
                border2 = (new Color(152, 144, 120));
                break;
            case Fighting:
                original1 = (new Color(248, 168, 168));
                original2 = (new Color(248, 112, 112));
                border1 = (new Color(208, 64, 64));
                border2 = (new Color(208, 64, 64));
                break;
            case Flying:
                original1 = (new Color(88, 200, 240));
                original2 = (new Color(184, 184, 168));
                border1 = (new Color(48, 160, 184));
                border2 = (new Color(152, 144, 120));
                break;
            case Rock:
                original1 = (new Color(216, 200, 244));
                original2 = (new Color(200, 160, 72));
                border1 = (new Color(144, 120, 56));
                border2 = (new Color(144, 120, 56));
                break;
            case Ground:
                original1 = (new Color(224, 224, 0));
                original2 = (new Color(200, 160, 72));
                border1 = (new Color(144, 120, 56));
                border2 = (new Color(144, 120, 56));
                break;
            case Steel:
                original1 = (new Color(232, 232, 232));
                original2 = (new Color(184, 184, 208));
                border1 = (new Color(144, 144, 176));
                border2 = (new Color(144, 144, 176));
                break;
            case Ghost:
                original1 = (new Color(208, 176, 248));
                original2 = (new Color(168, 112, 248));
                border1 = (new Color(120, 88, 176));
                border2 = (new Color(120, 88, 176));
                break;
            case Dark:
                original1 = (new Color(184, 176, 168));
                original2 = (new Color(144, 136, 136));
                border1 = (new Color(104, 104, 104));
                border2 = (new Color(104, 104, 104));
                break;
            case Psychic:
                original1 = (new Color(248, 152, 216));
                original2 = (new Color(248, 88, 136));
                border1 = (new Color(192, 40, 136));
                border2 = (new Color(192, 40, 136));
                break;
            case Bug:
                original1 = (new Color(216, 224, 200));
                original2 = (new Color(160, 200, 136));
                border1 = (new Color(120, 152, 80));
                border2 = (new Color(120, 152, 80));
                break;
            case Poison:
                original1 = (new Color(240, 208, 248));
                original2 = (new Color(224, 144, 248));
                border1 = (new Color(184, 72, 224));
                border2 = (new Color(184, 72, 224));
                break;
            case Grass:
                original1 = (new Color(216, 240, 192));
                original2 = (new Color(144, 232, 128));
                border1 = (new Color(88, 184, 72));
                border2 = (new Color(88, 184, 72));
                break;
            case Water:
                original1 = (new Color(200, 216, 248));
                original2 = (new Color(104, 152, 248));
                border1 = (new Color(48, 112, 192));
                border2 = (new Color(48, 112, 192));
                break;
            case Ice:
                original1 = (new Color(192, 248, 248));
                original2 = (new Color(48, 216, 208));
                border1 = (new Color(56, 152, 136));
                border2 = (new Color(56, 152, 136));
                break;
            case Fire:
                original1 = (new Color(248, 192, 144));
                original2 = (new Color(248, 144, 48));
                border1 = (new Color(192, 104, 32));
                border2 = (new Color(192, 104, 32));
                break;
            case Electric:
                original1 = (new Color(248, 240, 208));
                original2 = (new Color(224, 224, 0));
                border1 = (new Color(176, 168, 16));
                border2 = (new Color(176, 168, 16));
                break;
            case Fairy:
                original1 = (new Color(255, 197, 255));
                original2 = (new Color(255, 101, 213));
                border1 = (new Color(237, 85, 181));
                border2 = (new Color(237, 85, 181));
                break;
            case Dragon:
                original1 = (new Color(104, 152, 248));
                original2 = (new Color(248, 112, 112));
                border1 = (new Color(48, 112, 192));
                border2 = (new Color(208, 64, 64));
                break;
            case Dinosaur:
                original1 = (new Color(127, 51, 0));
                original2 = (new Color(0, 0, 0));
                break;
            case Light:
                original1 = (new Color(255, 236, 142));
                original2 = (new Color(255, 255, 255));
                break;
            default:
                original1 = (new Color(0, 0, 0));
                original2 = (new Color(100, 100, 100));
                border1 = (new Color(255, 255, 255));
                border2 = (new Color(155, 155, 155));
                break;
        }
        color1 = original1; color2 = original2;
        TypeOfAttacks typeOfAttack = move.getType();
        
        ImageIcon imageIcon = SpriteImage.getImage(Board.ROOT+IMAGE+typeOfAttack.name()+".png");
        type.setIcon(SpriteImage.getScaledImage(imageIcon, ICON_WIDTH*mult, ICON_HEIGTH*mult));
        imageIcon = SpriteImage.getImage(Board.ROOT+IMAGE+typeMove.name()+".png");
        stat.setIcon(SpriteImage.getScaledImage(imageIcon, ICON_WIDTH*mult, ICON_HEIGTH*mult));
        
        this.setBackground(original1);
        this.setBorder(new GradientBorder(BORDER*mult, BORDER*mult, BORDER*mult, BORDER*mult, border1, border2));
    }

    private void setTextColor() {
        name.setForeground(Color.black);
        PP.setForeground(Color.black);
    }

    private void addListener() {
        ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (move.getPP() > 0) {
                    for (int i = 0; i < 2; ++i) {
                        Color c;
                        if (i == 0) {
                            c = original1;
                        } else {
                            c = original2;
                        }
                        Color back = new Color(c.getRed(), c.getGreen(), c.getBlue());
                        int red, green, blue;
                        if ((back.getRed() + 50) <= 255) {
                            red = c.getRed() + 50;
                        } else {
                            red = 255;
                        }
                        if ((back.getGreen() + 50) <= 255) {
                            green = c.getGreen() + 50;
                        } else {
                            green = 255;
                        }
                        if ((back.getBlue() + 50) <= 255) {
                            blue = c.getBlue() + 50;
                        } else {
                            blue = 255;
                        }
                        if (i == 0) {
                            color1 = new Color(red, green, blue);
                        } else {
                            color2 = new Color(red, green, blue);
                        }
                    }
                    Graphics g = getGraphics();
                    paintComponent(g);
                    repaint();
                    revalidate();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (move.getPP() > 0) {
                    recolor();
                }
            }
        };
        this.addMouseListener(ma);
    }
    public void recolor() {
        color1 = original1;
        color2 = original2;
        Graphics g = getGraphics();
        paintComponent(g);
        repaint();
        revalidate();
    }

    public JLabel getPP() {
        return PP;
    }
    public JLabel getMoveName() {
        return name;
    }
    public JLabel getStat() {
        return stat;
    }
    public JLabel getType() {
        return type;
    }
    public Move getMove() {
        return move;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
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
        PPPanel = new javax.swing.JPanel();
        PP = new javax.swing.JLabel();
        IconsPanel = new javax.swing.JPanel();
        stat = new javax.swing.JLabel();
        type = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setMaximumSize(new java.awt.Dimension(320, 120));
        setMinimumSize(new java.awt.Dimension(160, 60));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 60));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridBagLayout());

        NamePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        NamePanel.setMaximumSize(new java.awt.Dimension(120, 30));
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(120, 30));
        NamePanel.setLayout(new java.awt.BorderLayout());

        name.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        name.setText("NAME");
        name.setToolTipText("");
        name.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        name.setMaximumSize(new java.awt.Dimension(220, 30));
        name.setMinimumSize(new java.awt.Dimension(110, 30));
        name.setPreferredSize(new java.awt.Dimension(110, 30));
        NamePanel.add(name, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(NamePanel, gridBagConstraints);

        PPPanel.setMaximumSize(new java.awt.Dimension(100, 60));
        PPPanel.setMinimumSize(new java.awt.Dimension(50, 30));
        PPPanel.setOpaque(false);
        PPPanel.setPreferredSize(new java.awt.Dimension(50, 30));
        PPPanel.setLayout(new java.awt.BorderLayout());

        PP.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        PP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PP.setText("PP/MAX");
        PP.setToolTipText("");
        PP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PP.setMaximumSize(new java.awt.Dimension(100, 60));
        PP.setMinimumSize(new java.awt.Dimension(50, 30));
        PP.setPreferredSize(new java.awt.Dimension(50, 30));
        PPPanel.add(PP, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(PPPanel, gridBagConstraints);

        IconsPanel.setMaximumSize(new java.awt.Dimension(120, 30));
        IconsPanel.setOpaque(false);
        IconsPanel.setPreferredSize(new java.awt.Dimension(120, 30));
        IconsPanel.setLayout(new java.awt.GridLayout(1, 0));

        stat.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        stat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stat.setToolTipText("");
        stat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stat.setMaximumSize(new java.awt.Dimension(60, 30));
        stat.setMinimumSize(new java.awt.Dimension(60, 30));
        stat.setPreferredSize(new java.awt.Dimension(60, 30));
        IconsPanel.add(stat);

        type.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        type.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        type.setMaximumSize(new java.awt.Dimension(60, 30));
        type.setMinimumSize(new java.awt.Dimension(60, 30));
        type.setPreferredSize(new java.awt.Dimension(60, 30));
        IconsPanel.add(type);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(IconsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel IconsPanel;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JLabel PP;
    private javax.swing.JPanel PPPanel;
    private javax.swing.JLabel name;
    private javax.swing.JLabel stat;
    private javax.swing.JLabel type;
    // End of variables declaration//GEN-END:variables
}

class GradientBorder implements Border {
    private final Insets margin;
    private final Color col1, col2;

    public GradientBorder (int top, int left, int bottom, int right, Color c1, Color c2) {
        super();
        margin = new Insets(top, left, bottom, right);
        col1 = c1;
        col2 = c2;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint (new GradientPaint(x, y, col1, x, y + height, col2));

        Area border = new Area (new Rectangle(x, y, width, height));
        border.subtract (new Area(new Rectangle(x + margin.left, y + margin.top,
                width - margin.left - margin.right, height - margin.top - margin.bottom)));
        g2d.fill(border);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return margin;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}