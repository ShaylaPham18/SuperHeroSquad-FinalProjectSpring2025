package loader;
import model.*;
 /**
 *In this file we should add all the loader methods of the files to read the files
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Razan, Justin, Shayla

/**
 * This is where the external files are being loaded: puzzles.txt, rooms.txt, items.txt
 */

public class FileLoader {

    public static List<Puzzle> loadPuzzles(String filePath) throws IOException {
        List<Puzzle> puzzles = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 8) {
                String name = parts[0].trim();
                String description = parts[1].trim();
                String roomLocation = parts[2].trim();
                String correctAnswer = parts[3].trim();
                String resultWhenSolved = parts[4].trim();
                int maxAttempts = Integer.parseInt(parts[5].trim());
                String hint = parts[6].trim();
                String requiredItem = parts[7].trim();

                Puzzle puzzle = new Puzzle(name, description, roomLocation, correctAnswer, resultWhenSolved, maxAttempts, hint);
                puzzle.setRequiredItem(requiredItem);
                puzzles.add(puzzle);
            } else {
                System.err.println("⚠️ Invalid puzzle format: " + line);
            }
        }

        reader.close();
        return puzzles;
    }


    Map<String, Room> roomMap = new HashMap<>();
    //justin
    public Map<String, Room> readRooms(){
        Map<String, String> roomExits = new HashMap<>();
        try {
            //you guys use this 1 PLEASE JUST COMMENT AND UNCOMMENT
            BufferedReader bufferedReader = new BufferedReader(new FileReader("room.txt"));
            //BufferedReader bufferedReader = new BufferedReader(new FileReader("FinalGame_Project/room.txt"));
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",",6);
                String roomId = parts[0].trim();
                String roomName = parts[1].trim();
                String roomDescription = parts[2].trim();

                Room room = new Room(roomId, roomName, roomDescription);
                roomMap.put(roomId, room);
                if (parts.length==6){
                    String locked=parts[4].trim().toLowerCase();
                    String unlockItem=parts[5].trim().toLowerCase();
                    if (locked.equalsIgnoreCase("lock")){
                        room.setRoomIsLocked(true);
                        room.setUnlockItem(unlockItem);
                    }
                }

                if (parts.length > 3) {
                    roomExits.put(roomId, parts[3].trim());
                }
            }
            bufferedReader.close();
            for (Map.Entry<String, String> entry : roomExits.entrySet()) {
                String roomID2 = entry.getKey();
                String[] exitSplit = entry.getValue().split("\\|");

                Room currentRoom = roomMap.get(roomID2);
                for (String exit : exitSplit) {
                    String[] exitSplitParts = exit.split("->");
                    if (exitSplitParts.length == 2) {
                        String exitDirection = exitSplitParts[0].trim().toUpperCase();
                        String roomGoingTo = exitSplitParts[1].trim();
                        Room exitRoom = roomMap.get(roomGoingTo);
                        if (exitRoom != null) {
                            currentRoom.setExits(exitDirection, exitRoom);
                        } else {
                            System.err.println("Warning: Exit room " + roomGoingTo + " not found for " + roomID2);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return roomMap;
    }

    //Shayla

    /**
     * This loads items from items.txt and puts them in the rooms they are supposed to be in
     *
     * @param filePath items.txt in  GameMain
     * @param rooms map of the room IDs to Room objects
     */
    public static void loadItems(String filePath, Map<String, Room> rooms) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); //Header in the text file
            while ((line = reader.readLine()) != null) {

                //Parsed through | and there are 7 different parts
                String[] parts = line.split("\\|");
                if (parts.length < 7) continue;
                int itemId = Integer.parseInt(parts[0].trim());
                String itemName = parts[1].trim();
                String itemType = parts[2].trim().toLowerCase();
                int itemStat = Integer.parseInt(parts[3].trim());
                String itemDescription = parts[4].trim();
                String roomId = parts[5].trim();
                int quantity = Integer.parseInt(parts[6].trim());

                Room room = rooms.get(roomId);
                if (room == null) {
                    System.err.println("Room not found for ID: " + roomId);
                    continue;
                }

                //Creating multiple instances of an item so that every item is its own instance
                //easier to handle when you want to use one out of the 5 in your inventory
                for (int i = 0; i < quantity; i++) {
                    Items item;
                    switch (itemType) {
                        case "consumable" -> item = new Consumables(
                                itemId, itemName, itemType, itemStat, itemDescription, roomId, 1, itemStat);
                        case "ammunition" -> item = new Ammunition(
                                itemId, itemName, itemType, itemStat, itemDescription, roomId, 1, itemStat);
                        default -> item = new Items(
                                itemId, itemName, itemType, itemStat, itemDescription, roomId, 1);
                    }
                    room.getRoomInventory().add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
    }
}
