import java.util.ArrayList;
public class Account {

    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;


    public Account(String name, User holder, Bank theBank) {

        this.name = name;
        this.holder = holder;

        this.uuid = theBank.getNewAccountUUID(); // get ne account id

        this.transactions = new ArrayList<Transaction>(); // init transacitons

        holder.addAccount(this);
        theBank.addAccount(this);
    }
}
