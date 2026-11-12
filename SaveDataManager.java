
public class SaveDataManager {
    public static boolean createSaveData() throws java.io.IOException {
        java.io.File saveFile = new java.io.File("game.wecib");
        if (saveFile.exists()) {
            System.out.println("Save data error! Save file already exists, but is trying to be created.");
            throw new java.io.IOException("Save file already exists, but is trying to be created.");
        }
        else {
            java.io.PrintWriter output = new java.io.PrintWriter(saveFile);
            output.println("GACHASAVE1.0");
            return true;
        }
    }

    public static void saveDataRecieve(Object[] data) {
        String[] formattedData = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            formattedData[i] = String.valueOf(data[i]);
        }
        
        saveDataWrite(formattedData);
    }

    static void saveDataWrite(String[] data) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("game.wecib"))){
            for (String value: data) {
                writer.write(value);
                writer.newLine();
            }

        } catch (Exception e) {
            System.out.println("Error writing save data.");
            e.printStackTrace();
        }

    }

    public static String[] readSaveData() {
        java.util.ArrayList<String> data = new java.util.ArrayList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("game.wecib"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("GACHASAVE1.0")) {
                    continue;
                }
                data.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toArray(new String[0]);
    }
}
