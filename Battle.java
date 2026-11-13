/* Clanker generated */
import java.util.ArrayList;
import java.util.Scanner;
 
/**
 * Battle: turn-based fight between the human and a CPU opponent.
 *
 * Each round both sides secretly pick a Person card from their hand.
 *  - Damage dealt = (Person attack * team multiplier) - (enemy card defense + enemy furniture defense), minimum 1
 *  - Team multiplier = 1 + sum of the multipliers of every Furniture card in that hand
 *  - Both attacks land at the same time. First side at 0 HP loses.
 */
public class Battle {
    public static final int HAND_SIZE = 5;
    public static final int START_HEALTH = 100;
    public static final int WIN_REWARD = 150;
 
    private final HumanPlayer human;
    private final CPUPlayer cpu;
    private final Scanner input;
 
    public Battle(HumanPlayer human, CPUPlayer cpu, Scanner input) {
        this.human = human;
        this.cpu = cpu;
        this.input = input;
    }
 
    /** Runs the whole fight. Returns true if the human won. */
    public boolean fight() {
        ArrayList<Cards> humanHand = human.getHand();
        ArrayList<Cards> cpuHand = cpu.getHand();
 
        ArrayList<Person_Card> humanFighters = personCards(humanHand);
        ArrayList<Person_Card> cpuFighters = personCards(cpuHand);
 
        if (humanFighters.isEmpty() || cpuFighters.isEmpty()) {
            System.out.println("A side has no Person cards to fight with. Battle cancelled.");
            return false;
        }
 
        int humanMult = teamMultiplier(humanHand);
        int cpuMult = teamMultiplier(cpuHand);
        int humanFurnDef = furnitureDefense(humanHand);
        int cpuFurnDef = furnitureDefense(cpuHand);
 
        System.out.println("\n=== BATTLE START: " + human.getName() + " vs " + cpu.getName() + " ===");
        printHand("Your hand", humanHand);
        System.out.println("Your furniture bonus: x" + humanMult + " attack, +" + humanFurnDef + " defense");
        System.out.println("Enemy furniture bonus: x" + cpuMult + " attack, +" + cpuFurnDef + " defense");
 
        int round = 1;
        while (!human.isDefeated() && !cpu.isDefeated()) {
            System.out.println("\n--- Round " + round + " ---");
            System.out.println("Your HP: " + human.getHealth() + " | " + cpu.getName() + " HP: " + cpu.getHealth());
 
            Person_Card mine = chooseCard(humanFighters);
            Person_Card theirs = cpuFighters.get((int) (Math.random() * cpuFighters.size()));
            System.out.println(cpu.getName() + " plays: " + theirs.getName());
 
            // work out both hits first so the attacks are simultaneous
            int toCpu = Math.max(1, mine.calculateDmg(humanMult) - (theirs.getDefense() + cpuFurnDef));
            int toHuman = Math.max(1, theirs.calculateDmg(cpuMult) - (mine.getDefense() + humanFurnDef));
 
            human.attack(cpu, toCpu);
            cpu.attack(human, toHuman);
 
            System.out.println(mine.getName() + " hits for " + toCpu + "!");
            System.out.println(theirs.getName() + " hits you for " + toHuman + "!");
            round++;
        }
 
        boolean won = cpu.isDefeated() && !human.isDefeated();
        human.addRecord(cpu); // true in the record = the opponent was defeated
 
        System.out.println("\n=== BATTLE OVER ===");
        if (won) {
            System.out.println("You win! +$" + WIN_REWARD);
        } else if (human.isDefeated() && cpu.isDefeated()) {
            System.out.println("Double knockout. It's a draw.");
        } else {
            System.out.println("You lost. Better luck next time!");
        }
        return won;
    }
 
    private Person_Card chooseCard(ArrayList<Person_Card> fighters) {
        while (true) {
            System.out.println("Choose a card to play:");
            for (int i = 0; i < fighters.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + fighters.get(i));
            }
            System.out.print("> ");
            try {
                int pick = Integer.parseInt(input.nextLine().trim());
                if (pick >= 1 && pick <= fighters.size()) {
                    return fighters.get(pick - 1);
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter a number from 1 to " + fighters.size() + ".");
        }
    }
 
    private static ArrayList<Person_Card> personCards(ArrayList<Cards> hand) {
        ArrayList<Person_Card> result = new ArrayList<>();
        for (Cards c : hand) {
            if (c instanceof Person_Card) {
                result.add((Person_Card) c);
            }
        }
        return result;
    }
 
    private static int teamMultiplier(ArrayList<Cards> hand) {
        int mult = 1;
        for (Cards c : hand) {
            if (c instanceof Furniture_Card) {
                mult += ((Furniture_Card) c).getMultiplier();
            }
        }
        return mult;
    }
 
    private static int furnitureDefense(ArrayList<Cards> hand) {
        int def = 0;
        for (Cards c : hand) {
            if (c instanceof Furniture_Card) {
                def += c.getDefense();
            }
        }
        return def;
    }
 
    private static void printHand(String label, ArrayList<Cards> hand) {
        System.out.println(label + ":");
        for (Cards c : hand) {
            System.out.println("  " + c);
        }
    }
}