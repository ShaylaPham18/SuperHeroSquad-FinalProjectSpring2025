package controller;

import loader.FileLoader;
import model.Items;
import model.Player;
import model.Puzzle;
import model.Room;
import model.Monster;
import view.ItemView;
import view.PuzzleView;
import view.Frame;
import loader.MonsterLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

public class GameController {
    private final Player player;
    private final Map<String, Room> rooms;
    private final Scanner scanner;
    private final ItemController itemController;
    private final ItemView itemView = new ItemView();
    KeyBoardShortCuts keyBoardShortCuts = new KeyBoardShortCuts();

    // Jose Montejo
    // MonsterSpawnManager to handle monster spawning logic
    private MonsterSpawnManager monsterSpawnManager;

    public GameController(Player player, Map<String, Room> rooms) {
        this.player = player;
        this.rooms = rooms;
        this.scanner = new Scanner(System.in);
        this.itemController = new ItemController(scanner, itemView, player);

        // Jose Montejo
        // Initialize monster spawning system
        try {
            List<Monster> monsters = MonsterLoader.loadMonsters("FinalGame_Project/monsters.txt");
            Map<String, List<Monster>> monstersByLocation = MonsterLoader.getMonstersByLocation(monsters);
            this.monsterSpawnManager = new MonsterSpawnManager(monstersByLocation);


        } catch (Exception e) {
            System.err.println("Error loading monsters: " + e.getMessage());
        }
    }

    //Justin, Razan, Shayla, Nelly
    //Navigation win condition block
    public void start() {
        Room current = rooms.get("1ew");
        if (current == null) {
            System.err.println("no room");
            return;
        }
        player.setCurrentRoom(current);
        System.out.println("🎮🎮🎮🎮🎮🎮🎮🎮 Welcome to The Infected Hospital! 🎮🎮🎮🎮🎮🎮🎮🎮");
        System.out.println("Type go<Direction> to navigate || or help to view all commands || or quit to end the game || explore || inspect");
        System.out.println("\nStaring room: " + current.getRoomName() + " || available exits:" + current.getExitDirections());

        boolean running = true;
        while (running) {
            // 🏁 WIN CONDITION CHECK
            if (checkWinCondition(player)) {
                System.out.println("🎉 YOU WIN! You’ve collected all keys and reached the final room!");
                running = false;
                continue;
            }

            System.out.println("Type go<Direction> to navigate || or help to view all commands || or quit to end the game || explore || inspect");
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            //quit original if statement
            if (input.equals("quit")) {
                System.out.println("👋 Thanks for playing!");
                running = false;

            }

            //help
            else if (input.equalsIgnoreCase("help")) {
                Frame frame = new Frame();
                frame.helpMenu();
            }

            //map
            else if (input.equalsIgnoreCase("map")) {
                Frame frame = new Frame();
                frame.map();
            }

            //stats
            else if (input.equalsIgnoreCase("stats") || input.equalsIgnoreCase("stat") || input.equalsIgnoreCase("st")) {
                player.showStats();
            }

            //explore
            else if (input.equalsIgnoreCase("explore") || input.equalsIgnoreCase("ex")) {
                Room room = player.getCurrentRoom();
                System.out.println("📍 " + room.getRoomName());
                System.out.println(room.getRoomDescription());
                // use explore to check if there is a puzzle in the room --> Razan
                if (room.getPuzzle() != null && !room.getPuzzle().isSolved()) {
                    System.out.println("👀 Something about this room seems tricky... Maybe try 'inspect'?");
                }

                if (!room.getRoomInventory().isEmpty()) {
                    System.out.println(" --> There seems to be stuff in this room. Perhaps you should inspect them.");
                }

                System.out.println("🚪 Exits: " + room.getExitDirections());

            }
            //navigation
            else if (input.startsWith("go")) {
                String previousRoomID = current.getRoomID(); // Store previous room ID
                current.setRoomHasBeenVisited(true);
                String orginalDirection = input.substring(2).toUpperCase().trim();
                String direction = keyBoardShortCuts.resolveShortcut(orginalDirection);
                if (direction.isBlank()) {
                    System.out.println("Which way do you want to go");
                    continue;
                }
                Room next = player.getCurrentRoom().getExits(direction);
                if (next != null) {
                    if (next.isRoomIsLocked()){
                        System.err.println("The "+next.getRoomName()+" is locked");
                        continue;
                    }
                    player.setCurrentRoom(next);
                    System.out.println("➡️ You moved to: " + next.getRoomName() + " || available exits: " + next.getExitDirections());

                    if (next.isRoomHasBeenVisited()) {
                        next.beenHereBefore();
                    }
                    next.setRoomHasBeenVisited(true);
                } else {
                    System.out.println("❌ You can't go that way.");
                    continue;
                }
                // Jose Montejo
                // Check for monster encounter in the new room
                if (monsterSpawnManager != null) {
                    boolean monsterDefeated = monsterSpawnManager.checkForMonsterEncounter(player, next, previousRoomID);

                    // If player died during monster encounter, handle game over
                    if (player.getHealth() <= 0) {
                        System.out.println("You have been killed. Game over.");
                        running = false;
                    }

                    // Note: Player movement back to previous room is now handled in MonsterController
                    // when the player explicitly chooses to flee
                }
            }
            //solve --> Razan
            else if (input.equals("solve") || input.equalsIgnoreCase("s")) {
                PuzzleController.handlePuzzle(player);
                //inventory --> Razan
            } else if (input.equalsIgnoreCase("inventory") || input.equalsIgnoreCase("inv")) {
                player.showInventory();
            }
            //inspect --> Razan
            else if (input.equals("inspect") || input.equalsIgnoreCase("ins")) {
                handleInspect();
            }

            //Shayla
            //Take command
            else if (input.startsWith("take")) {
                String itemName = input.substring(4).trim();
                if (itemName.isBlank()) {
                    System.out.println("What item did you want to take?");
                    continue;
                }
                itemController.takeItem(itemName, player.getCurrentRoom());
            }

            //Shay, drop command
            else if (input.startsWith("drop")) {
                String itemName = input.substring(4).trim();
                itemController.dropItem(itemName, player.getCurrentRoom());
            }

            //Shay, for consume item
            else if (input.startsWith("use")) {
                String itemName = input.substring(3).trim();
                itemController.consumeItem(itemName);
            }

            //else for invalid commands/input
            else {
                System.err.println("❓ Unknown command. Type go<Direction> to navigate || or help to view all commands || or quit to end the game");
            }
        }
    }//end of start()




