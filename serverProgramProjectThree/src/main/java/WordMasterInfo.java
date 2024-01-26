import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class WordMasterInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public char letterGuessed;
    public int attemptCount;
    public String chosenCategory;

    public ArrayList<Character> currentLetters;
    HashMap<String, Integer> categoryData;

    public boolean playAgain;
    public boolean startAgain;
    public boolean ca1;
    public boolean ca2;
    public boolean ca3;

    public WordMasterInfo(){
        this.letterGuessed = ' ';
        this.attemptCount = 6;
        this.chosenCategory = "";
        currentLetters = new ArrayList<>();
        categoryData = new HashMap<>();
        playAgain = false;
        startAgain = false;
        ca1 = false;
        ca2 = false;
        ca3 = false;
    }

    public WordMasterInfo(char letterGuessed, int attemptCount, String chosenCategory,
                          HashMap<String, Integer> categoryData,
                          ArrayList<Character> currentLetters,
                          boolean playAgain, boolean startAgain, boolean ca1, boolean ca2, boolean ca3) {
        this.letterGuessed = letterGuessed;
        this.attemptCount = attemptCount;
        this.chosenCategory = chosenCategory;
        this.categoryData = new HashMap<>(categoryData);
        this.currentLetters = new ArrayList<>(currentLetters);
        this.playAgain = playAgain;
        this.startAgain = startAgain;
        this.ca1 = ca1;
        this.ca2 = ca2;
        this.ca3 = ca3;
    }
}