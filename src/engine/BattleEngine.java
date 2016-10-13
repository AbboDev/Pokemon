package engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import object.BagItem;
import object.Pokemon;
import object.Trainer;
import object.Move;

/**
 * @author Thomas
 */
public class BattleEngine {
    public enum Weather {
        Normal, Cloud, Sunny, Rainy,
        Snow, Hail, SandStorm, Fog
    };
    public enum Arena {
        Normal, Cave, Aerial, Submarine
    };
    private Calendar calendar;
    
    private final int numberOfPokemon;
    private final ArrayList<Pokemon> orderOfAttack = new ArrayList<>();
    
    private int damage;
    
    private final Weather weather;
    private final Arena arena;
    
    private final boolean isTrainer;
    
    private final Random rand;
        
    public BattleEngine(int number, Weather weather, Arena arena) {
        numberOfPokemon = number;
        if (weather != null) {
            this.weather = weather;
        } else {
            this.weather = Weather.Normal;
        }
        if (arena != null) {
            this.arena = arena;
        } else {
            this.arena = Arena.Normal;
        }
        isTrainer = false;
        calendar = Calendar.getInstance();
        rand = new Random();
    }
    
    public void round(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        calcSpeedPriority2(playerPkmn, playerMove, enemyPkmn, enemyMove);
        if (orderOfAttack.get(0) == enemyPkmn) {
            System.out.println(enemyPkmn.getName()+" use "+enemyMove.getName());
            damage = calcDamage(enemyPkmn, playerPkmn, enemyMove);
            System.out.println(playerPkmn.getName()+" loses "+damage);
            playerPkmn.takeDamage(damage);
            orderOfAttack.remove(0);
            if (playerPkmn.getHP() > 0) {
                System.out.println(playerPkmn.getName()+" use "+playerMove.getName());
                damage = calcDamage(playerPkmn, enemyPkmn, playerMove);
                System.out.println(enemyPkmn.getName()+" loses "+damage);
                enemyPkmn.takeDamage(damage);
                orderOfAttack.remove(0);
                if (playerPkmn.getHP() <= 0) {
                    enemyPkmn.setStatus(Pokemon.Status.KO);
                    System.out.println(enemyPkmn.getName()+" is defeat.");
                }
            } else {
                playerPkmn.setStatus(Pokemon.Status.KO);
                System.out.println(playerPkmn.getName()+" is defeat.");
            }
        } else if (orderOfAttack.get(0) == playerPkmn) {
            System.out.println(playerPkmn.getName()+" use "+playerMove.getName());
            damage = calcDamage(playerPkmn, enemyPkmn, playerMove);
            System.out.println(enemyPkmn.getName()+" loses "+damage);
            enemyPkmn.takeDamage(damage);
            orderOfAttack.remove(0);
            if (enemyPkmn.getHP() > 0) {
                System.out.println(enemyPkmn.getName()+" use "+enemyMove.getName());
                damage = calcDamage(enemyPkmn, playerPkmn, enemyMove);
                System.out.println(playerPkmn.getName()+" loses "+damage);
                playerPkmn.takeDamage(damage);
                orderOfAttack.remove(0);
                if (playerPkmn.getHP() <= 0) {
                    playerPkmn.setStatus(Pokemon.Status.KO);
                    System.out.println(playerPkmn.getName()+" is defeat.");
                }
            } else {
                enemyPkmn.setStatus(Pokemon.Status.KO);
                System.out.println(enemyPkmn.getName()+" is defeat.");
            }
        }
    }
    
