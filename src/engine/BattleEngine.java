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
    
    private final Calendar calendar;
    private final int numberOfPokemon;
    private final ArrayList<Pokemon> orderOfAttack = new ArrayList<>();
    private final ArrayList<Move> moveOrder = new ArrayList<>();
    private final ArrayList<Integer> damage;
    private Weather weather;
    private int weatherClock;
    private final Arena arena;
    private int round;
//    private int pkmn1Round, pkmn2Round;
    private int playerPkmnRecoil, enemyPkmnRecoil;
    private final boolean isTrainer;
    private final Random rand;

    /**
     *
     * @param number
     * @param weather
     * @param arena
     * @param isTrainer
     */
    public BattleEngine(int number, Weather weather, Arena arena, boolean isTrainer) {
        weatherClock = 0;
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
        this.isTrainer = isTrainer;
        calendar = Calendar.getInstance();
        rand = new Random();
        damage = new ArrayList<>();
    }

    /**
     *
     * @param playerPkmn
     * @param playerMove
     * @param enemyPkmn
     * @param enemyMove
     */
    public void setPriority(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        if (numberOfPokemon == 2)
            calcSpeedPriority2(playerPkmn, playerMove, enemyPkmn, enemyMove);
        else
            calcSpeedPriority2(playerPkmn, playerMove, enemyPkmn, enemyMove);
    }

    /**
     *
     * @param playerPkmn
     * @param playerMove
     * @param enemyPkmn
     * @param enemyMove
     * @return
     */
    public ArrayList<Pokemon> firstMove(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        changeOrder(playerPkmn, playerMove, enemyPkmn, enemyMove);
        resetDamage();
        
        action(orderOfAttack.get(0), orderOfAttack.get(1), moveOrder.get(0));
        return checkIfKO(orderOfAttack.get(0), orderOfAttack.get(1));
    }

    /**
     *
     * @param playerPkmn
     * @param playerMove
     * @param enemyPkmn
     * @param enemyMove
     * @return
     */
    public ArrayList<Pokemon> secondMove(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        changeOrder(playerPkmn, playerMove, enemyPkmn, enemyMove);
        resetDamage();
        
        action(orderOfAttack.get(1), orderOfAttack.get(0), moveOrder.get(1));
        return checkIfKO(orderOfAttack.get(1), orderOfAttack.get(0));
    }

    /**
     *
     * @param playerPkmn
     * @param enemyPkmn
     * @return 
     */
    public ArrayList<Pokemon> setRoundFinish(Pokemon playerPkmn, Pokemon enemyPkmn) {
        return endTurnAction(playerPkmn, enemyPkmn);
    }

    /**
     *
     * @param index
     * @return
     */
    public Pokemon getPokemonFromOrder(int index) {
        return orderOfAttack.get(index);
    }
    
    public void resetDamage() {
        damage.clear();
    }
    
    public void changeOrder(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        if (playerPkmn != null) {
            if (isFirst(playerPkmn)) {
                moveOrder.remove(0);
                moveOrder.add(0, playerMove);
            } else {
                moveOrder.remove(1);
                moveOrder.add(1, playerMove);
            }
        }
        if (enemyPkmn != null) {
            if (isFirst(enemyPkmn)) {
                moveOrder.remove(0);
                moveOrder.add(0, enemyMove);
            } else {
                moveOrder.remove(1);
                moveOrder.add(1, enemyMove);
            }
        }
    }
    
    private boolean isFirst(Pokemon pkmn) {
        return pkmn == orderOfAttack.get(0);
    }

    private void action(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        int n = move.getHitMin();
        if (move.getHitMin() > 1) {
            if (move.getHitMax() != move.getHitMin()) {
                n = rand.nextInt((((move.getHitMax() - move.getHitMin()))+1)+move.getHitMin());
                System.out.println(n+"");
            }
        }
        for (int i = 0; i < n; ++i) {
            if (pkmnATK.getStatus() != Pokemon.Status.KO) {
                if (pkmnATK.getRoundSLP() == 0 && pkmnATK.getStatus() == Pokemon.Status.Asleep) {
                    pkmnATK.setStatus(Pokemon.Status.OK);
                    System.out.println(pkmnATK.getSurname()+" wakes up!");
                }
                if (pkmnATK.getRoundCNF() == 0 && pkmnATK.getIfConfused()) {
                    pkmnATK.setIfConfused(false);
                    System.out.println(pkmnATK.getSurname()+" gets out of confusion!");
                }
                if (!canAttackIfFrozen(pkmnATK)) {
                    System.out.println(pkmnATK.getSurname() + " is frozen solid!"); break;
                } else if (!canAttackIfParalyzed(pkmnATK)) {
                    System.out.println(pkmnATK.getSurname() + " is paralyzed!"); break;
                } else if (!canAttackIfInfatuated(pkmnATK)) {
                    System.out.println(pkmnATK.getSurname()+" is infatuated of "+pkmnDEF.getSurname()); break;
                } else if (pkmnATK.getStatus() == Pokemon.Status.Asleep) {
                    System.out.println(pkmnATK.getSurname()+" is sleeping..."); break;
                } else if (pkmnATK.getIfFlinched()) {
                    System.out.println(pkmnATK.getSurname()+" flinched!"); break;
                } else {
                    if (canAttackIfConfused(pkmnATK)) {
                        if (!"Switch".equals(move.getName())) {
                            System.out.println(pkmnATK.getSurname() + " use " + move.getName());
                        }

                        if (move.getType() != Move.TypeOfAttacks.Status) {
                            damage.add(calcDamage(pkmnATK, pkmnDEF, move));
                            System.out.println(pkmnDEF.getSurname() + " loses " + damage);
                            pkmnDEF.takeDamage(damage.get(i));
                        }
                        calcEffect(pkmnATK, pkmnDEF, move);
                        setWeather(move);
                    } else if (!canAttackIfConfused(pkmnATK)) {
                        damage.add(takeRecoilFromConfusion(pkmnATK));
                        pkmnATK.takeDamage(damage.get(i));
                        System.out.println(pkmnATK.getSurname() + " hit itself on its confusion!");
                        break;
                    }
                }
            }
        }
        if (n > 1) {
            System.out.println("Hit "+n+" times!");
        }
    }
    
    private ArrayList<Pokemon> checkIfKO(Pokemon pkmnATK, Pokemon pkmnDEF) {
        ArrayList<Pokemon> pkmn = new ArrayList<>();
        if (pkmnATK.getHP() <= 0 && pkmnDEF.getHP() <= 0) {
            pkmnATK.defeat(); pkmnDEF.defeat();
            System.out.println(pkmnATK.getSurname() + " is defeat.");
            System.out.println(pkmnDEF.getSurname() + " is defeat.");
            pkmn.add(pkmnATK); pkmn.add(pkmnDEF);
            return pkmn;
        } else {
            if (pkmnATK.getHP() <= 0) {
                pkmnATK.defeat(); pkmn.add(pkmnATK);
                System.out.println(pkmnATK.getSurname() + " is defeat.");
                return pkmn;
            } else if (pkmnDEF.getHP() <= 0) {
                pkmnDEF.defeat(); pkmn.add(pkmnDEF);
                System.out.println(pkmnDEF.getSurname() + " is defeat.");
                return pkmn;
            }
        }
        return null;
    }

    private void calcSpeedPriority2(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        System.out.println(playerPkmn.getSurname()+":"+playerPkmn.getHP()+" - "+enemyPkmn.getSurname()+":"+enemyPkmn.getHP());
        int pkmnSPD1 = playerPkmn.getTempSpd();
        if (playerPkmn.getStatus() == Pokemon.Status.Paralysis) {
            pkmnSPD1 /= 4;
        }
        int pkmnSPD2 = enemyPkmn.getTempSpd();
        if (enemyPkmn.getStatus() == Pokemon.Status.Paralysis) {
            pkmnSPD2 /= 4;
        }
        if (playerMove.getPriority() == enemyMove.getPriority()) {
            if (pkmnSPD1 > pkmnSPD2) {
                orderOfAttack.add(playerPkmn);
                moveOrder.add(playerMove);
                orderOfAttack.add(enemyPkmn);
                moveOrder.add(enemyMove);
            } else if (pkmnSPD1 < pkmnSPD2) {
                orderOfAttack.add(enemyPkmn);
                moveOrder.add(enemyMove);
                orderOfAttack.add(playerPkmn);
                moveOrder.add(playerMove);
            } else if (pkmnSPD1 == pkmnSPD2) {
                int random = rand.nextInt((100 - 1) + 1) + 1;
                if (random >= 50 && random < 100) {
                    orderOfAttack.add(enemyPkmn);
                    moveOrder.add(enemyMove);
                    orderOfAttack.add(playerPkmn);
                    moveOrder.add(playerMove);
                } else {
                    orderOfAttack.add(playerPkmn);
                    moveOrder.add(playerMove);
                    orderOfAttack.add(enemyPkmn);
                    moveOrder.add(enemyMove);
                }
            }
        } else {
            if (playerMove.getPriority() > enemyMove.getPriority()) {
                orderOfAttack.add(playerPkmn);
                moveOrder.add(playerMove);
                orderOfAttack.add(enemyPkmn);
                moveOrder.add(enemyMove);
            } else if (playerMove.getPriority() < enemyMove.getPriority()) {
                orderOfAttack.add(enemyPkmn);
                moveOrder.add(enemyMove);
                orderOfAttack.add(playerPkmn);
                moveOrder.add(playerMove);
            }
        }
    }

    private int calcDamage(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        int maxCritical = 16;
        int random;
        double attack = 0, defense = 0;
        double STAB = 1, critical = 1;
        double multiplier, N, modificator;
        double weatherMult = 1, itemMult = 1;
        if (move.getType() == Move.TypeOfAttacks.Physical) {
            attack = pkmnATK.getTempAtk();
            if (pkmnATK.getStatus() == Pokemon.Status.Burn) {
                attack /= 2;
            }
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
                    itemMult = (pkmnATK.getOwnItem().getValueOfEffect()) / 100;
                } else if (pkmnATK.getOwnItem().getCoE() == BagItem.CalcOfEffect.Unit) {
                    itemMult = pkmnATK.getOwnItem().getValueOfEffect();
                }
            } else if (pkmnATK.getOwnItem().getToI() == BagItem.TypeOfItem.AddCritical) {
                maxCritical /= pkmnATK.getOwnItem().getValueOfEffect();
            }
        }
        if (move.getMoveType() == Pokemon.Type.Fire) {
            if (this.weather == Weather.Sunny) weatherMult = 1.5;
            else if (this.weather == Weather.Rainy) weatherMult = 0.5;
        } else if (move.getMoveType() == Pokemon.Type.Water) {
            if (this.weather == Weather.Rainy) weatherMult = 1.5;
            else if (this.weather == Weather.Sunny) weatherMult = 0.5;
        } else if ("Solarbeam".equals(move.getName())) {
            if (null != this.weather) switch (this.weather) {
                case Sunny:
                    weatherMult = 1;
                    break;
                case Normal:
                    weatherMult = 1;
                    break;
                default:
                    weatherMult = 0.5;
                    break;
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
        modificator = weatherMult * itemMult;
        
        int damages = (int) ((((2 * pkmnATK.getLevel() + 10) * attack * move.getPower() / (250 * defense)) + 2)
                * multiplier * STAB * modificator * critical * (N /= 100));
        return damages;
    }
    
    //If the confusion flag is on, it receive a damage
    //from a move which has a power of 40 and no Type
    private int takeRecoilFromConfusion(Pokemon pkmn) {
        double attack = pkmn.getTempAtk(), defense = pkmn.getTempDef();
        int N = (rand.nextInt((100 - 85) + 1) + 85);
        int damages = (int) ((((2 * pkmn.getLevel() + 10) * attack * 40 / (250 * defense)) + 2) * (N /= 100));
        return damages;
    }
    
    //Controll if move dealt stats, status or other to self or other Pokemons
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
                    if (pkmnATK.getStatus() == Pokemon.Status.OK) {
                        pkmnATK.setStatus(move);
                    } else {
                        System.out.println(pkmnATK.getSurname()+" is already "+pkmnATK.getStatus().toString());
                    }
                } else {
                    if (pkmnDEF.getStatus() == Pokemon.Status.OK) {
                        pkmnDEF.setStatus(move);
                    } else {
                        System.out.println(pkmnDEF.getSurname()+" is already "+pkmnDEF.getStatus().toString());
                    }
                }
            }
        }
        if (move.getIfConfuse()) {
            int N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                if (move.getAreaOfStatus() == Move.Area.Self) {
                    if (!pkmnATK.getIfConfused()) {
                        pkmnATK.setIfConfused(true);
                    }
                } else {
                    if (!pkmnDEF.getIfConfused()) {
                        pkmnDEF.setIfConfused(true);
                    }
                }
            }
        }
        if (move.getIfInfatuated()) {
            if ((pkmnATK.getIfAsessual() == true && pkmnDEF.getIfAsessual() == true) || 
                    (pkmnATK.getIfMale() != pkmnDEF.getIfMale())) {
                int N = (rand.nextInt((100 - 0) + 1) + 0);
                if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                    if (move.getAreaOfStatus() == Move.Area.Self) {
                        if (!pkmnATK.getIfInfatuated()) {
                            pkmnATK.setIfInfatuated(true);
                        }
                    } else {
                        if (!pkmnDEF.getIfInfatuated()) {
                            pkmnDEF.setIfInfatuated(true);
                        }
                    }
                }
            }
        } 
        if (move.getIfFlinch()) {
            int N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                if (move.getAreaOfStatus() == Move.Area.Self) {
                    if (!pkmnATK.getIfFlinched()) {
                        pkmnATK.setIfFlinched(true);
                    }
                } else {
                    if (!pkmnDEF.getIfFlinched()) {
                        pkmnDEF.setIfFlinched(true);
                    }
                }
            }
        } 
    }
    
    //Set weather of Engine with the move
    private void setWeather(Move move) {
        if (move.getWeather() != null) {
            switch (move.getWeather()) {
                case Hail:
                    weather = Weather.Hail;
                    weatherClock = move.getWeatherRound();
                    System.out.println(move.getWeather());
                    break;
                case Rainy:
                    weather = Weather.Rainy;
                    weatherClock = move.getWeatherRound();
                    System.out.println(move.getWeather());
                    break;
                case SandStorm:
                    weather = Weather.SandStorm;
                    weatherClock = move.getWeatherRound();
                    System.out.println(move.getWeather());
                    break;
                case Sunny:
                    weather = Weather.Sunny;
                    weatherClock = move.getWeatherRound();
                    System.out.println(move.getWeather());
                    break;
                case Reset:
                    weather = Weather.Normal;
                    weatherClock = move.getWeatherRound();
                    System.out.println(move.getWeather());
                    break;
                default:
                    break;
            }
        }
    }
    
    //When the turn end it calc relative modifiers
    private ArrayList<Pokemon> endTurnAction(Pokemon playerPkmn, Pokemon enemyPkmn) {
        playerPkmn.decreaseRoundCNF();
        enemyPkmn.decreaseRoundCNF();
        playerPkmn.decreaseRoundSLP();
        enemyPkmn.decreaseRoundSLP();
        playerPkmn.setIfFlinched(false);
        enemyPkmn.setIfFlinched(false);
        playerPkmnRecoil = givePSNorBRNDamages(playerPkmn);
        enemyPkmnRecoil = givePSNorBRNDamages(enemyPkmn);
        if (weatherClock > 0) {
            --weatherClock;
        }
        return checkIfKO(playerPkmn, enemyPkmn);
    }
    
    //If Pokemon has malus it takes relative damage
    private int givePSNorBRNDamages(Pokemon pkmn) {
        if (null != pkmn.getStatus()) {
            int statusDamage = 0;
            switch (pkmn.getStatus()) {
                case Poison:
                    statusDamage = pkmn.getMaxHP() / 8;
                    pkmn.takeDamage(statusDamage);
                    System.out.println("Poison inflict "+statusDamage);
                    break;
                case BadPoison:
                    statusDamage = (pkmn.getMaxHP() / 16) * pkmn.getRoundBadPSN();
                    pkmn.takeDamage(statusDamage);
                    System.out.println("Bad Poison inflict "+statusDamage+" - "+pkmn.getRoundBadPSN());
                    pkmn.increaseRoundBPSN();
                    break;
                case Burn:
                    statusDamage = pkmn.getMaxHP() / 8;
                    pkmn.takeDamage(statusDamage);
                    System.out.println("Burn inflict "+statusDamage);
                    break;
                default:
                    break;
            }
            return statusDamage;
        }
        return 0;
    }
    
    //If pokemon is paralyzed: if return true the pokemon
    //attack normally, else it doesn't attack
    private boolean canAttackIfParalyzed(Pokemon pkmn) {
        if (pkmn.getStatus() == Pokemon.Status.Paralysis) {
            int r = rand.nextInt((100 - 0) + 1) - 0;
            return r >= 0 && r < 75; //true = attack!
        } return true;
    }
    //If pokemon is frozen: if return true the pokemon
    //attack normally and, eventually, it unfrozen,
    //else it doesn't attack
    private boolean canAttackIfFrozen(Pokemon pkmn) {
        if (pkmn.getStatus() == Pokemon.Status.Freeze) {
            int r = rand.nextInt((100 - 0) + 1) - 0;
            if (r >= 0 && r < 10) {
                pkmn.setStatus(Pokemon.Status.OK);
                return true;//true = attack!
            }
        } return true;
    }
    //If pokemon is infatuated: if return true the pokemon
    //attack normally, else it doesn't attack
    private boolean canAttackIfInfatuated(Pokemon pkmn) {
        if (pkmn.getIfInfatuated()) {
            int r = rand.nextInt((100 - 0) + 1) - 0;
            return r >= 0 && r < 50; //true = attack!
        } return true;
    }
    //If pokemon is confused: if return true the pokemon
    //attack normally, else it hits itself
    private boolean canAttackIfConfused(Pokemon pkmn) {
        if (pkmn.getIfConfused()) {
            int r = rand.nextInt((100 - 0) + 1) - 0;
            pkmn.decreaseRoundCNF();
            return r >= 0 && r < 50; //true = attack!
        } return true;
    }
    
    /**
     * Return to the parent a string which allow 
     * to color background or set weather animation
     * @return
     */
    public String getWeather() {
        String tempColor;
        switch (weather) {
            case Normal:
                tempColor = "102,204,0";
                break;
            case Hail:
                tempColor = "153,255,255";
                break;
            case Rainy:
                tempColor = "0,102,255";
                break;
            case SandStorm:
                tempColor = "102,51,0";
                break;
            case Sunny:
                tempColor = "255,153,0";
                break;
            default:
                tempColor = "102,204,0";
                break;
        }
        return tempColor;
    }

    /**
     * 
     * @param trn
     * @param pkmnOut
     * @param pkmnIn
     */
    public void switchPkmn(Trainer trn, Pokemon pkmnOut, Pokemon pkmnIn) {
        trn.getParty().switchPokemon(pkmnOut, pkmnIn);
        if (pkmnIn != null) {
            if (isFirst(pkmnOut)) {
                orderOfAttack.remove(0);
                orderOfAttack.add(0, pkmnIn);
            } else {
                orderOfAttack.remove(1);
                orderOfAttack.add(1, pkmnIn);
            }
        }
        System.out.println("Go " + trn.getParty().getPkmn(0).getSurname());
    }

    /**
     *
     * @param index
     * @return
     */
    public int getDamage(int index) {
        if (!damage.isEmpty()) {
            return damage.get(index);
        } return 0;
    }
    /**
     *
     * @return
     */
    public ArrayList<Integer> getDamageArray() {
        return damage;
    }
    /**
     *
     * @return
     */
    public int getPlayerPkmnRecoil() {
        return playerPkmnRecoil;
    }
    /**
     *
     * @return
     */
    public int getEnemyPkmnRecoil() {
        return enemyPkmnRecoil;
    }
    /**
     *
     */
    public void flush() {
        moveOrder.clear();
        orderOfAttack.clear();
        playerPkmnRecoil = enemyPkmnRecoil = 0;
    }
}
