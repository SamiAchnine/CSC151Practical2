public class Cards {
    private String ID;
    private String name;
    private String description;
    private String rarity;
    private int defense;

    Cards(String id, String name, String desc, String rare, int def) {
        this.ID = id;
        this.name = name;
        this.description = desc;
        this.rarity = rare;
        this.defense = def;
    }

    public String toString() {
        return "ID: " + ID + "Name: " + name + "Description: " + description
        + "Rarity: " + rarity + "Defense: " + defense;
    }

    /** ID getter
      returns @ID (String)
    **/
    public String getId() {
        return ID;
    }

    /** name getter
      returns @name (String)
    **/
    public String getName() {
        return name;
    }

    /** description getter
      returns @description (String)
    **/
    public String getDesc() {
        return description;
    }

    /** rarity getter
      returns @rarity (String)
    **/
    public String getRarity() {
        return rarity;
    }

    /** defense getter
      returns @defense (int)
    **/
    public int getDefense() {
        return defense;
    }
}

class Person_Card extends Cards {
    private int attack;
    Person_Card(String id, String name, String desc, String rare, int def, int atk) {
        super(id, name, desc, rare, def); 
        this.attack = atk;
    }

    @Override 
    public String toString() {
        return super.toString() + "Attack: " + attack;
    }

    /** attack getter
      returns @attack (int)
    **/
    public int getAttack() {
        return attack;
    }

    public int calculateDmg(int multiplier) {
        return attack * multiplier;
    }
}

/* Furniture cards don't have attack. They only have defense; if they are in a person's hand they add a multiplier to the Person cards. */
class Furniture_Card extends Cards {
    private int multiplier;
    Furniture_Card(String id, String name, String desc, String rare, int def, int mult) {
        super(id, name, desc, rare, def); 
        this.multiplier = mult;
    }

    @Override 
    public String toString() {
        return super.toString() + "Current Multiplier:" + multiplier;
    }

    /** multiplier getter
      returns @multiplier (int)
    **/
    public int getMultiplier() {
        return multiplier;
    }

    /* still need to code the logic for the multiplier buff IF in hand */
}