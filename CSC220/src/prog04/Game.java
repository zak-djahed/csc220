package prog04;

import java.util.Stack;

import prog02.UserInterface;
import prog02.ConsoleUI;
import prog02.GUI;

public class Game {
    static UserInterface ui = new ConsoleUI();
    // static UserInterface ui = new GUI("Tower of Hanoi");

    static public void main(String[] args) {
        int n = getInt("How many disks?");
        if (n <= 0) return;
        Game tower = new Game(n);

        String[] commands = {"Human plays.", "Computer plays."};
        int c = ui.getCommand(commands);
        if (c == -1) return;
        if (c == 0) tower.play();
        else tower.solve();
    }

    /**
     * Get an integer from the user using prompt as the request.
     * Return 0 if user cancels.
     */
    static int getInt(String prompt) {
        while (true) {
            String number = ui.getInfo(prompt);
            if (number == null) return 0;
            try {
                return Integer.parseInt(number);
            } catch (Exception e) {
                ui.sendMessage(number + " is not a number.  Try again.");
            }
        }
    }

    int nDisks;
    StackInterface<Integer>[] pegs = (StackInterface<Integer>[]) new ArrayStack[3];

    Game(int nDisks) {
        this.nDisks = nDisks;
        for (int i = 0; i < pegs.length; i++) {
            pegs[i] = new ArrayStack<Integer>();
        }

        for (int i = nDisks; i > 0; i--){
            pegs[0].push(i);
        }
        // EXERCISE: Initialize game with pile of nDisks disks on peg 'a' (pegs[0]).


    }

    void play() {
        String[] moves = {"ab", "ac", "ba", "bc", "ca", "cb"};
        int[] froms = {0, 0, 1, 1, 2, 2};
        int[] tos = {1, 2, 0, 2, 0, 1};

        boolean fixthis = true;

        while (!pegs[0].empty() || !pegs[1].empty()) {
            displayPegs();
            int imove = ui.getCommand(moves);
            if (imove == -1) return;
            int from = froms[imove];
            int to = tos[imove];
            move(from, to);
        }

        displayPegs();
        ui.sendMessage("You win!");
    }

    String stackToString(StackInterface<Integer> peg) {
        StackInterface<Integer> helper = new ArrayStack<Integer>();

        // String to append items to.
        String s = "";

        // EXERCISE:  append the items in peg to s from bottom to top.
        while(!(peg.empty())){
            helper.push(peg.pop());
        }

        while(!(helper.empty())){
            Integer val3 = helper.pop();
            peg.push(val3);
            s += " " + val3.toString() + " ";
        }

        return s;
    }

    void displayPegs() {
        String s = "";
        for (int i = 0; i < pegs.length; i++) {
            char abc = (char) ('a' + i);
            s = s + abc + stackToString(pegs[i]);
            if (i < pegs.length - 1) s = s + "\n";
        }
        ui.sendMessage(s);
    }

    void move(int from, int to) {
        // EXERCISE:  move one disk form pegs[from] to pegs[to].
        // Don't allow illegal moves:  send a warning message instead.
        // For example "Cannot place disk 2 on top of disk 1."
        // Use ui.sendMessage() to send messages.

        if(pegs[from].empty()){
            ui.sendMessage("Cannot remove anything from an empty peg!");
        }
        else if (!(pegs[from].empty()) && !(pegs[to].empty()) && (pegs[from].peek() > pegs[to].peek() ) ){
            ui.sendMessage("You are not allowed to move " + pegs[from].peek() + " to " + pegs[to].peek());
        }
        else{
            pegs[to].push(pegs[from].pop());
        }
    }

    // EXERCISE

    // EXERCISE
    StackInterface<Goal> goals = new LinkedStack<Goal>();
    public class Goal{
        int fromPeg;
        int toPeg;
        int nMove;

        Goal (int nMove, int fromPeg, int toPeg){
            this.nMove = nMove;
            this.fromPeg = fromPeg;
            this.toPeg = toPeg;
        }

        public String toString(){
            String[] pegs = {"a", "b", "c"};
            String end = "";
            end += "Move " + nMove + " disks";
            if(nMove > 1){
                end += "s";
            }
            end += " from peg " + pegs[fromPeg] + " to peg " + pegs[toPeg] + ".";
            return end;
        }
        public void displayGoals(){
            StackInterface<Goal> helper2 = new ListStack<Goal>();
            String string = "";
            while(!goals.empty()){
                string += "Move " + goals.peek().nMove + " disks from " + (char)('a' + goals.peek().fromPeg) + " to " + (char)('a' + goals.peek().toPeg) + ".\n";
                helper2.push(goals.pop());
            }
            ui.sendMessage(string);
            while(!helper2.empty()){
                goals.push(helper2.pop());
            }
        }


    }
    void solve() {
        // EXERCISE 13
        goals.push(new Goal(nDisks, 0, 2));
        while (! goals.empty()){
            int number = goals.peek().nMove;
            int start = goals.peek().fromPeg;
            int finish = goals.peek().toPeg;
            int temp = 0;
            if(start + finish == 1){
                temp = 2;
            }
            else if(start + finish == 3){
                temp = 0;
            }
            else{
                temp = 1;
            }
            Goal useGoal = new Goal(temp, start, finish);
            useGoal.displayGoals();
            goals.pop();
            if (number == 1){
                move(start, finish);
                displayPegs();
            }
            else{
                goals.push(new Goal(number -1, temp, finish ));
                goals.push(new Goal(1, start, finish));
                goals.push(new Goal(number -1, start, temp));
            }
        }
    }
}