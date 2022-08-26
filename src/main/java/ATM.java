import java.sql.SQLOutput;
import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Bank theBank = new Bank("Bank of Cordelle"); // init bank

        User aUser = theBank.addUser("John", "Doe", "1234");

        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {

            // stay in the login prompt until successfull login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            ATM.printUserMenu(curUser, sc);

        }

    }

    /**
     *
     * @param theBank
     * @param sc
     * @return
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        String userID;
        String pin;
        User authUser;

        // prompt for id/pin until correct one is reached
        do {
            System.out.printf("\n\n Welcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.printf("Enter pin: ");
            pin = sc.nextLine();

            // try to get the user object correesponding ot the id and pin combo

            authUser = theBank.userLogin(userID, pin);
            if (authUser ==  null) {
                System.out.println("incorrect user ID/pin combo. Please tyy again.");
            }

        } while( authUser == null);

        return authUser;
    }

    /**
     *
     * @param theUser
     * @param sc
     */
    public static void printUserMenu(User theUser, Scanner sc) {

        //print summer of user accounts
        theUser.printAccountsSummary();

        int choice;

        do {
            System.out.printf("Welcome %s, what would you like to do?", theUser.getFirstName());

            System.out.println(" 1) Show transaction history");
            System.out.println(" 2) Withdrawl");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.println("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Choose 1-5");
            }
        }while (choice < 1 || choice > 5);

        //process choice
        switch(choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
        }

        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }
    }

    /**
     *
     * @param theUser
     * @param sc
     */
    public  static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;

        //get account history
        do {
            System.out.printf("Enter the number (1-%d) of the account whose transactions you want to see: ",
                theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account, try again.");
            }
        } while(theAcct < 0 || theAcct >= theUser.numAccounts());

        theUser.printAcctTransHistory(theAcct);
    }

    /**
     *
     * @param theUser
     * @param sc
     */
    public static void transferFunds(User theUser, Scanner sc) {

        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        //get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the acount\n to transfer from: ");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account, try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //get account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the acount\n to transfer to: ");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account, try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        do {
            System.out.printf("Enter the amount to transfer (max %0.2f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n balance of $%0.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // do the transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Transfer from account %s", theUser.getAcctUUID(fromAcct)));
    }

    /**
     *
     * @param theUser
     * @param sc
     */
    public static void withdrawFunds(User theUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        //get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the acount\n to transfer from: ");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account, try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //get account to transfer
        do {
            System.out.printf("Enter the amount to transfer (max %0.2f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n balance of $%0.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        sc.nextLine();

        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(fromAcct, -1*amount, memo);
    }

    /**
     *
     * @param theUser
     * @param sc
     */
    public static void depositFunds(User theUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        //get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the acount\n to transfer from: ");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account, try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        //get account to transfer
        do {
            System.out.printf("Enter the amount to transfer (max %0.2f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n balance of $%0.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        sc.nextLine();

        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(toAcct, -1*amount, memo);
    }
}
