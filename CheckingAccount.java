//Checking Account class
public class CheckingAccount {
    private String userId;
    private String bankAccountNumber;
    private double balance;

    //CheckingAccount constructor
    public CheckingAccount(String userId, String bankAccountNumber, double balance) {
        this.userId = userId;
        this.bankAccountNumber = bankAccountNumber;
        this.balance = balance;


    }

    //getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
