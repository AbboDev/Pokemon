package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import objects.Pokemon.Type;
import screen.Board;

import static objects.Move.StatsOfAttacks.*;

/**
 * @author Thomas
 */
public class Move {
    public enum TypeOfAttacks {
        Physical, Special, Status
    };
    public enum StatusOfAttacks {
        Poison, Paralysis, Freeze, Burn,
        BadPoisoned, Sleep
    };
    public enum StatsOfAttacks {
        Atk, Def, SpAtk, SpDef,
        Spd, Acc, Eva, Critical
    };
    public enum Area {
        Self, One, All, AllEnemies,
        EachOther, Other,
    };
    public enum WeatherOfAttacks {
        Sunny, Rainy, Hail, SandStorm, Reset
    };
    
    private Type moveType;
    private int power;
    private int fixDamage;
    private int accuracy;
    private int maxPP;
    private int PP;
    
    private int round;
    private int hitMin, hitMax;
    private int priority;
    
    private TypeOfAttacks type;
    private Area areaOfAttack;
    
    private StatusOfAttacks status;
    private int statusPercentage;
    private Area areaOfStatus;
    
    private HashMap<StatsOfAttacks, Integer> statsLevel = new HashMap<>();
    private int statsPercentage;
//    private StatsOfAttacks[] stats = new StatsOfAttacks[7];
//    private int[] statsLevel = new int[7];
    private Area areaOfStats;
    
    private WeatherOfAttacks weather;
    private int weatherRound;
    
    private String effect;
    
    //boolean
    private boolean isOneHitKO = false;
    private boolean hasFixDamage = false;
    private boolean alwaysHit = false;
        
    private boolean secondTurnRest = false;
    private boolean firstTurnRest = false;
    
    private boolean canFlinch = false;
    private boolean canConfuse = false;
    private boolean canInfatuated = false;
    
    private String name;
    public static File MOVES;
    
    private void assignFile() {
        MOVES = new File(Board.ROOT+"/resources/database/move.csv");
    }
    
    public Move (String searchName) {
        assignFile();
        BufferedReader bufferStats = null;
        String split = ";", splitMultiple = "§";
        String identifierFixD = "*", idientifierNull = "o";
        try {
            String line;
            bufferStats = new BufferedReader(new FileReader(MOVES));
            while ((line = bufferStats.readLine()) != null) {
                String[] currentLine = line.split(split);
                if (currentLine[0].equals(searchName)) {
                    name = currentLine[0];
                    moveType = setType(currentLine[1]);
                    type = setTypeOfAttack(currentLine[2]);
                    PP = maxPP = parseInteger(currentLine[3]);
                    if ("KO".equals(currentLine[4])) {
                        power = 0;
                        isOneHitKO = true;
                        accuracy = 30;
                    } else {
                        if (currentLine[4].contains(identifierFixD)) {
                            power = 0;
                            hasFixDamage = true;
                            fixDamage = parseInteger(currentLine[4].substring(1));
                        } else if (currentLine[4].contains(idientifierNull)) {
                            power = 0;
                        } else {
                            power = parseInteger(currentLine[4]);
                        }
                        if (!idientifierNull.equals(currentLine[5])) {
                            String percentage = currentLine[5].substring(0, currentLine[5].length()-1);
                            accuracy = parseInteger(percentage);
                        } else {
                            accuracy = 0;
                            alwaysHit = true;
                        }
                    }                    
                    areaOfAttack = setArea(currentLine[6]);
                    
                    if (currentLine[7].contains(splitMultiple)) {
                        String[] statusArray = currentLine[10].split(splitMultiple);
                        Random rand = new Random(); int random;
                        random = rand.nextInt((statusArray.length - 1) + 1) + 1;
                        status = setStatus(statusArray[random-1]);
                    } else if (!"".equals(currentLine[7])) {
                        status = setStatus(currentLine[7]);
                    }
                    if (!"".equals(currentLine[8])) {
                        statusPercentage = parseInteger(currentLine[8]);
                    } else {
                        statusPercentage = 0;
                    }
                    areaOfStatus = setArea(currentLine[9]);
                    
                    statsLevel.put(Atk, 0); statsLevel.put(Def, 0);
                    statsLevel.put(SpAtk, 0); statsLevel.put(SpDef, 0);
                    statsLevel.put(Spd, 0); statsLevel.put(Eva, 0);
                    statsLevel.put(Acc, 0); statsLevel.put(Critical, 0);
                    if (currentLine[10].contains(splitMultiple)) {
                        String[] statsArray = currentLine[10].split(splitMultiple);
                        String[] levelArray = currentLine[12].split(splitMultiple);
                        for (int i = 0; i < statsArray.length-1; ++i) {
                            setStatsLevel(statsArray[i], parseInteger(levelArray[i]));
                        }
                    } else if (!"".equals(currentLine[10])) {
                        setStatsLevel(currentLine[10], parseInteger(currentLine[12]));
                    }
                    if (!"".equals(currentLine[11])) {
                        statsPercentage = parseInteger(currentLine[11]);
                    } else {
                        statsPercentage = 0;
                    }
                    areaOfStats = setArea(currentLine[13]);
                    
                    weather = setWeather(currentLine[14]);
                    if (weather != WeatherOfAttacks.Reset) {
                        weatherRound = 6;
                    } else {
                        weatherRound = 0;
                    }
                    
                    priority = parseInteger(currentLine[15]);
                    try {
                        round = parseInteger(currentLine[16]);
                    } catch (Exception e) {
                        switch (currentLine[16]) {
                            case "1_o":
                                secondTurnRest = true; break;
                            case "o_2":
                                firstTurnRest = true; break;
                        }
                    }
                    if (currentLine[17].contains("_")) {
                        String[] hit = currentLine[17].split("_");
                        hitMin = parseInteger(hit[0]);
                        hitMax = parseInteger(hit[1]);
                    } else {
                        hitMin = parseInteger(currentLine[17]);
                        hitMax = hitMin;
                    }
                    
                    if (currentLine.length > 18) {
                        effect = (currentLine[18]);
                    }
                }
            }
        } catch(FileNotFoundException e) {} catch (IOException e) {}
        finally {
            if (bufferStats != null) {
                try { bufferStats.close(); } catch (IOException e) {}
            }
        }
    }

