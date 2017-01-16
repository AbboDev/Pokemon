package engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import objects.BagItem;
import objects.Pokemon;
import objects.Trainer;
import objects.Move;
import objects.Move.StatsOfAttacks;

import static objects.Pokemon.Status.*;

/**
 * @author Thomas
 */
public class BattleEngine {
    private static final int BASIC_POWER = 40;
    
    public enum Weather {
        Normal, Cloud, Sunny, Rainy,
        Snow, Hail, SandStorm, Fog
    };
    public enum Arena {
        Normal, Cave, Aerial, Submarine
    };
    public enum Key {
        Attack, Defense, CountHit, KO,
        Critical, Already, Weather, Switch,
        Damage, Nothing, Effect, Status, Stats,
        WeatherStart
    };
    
    private final Calendar calendar;
    private final int numberOfPokemon;
    private final ArrayList<Pokemon> orderOfAttack = new ArrayList<>();
    private final ArrayList<Move> moveOrder = new ArrayList<>();
    private final ArrayList<Integer> damage;
    private final Map<Key, String> actionText;
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
        round = 0;
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
        System.out.println(calendar.getTime());
        rand = new Random();
        damage = new ArrayList<>();
        actionText = new HashMap<>();
    }

    /**
     *
     * @param playerPkmn
     * @param playerMove
     * @param enemyPkmn
     * @param enemyMove
     */
    public void setPriority(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        ++round; System.out.println("Round "+round);
        System.out.println("Round weather "+weatherClock);
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
    
    /**
     * 
     */
    public void resetDamage() {
        damage.clear();
    }
    
    /**
     * 
     * @param playerPkmn
     * @param playerMove
     * @param enemyPkmn
     * @param enemyMove 
     */
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
    
    //
    private boolean isFirst(Pokemon pkmn) {
        return pkmn == orderOfAttack.get(0);
    }

    //
    private void action(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        int n = move.getHitMin();
        if (move.getHitMin() > 1) {
            if (move.getHitMax() != move.getHitMin()) {
                n = rand.nextInt((((move.getHitMax() - move.getHitMin()))+1)+move.getHitMin());
            }
        }
        for (int i = 0; i < n; ++i) {
            if (pkmnATK.getStatus() != Pokemon.Status.KO) {
                if (pkmnATK.getRoundSLP() == 0 && pkmnATK.getStatus() == Pokemon.Status.Asleep) {
                    pkmnATK.setStatus(Pokemon.Status.OK);
                    actionText.put(Key.Attack, pkmnATK.getSurname()+" wakes up!");
                }
                if (pkmnATK.getRoundCNF() == 0 && pkmnATK.getIfConfused()) {
                    pkmnATK.setIfConfused(false);
                    actionText.put(Key.Attack, pkmnATK.getSurname()+" gets out of confusion!");
                }
                if (!canAttackIfFrozen(pkmnATK)) {
                    actionText.put(Key.Attack, pkmnATK.getSurname() + " is frozen solid!"); break;
                } else if (!canAttackIfParalyzed(pkmnATK)) {
                    actionText.put(Key.Attack, pkmnATK.getSurname() + " is paralyzed! It can't move!"); break;
                } else if (!canAttackIfInfatuated(pkmnATK)) {
                    actionText.put(Key.Attack, pkmnATK.getSurname()+" is infatuated of "+pkmnDEF.getSurname()); break;
                } else if (pkmnATK.getStatus() == Pokemon.Status.Asleep) {
                    actionText.put(Key.Attack, pkmnATK.getSurname()+" is fast asleep!"); break;
                } else if (pkmnATK.getIfFlinched()) {
                    actionText.put(Key.Attack, pkmnATK.getSurname()+" flinched!"); break;
                } else {
                    if (canAttackIfConfused(pkmnATK)) {
                        if (!"Switch".equals(move.getName())) {
                            actionText.put(Key.Attack, pkmnATK.getSurname()+" use "+move.getName()+".");
                        }
                        if (move.getType() != Move.TypeOfAttacks.Status) {
                            damage.add(calcDamage(pkmnATK, pkmnDEF, move));
                            //feature (?)
                            actionText.put(Key.Defense, pkmnDEF.getSurname()+" loses "+damage+".");
                            pkmnDEF.takeDamage(damage.get(i));
                        }
                        boolean noChange1 = calcEffect(pkmnATK, pkmnDEF, move);
                        boolean noChange2 = setWeather(move);
                        if (!noChange1 && !noChange2 && damage.isEmpty()) {
                            if (!"Switch".equals(move.getName())) {
                                actionText.put(Key.Nothing, "Nothing happened...");
                            }
                        }
                    } else if (!canAttackIfConfused(pkmnATK)) {
                        damage.add(takeRecoilFromConfusion(pkmnATK));
                        pkmnATK.takeDamage(damage.get(i));
                        actionText.put(Key.Attack, pkmnATK.getSurname() + " hit itself on its confusion!");
                        break;
                    }
                }
            }
        }
        if (n > 1) {
            actionText.put(Key.CountHit, "Hit "+n+" times!");
        }
    }
    
    //
    private ArrayList<Pokemon> checkIfKO(Pokemon pkmnATK, Pokemon pkmnDEF) {
        ArrayList<Pokemon> pkmn = new ArrayList<>();
            if (pkmnATK.getStat("HP") <= 0) {
                pkmnATK.defeat(); pkmn.add(pkmnATK);
                actionText.put(Key.KO, pkmnATK.getSurname() + " is defeat.");
                return pkmn;
            } if (pkmnDEF.getStat("HP") <= 0) {
                pkmnDEF.defeat(); pkmn.add(pkmnDEF);
                actionText.put(Key.KO, pkmnDEF.getSurname() + " is defeat.");
                return pkmn;
            }
        return null;
    }

    //
    private void calcSpeedPriority2(Pokemon playerPkmn, Move playerMove, Pokemon enemyPkmn, Move enemyMove) {
        int pkmnSPD1 = playerPkmn.getTempBaseStat(StatsOfAttacks.Spd);
        if (playerPkmn.getStatus() == Pokemon.Status.Paralysis) {
            pkmnSPD1 /= 4;
        }
        int pkmnSPD2 = enemyPkmn.getTempBaseStat(StatsOfAttacks.Spd);
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
    
    //
    private int calcDamage(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        int maxCritical = 16;
        int random;
        double attack = 0, defense = 0;
        double STAB = 1, critical = 1;
        double multiplier, N, modificator;
        double weatherMult = 1, itemMult = 1;
        if (move.getType() == Move.TypeOfAttacks.Physical) {
            attack = pkmnATK.getTempBaseStat(StatsOfAttacks.Atk);
            if (pkmnATK.getStatus() == Pokemon.Status.Burn) {
                attack /= 2;
            }
            defense = pkmnDEF.getTempBaseStat(StatsOfAttacks.Def);
        } else if (move.getType() == Move.TypeOfAttacks.Special) {
            attack = pkmnATK.getTempBaseStat(StatsOfAttacks.Atk);
            defense = pkmnDEF.getTempBaseStat(StatsOfAttacks.Def);
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
            if (this.weather != null) switch (this.weather) {
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
            actionText.put(Key.Critical, "Critical Hit!");
            if (attack < pkmnATK.getStat("Atk")) {
                attack = pkmnATK.getStat("Atk");
            }
            if (defense > pkmnDEF.getStat("Def")) {
                defense = pkmnDEF.getStat("Def");
            }
        }
        multiplier = move.moveEffect(pkmnDEF.getFirstType(), pkmnDEF.getSecondType(), pkmnATK.getIfPowered());
        if (multiplier == 0) actionText.put(Key.Effect, "It's not effective...");
        else if (multiplier == 0.25) actionText.put(Key.Effect, "It's not really effective...");
        else if (multiplier == 0.5) actionText.put(Key.Effect, "It's not very effective...!");
        else if (multiplier == 2) actionText.put(Key.Effect, "It's effective!");
        else if (multiplier == 4) actionText.put(Key.Effect, "It's super effective!!");
        N = (rand.nextInt((100 - 85) + 1) + 85);
        modificator = weatherMult * itemMult;
        
        int damages = (int) ((((2 * pkmnATK.getStat("Level") + 10) * attack * move.getPower() / (250 * defense)) + 2)
                * multiplier * STAB * modificator * critical * (N /= 100));
        return damages;
    }
    
    //If the confusion flag is on, it receive a damage
    //from a move which has a power of 40 and no Type
    private int takeRecoilFromConfusion(Pokemon pkmn) {
        double attack = pkmn.getTempBaseStat(StatsOfAttacks.Atk), defense = pkmn.getTempBaseStat(StatsOfAttacks.Def);
        int N = (rand.nextInt((100 - 85) + 1) + 85);
        int damages = (int) ((((2 * pkmn.getStat("Level") + 10) * attack * BASIC_POWER / (250 * defense)) + 2) * (N /= 100));
        return damages;
    }
    
    //Controll if move dealt stats, status or other to self or other Pokemons
    private boolean calcEffect(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        Pokemon pkmn; boolean temp = false; int N;
//        for (StatsOfAttacks stat: move.getStatLevelSet()) {
//            if (stat != null) {
                N = (rand.nextInt((100 - 0) + 1) + 0);
                if (N <= move.getStatsPercentage() || move.getStatsPercentage() == 100) {
                    printStats(pkmnATK, pkmnDEF, move);
                    temp = true;
                }
//            }
//        }
        if (move.getStatus() != null) {
            N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                printStatus(pkmnATK, pkmnDEF, move);
                temp = true;
            }
        }
        if (move.getIfConfuse()) {
            N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                if (move.getAreaOfStatus() == Move.Area.Self) { pkmn = pkmnATK; }
                else { pkmn = pkmnDEF; }
                if (!pkmn.getIfConfused()) {
                    pkmn.setIfConfused(true);
                }
                actionText.put(Key.Status, pkmn.getSurname()+" is confused!");
                temp = true;
            }
        }
        if (move.getIfInfatuated()) {
            if ((pkmnATK.getIfAsessual() == true && pkmnDEF.getIfAsessual() == true) || 
                    (pkmnATK.getIfMale() != pkmnDEF.getIfMale())) {
                N = (rand.nextInt((100 - 0) + 1) + 0);
                if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                    if (move.getAreaOfStatus() == Move.Area.Self) { pkmn = pkmnATK; }
                    else { pkmn = pkmnDEF; }
                    if (!pkmn.getIfInfatuated()) {
                        pkmn.setIfInfatuated(true);
                    }
                    temp = true;
                }
            }
        } 
        if (move.getIfFlinch()) {
            N = (rand.nextInt((100 - 0) + 1) + 0);
            if (N <= move.getStatusPercentage() || move.getStatusPercentage() == 100) {
                if (move.getAreaOfStatus() == Move.Area.Self) { pkmn = pkmnATK; }
                else { pkmn = pkmnDEF; }
                if (!pkmn.getIfFlinched()) {
                    pkmn.setIfFlinched(true);
                }
                temp = true;
            }
        }
        return temp;
    }
    //
    private void printStatus(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        String string; Pokemon pkmn;
        if (move.getAreaOfStatus() == Move.Area.Self) { pkmn = pkmnATK; }
        else { pkmn = pkmnDEF; }
        
        if (pkmn.getStatus() == Pokemon.Status.OK) {
            pkmn.setStatus(move);
            switch (pkmn.getStatus()) {
                case Asleep:
                    actionText.put(Key.Status, pkmn.getSurname()+" fell asleep...");
                    break;
                case BadPoison:
                    actionText.put(Key.Status, pkmn.getSurname()+" is badly poisoned!");
                    break;
                case Burn:
                    actionText.put(Key.Status, pkmn.getSurname()+" was burned!");
                    break;
                case Freeze:
                    actionText.put(Key.Status, pkmn.getSurname()+" was frozen solid!");
                    break;
                case Paralysis:
                    actionText.put(Key.Status, pkmn.getSurname()+" paralyzed!<br>It may unable to move!");
                    break;
                case Poison:
                    actionText.put(Key.Status, pkmn.getSurname()+" was poisoned!");
                    break;
                default:
                    break;
            }
        } else {
            string = pkmn.getSurname()+" is already ";
            actionText.put(Key.Status, string+pkmn.getStatus());
        }
    }
    //
    private void printStats(Pokemon pkmnATK, Pokemon pkmnDEF, Move move) {
        String string = ""; Pokemon pkmn; char split = '§';
        if (move.getAreaOfStatus() == Move.Area.Self) { pkmn = pkmnATK; }
        else { pkmn = pkmnDEF; }
        
        for (Map.Entry<StatsOfAttacks, Integer> statLvl : move.getStatLevelSet()) {
            if (statLvl.getValue() != 0) {
                string += pkmn.getSurname()+"'s "+pkmn.getStatName(statLvl.getKey().toString()+"<br>");
                if (pkmn.getLevelStats(statLvl.getKey()) >= 6) {
                    actionText.put(Key.Stats, string+"won't go any higher!");
                } else if (pkmn.getLevelStats(statLvl.getKey()) <= -6) {
                    actionText.put(Key.Stats, string+"won't go any lower!");
                } else {
                    pkmn.setTempStats(move);
                    switch (statLvl.getValue()) {
                        case -3:
                            actionText.put(Key.Stats, string+" severely fell!");
                            break;
                        case -2:
                            actionText.put(Key.Stats, string+" harshly fell!");
                            break;
                        case -1:
                            actionText.put(Key.Stats, string+" fell!");
                            break;
                        case 3:
                            actionText.put(Key.Stats, string+" rose drastically!");
                            break;
                        case 2:
                            actionText.put(Key.Stats, string+" rose sharply!");
                            break;
                        case 1:
                            actionText.put(Key.Stats, string+" rose!");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    
    //Set weather of Engine with the move
    private boolean setWeather(Move move) {
        boolean temp = false;
        if (move.getWeather() != null) {
            temp = true;
            switch (move.getWeather()) {
                case Hail:
                    weather = Weather.Hail;
                    weatherClock = move.getWeatherRound();
                    actionText.put(Key.WeatherStart, "It started to hail!");
                    break;
                case Rainy:
                    weather = Weather.Rainy;
                    weatherClock = move.getWeatherRound();
                    actionText.put(Key.WeatherStart, "It started to rain!");
                    break;
                case SandStorm:
                    weather = Weather.SandStorm;
                    weatherClock = move.getWeatherRound();
                    actionText.put(Key.WeatherStart, "A sandstorm kicked up!");
                    break;
                case Sunny:
                    weather = Weather.Sunny;
                    weatherClock = move.getWeatherRound();
                    actionText.put(Key.WeatherStart, "The sunlight turned harsh!");
                    break;
                case Reset:
                    weather = Weather.Normal;
                    weatherClock = move.getWeatherRound();
                    actionText.put(Key.WeatherStart, "The weather became clear.");
                    break;
                default:
                    temp = false;
                    break;
            }
        }
        return temp;
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
            boolean stop = false;
            --weatherClock;
            if (weatherClock == 0) {
                stop = true;
            }
            printWeather(stop);
        }
        return checkIfKO(playerPkmn, enemyPkmn);
    }
    
    private void printWeather(boolean stop) {
        switch (weather) {
            case Hail:
                if (!stop) {
                    actionText.put(Key.Weather, "Hail continues to fall.");
                } else {
                    actionText.put(Key.Weather, "The hail stopped.");
                }
                break;
            case Rainy:
                if (!stop) {
                    actionText.put(Key.Weather, "Rain continues to fall.");
                } else {
                    actionText.put(Key.Weather, "The rain stopped.");
                }
                break;
            case SandStorm:
                if (!stop) {
                    actionText.put(Key.Weather, "The sandstorm rages.");
                } else {
                    actionText.put(Key.Weather, "The sandstorm subsided.");
                }
                break;
            case Sunny:
                if (!stop) {
                    actionText.put(Key.Weather, "The sunlight is strong.");
                } else {
                    actionText.put(Key.Weather, "The sunlight faded.");
                }
                break;
//            case Normal:
//                if (!stop) {
//                    actionText.put(Key.Weather, "The weather became clear.");
//                }
//                break;
            default:
                break;
        }
        if (stop) {
            weather = Weather.Normal;
        }
    }
    
    //If Pokemon has malus it takes relative damage
    private int givePSNorBRNDamages(Pokemon pkmn) {
        if (null != pkmn.getStatus()) {
            int statusDamage = 0;
            switch (pkmn.getStatus()) {
                case Poison:
                    statusDamage = pkmn.getStat("MaxHP") / 8;
                    if (statusDamage == 0) statusDamage = 1;
                    pkmn.takeDamage(statusDamage);
                    actionText.put(Key.Damage, pkmn.getSurname()+" is hurt by poison!");
                    break;
                case BadPoison:
                    statusDamage = (pkmn.getStat("MaxHP") / 16) * pkmn.getRoundBadPSN();
                    if (statusDamage == 0) statusDamage = pkmn.getRoundBadPSN();
                    pkmn.takeDamage(statusDamage);
                    actionText.put(Key.Damage, pkmn.getSurname()+" is hurt by poison!");
                    pkmn.increaseRoundBPSN();
                    break;
                case Burn:
                    statusDamage = pkmn.getStat("MaxHP") / 8;
                    if (statusDamage == 0) statusDamage = 1;
                    pkmn.takeDamage(statusDamage);
                    actionText.put(Key.Damage, pkmn.getSurname()+" is hurt by its burn!");
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
        actionText.put(Key.Switch, "Go " + trn.getParty().getPkmn(0).getSurname()+".");
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
    /**
     *
     * @param key
     * @return
     */
    public String getActionText(Key key) {
        if (actionText.containsKey(key)) {
            return actionText.get(key);
        } else {
            return "";
        }
    }
    /**
     *
     * @param key
     * @return
     */
    public boolean isKeyContain(Key key) {
        return actionText.containsKey(key);
    }
    /**
     *
     */
    public void eraseAction() {
        actionText.clear();
    }
}
