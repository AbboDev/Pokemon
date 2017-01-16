package custom_texture;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import objects.Pokemon;
import objects.Pokemon.Status;
import screen.Board;

import static objects.Pokemon.Status.*;

/**
 * @author Thomas
 */
public class PkmnPartyPanel extends ExpandPanel {
    private static final long serialVersionUID = -7528379132904240646L;
    private final static String PATH = Board.ROOT+"/resources/image/";
    private static final int TIMER_DELAY = 150;
    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGTH = 30;
    private final static int ICON_SIZE = 20;
    
    private final Pokemon pokemon;
    private final int startX, startY;
    private Color original;
    private Timer timerIcon;
    private MouseAdapter ma;
    private String path;

    /**
     * Creates new form PkmnPartyPanel
     * @param pkmn
     * @param mult
     */
    public PkmnPartyPanel (Pokemon pkmn, int mult) {
        initComponents();
        expandComponent(mult);
        
        pokemon = pkmn;
        startX = Icon.getX();
        startY = Icon.getY();
        getPath();
        
        setTextColor();
        refreshAll(mult);
        startIconTimer(mult);
    }
    
    public final void refreshAll(int mult) {
        ImageIcon image = SpriteImage.getImage(path);
        Icon.setIcon(SpriteImage.getScaledImage(image, ICON_WIDTH*mult, ICON_HEIGTH*mult));
        Name.setText(pokemon.getSurname());
        if (!pokemon.getIfAsessual()) {
            String sex;
            if (pokemon.getIfMale()) { sex = "Male"; } else { sex = "Female"; }
            Gender.setIcon(SpriteImage.getScaledImage(SpriteImage.getImage(PATH+sex+".png"), ICON_SIZE*mult));
        }
        HP.setText(pokemon.getStat("HP")+"/"+pokemon.getStat("MaxHP"));
        changeStatus(pokemon.getStatus());
    }
    
