/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_texture;

import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import object.Pokemon;

/**
 *
 * @author Thomas
 */
public class PokemonPanel extends JPanel {
    private JLabel name, sex, icon;
    public PokemonPanel (Pokemon pkmn) {
        super();
        name.setText(pkmn.getSurname());
        String sexString;
        if (pkmn.getIfMale()) {
            sexString = "♂";
        } else {
            sexString = "♀";
        }
        sex.setText(sexString);
    }
    
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
    }
}
