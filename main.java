import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int money = 500;
        ArrayList<Cards> collection = new ArrayList<>();


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
            money = (Integer)saveData[0];
            collection = (java.util.ArrayList<Cards>)saveData[1];
        }
        
        System.out.print("Welcome to the WECIB Gacha Battle Game!");
        Scanner input = new Scanner(System.in);
        Gacha gacha = new Gacha();


        System.out.print("You start with $" + money + "and 6 free pulls to get you started.");
        for (int i = 1; i <= 6; i++) {
            System.out.print("Free pull #" + i + ". Press enter to pull!");
            input.nextLine();
            addPull(gacha, collection);
        }

        while (money >= Gacha.PULL_COST) {
            System.out.print("Your current balance is $" + money + ". Pull again for $" + Gacha.PULL_COST + "? (Y/N)");
            String answer = input.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                break;
            } else {
                money -= Gacha.PULL_COST;
                addPull(gacha, collection);
            }
        }

        System.out.println("\nYour collection: " + collection.size());
        for (Cards c : collection) {
            System.out.println(" " + c);
        }
        Object[] dataToSave = {money, collection};
        SaveDataManager.saveDataRecieve(dataToSave);
    }

    private static void addPull(Gacha gacha, ArrayList<Cards> collection) {
        Cards pulled = gacha.pull();
        if (pulled == null) {
            System.out.print("Cannot read card file.");
            return;
        } else {
            collection.add(pulled);
            System.out.println("You got: " + pulled + "\n");
        }
    }
}