    private void calcSpeedPriority2(Pokemon pkmn1, Move move1, Pokemon pkmn2, Move move2) {
        orderOfAttack.clear();
        System.out.println(pkmn1.getTempSpd()+" - "+pkmn2.getTempSpd());
        if (move1.getPriority() == move2.getPriority()) {
            if (pkmn1.getTempSpd() > pkmn2.getTempSpd()) {
                orderOfAttack.add(pkmn1);
                orderOfAttack.add(pkmn2);
            } else if (pkmn1.getTempSpd() < pkmn2.getTempSpd()) {
                orderOfAttack.add(pkmn2);
                orderOfAttack.add(pkmn1);
            } else if (pkmn1.getTempSpd() == pkmn2.getTempSpd()) {
                int random = rand.nextInt((100 - 1) + 1) + 1;
                if (random >= 50 && random < 100) {
                    orderOfAttack.add(pkmn2);
                    orderOfAttack.add(pkmn1);
                } else {
                    orderOfAttack.add(pkmn1);
                    orderOfAttack.add(pkmn2);
                }
            }
        } else {
            if (move1.getPriority() > move2.getPriority()) {
                orderOfAttack.add(pkmn1);
                orderOfAttack.add(pkmn2);
            } else if (move1.getPriority() < move2.getPriority()) {
                orderOfAttack.add(pkmn2);
                orderOfAttack.add(pkmn1);
            }
        }
    }    
    private int calcDamage(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        int maxCritical = 16;
        int random;
        double attack = 0, defense = 0;
        double STAB = 1, modificator = 1, critical = 1;
        double multiplier, N;
        if (move.getType() == Move.TypeOfAttacks.Physical) {
            attack = pkmnATK.getTempAtk();
            defense = pkmnDEF.getTempDef();
        } else if (move.getType() == Move.TypeOfAttacks.Special) {
            attack = pkmnATK.getTempSpAtk();
            defense = pkmnDEF.getTempSpDef();
        }
        if (pkmnATK.getFirstType() == move.getMoveType() || pkmnATK.getSecondType() == move.getMoveType()) {
            STAB = 1.5;
        }
        if (pkmnATK.getOwnItem() != null) {
            if (pkmnATK.getOwnItem().getToI() == BagItem.TypeOfItem.Multiplier) {
                if (pkmnATK.getOwnItem().getCoE() == BagItem.CalcOfEffect.Percentage) {
                    modificator = (pkmnATK.getOwnItem().getValueOfEffect()) / 100;
                } else if (pkmnATK.getOwnItem().getCoE() == BagItem.CalcOfEffect.Unit) {
                    modificator = pkmnATK.getOwnItem().getValueOfEffect();
                }
            } else if (pkmnATK.getOwnItem().getToI() == BagItem.TypeOfItem.AddCritical) {
                maxCritical /= pkmnATK.getOwnItem().getValueOfEffect();
            }
        }
        maxCritical /= pkmnATK.getTempCrit();
        if (maxCritical <= 0) {
            maxCritical = 1;
        }
        random = rand.nextInt((maxCritical - 1) + 1) + 1;
        if (random == 1) {
            critical = 2;
            System.out.println("Critical Hit!");
            if (attack < pkmnATK.getAttack()) {
                attack = pkmnATK.getAttack();
            }
            if (defense > pkmnDEF.getDefense()) {
                defense = pkmnDEF.getDefense();
            }
        }
        multiplier = move.moveEffect(pkmnDEF.getFirstType(), pkmnDEF.getSecondType(), pkmnATK.getIfPowered());
    	N = (rand.nextInt((100 - 85) + 1) + 85);
        
        int damages = (int) ((((2*pkmnATK.getLevel()+10)*attack*move.getPower()/(250*defense))+2)
                * multiplier * STAB * modificator * critical * (N/= 100));
        calcEffect(pkmnATK, pkmnDEF, move);
        return damages;
    }
    private void calcEffect(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        if (move.getStats()[0] != null) {
            int N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatsPercentage() || move.getStatsPercentage() == 100) {
                if (move.getAreaOfStats() == Move.Area.Self) {
                    pkmnATK.setTempStats(move);
                } else {
                    pkmnDEF.setTempStats(move);
                }
            }
        }
        if (move.getStatus() != null) {
            int N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                if (move.getAreaOfStatus() == Move.Area.Self) {
                    pkmnATK.setStatus(move);
                } else {
                    pkmnDEF.setStatus(move);
                }
            }
        }
    }
    
    public void switchPkmn(Trainer trn, Pokemon pkmnOut, Pokemon pkmnIn) {
        trn.getParty().switchPokemon(pkmnOut, pkmnIn);
        System.out.println("Go "+trn.getParty().getPkmn(0).getName());
    }
}