    private void getPath() {
        boolean shinyBln = pokemon.getIfShiny();
        boolean asexBln = pokemon.getIfAsessual();
        boolean femaleBln = !pokemon.getIfMale();
        ArrayList<Boolean> bool = new ArrayList<>();
        bool.add(shinyBln);
        bool.add(asexBln);
        bool.add(femaleBln);
        path = Pokemon.getImagePath(bool, pokemon.getID(), "icons");
    }
    private void changeStatus(Status st) {
        if (st != null) {
            switch (st) {
                case KO:
                    this.setBackground(Color.red);
                    Name.setForeground(Color.lightGray);
                    HP.setForeground(Color.lightGray);
                    Status.setForeground(Color.lightGray);
                    Status.setText("");
                    break;
                case Asleep:
                    this.setBackground(Color.gray);
                    Status.setText("SLP");
                    break;
                case Poison:
                    this.setBackground(Color.magenta);
                    Status.setText("PSN");
                    break;
                case BadPoison:
                    this.setBackground(Color.darkGray);
                    Status.setText("BPSN");
                    break;
                case Paralysis:
                    this.setBackground(Color.yellow);
                    Status.setText("PAR");
                    break;
                case Burn:
                    this.setBackground(Color.orange);
                    Status.setText("BRN");
                    break;
                case Freeze:
                    this.setBackground(Color.cyan);
                    Status.setText("FRZ");
                    break;
                default:
                    this.setBackground(Color.blue);
                    Status.setText("");
                    break;
            }
        }
    }
    private void setTextColor() {
        Name.setForeground(Color.black);
        Gender.setForeground(Color.black);
        HP.setForeground(Color.black);
        Status.setForeground(Color.black);
    }
    private void startIconTimer(final int mult) {
        timerIcon = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pokemon.getStatus() != KO) {
                    if (pokemon.getStatus() == OK) {
                        if (Icon.getY() == startY) {
                            Icon.setLocation(startX, startY + 1*mult);
                        } else {
                            Icon.setLocation(startX, startY);
                        }
                        int delay = ((pokemon.getStat("MaxHP") / pokemon.getStat("HP")) > 50)
                                ? 50 : pokemon.getStat("MaxHP") / pokemon.getStat("HP");
                        timerIcon.setDelay(TIMER_DELAY * (delay));
                    } else if (pokemon.getStatus() != Asleep) {
                        if (Icon.getX() == startX) {
                            Icon.setLocation(startX + 3, startY);
                            timerIcon.setDelay(TIMER_DELAY);
                        } else {
                            Icon.setLocation(startX, startY);
                            timerIcon.setDelay(TIMER_DELAY*5);
                        }
                    }
                }
            }
        });
        timerIcon.start();
    }
    private void addListener() {
        ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                original = getBackground();
                Color back = new Color (getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue());
                int red, green, blue;
                if ((back.getRed()+50) <= 255) {
                    red = getBackground().getRed() + 50;
                } else {
                    red = 255;
                }
                if ((back.getGreen()+50) <= 255) {
                    green = getBackground().getGreen() + 50;
                } else {
                    green = 255;
                }
                if ((back.getBlue()+50) <= 255) {
                    blue = getBackground().getBlue() + 50;
                } else {
                    blue = 255;
                }
                setBackground(new Color (red, green, blue));
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(original);
            }
        };
        this.addMouseListener(ma);
    }
    
    public Pokemon getPokemon() {
        return pokemon;
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

        IconPanel = new javax.swing.JPanel();
        Icon = new javax.swing.JLabel();
        NamePanel = new javax.swing.JPanel();
        Name = new javax.swing.JLabel();
        GenderPanel = new javax.swing.JPanel();
        Gender = new javax.swing.JLabel();
        StatsPanel = new javax.swing.JPanel();
        HP = new javax.swing.JLabel();
        Status = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 51, 255));
        setMaximumSize(new java.awt.Dimension(320, 120));
        setMinimumSize(new java.awt.Dimension(160, 60));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 60));
        setLayout(new java.awt.GridBagLayout());

        IconPanel.setInheritsPopupMenu(true);
        IconPanel.setMaximumSize(new java.awt.Dimension(80, 64));
        IconPanel.setMinimumSize(new java.awt.Dimension(40, 32));
        IconPanel.setOpaque(false);
        IconPanel.setPreferredSize(new java.awt.Dimension(40, 32));
        IconPanel.setLayout(new java.awt.GridLayout(1, 0));

        Icon.setBackground(new java.awt.Color(204, 204, 204));
        Icon.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Icon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Icon.setDoubleBuffered(true);
        Icon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Icon.setMaximumSize(new java.awt.Dimension(80, 64));
        Icon.setMinimumSize(new java.awt.Dimension(40, 32));
        Icon.setPreferredSize(new java.awt.Dimension(40, 32));
        Icon.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        IconPanel.add(Icon);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(IconPanel, gridBagConstraints);

        NamePanel.setBackground(new java.awt.Color(204, 204, 204));
        NamePanel.setInheritsPopupMenu(true);
        NamePanel.setMaximumSize(new java.awt.Dimension(180, 64));
        NamePanel.setName(""); // NOI18N
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(90, 32));
        NamePanel.setLayout(new java.awt.BorderLayout());

        Name.setBackground(new java.awt.Color(204, 204, 204));
        Name.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Name.setText("NAME");
        Name.setDoubleBuffered(true);
        Name.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Name.setMaximumSize(new java.awt.Dimension(180, 64));
        Name.setMinimumSize(new java.awt.Dimension(90, 32));
        Name.setName(""); // NOI18N
        Name.setPreferredSize(new java.awt.Dimension(90, 32));
        NamePanel.add(Name, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(NamePanel, gridBagConstraints);

        GenderPanel.setBackground(new java.awt.Color(204, 204, 204));
        GenderPanel.setInheritsPopupMenu(true);
        GenderPanel.setMaximumSize(new java.awt.Dimension(60, 64));
        GenderPanel.setOpaque(false);
        GenderPanel.setPreferredSize(new java.awt.Dimension(30, 32));
        GenderPanel.setLayout(new java.awt.BorderLayout());

        Gender.setBackground(new java.awt.Color(204, 204, 204));
        Gender.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Gender.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Gender.setDoubleBuffered(true);
        Gender.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Gender.setMaximumSize(new java.awt.Dimension(60, 64));
        Gender.setMinimumSize(new java.awt.Dimension(30, 32));
        Gender.setPreferredSize(new java.awt.Dimension(30, 32));
        GenderPanel.add(Gender, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(GenderPanel, gridBagConstraints);

        StatsPanel.setBackground(new java.awt.Color(204, 204, 204));
        StatsPanel.setInheritsPopupMenu(true);
        StatsPanel.setMaximumSize(new java.awt.Dimension(320, 60));
        StatsPanel.setMinimumSize(new java.awt.Dimension(160, 30));
        StatsPanel.setOpaque(false);
        StatsPanel.setPreferredSize(new java.awt.Dimension(160, 30));
        StatsPanel.setLayout(new java.awt.GridBagLayout());

        HP.setBackground(new java.awt.Color(204, 204, 204));
        HP.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        HP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HP.setDoubleBuffered(true);
        HP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        HP.setMaximumSize(new java.awt.Dimension(260, 60));
        HP.setMinimumSize(new java.awt.Dimension(130, 30));
        HP.setPreferredSize(new java.awt.Dimension(130, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        StatsPanel.add(HP, gridBagConstraints);

        Status.setBackground(new java.awt.Color(204, 204, 204));
        Status.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Status.setDoubleBuffered(true);
        Status.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Status.setMaximumSize(new java.awt.Dimension(100, 60));
        Status.setMinimumSize(new java.awt.Dimension(50, 30));
        Status.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        StatsPanel.add(Status, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(StatsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Gender;
    private javax.swing.JPanel GenderPanel;
    private javax.swing.JLabel HP;
    private javax.swing.JLabel Icon;
    private javax.swing.JPanel IconPanel;
    private javax.swing.JLabel Name;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JPanel StatsPanel;
    private javax.swing.JLabel Status;
    // End of variables declaration//GEN-END:variables
}