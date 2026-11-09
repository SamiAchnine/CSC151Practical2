public class Player {
    private String name;
    private int health;
    /* private deck deck; */
    /* private arrayList hand */

    public Player(String name, int health) {
        this.name = name;
        this.health = health;
        /* this hand = new.arraylist */
    }

    /* add method drawing a card */

    public void takeDamage(int battle_damage) {
        health -= battle_damage;
    }

    /* add boolean method here */
}

/* human player subclass */

/* cpu player subclass */