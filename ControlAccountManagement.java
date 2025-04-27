import java.sql.SQLException;

//abstract class ControlAccountManagement
public abstract class ControlAccountManagement {
   //abstract method-six choices for the banking customer to decide on.
   abstract void createAccount(String bankAccountNumber);

   abstract void deleteAccount(String bankAccountNumber);


   abstract void printAccountDetails(String bankAccountNumber);

   abstract void deposit(String bankAccountNumber, double amount) throws SQLException;

   abstract void withdraw(String bankAccountNumber, double amount) throws SQLException;

   abstract void printTransactionDetails(String bankAccountNumber);


}
