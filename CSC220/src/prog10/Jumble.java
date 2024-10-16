package prog10;
import java.io.File;
import java.util.*;

import prog02.UserInterface;
import prog02.ConsoleUI;
import prog02.GUI;
import prog09.BTree;

public class Jumble {
    /**
     * Sort the letters in a word.
     * @param word Input word to be sorted, like "computer".
     * @return Sorted version of word, like "cemoptru".
     */
    public static String sort (String word) {
        char[] sorted = new char[word.length()];
        for (int n = 0; n < word.length(); n++) {
            char c = word.charAt(n);
            int i = n;

            while (i > 0 && c < sorted[i-1]) {
                sorted[i] = sorted[i-1];
                i--;
            }

            sorted[i] = c;
        }

        return new String(sorted, 0, word.length());
    }

    public static void main (String[] args) {
        // UserInterface ui = new GUI("Jumble");
        UserInterface ui = new ConsoleUI();

        //Map<String,String> map = new TreeMap<String,String>();
        //Map<String,String> map = new PDMap();
        //Map<String,String> map = new LinkedMap<String,String>();
        //Map<String,String> map = new SkipMap<String,String>();
        //BST<String,String> map = new BST<String,String>();
        //Map<String, List<String>> map = new BTree<String, List<String>>();

        Map<String, List<String>> map = new HashTable<String, List<String>>();

        /*
        map.remove(sort(word));
        if(map.get(sort(word)) != null){
            System.out.println("The key you removed is still there");
        }
        map.put(sort(word), word);
        */

        Scanner in = null;
        do {
            try {
                in = new Scanner(new File(ui.getInfo("Enter word file.")));
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Try again.");
            }
        } while (in == null);

        int n = 0;
        while (in.hasNextLine()) {
            String word = in.nextLine();
            if (n++ % 1000 == 0)
                System.out.println(word + " sorted is " + sort(word));

            // EXERCISE 1: Read the Jumble class.
            // Insert an entry for word into map.
            // What is the key?  What is the value?
            ///
            String sorted = sort(word);

            List<String> wordList;
            if(!map.containsKey(sorted)){
                wordList = new ArrayList<>();
                wordList.add(word);
                map.put(sorted, wordList);
            }
            else{
                wordList = map.get(sorted);
                wordList.add(word);
            }


            ///

            // EXERCISE 9: Remove the word using its key.
            // Make sure remove returns the word.
            // Make sure get returns null now.
            // Put it back again.
            ///

            /*
            map.remove(sort(word));
            if(map.get(sort(word)) != null){
                System.out.println("The key you removed is still there");
            }
            map.put(sort(word), wordList);
            */


            ///
        }
        String jumble = "";
        while(jumble != null){
            jumble = ui.getInfo("Enter jumble.");
            if(jumble == null){
                break;
            }
            List<String >words = map.get(sort(jumble));
            if (words == null)
                ui.sendMessage("No match for " + jumble);
            else
                ui.sendMessage(jumble + " unjumbled is " + words);
        }

        while (true) {
            String lettersJumble = ui.getInfo("Enter letters from clues.");
            if (lettersJumble == null)
                return;

            String word = null;
            String sortedLetters = sort(lettersJumble);
            int letter = 0;
            do{
                String word1letters = ui.getInfo("How many letters are there in the first word?");
                try{
                    letter = Integer.parseInt(word1letters);
                    if(letter<=0){
                        ui.sendMessage(word1letters + " is not a positive number.");
                    }
                }
                catch(Exception exception){
                    ui.sendMessage(word1letters + " is not a number.");
                }
            }while(letter <= 0);

            // EXERCISE 1:  Look up the jumble in the map.
            // What key do you use?
            // Set word to the result.

            ///

            ///
            for(String key1 : map.keySet()){
                if(key1.length() == letter){
                    String key2 = "";
                    int key1index = 0;
                    for(int i = 0; i < sortedLetters.length(); i++){
                        if(key1index < key1.length() && sortedLetters.charAt(i) == key1.charAt(key1index)){
                            key1index++;
                        }
                        else{
                            key2 += sortedLetters.charAt(i);
                        }
                    }
                    if(key1index == key1.length()){
                        if(map.containsKey(key2)){
                            ui.sendMessage(map.get(key1) + " " + map.get(key2));
                        }
                    }
                }
            }



        }

    }

    /*
    void removeTest(){
        System.out.println("Test for put: " + map.put(word, sort(word)));
        System.out.println("Test for get: " + map.get(word));
        System.out.println("Test for remove: " + map.remove(word));
        System.out.println("Test for get null: " +  map.get(word));
        System.out.println("Test for remove null: " + map.remove(word));
        System.out.println("Try put again: " + map.put(word, sort(word)));
    }
    */


}
