package objects;

import java.util.ArrayList;
import java.util.Random;
/**
 * @author Thomas
 */
public class Party {
    private int maxNumber = 12;
    private final ArrayList<Pokemon> party;
    
    public Party() {
        party = new ArrayList<>();
    }
    
    public void addItemToPkmn(int position, BagItem item) {
        party.get(position).giveItem(item);
    }
    
    public Pokemon getPkmn(int position) {
        return party.get(position);
    }
    public Pokemon getRandomPkmn() {
        Random r = new Random();
        int index = r.nextInt(party.size());
        while (party.get(index).getStatus() == Pokemon.Status.KO) {
            index = r.nextInt(party.size());
        }
        return party.get(index);
    }
    public void releasePkmn(int position) {
        if (party.size() > 1) {
            party.remove(position);
        }
    }
    public void addPkmnToParty(Pokemon pkmn) {
        if (party.size() < maxNumber) {
            party.add(pkmn);
        }
    }
    /**
     * It moves the pkmn to a determinate position
     * make the other to scale
     * @param pkmn
     * @param position 
     */
    public void switchPosition(Pokemon pkmn, int position) {
        if (party.size() >= 2) {
            if (position > party.size()) position = party.size() - 1;
            if (position > maxNumber) position = maxNumber - 1;
            if (party.contains(pkmn)) {
                Pokemon pkmnCopy = party.get(party.indexOf(pkmn));
                party.remove(pkmn);
                party.add(position, pkmnCopy);
            }
        }
    }
    /**
     * It moves the pkmn to a determinate position
     * make the other to scale
     * @param pkmn
     * @param pkmn2 
     */
    public void switchPokemon(Pokemon pkmn, Pokemon pkmn2) {
        if (party.size() >= 2) {
            if (party.contains(pkmn) && party.contains(pkmn2)) {
                Pokemon pkmnCopy = party.get(party.indexOf(pkmn));
                Pokemon pkmnCopy2 = party.get(party.indexOf(pkmn2));
                
                int i = party.indexOf(pkmn), y = party.indexOf(pkmn2);
                
                party.remove(pkmn);
                party.add(y, pkmnCopy);
                party.remove(pkmn2);
                party.add(i, pkmnCopy2);
            }
        }
    }

    public int getMaxNumber() {
        return maxNumber;
    }
    public void updateMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public ArrayList<Pokemon> getArrayParty() {
        return party;
    }
    public int getSize() {
        return party.size();
    }
}