    public Type getMoveType() { return moveType; }

    public int getPower() { return power; }
    public int getFixDamage() { return fixDamage; }
    public int getRound() { return round; }
    public int getHitMin() { return hitMin; }
    public int getHitMax() { return hitMax; }
    public int getAccuracy() { return accuracy; }
    public int getPriority() { return priority; }
    
    public int getPP() { return PP; }
    public int getMaxPP() { return maxPP; }
    public void decreasePP(boolean morePP, int usedPP) {
        if (PP > 0) {
            if (morePP) {
                if (PP-usedPP > 0) {
                    PP -= usedPP;
                } else if (PP-usedPP <= 0) {
                    PP = 0;
                }
            } else {
                if (PP > 0) { --PP; }
            }
        }
    }
    public void increasePP(int givePP) {
        if (PP+givePP == maxPP) {
            PP = maxPP;
        } else {
            PP += givePP;
        }
    }
    
    public TypeOfAttacks getType() { return type; }
    public StatusOfAttacks getStatus() { return status; }
    public int getStatusPercentage() { return statusPercentage; }
    public int getStatsPercentage() { return statsPercentage; }
    public int getStatLevel(StatsOfAttacks key) { return statsLevel.get(key); }
    public Set<StatsOfAttacks> getStatsSet() { return statsLevel.keySet(); }
    public Set<Map.Entry<StatsOfAttacks, Integer>> getStatLevelSet() { return statsLevel.entrySet(); }
    public Area getAreaOfAttack() { return areaOfAttack; }
    public Area getAreaOfStats() { return areaOfStats; }
    public Area getAreaOfStatus() { return areaOfStatus; }
    public WeatherOfAttacks getWeather() { return weather; }
    public int getWeatherRound() { return weatherRound; }
    public String getEffect() { return effect; }
    
    public boolean isOneHitKO() { return isOneHitKO; }
    public boolean isFixDamage() { return hasFixDamage; }
    public boolean isAlwaysHit() { return alwaysHit; }

    public boolean isSecondTurnRest() { return secondTurnRest; }
    public boolean isFirstTurnRest() { return firstTurnRest; }

    public boolean getIfFlinch() { return canFlinch; }
    public boolean getIfConfuse() { return canConfuse; }
    public boolean getIfInfatuated() { return canInfatuated; }
        
    public String getName() { return name; }
        
    private int parseInteger(String i) {
        return Integer.parseInt(i);
    }
        
