package prog03;

import prog02.UserInterface;
import prog02.GUI;

/**
 * @author vjm
 */
public class Main {
    /**
     * Use this variable to store the result of each call to fib.
     */
    public static double fibn;

    /**
     * Determine the time in microseconds it takes to calculate the
     * n'th Fibonacci number.
     *
     * @param fib an object that implements the Fib interface
     * @param n   the index of the Fibonacci number to calculate
     * @return the time for the call to fib(n)
     */
    public static double time(Fib fib, int n) {
        // Get the current time in nanoseconds.
        long start = System.nanoTime();

        // Calculate the n'th Fibonacci number.  Store the
        // result in fibn.
        //fib.SOMETHING
        fibn = fib.fib(n); // fix this

        // Get the current time in nanoseconds.
        long end = 0; // fix this
        end = System.nanoTime();

        // Return the difference between the end time and the
        // start time divided by 1000.0 to convert to microseconds.
        return (end - start)/1000.0; // fix this
    }

    /**
     * Determine the average time in microseconds it takes to calculate
     * the n'th Fibonacci number.
     *
     * @param fib    an object that implements the Fib interface
     * @param n      the index of the Fibonacci number to calculate
     * @param ncalls the number of calls to average over
     * @return the average time per call
     */
    public static double averageTime(Fib fib, int n, int ncalls) {
        double totalTime = 0;
        for (int i = 0; i < ncalls; i++){
            totalTime += time(fib, n);
        }
        // Add up the total call time for ncalls calls to time (above).
        //Write a loop here to calculate the time with a line inside the loop

        // Return the average time.
        return totalTime / ncalls;
    }

    /**
     * Determine the time in microseconds it takes to to calculate the
     * n'th Fibonacci number.  Average over enough calls for a total
     * time of at least one second.
     *
     * @param fib an object that implements the Fib interface
     * @param n   the index of the Fibonacci number to calculate
     * @return the time it it takes to compute the n'th Fibonacci number
     */
    public static double accurateTime(Fib fib, int n) {
        // Get the time in microseconds for one call.
        double t = averageTime(fib, n, 1 ); // fix this
        if (t >= Math.pow(10, 6))
            return t;
        // If the time is (equivalent to) more than a second, return it.

        // Estimate the number of calls that would add up to one second.
        // Use   (int)(YOUR EXPRESSION)   so you can save it into an int variable.
        int numcalls = (int) ((Math.pow(10,6))/t); // fix this
        if(numcalls > Math.pow(10,6))
            numcalls = (int) Math.pow(10,6);

        System.out.println("numcalls " + numcalls);

        // Get the average time using averageTime above with that many
        // calls and return it.
        return averageTime(fib, n, numcalls); // fix this
    }

    //private static UserInterface ui = new TestUI("Fibonacci experiments");
    private static UserInterface ui = new TestUI("Fibonacci experiments");

    /**
     * Get a non-negative integer from the using using ui.
     * If the user enters a negative integer, like -2, say
     * "-2 is negative...invalid"
     * If the user enters a non-integer, like abc, say
     * "abc is not an integer"
     * If the user clicks cancel, return -1.
     *
     * @return the non-negative integer entered by the user or -1 for cancel.
     */
    static int getInteger() {
        String s = ui.getInfo("Enter n");
        boolean number = false;
        while(s == null || !number || Integer.parseInt(s) < 0){
            if (s == null)
                return -1; // user clicked cancel
            else if (!number){
                try {
                    Integer.parseInt(s);
                    number = true;
                }
                catch(NumberFormatException nfe){
                    ui.sendMessage(s + " is not an integer.");
                    s = ui.getInfo("Enter n");
                }
            }
            else if(Integer.parseInt(s) < 0){
                number = false;
                ui.sendMessage(s + " is a negative integer.");
                s = ui.getInfo("Enter n");
            }
        }


        return Integer.parseInt(s); // does not catch any errors
    }

    public static void doExperiments(Fib fib) {
        System.out.println("doExperiments" + fib);
        int integer = 0;
        int tracker = 0;
        //double c = time1 / fib.O(integer);
        while(true){
            integer = getInteger();
            if (integer == -1)
                return;
            double estTime = fib.estimateTime(integer);
            if (tracker > 0){
                ui.sendMessage("Estimated time: " + estTime + " microseconds");
            }
            if (estTime > 3.6 * Math.pow(10,9)) {
                ui.sendMessage("The estimated running time is more than an hour. Do you want to continue?");
                String[] options = {"Yes", "No"};
                int c = ui.getCommand(options);
                switch (c){
                    case -1:
                        return;
                    case 0:
                        break;
                    case 1:
                        continue;
                }
            }
            double accTime = accurateTime(fib, integer);
            fib.saveConstant(integer, accTime);
            double percentError = 0;
            if(tracker == 0)
                ui.sendMessage("fib(" + integer + ") = " + fibn + " in " + accTime + " microseconds.");
            else
                ui.sendMessage( "fib(" + integer + ") = " + fibn + " in " + accTime + " microseconds. " + ((accTime - estTime)/estTime) * 100 + "% error");
            tracker++;
        }



            //double time2est = c * efib.O(n2);
                //How do I do estimated time?
                //accurateTime()


    }

    public static void doExperiments() {
        // Give the user a choice instead, in a loop, with the option to exit.
        //doExperiments(new ExponentialFib());

        String[] options = {"ExponentialFib", "LinearFib", "LogFib", "ConstantFib", "MysteryFib", "Exit"};
        while (true) {
            int choice = ui.getCommand(options);
            switch (choice) {
                case 0:
                    doExperiments(new ExponentialFib());
                    break;
                case 1:
                    doExperiments(new LinearFib());
                    break;
                case 2:
                    doExperiments(new LogFib());
                    break;
                case 3:
                    doExperiments(new ConstantFib());
                    break;
                case 4:
                    doExperiments(new MysteryFib());
                    break;
                case 5:
                    return;
            }
        }
    }


    static void labExperiments() {
        // Create (Exponential time) Fib object and test it.
        Fib efib = new ConstantFib();
        System.out.println(efib);
        for (int i = 0; i < 11; i++)
            System.out.println(i + " " + efib.fib(i));

        // Determine running time for n1 = 20 and print it out.
        int n1 = 20;
        double time1 = accurateTime(efib, n1); //Replace ncalls with your Value (total/time1)
        System.out.println("n1 " + n1 + " time1 " + time1);

        // Calculate constant:  time = constant times O(n).
        double c = time1 / efib.O(n1);
        System.out.println("c " + c);

        // Estimate running time for n2=30.
        int n2 = 30;
        double time2est = c * efib.O(n2);
        System.out.println("n2 " + n2 + " estimated time " + time2est);

        // Calculate actual running time for n2=30.
        double time2 = accurateTime(efib, n2);
        System.out.println("n2 " + n2 + " actual time " + time2);

        // Estimate how long ExponentialFib.fib(100) would take.
        int n3 = 100;
        //n3 *= (Math.pow(10,6) * 60 * 60 * 24 * 365);
        double time3est = c * efib.O(n3);
        double sec = time3est/Math.pow(10,6);
        double min = sec/60;
        double hour = min/60;
        double day = hour/24;
        double year = day/365.25;

        System.out.println("n3 " + n3 + " actual time " + year);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        labExperiments();
        doExperiments();
    }
}

