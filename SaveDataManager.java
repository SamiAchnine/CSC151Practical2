// comments added by GPT and modified to be accurate

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
            output.close(); // Close the writer to ensure the header is saved and the file is released.
            return true;
        }
    }

    public static void saveDataRecieve(Object[] data) {
        // The handling of the different cards was created by GPT
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("game.wecib"))) {
            writer.write("GACHASAVE1.0");
            writer.newLine();

            for (Object value : data) {

                // ArrayLists need special handling because their elements may be custom objects.
                // ArrayList<?> is an arraylist which contains any type -Sami
                if (value instanceof java.util.ArrayList<?>) {
                    java.util.ArrayList<?> list = (java.util.ArrayList<?>) value;

                    // Write the number of cards so the reading method knows how many to reconstruct.
                    Integer listSize = new Integer(list.size());
                    writer.write(listSize.toString());
                    writer.newLine();

                    for (Object element : list) {

                        // Check subclasses before their superclass so their additional variables are saved.
                        if (element instanceof Person_Card) {
                            Person_Card card = (Person_Card) element;

                            writer.write("PERSON");
                            writer.newLine();

                            writer.write(card.getId()); writer.newLine();
                            writer.write(card.getName()); writer.newLine();
                            writer.write(card.getDesc()); writer.newLine();
                            writer.write(card.getRarity()); writer.newLine();
                            writer.write(String.valueOf(card.getDefense())); writer.newLine();
                            writer.write(String.valueOf(card.getAttack())); writer.newLine();
                        }
                        else if (element instanceof Furniture_Card) {
                            Furniture_Card card = (Furniture_Card) element;

                            writer.write("FURNITURE"); writer.newLine();

                            writer.write(card.getId()); writer.newLine();
                            writer.write(card.getName()); writer.newLine();
                            writer.write(card.getDesc()); writer.newLine();
                            writer.write(card.getRarity()); writer.newLine();
                            writer.write(String.valueOf(card.getDefense())); writer.newLine();
                            writer.write(String.valueOf(card.getMultiplier())); writer.newLine();
                        }
                        else {
                            // Prevent unsupported objects from silently producing corrupt save data.
                            throw new IllegalArgumentException("Unknown card type: " +
                                (element == null ? "null" : element.getClass().getName()));
                        }
                    }
                }
                else {
                    // Normal values are stored as Strings, one per line.
                    writer.write(String.valueOf(value));
                    writer.newLine();
                }
            }

        } catch (Exception e) {
            System.out.println("Error writing save data.");
            e.printStackTrace();
        }
    }

    public static Object[] readSaveData() {
        java.util.ArrayList<Object> data = new java.util.ArrayList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("game.wecib"))) {
            String header = reader.readLine();
            if (!header.equals("GACHASAVE1.0")) {
                throw new java.io.IOException("Save file invalid");
            }
            
            while (reader.readLine() != null) {
                if (!reader.readLine().equals("FURNITURE") || !reader.readLine().equals("PERSON") ) {
                    data.add(reader.readLine());
                }
                else if (reader.readLine().equals("FURNITURE") || reader.readLine().equals("PERSON")) {
                    data.add(readCards(reader));
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toArray(new String[0]);
    }

    // This method was generated by GPT; it was generated alongside the modifications to the card reading logic
    public static java.util.ArrayList<Cards> readCards(java.io.BufferedReader reader) throws java.io.IOException {
        // Read the number of cards saved in the ArrayList.
        int cardCount = Integer.parseInt(reader.readLine());

        java.util.ArrayList<Cards> cards = new java.util.ArrayList<>();

        for (int i = 0; i < cardCount; i++) {
            // Identify the subclass before reading its subclass-specific variable.
            String cardType = reader.readLine();

            String id = reader.readLine();
            String name = reader.readLine();
            String desc = reader.readLine();
            String rare = reader.readLine();
            int def = Integer.parseInt(reader.readLine());

            if (cardType.equals("PERSON")) {
                int atk = Integer.parseInt(reader.readLine());
                cards.add(new Person_Card(id, name, desc, rare, def, atk));
            }
            else if (cardType.equals("FURNITURE")) {
                int mult = Integer.parseInt(reader.readLine());
                cards.add(new Furniture_Card(id, name, desc, rare, def, mult));
            }
            else {
                throw new java.io.IOException("Unknown card type: " + cardType);
            }
        }

        return cards;
    }
}