    /**
     * Jose Montejo
     * handlePlayerFlee
     * Moves the player back to the previous room when they successfully flee from a monster.
     *
     * @param previousRoomID The ID of the room to flee to
     * @return true if the player successfully fled, false otherwise
     */
    public boolean handlePlayerFlee(String previousRoomID) {
        return movePlayerToRoom(previousRoomID);
    }

    // ✅ WIN CONDITION METHOD
    public boolean checkWinCondition(Player player) {
        // Final room ID is assumed to be "3roof" – update if different
        boolean inFinalRoom = player.getCurrentRoom().getRoomID().equalsIgnoreCase("3roof");

        boolean hasAllKeys = player.hasItem("Keycard") &&
                player.hasItem("ElevatorPass") &&
                player.hasItem("MasterKey");

        return inFinalRoom && hasAllKeys;
    }

    //Razan, Shayla
    public void handleInspect() {
        Room room = player.getCurrentRoom();
        Puzzle puzzle = room.getPuzzle();

        if (puzzle == null) {
            System.out.println("🔍 You look around carefully, but there's no puzzle to inspect here.");
        } else if (puzzle.isSolved()) {
            System.out.println("✅ You recall solving the puzzle here already.");
        } else {
            System.out.println("🧩 You uncover a hidden mechanism... It's a puzzle!");
            System.out.println("📝 " + puzzle.getDescription());
            System.out.println("Use the 'solve' command to attempt solving it.");
        }

        //Shayla
        //Inspect items in a room
        itemController.inspectRoomItems(room);
}
    /**
     * Jose Montejo
     * movePlayerToRoom
     * Moves the player to a specific room by ID without changing room visit status.
     * Used when fleeing from combat to return to the previous room.
     *
     * @param roomID The ID of the room to move to
     * @return true if successful, false if room not found
     */
    private boolean movePlayerToRoom(String roomID) {
        Room room = rooms.get(roomID);
        if (room != null) {
            player.setCurrentRoom(room);
            System.out.println("You fled to: " + room.getRoomName() + " || available exits: " + room.getExitDirections());
            return true;
        }
        return false;
    }
}

