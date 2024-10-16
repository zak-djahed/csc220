package prog07;

import prog02.UserInterface;
import prog02.ConsoleUI;
import prog02.GUI;
import prog05.ArrayQueue;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class NotWordle { // NotWordle class
    UserInterface ui; // class variable
    List <String> allWords = new ArrayList<String>();
    NotWordle(UserInterface ui) { // constructor that takes a UserInterface

        this.ui = ui; // and stores it in a class variable
    }

    public static void main(String[] args) {
        UserInterface platform = new GUI("NotWordle");
        NotWordle game = new NotWordle(platform);

        String fileName = platform.getInfo("Enter a word file: ");

        while(!game.loadWords(fileName)){
            fileName = platform.getInfo("Enter a file");
            if (fileName == null){
                return;
            }
        }
        String startWord = "";
        String targetWord = "";

        String[] commands = {"Human plays.", "Computer plays."};

        startWord = platform.getInfo("Enter a starting word");

        while (game.find(startWord) == -1 || startWord == null){
            if(startWord == null){
                return;
            }
            else if(game.find(startWord) < 0 ){
                platform.sendMessage(startWord + " is not a real word.");
                startWord = platform.getInfo("Please type another word: ");
            }
        }

        targetWord = platform.getInfo("Enter a target word");

        while(game.find(targetWord) == -1 || targetWord == null){
            if(targetWord == null)
                return;
            else if(game.find(targetWord) < 0){
                platform.sendMessage(targetWord + " is not a real word.");
                targetWord = platform.getInfo("Please type another word: ");
            }
        }



        int c = platform.getCommand(commands);
        switch (c){
            case -1:
                return;
            case 0:
                game.play(startWord , targetWord);
                return;
            case 1:
                game.solve(startWord, targetWord);
                return;
        }
    }

    void play(String start, String target){
        while(true){
            ui.sendMessage("Current word: " + start + "\nTarget word: " + target);
            String pastStart = start;
            start = ui.getInfo("What is your next word?");
            if(start == null){
                return;
            }
            if (find(start) < 0){
                ui.sendMessage(start + " is not a real word.");
                start = ui.getInfo("Please type another word: ");
            }
            else if (oneLetterDifferent(pastStart, start)){
                if(start.equals(target)){
                    ui.sendMessage("You win!");
                    return;
                }
                ui.sendMessage(start + " is one letter away from " + pastStart);
            }
            else if (!oneLetterDifferent(pastStart, start)){
                ui.sendMessage(start + " is more than one letter away from " + pastStart);
                start = ui.getInfo("Please type another word: ");
            }

        }
    }
    void solve(String start, String target){
        int[] parentIndices = new int [allWords.size()];
        Arrays.fill(parentIndices, -1);
        PriorityQueue<Integer> indexTrack = new PriorityQueue<Integer>(new IndexComparator(parentIndices, target));
        int parentIndex = 0;
        String currWord;
        String currWord2;
        int startIndex = find(start);
        indexTrack.offer(startIndex);
        int pollCount = 0;
        while(!indexTrack.isEmpty()){
            parentIndex = indexTrack.poll();
            pollCount++;
            currWord = allWords.get(parentIndex);
            for(int i = 0; i < allWords.size(); i++){
                currWord2 = allWords.get(i);
                if ((i != startIndex && parentIndices[i] == -1 || numSteps(parentIndices, parentIndex) + 1 < numSteps(parentIndices, i)) && oneLetterDifferent(currWord, currWord2)){
                    parentIndices[i] = parentIndex;
                    if(parentIndices[i] == -1){
                        indexTrack.remove(i);
                    }
                    indexTrack.offer(i);
                    System.out.println(" " + currWord2);

                    if(currWord2.equals(target)){
                        ui.sendMessage(start + " and " + target + " are " + numSteps(parentIndices, parentIndex) + " steps apart.");
                        ui.sendMessage("Poll is called " + pollCount + " times.");
                        ui.sendMessage("Go to " + target + " from " + currWord);
                        String finMessage = currWord2 + "\n";
                        while (i != startIndex){
                            i = parentIndices[i];
                            finMessage = allWords.get(i) + "\n" + finMessage;
                        }
                        ui.sendMessage(finMessage);
                        return;
                    }
                }
            }
        }
    }

    static boolean oneLetterDifferent(String firstWord, String secondWord){
        if(firstWord.length() != secondWord.length()){
            return false;
        }
        int looper = 0;
        for(int i = 0; i < firstWord.length(); i++){
            if(firstWord.charAt(i) != secondWord.charAt(i)){
                looper++;
            }
        }
        return (looper == 1);
    }
    boolean loadWords (String file){
        Scanner scan;
        try{
            File filename = new File(file);
            scan = new Scanner(filename);
        }
        catch (FileNotFoundException fi){
            ui.sendMessage("Error: " + fi);
            return false;
        }
        while(scan.hasNextLine()){
            allWords.add(scan.nextLine());
        }
        scan.close();
        return true;
    }
    int find (String word){
        for (String element: allWords){
            if (word.equals(element)){
                return allWords.indexOf(element);
            }
        }
        return -1;
    }
    static int numDifferent (String firstWord, String secondWord){
        int countSim = 0;
        for(int i = 0; i < firstWord.length(); i++){
            if(firstWord.charAt(i) != secondWord.charAt(i)){
                countSim++;
            }
        }
        return countSim;
    }
    static int numSteps (int[] parentIndices, int index){
        int stepsToDone = 0;
        while(parentIndices[index] != -1){
            index = parentIndices[index];
            stepsToDone++;
        }
        return stepsToDone;
    }

    public class IndexComparator implements Comparator<Integer>{
        private String target;
        private int[] parentIndices;

        public IndexComparator(int[] parentIndices, String target){
            this.parentIndices = parentIndices;
            this.target = target;
        }
        public int priority(int index){
            return numDifferent(allWords.get(index), target) + numSteps(parentIndices, index);
        }
        public int compare(Integer index1, Integer index2){
            return (priority(index1) - priority(index2));
        }
    }


}
