package custom_texture;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class PkmnPartyPanel extends GradientPanel {
    private static final long serialVersionUID = -7528379132904240646L;
    private final static String PATH = Board.ROOT+"/resources/image/";
    private static final int TIMER_DELAY = 150;
    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGTH = 30;
    private final static int ICON_SIZE = 20;
    
    private final Pokemon pokemon;
    private final int startX, startY;
    private Timer timerIcon;
    private String path;

    /**
     * Creates new form PkmnPartyPanel
     * @param pkmn
     */
    public PkmnPartyPanel (Pokemon pkmn) {
//        super();
        initComponents();
        expandComponent();
        
        pokemon = pkmn;
        startX = Icon.getX();
        startY = Icon.getY();
        getPath();
        
        setTextColor();
        refreshAll();
        startIconTimer();
    }
    
    public final void refreshAll() {
        int width = (int) (ICON_WIDTH*(Board.xDIM)); int height = (int) (ICON_HEIGTH*(Board.yDIM));
        ImageIcon image = ImageConverter.getImage(path);
        Icon.setIcon(ImageConverter.getScaledImage(image, width, height));
        Name.setText(pokemon.getSurname());
        if (!pokemon.getIfAsessual()) {
            String sex;
            if (pokemon.getIfMale()) { sex = "Male"; } else { sex = "Female"; }
            int sizex = (int) (ICON_SIZE*(Board.xDIM));
            int sizey = (int) (ICON_SIZE*(Board.yDIM));
            Gender.setIcon(ImageConverter.getScaledImage(
                    ImageConverter.getImage(PATH+sex+".png"), sizex, sizey));
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
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(255, 0, 0);
                    back2 = new Color(200, 0, 20);
                    Name.setForeground(Color.lightGray);
                    HP.setForeground(Color.lightGray);
                    Status.setForeground(Color.lightGray);
                    Status.setText("");
                    break;
                case Asleep:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(200, 200, 200);
                    back2 = new Color(50, 50, 50);
                    Status.setText("SLP");
                    break;
                case Poison:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(250, 0, 250);
                    back2 = new Color(120, 0, 120);
                    Status.setText("PSN");
                    break;
                case BadPoison:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(220, 0, 220);
                    back2 = new Color(90, 0, 90);
                    Status.setText("BPSN");
                    break;
                case Paralysis:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(255, 200, 0);
                    back2 = new Color(185, 130, 0);
                    Status.setText("PAR");
                    break;
                case Burn:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(200, 100, 0);
                    back2 = new Color(100, 50, 0);
                    Status.setText("BRN");
                    break;
                case Freeze:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(0, 200, 200);
                    back2 = new Color(0, 100, 100);
                    Status.setText("FRZ");
                    break;
                default:
                    border1 = new Color(200, 200, 200);
                    border2 = new Color(180, 180, 180);
                    back1 = new Color(0, 0, 255);
                    back2 = new Color(0, 0, 200);
                    Status.setText("");
                    break;
            }
        }
        int borderx = (int) (BORDER*(Board.xDIM));
        int bordery = (int) (BORDER*(Board.yDIM));
        this.setBorder(new GradientBorder(borderx, bordery, border1, border2));
    }
    private void setTextColor() {
        Name.setForeground(Color.black);
        Gender.setForeground(Color.black);
        HP.setForeground(Color.black);
        Status.setForeground(Color.black);
    }
    
    private void startIconTimer() {
        timerIcon = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pokemon.getStatus() != KO) {
                    if (pokemon.getStatus() == OK) {
                        if (Icon.getY() == startY) {
                            Icon.setLocation(startX, (int) (startY + 1*(Board.yDIM)));
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
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(80, 30));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 60));
        setLayout(new java.awt.GridBagLayout());

        IconPanel.setInheritsPopupMenu(true);
        IconPanel.setMaximumSize(null);
        IconPanel.setMinimumSize(new java.awt.Dimension(40, 30));
        IconPanel.setOpaque(false);
        IconPanel.setPreferredSize(new java.awt.Dimension(40, 30));
        IconPanel.setLayout(new java.awt.GridLayout(1, 0));

        Icon.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Icon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Icon.setMaximumSize(null);
        Icon.setMinimumSize(new java.awt.Dimension(40, 30));
        Icon.setPreferredSize(new java.awt.Dimension(40, 30));
        IconPanel.add(Icon);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(IconPanel, gridBagConstraints);

        NamePanel.setBackground(new java.awt.Color(204, 204, 204));
        NamePanel.setInheritsPopupMenu(true);
        NamePanel.setMinimumSize(new java.awt.Dimension(90, 30));
        NamePanel.setName(""); // NOI18N
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(90, 30));
        NamePanel.setLayout(new java.awt.BorderLayout());

        Name.setBackground(new java.awt.Color(204, 204, 204));
        Name.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Name.setText("NAME");
        Name.setDoubleBuffered(true);
        Name.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Name.setMaximumSize(null);
        Name.setMinimumSize(new java.awt.Dimension(90, 30));
        Name.setName(""); // NOI18N
        Name.setPreferredSize(new java.awt.Dimension(90, 30));
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
        GenderPanel.setMinimumSize(new java.awt.Dimension(30, 30));
        GenderPanel.setName(""); // NOI18N
        GenderPanel.setOpaque(false);
        GenderPanel.setPreferredSize(new java.awt.Dimension(30, 30));
        GenderPanel.setLayout(new java.awt.BorderLayout());

        Gender.setBackground(new java.awt.Color(204, 204, 204));
        Gender.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        Gender.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Gender.setDoubleBuffered(true);
        Gender.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Gender.setMaximumSize(null);
        Gender.setMinimumSize(new java.awt.Dimension(30, 30));
        Gender.setName(""); // NOI18N
        Gender.setPreferredSize(new java.awt.Dimension(30, 30));
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
        StatsPanel.setMinimumSize(new java.awt.Dimension(160, 30));
        StatsPanel.setOpaque(false);
        StatsPanel.setPreferredSize(new java.awt.Dimension(160, 30));
        StatsPanel.setLayout(new java.awt.GridBagLayout());

        HP.setBackground(new java.awt.Color(204, 204, 204));
        HP.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        HP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HP.setDoubleBuffered(true);
        HP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        HP.setMaximumSize(null);
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
        Status.setMaximumSize(null);
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