import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WordMasterLogic {
    public ArrayList<Character> currentLetters;
    public HashMap<String, Integer> categoryData;
    public HashMap<String, ArrayList<String>> categoryWords;
    public int attemptCount;
    public char letterGuessed;
    public String word;

    public boolean cat1 = false;
    public boolean cat2 = false;
    public boolean cat3 = false;
    public boolean gameEnded = false;
    public String chosenCategory;

    public WordMasterLogic() {
        fillCategory();
        attemptCount = 6;
    }

    public void fillCategory(){
        // Creates Category Attempt data
        categoryData = new HashMap<>();
        categoryData.put("animals", 3);
        categoryData.put("countries", 3);
        categoryData.put("food", 3);

        // Creates Category Type + Options Map
        categoryWords = new HashMap<>();
        categoryWords.put("animals", new ArrayList<>());
        categoryWords.put("countries", new ArrayList<>());
        categoryWords.put("food", new ArrayList<>());

        // Filling in data
        categoryWords.get("animals").add("monkey");
        categoryWords.get("animals").add("dog");
        categoryWords.get("animals").add("fish");
        categoryWords.get("animals").add("cat");
        categoryWords.get("animals").add("elephant");
        categoryWords.get("animals").add("fly");

        categoryWords.get("countries").add("canada");
        categoryWords.get("countries").add("algeria");
        categoryWords.get("countries").add("pakistan");
        categoryWords.get("countries").add("mexico");
        categoryWords.get("countries").add("germany");
        categoryWords.get("countries").add("australia");

        categoryWords.get("food").add("pizza");
        categoryWords.get("food").add("toast");
        categoryWords.get("food").add("biryani");
        categoryWords.get("food").add("eggs");
        categoryWords.get("food").add("cake");
        categoryWords.get("food").add("curry");
    }

    public String generateWord(String chosenCategory) {
        this.chosenCategory = chosenCategory;
        Random random = new Random();
        // Generates a random integer in the range 0 to 5
        int randomNumber = random.nextInt(categoryWords.get(chosenCategory).size());

        if (!categoryWords.get(chosenCategory).isEmpty()) {
            word = categoryWords.get(chosenCategory).remove(randomNumber);
            currentLetters = new ArrayList<>(word.length());
            for (int i = 0; i < word.length(); i++){
                currentLetters.add('_');
            }
            return word;
        } else {
            return "";
        }
    }


    // Returns the locations of the guess
    // when returned, the client should just see if the array is empty
    public boolean checkGuess(char letterGuessed) {
        boolean correct = false;
        for(int i = 0; i < word.length();i++){
            if(letterGuessed == word.charAt(i)){
                currentLetters.set(i, letterGuessed);
                correct = true;
            }
        }
        if(!correct){
            attemptCount--;
            if (attemptCount <= 0) {
                // Update the correct category's key in categoryData HashMap
                int currentCount = categoryData.get(chosenCategory);
                currentCount--;
                if(currentCount == 0)
                    gameEnded = true;
                categoryData.put(chosenCategory, currentCount);
                //     System.out.println(categoryName);
            }
        }
        return correct;
    }

    public boolean evaluateWord(String chosenCategory) {
        StringBuilder stringBuilder = new StringBuilder(currentLetters.size());
        for (Character character : currentLetters) {
            stringBuilder.append(character);
        }
        String guessedWord = stringBuilder.toString();

        boolean result = guessedWord.equals(word);

        if(result){
            switch(chosenCategory){
                case "animals":
                    cat1 = true;
                    break;
                case "countries":
                    cat2 = true;
                    break;
                case "food":
                    cat3 = true;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid index");
            }
        }
        return result;
    }
}