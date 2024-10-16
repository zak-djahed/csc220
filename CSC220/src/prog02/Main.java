package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
            boolean changed = false; //Step 7

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};

        String name, number, oldNumber;

        while (true) {
            int c = ui.getCommand(commands);
            switch (c) {
                case -1:
                    ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                    break;
                case 0: //Add or Change Entry
                    name = ui.getInfo("Enter name");
                    if(name == null || name.isEmpty()) {
                        break;
                    }
                    number = ui.getInfo("Enter number");
                    if (number == null){
                        break;
                    }
                    oldNumber = pd.addOrChangeEntry(name, number);
                    if (oldNumber == null) {
                        ui.sendMessage(name + " was added to the directory\n" + "New number: " + number);
                    }
                    else {
                        ui.sendMessage("Number for " + name + " was changed\n" + "Old number: " + oldNumber + "\nNew number: " + number);
                    }
                    changed = true; //Step 7

                    break;
                case 1: //Look Up Entry
                    name = ui.getInfo("Enter name ");
                    number = pd.lookupEntry(name);
                    if(name == null || name.isEmpty()) {
                        break;
                    }
                    if(number == null){
                        ui.sendMessage(name + " is not listed in the directory");
                        break;
                    }
                    ui.sendMessage(name + " has number " + number);
                    break;
                case 2: //Remove Entry
                    name = ui.getInfo("Enter name");
                    if (name == null || name.isEmpty()) {
                        break;
                    }
                    number = pd.removeEntry(name);
                    if (number == null) {
                        ui.sendMessage(name + " is not listed in the directory");
                    }
                    else {
                        ui.sendMessage("Removed entry with name " + name + " and number " + number);
                    }
                    changed = true;
                    break;
                case 3: //Save Directory
                    pd.save();
                    changed = false;
                    break;
                case 4: //Exit //Fill our for HW step 7
                    if(changed){
                        ui.sendMessage("Do you really want to exit without saving?");
                        String[] options = {"YES", "NO"};
                        ui.getCommand(options);
                        if(ui.getCommand(options) == -1 || ui.getCommand(options) == 1){
                            break;
                        }
                        else if(ui.getCommand(options) == 0){
                            return;
                        }
                    }
                    return;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        PhoneDirectory pd = new SortedPD();
        UserInterface ui = new GUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
