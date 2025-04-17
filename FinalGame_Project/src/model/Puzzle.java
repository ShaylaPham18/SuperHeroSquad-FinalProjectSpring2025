package model;

/**
 * Razan Abdalla
 * Name | Description | RoomID | CorrectAnswer | ResultWhenSolved | MaxAttempts | Hint
*/
public class Puzzle {
    private String name;
    private String description;
    private String roomLocation;
    private String correctAnswer;
    private String resultWhenSolved;
    private int maxAttempts;
    private int currentAttempts;
    private String hint;
    private boolean solved;
    private String hintItem;


    //
    public Puzzle(String name, String description, String roomLocation,
                  String correctAnswer, String resultWhenSolved,
                  int maxAttempts, String hint) {
        this.name = name;
        this.description = description;
        this.roomLocation = roomLocation;
        this.correctAnswer = correctAnswer;
        this.resultWhenSolved = resultWhenSolved;
        this.maxAttempts = maxAttempts;
        this.currentAttempts = 0;  // default value
        this.hint = hint;
        this.solved = false;       // default value
    }


    // added getter and setter -- I will delete the extra later
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public String getRoomLocation() {return roomLocation;}
    public String getResultWhenSolved() {return resultWhenSolved;}
    public int getCurrentAttempts() {return currentAttempts;}
    public String getHint() {return hint;}
    public boolean isSolved() {return solved;}
    public void setSolved(boolean solved) {this.solved = solved;}
    public String getHintItem() {
        return hintItem;
    }

    public boolean attempt(String input) {
        currentAttempts++;
        System.out.println("--> Attempt #" + currentAttempts + ": input = '" + input );
        return false;
    }

    //check if the hint is  available to the player.
    // added hint method to be available -->
    // Hint is available if 3 or more attempts have been made and the puzzle is not yet solved.
    public boolean canGetHint() {
        return currentAttempts >= 3 && !solved;
    }
}
