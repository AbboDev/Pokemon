package object;

import java.util.ArrayList;
/**
 * @author Thomas
 */
public class TrainerCard {
    public String octID;
    public String hexID;	
    public int money;
    
    public ArrayList<String> badge;

    public TrainerCard(String octID, String hexID, int money) {
	this.octID = octID;
	this.hexID = hexID;
	this.money = money;
	badge = new ArrayList<>();
    }

    public String getOctID() {
        return octID;
    }

    public String getHexID() {
        return hexID;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<String> getBadge() {
        return badge;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
