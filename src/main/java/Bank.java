import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts;

    public String getNewUserUUID() {

        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        // loop until we get unique ID
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c <len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }
    public String getNewAccountUUID() {
        return "nothing";
    }

    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }
}
