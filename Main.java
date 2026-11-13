import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int money = 500;
        ArrayList<Cards> collection = new ArrayList<>();
        String name = "";


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
            saveData = SaveDataManager.readSaveData();
            money = Integer.parseInt((String)saveData[0]);
            name = (String)saveData[1];
            collection = (java.util.ArrayList<Cards>)saveData[2];
        }
        
        System.out.print("Welcome to the WECIB Gacha Battle Game!\n");
        Scanner input = new Scanner(System.in);
        Gacha gacha = new Gacha();

        if (name.equals("") && collection.isEmpty()) {
            // FTUE
            System.out.print("Enter your name to continue: ");
            name = input.next();

            System.out.println("You start with $" + money + " and 6 free pulls to get you started.");
            for (int i = 1; i <= 6; i++) {
                System.out.print("Free pull #" + i + ". Press enter to pull!");
                input.nextLine();
                addPull(gacha, collection);
            }

            saveGame(money, name, collection);
        }
        else {
            System.out.println("Hi, " + name + "!");
        }

        // main loop
        while (money >= Gacha.PULL_COST) {
            System.out.print("Your current balance is $" + money + ". Would you like to pull again for $" + Gacha.PULL_COST + "? (Y/N)");
            String answer = input.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                break;
            } else {
                money -= Gacha.PULL_COST;
                addPull(gacha, collection);
            }
        }

        // prints collection if the player denies pulling with money
        System.out.println("\nYour collection: " + collection.size());
        for (Cards c : collection) {
            System.out.println(" " + c);
        }

        // saves game upon exit
        saveGame(money, name, collection);
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

    private static void saveGame(int money, String name, ArrayList<Cards> collection) {
        Object[] dataToSave = {money, name, collection};
        SaveDataManager.saveDataRecieve(dataToSave);
    }
}