import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//this class focuses on all the implementation for the option user has in the banking system
public class AccountManager extends ControlAccountManagement{
    private CheckingAccount checkingAccount; //used for setting and getting parts of the object

    //this method is used to set the contents for our checking account
    void setCheckingAccount(String bankAccountNumber) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "SELECT user_id, bank_account_number, current_balance FROM account WHERE bank_account_number=?";

        var user_id = "";
        var balance = 0;

        try(Connection conn = DriverManager.getConnection(url,"root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankAccountNumber);
            var rs = ps.executeQuery();
            if(rs.next()) {
                user_id = rs.getString("user_id");
                balance = rs.getInt("current_balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkingAccount = new CheckingAccount(user_id, bankAccountNumber, balance);

    }
    //method receives the bankAccountNumber, and deletes the account
    @Override
    void deleteAccount(String bankAccountNumber) {
        //implementation to access account from table, and delete it
        try {
            deleteRow(bankAccountNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //method for the operation of deleting an account from the database
    static void deleteRow(String accountNumber) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "DELETE FROM account WHERE bank_account_number=?";


        //check if bank account number exists
        if(!bankAccountNumberExists(accountNumber)) {
            System.err.println("Account Number does not exist.");
            new BankEntryPoint().userOptions();
        }

        //check if entered bank account number belongs to user
        if(!(bankAccountBelongsToUser(accountNumber))) {
            System.err.println("Account Number does not belongs to user.");
            new BankEntryPoint().userOptions();
        }


        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);

            int rows = ps.executeUpdate();


        }
    }



    //overridden method for creating an account
    @Override
    void createAccount(String bankAccountNumber) {
        Scanner console = new Scanner(System.in);
        //getting the userId for currentSession
        String currentUserId = User.sessUserId;
        //initial balance amount to add
        System.out.println("What do you want to deposit as your initial balance?");
        double initialDeposit = console.nextDouble();

        checkingAccount = new CheckingAccount(currentUserId, bankAccountNumber, initialDeposit);

        int userAccounts = findTotalNumberOfAccounts(checkingAccount.getUserId());

        if(userAccounts >= 1) {
            System.err.println("Error: You already have an account.");
            new BankEntryPoint().userOptions();
        }


        //implementation of adding account details to sql table
        try {
            insertRow(checkingAccount.getUserId(), checkingAccount.getBankAccountNumber(), checkingAccount.getBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //method for inserting a row in the account table when an account is created
    static void insertRow(String user_id, String accountNumber, double balance) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "INSERT INTO account( user_id, bank_account_number, current_balance) VALUES(?,?,?)";

        //get a connection to the database
        try(var conn = DriverManager.getConnection(url,"root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, user_id);
            ps.setString(2, accountNumber);
            ps.setDouble(3, balance);

            ps.executeUpdate();

            System.out.println("Update Successful");
        }
    }
    //method does the operation of finding the total amount of accounts for a specific user_id-used in createAccount() method
    static int findTotalNumberOfAccounts(String user_id) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "SELECT COUNT(*) AS total_number_of_accounts FROM account WHERE user_id=?";

        int count = 0;

        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, user_id);

            var rs = ps.executeQuery();
            if(rs.next()) {
                count = rs.getInt("total_number_of_accounts");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    //method does the operation of finding if the account number exists
    static boolean bankAccountNumberExists(String bankAccountNumber) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";
        String sql = "SELECT COUNT(*) FROM account WHERE bank_account_number=?";
        int count = 0;

        try(var conn = DriverManager.getConnection(url,"root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankAccountNumber);

            var rs = ps.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count > 0;
    }
    //method does the operation of finding if the account number belongs to user in current session
    static boolean bankAccountBelongsToUser(String bankAccountNumber) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";
        String sql = "SELECT user_id FROM account WHERE bank_account_number=?";
        String userIdOfAccount = "";

        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
             ps.setString(1, bankAccountNumber);

             var rs = ps.executeQuery();
             if(rs.next()) {
                 userIdOfAccount = rs.getString("user_id");
             }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        if(userIdOfAccount.equals(User.sessUserId)) {
            return true;
        }
        return false;
    }

    //method that does the operation that prints account information
    @Override
    void printAccountDetails(String bankAccountNumber) {
        //check if bank account number exists
        if(!bankAccountNumberExists(bankAccountNumber)) {
            System.err.println("Account Number does not exist.");
            new BankEntryPoint().userOptions();
        }
        //check if entered bank account number belongs to user
        if(!(bankAccountBelongsToUser(bankAccountNumber))) {
            System.err.println("Account Number does not belongs to user.");
            new BankEntryPoint().userOptions();
        }

        //implementation of getting the details from the account table and printing it out
        setCheckingAccount(bankAccountNumber);
        System.out.println("Bank Account Number is: " + checkingAccount.getBankAccountNumber() + "\n The user of this bank is: " + checkingAccount.getUserId() + "\n The current balance on your account is: " + checkingAccount.getBalance());

    }
    //method that does the operation of depositing money
    @Override
    void deposit(String bankAccountNumber, double amount) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";
        //implementation to add a row to the transaction table
        String sql = "INSERT INTO transaction(bank_account_number, transaction_type, amount_entered) VALUES(?,?,?)";

        //check if bank account number exists
        if(!bankAccountNumberExists(bankAccountNumber)) {
            System.err.println("Account Number does not exist.");
            new BankEntryPoint().userOptions();
        }
        //check if entered bank account number belongs to user
        if(!(bankAccountBelongsToUser(bankAccountNumber))) {
            System.err.println("Account Number does not belongs to user.");
            new BankEntryPoint().userOptions();
        }

        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankAccountNumber);
            ps.setString(2, "D");
            ps.setDouble(3, amount);

            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        setCheckingAccount(bankAccountNumber);
        //receive the updated balance after depositing
        checkingAccount.setBalance(checkingAccount.getBalance() + amount);
        double updatedBalance = checkingAccount.getBalance();


        //implementation to update the balance in the account table
        String sql2 = "UPDATE account SET current_balance=? WHERE bank_account_number=?";
        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql2)) {
            ps.setDouble(1, updatedBalance);
            ps.setString(2, bankAccountNumber);

            ps.executeUpdate();
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Deposit Successful");
    }

    //method that does the operation of withdrawing money
    @Override
    void withdraw(String bankAccountNumber, double amount) {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";
        //implementation to add a row to the transaction table
        String sql = "INSERT INTO transaction(bank_account_number, transaction_type, amount_entered) VALUES(?,?,?)";

        //check if bank account number exists
        if(!bankAccountNumberExists(bankAccountNumber)) {
            System.err.println("Account Number does not exist.");
            new BankEntryPoint().userOptions();
        }
        //check if entered bank account number belongs to user
        if(!(bankAccountBelongsToUser(bankAccountNumber))) {
            System.err.println("Account Number does not belongs to user.");
            new BankEntryPoint().userOptions();
        }

        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankAccountNumber);
            ps.setString(2, "W");
            ps.setDouble(3, amount);

            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }


        //receive the updated balance after depositing
        setCheckingAccount(bankAccountNumber);
        checkingAccount.setBalance(checkingAccount.getBalance() - amount);
        double updatedBalance = checkingAccount.getBalance();
        //implementation to update the balance in the account table
        String sql2 = "UPDATE account SET current_balance=? WHERE bank_account_number=?";
        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql2)) {
            ps.setDouble(1, updatedBalance);
            ps.setString(2, bankAccountNumber);

            ps.executeUpdate();
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Withdraw Successful");

    }
    //method that prints the transaction history from getting information from the transaction table
    @Override
    void printTransactionDetails(String bankAccountNumber) {
        //print all rows of the transactions table
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "SELECT bank_account_number, transaction_type, amount_entered, transaction_date FROM Transaction WHERE bank_account_number=?";

        //check if bank account number exists
        if(!bankAccountNumberExists(bankAccountNumber)) {
            System.err.println("Account Number does not exist.");
            new BankEntryPoint().userOptions();
        }
        //check if entered bank account number belongs to user
        if(!(bankAccountBelongsToUser(bankAccountNumber))) {
            System.err.println("Account Number does not belongs to user.");
            new BankEntryPoint().userOptions();
        }

        try(var conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); var ps = conn.prepareStatement(sql)) {
                ps.setString(1, bankAccountNumber);

                var rs = ps.executeQuery();

                while (rs.next()) {
                    var t_type = rs.getString("transaction_type");
                    var amount = rs.getDouble("amount_entered");
                    var date = rs.getString("transaction_date");
                    if(t_type.equals("D")) {
                        System.out.println("In the date of " + date + ", you deposited " + amount + ". The type of transaction is " + t_type + ".");
                    } else if(t_type.equals("W")) {
                        System.out.println("In the date of " + date + ", you withdrew " + amount + ". The type of transaction is " + t_type + ".");
                    }

                }
        } catch(SQLException e) {
            e.printStackTrace();
        }


    }
}
