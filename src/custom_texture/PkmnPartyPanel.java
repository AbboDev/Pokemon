package custom_texture;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import object.Pokemon;
import object.Pokemon.Status;
import static object.Pokemon.Status.*;

/**
 *
 * @author Thomas
 */
public class PkmnPartyPanel extends javax.swing.JPanel {
    private final static String ICON = "res/icons";
    private final static int TIMER_DELAY = 150;
    private Pokemon pokemon;
    private Color original;
    private int startX, startY;
    private Timer timerIcon;
    private MouseAdapter ma;

    /**
     * Creates new form PkmnPartyPanel
     * @param pkmn
     */
    public PkmnPartyPanel (Pokemon pkmn) {
        initComponents();
        addListener();
        
        pokemon = pkmn;
        startX = icon.getX();
        startY = icon.getY();
        
        setTextColor();
        refreshAll();
        startIconTimer();
    }
    
    public final void refreshAll() {
        boolean sexBoolean = !pokemon.getIfAsessual();
        icon.setIcon(pokemon.getSprite(ICON, 40, 30, false, sexBoolean));
        name.setText(pokemon.getSurname());
        String sexString;
        if (!pokemon.getIfAsessual()) {
            if (pokemon.getIfMale()) {
                sexString = "♂";
            } else {
                sexString = "♀";
            }
            sex.setText(sexString);
        }
        hp.setText(pokemon.getHP()+"/"+pokemon.getMaxHP());
        changeStatus(pokemon.getStatus());
    }
    
    private void changeStatus(Status st) {
        if (st != null) {
            switch (st) {
                case KO:
                    this.setBackground(Color.red);
                    name.setForeground(Color.lightGray);
                    hp.setForeground(Color.lightGray);
                    stat.setForeground(Color.lightGray);
                    break;
                case Asleep:
                    this.setBackground(Color.gray);
                    stat.setText("SLP");
                    break;
                case Poison:
                    this.setBackground(Color.magenta);
                    stat.setText("PSN");
                    break;
                case BadPoison:
                    this.setBackground(Color.darkGray);
                    stat.setText("BPSN");
                    break;
                case Paralysis:
                    this.setBackground(Color.yellow);
                    stat.setText("PAR");
                    break;
                case Burn:
                    this.setBackground(Color.orange);
                    stat.setText("BRN");
                    break;
                case Freeze:
                    this.setBackground(Color.cyan);
                    stat.setText("FRZ");
                    break;
                default:
                    this.setBackground(Color.blue);
                    break;
            }
        }
    }
    private void setTextColor() {
        name.setForeground(Color.black);
        sex.setForeground(Color.black);
        hp.setForeground(Color.black);
        stat.setForeground(Color.black);
    }
    private void startIconTimer() {
        timerIcon = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pokemon.getStatus() != KO) {
                    if (pokemon.getStatus() == OK) {
                        if (icon.getY() == startY) {
                            icon.setLocation(startX, startY + 3);
                        } else {
                            icon.setLocation(startX, startY);
                        }
                        int delay = ((pokemon.getMaxHP() / pokemon.getHP()) > 50) ? 50: pokemon.getMaxHP() / pokemon.getHP();
                        timerIcon.setDelay(TIMER_DELAY * (delay));
                    } else if (pokemon.getStatus() != Asleep) {
                        if (icon.getX() == startX) {
                            icon.setLocation(startX + 3, startY);
                            timerIcon.setDelay(TIMER_DELAY);
                        } else {
                            icon.setLocation(startX, startY);
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
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        sex = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        hp = new javax.swing.JLabel();
        stat = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 51, 255));
        setMaximumSize(new java.awt.Dimension(160, 60));
        setMinimumSize(new java.awt.Dimension(160, 60));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 60));

        icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        icon.setToolTipText("");
        icon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        icon.setMaximumSize(new java.awt.Dimension(32, 32));
        icon.setMinimumSize(new java.awt.Dimension(32, 32));
        icon.setName(""); // NOI18N
        icon.setPreferredSize(new java.awt.Dimension(32, 32));

        sex.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sex.setToolTipText("");
        sex.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name.setToolTipText("");
        name.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        stat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stat.setToolTipText("");
        stat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sex, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(hp, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stat, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel hp;
    private javax.swing.JLabel icon;
    private javax.swing.JLabel name;
    private javax.swing.JLabel sex;
    private javax.swing.JLabel stat;
    // End of variables declaration//GEN-END:variables
}