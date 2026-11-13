import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean debug = false;
        int money = 500;
        ArrayList<Cards> collection = new ArrayList<>();
        String name = "";
        Scanner input = new Scanner(System.in);
        Gacha gacha = new Gacha();
        HumanPlayer human = new HumanPlayer(name, Battle.START_HEALTH, collection);


        Object[] saveData; 
        File saveDataFile = new File("game.wecib");
        if (!saveDataFile.exists()) {
            try {
                SaveDataManager.createSaveData();
                System.out.println("Creating save data...");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            try {
                saveData = SaveDataManager.readSaveData();
                money = Integer.parseInt((String)saveData[0]);
                name = (String)saveData[1];
                collection = (java.util.ArrayList<Cards>)saveData[2];
            }
            catch (Exception e) {
                System.out.print("Save data could not be properly loaded. The game will now exit. Please validate the save file, and fix it, or delete it.");
                e.printStackTrace();
                System.exit(74); // exit code 74 -> IO error
            }
            
        }
        
        System.out.print("Welcome to the WECIB Gacha Battle Game!\n");

        if (name.equals("") && collection.isEmpty()) {
            // FTUE
            System.out.print("Enter your name to continue: ");
            name = input.nextLine();

            System.out.println("You start with $" + money + " and 6 free pulls to get you started.");
            for (int i = 1; i <= 6; i++) {
                System.out.print("Free pull #" + i + ". Press enter to pull!");
                input.nextLine();
                addPull(gacha, collection);
            }

            saveGame(money, name, collection, debug);
        }
        else {
            System.out.println("Hi, " + name + "!");
            System.out.println("Your save data has been successfully loaded.");
        }

        // main loop
        boolean playing = true;
    
        while (playing) {
            System.out.println("Money: " + money);
            System.out.println("Please select an option: ");
            System.out.println("\n1. Play the gacha");
            System.out.println("2. Enter Battle");
            System.out.println("3. View collection");
            System.out.println("4. Save & Exit");
            System.out.print("(1-4): ");

            String answer = input.nextLine();

            if (answer.equals("1")) {
                // gacha pull logic
                if (money >= Gacha.PULL_COST) {
                    money -= Gacha.PULL_COST;
                    addPull(gacha, collection);
                    human.setCollection(collection);
                    saveGame(money, name, collection, debug);
                } else {
                    System.out.println("Not enough money!");
                }

            } else if (answer.equals("2")) {
                // battle logic
                CPUPlayer cpu = new CPUPlayer("Charles P. Underwood", Battle.START_HEALTH);

                // CPU creates a collection of 1 card (will get overwritten later)
                cpu.drawCardIntoHand(collection);

                Battle battle = new Battle(human, cpu, input);

                if (battle.fight()) {
                    money += Battle.WIN_REWARD;
                }

            } else if (answer.equals("3")) {
                // collection viewer
                System.out.println("\nYour collection: " + collection.size());

                for (Cards c : collection) {
                    System.out.println(c);
                }

            } else if (answer.equals("4")) {
                saveGame(money, name, collection, debug);
                
                // ends game
                playing = false;
            } 
            else if (answer.equals("DELETE SAVE")) {
                System.out.println("This is going to delete all game saves. Okay?");
                System.out.print("Press Enter to confirm. ");
                input.nextLine();
                saveDataFile.delete();
                System.out.println("The save file was deleted. The game will now close.");
                System.out.print("Press Enter to confirm. ");
                input.nextLine();
                System.exit(0); // exit code 0 -> success
            }
            else if (answer.equals("99")) {
                // allows you to use view save points
                debug = true;
                System.out.println("--- WARNING: Save Debug Enabled ---");
            }
            else {
                System.out.println("Invalid option!");
            }

            
            // after every option, the game saves
            saveGame(money, name, collection, debug);
        }

    }

    private static void addPull(Gacha gacha, ArrayList<Cards> collection) {
        Cards pulled = gacha.pull();
        if (pulled == null) {
            System.out.print("Cannot read card file.");
            return;
        } else {
            collection.add(pulled);
            System.out.println("\nYou got: " + pulled);
        }
    }

    private static void saveGame(int money, String name, ArrayList<Cards> collection, boolean debug) {
        Object[] dataToSave = {money, name, collection};
        SaveDataManager.saveDataRecieve(dataToSave);
        if (debug) {
            System.out.println("Game saved!");
        }
    }
}