    private Type setType(String type) {
        Type tempType;
        switch (type) {
            case "Normal":
                tempType = Type.Normal; break;
            case "Fighting":
                tempType = Type.Fighting; break;
            case "Flying":
                tempType = Type.Flying; break;
            case "Rock":
                tempType = Type.Rock; break;
            case "Ground":
                tempType = Type.Ground; break;
            case "Steel":
                tempType = Type.Steel; break;
            case "Ghost":
                tempType = Type.Ghost; break;
            case "Dark":
                tempType = Type.Dark; break;
            case "Psychic":
                tempType = Type.Psychic; break;
            case "Bug":
                tempType = Type.Bug; break;
            case "Poison":
                tempType = Type.Poison; break;
            case "Grass":
                tempType = Type.Grass; break;
            case "Water":
                tempType = Type.Water; break;
            case "Ice":
                tempType = Type.Ice; break;
            case "Fire":
                tempType = Type.Fire; break;
            case "Electric":
                tempType = Type.Electric; break;
            case "Fairy":
                tempType = Type.Fairy; break;
            case "Dragon":
                tempType = Type.Dragon; break;
            case "Dinosaur":
                tempType = Type.Dinosaur; break;
            case "Light":
                tempType = Type.Light; break;
            default:
                tempType = null; break;
        }
        return tempType;
    }        
    private TypeOfAttacks setTypeOfAttack(String type) {
        TypeOfAttacks tempType;
        switch (type) {
            case "Physical":
                tempType = TypeOfAttacks.Physical; break;
            case "Special":
                tempType = TypeOfAttacks.Special; break;
            case "Status":
                tempType = TypeOfAttacks.Status; break;
            default:
                tempType = TypeOfAttacks.Status; break;
        }
        return tempType;
    }
    private StatusOfAttacks setStatus(String status) {
        StatusOfAttacks tempStatus = null;
        switch (status) {
            case "BadPsn":
                tempStatus = StatusOfAttacks.BadPoisoned; break;
            case "Burn":
                tempStatus = StatusOfAttacks.Burn; break;
            case "Freeze":
                tempStatus = StatusOfAttacks.Freeze; break;
            case "Par":
                tempStatus = StatusOfAttacks.Paralysis; break;
            case "Poison":
                tempStatus = StatusOfAttacks.Poison; break;
            case "Sleep":
                tempStatus = StatusOfAttacks.Sleep; break;
            case "Conf":
                canConfuse = true; break;
            case "Flinch":
                canFlinch = true; break;
            case "Infatuated":
                canInfatuated = true; break;
            default:
                tempStatus = null; break;
        }
        setTempStatus(status);
        return tempStatus;
    }
    private void setStatsLevel(String stat, int level) {
        switch (stat) {
            case "Atk":
                statsLevel.put(Atk, level); break;
            case "Def":
                statsLevel.put(Def, level); break;
            case "SpAtk":
                statsLevel.put(SpAtk, level); break;
            case "SpDef":
                statsLevel.put(SpDef, level); break;
            case "Spd":
                statsLevel.put(Spd, level); break;
            case "Acc":
                statsLevel.put(Acc, level); break;
            case "Eva":
                statsLevel.put(Eva, level); break;
            case "Critical":
                statsLevel.put(Critical, level); break;
            default: break;
        }
    }
    private WeatherOfAttacks setWeather(String weather) {
        WeatherOfAttacks tempWeather;
        switch (weather) {
            case "Hail":
                tempWeather = WeatherOfAttacks.Hail; break;
            case "Rainy":
                tempWeather = WeatherOfAttacks.Rainy; break;
            case "SandStorm":
                tempWeather = WeatherOfAttacks.SandStorm; break;
            case "Sunny":
                tempWeather = WeatherOfAttacks.Sunny; break;
            default:
                tempWeather = null; break;
        }
        return tempWeather;
    }
    private void setTempStatus(String status) {
        switch (status) {
            case "Confused":
                canConfuse = true; break;
            case "Flinch":
                canFlinch = true; break;
            case "Infatuated":
                canInfatuated = true; break;
            default:
                break;
        }
    }
    private Area setArea(String area) {
        Area tempArea;
        switch (area) {
            case "One":
                tempArea = Area.One; break;
            case "Self":
                tempArea = Area.Self; break;
            case "All":
                tempArea = Area.All; break;
            case "AllEnemies":
                tempArea = Area.AllEnemies; break;
            case "EachOther":
                tempArea = Area.EachOther; break;
            case "Other":
                tempArea = Area.Other; break;
            default:
                tempArea = Area.Self; break;
        }
        return tempArea;
    }
    
    public double moveEffect(Type enemyFirstType, Type enemySecondType, boolean isPoweredUp) {
        double multiplier = 1;
        Type currentType = enemyFirstType;
        //Calc of normal multiplier
        while (true) {
            switch (moveType) {
                case Normal:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Fighting:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Flying:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;				
                    }
                    break;
                case Rock:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                    }
                    break;
                case Ground:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;					
                    }
                    break;
                case Steel:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;				
                    }
                    break;
                case Ghost:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;					
                    }
                    break;
                case Dark:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;					
                    }
                    break;
                case Psychic:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Bug:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Light: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                    }
                    break;
                case Poison:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Grass:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Water: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                    }
                    break;
                case Water:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Water: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Ice:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Fire:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Electric:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Water: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Fairy:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                    }
                    break;
                case Dragon:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;					
                    }
                    break;
                case Dinosaur:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Water: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Light: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;					
                    }
                    break;
                case Light:
                    switch (currentType) {
                        case Normal: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fighting: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Flying: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Rock: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Ground: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Steel: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ghost: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Dark: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Psychic: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Bug: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Poison: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Grass: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Water: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Ice: if (isPoweredUp) { multiplier *= 3; } else { multiplier *= 2; } break;
                        case Fire: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Electric: if (isPoweredUp) { multiplier *= 0.75; } else { multiplier *= 0.5; } break;
                        case Dragon: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Fairy: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Dinosaur: if (isPoweredUp) { multiplier *= 1.5; } else { multiplier *= 1; } break;
                        case Light: if (isPoweredUp) { multiplier *= 0; } else { multiplier *= 0; } break;
                    }
                    break;
                default: break;
            }
            if (currentType == enemySecondType) {
                return multiplier;
            } else {
                currentType = enemySecondType;
            }
        }
    }
}
