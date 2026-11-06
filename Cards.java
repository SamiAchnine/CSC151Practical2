public class Cards {
    private int ID;
    private String name;
    private String description;
    private String rarity;
    private int defense;

    Cards(int id, String name, String desc, String rare, int def) {
        this.ID = id;
        this.name = name;
        this.description = desc;
        this.rarity = rare;
        this.defense = def;
    }

    /** ID getter
      returns @ID (int)
    **/
    public int getId() {
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
    Person_Card(int id, String name, String desc, String rare, int def, int atk) {
        super(id, name, desc, rare, def); 
        this.attack = atk;
    }

    /** attack getter
      returns @attack (int)
    **/
    public int getAttack() {
        return attack;
    }
}

/* Furniture cards don't have attack. They only have defense; if they are in a person's hand they add a multiplier to the Person cards. */
class Furniture_Card extends Cards {
    private int multiplier;
    Furniture_Card(int id, String name, String desc, String rare, int def, int mult) {
        super(id, name, desc, rare, def); 
        this.multiplier = mult;
    }

    /** multiplier getter
      returns @multiplier (int)
    **/
    public int getMultiplier() {
        return multiplier;
    }
}