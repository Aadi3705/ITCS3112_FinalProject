import java.sql.SQLException;
import java.util.Scanner;

public class BankEntryPoint  {
    //method will give the user options of what they can do in the bank
   void userOptions() {
       //variables declared
       var console = new Scanner(System.in);
       int option;
       //user can select 6 options, and they can select an exit option when they want to end the program
       do {
           System.out.println("This is the Nair Online Bank. Here are your options.");
           System.out.println("1. Create a checking account");
           System.out.println("2. Make a deposit/withdrawal");
           System.out.println("3. Print details of account");
           System.out.println("4. Delete a checking account");
           System.out.println("5. View Transaction history");
           System.out.println("-1. To Exit");
           System.out.println("Pick your option by typing in the number.");
           option = console.nextInt();
           if (option == 1)
               userAccountCreation();
           else if (option == 2)
               manageMoney();
           else if (option == 3)
               printDetails();
           else if (option == 4)
               userAccountDeletion();
           else if (option == 5)
               transactionHistory();
           else if(option == -1)
               System.out.println("Thank you for using Banking Application");
       }while(option != -1);

       System.exit(0); //to exit the program



   }

    //focuses on creating a checking account
   void userAccountCreation() {
       Scanner console = new Scanner(System.in);
       System.out.println("Please enter an 8-digit bank account number for your new account");
       String bankAccountNumber = console.nextLine();

       new AccountManager().createAccount(bankAccountNumber);
   }
   //focuses on deleting a checking account
   void userAccountDeletion() {
       Scanner console = new Scanner(System.in);
       System.out.println("You have decided to delete your account.");
       System.out.println("Enter your bank account number.");
       String bankAccountNumber = console.nextLine();
       new AccountManager().deleteAccount(bankAccountNumber);
   }
   //focuses on the money management part for the account
   void manageMoney()  {
       Scanner console = new Scanner(System.in);
       System.out.println("Enter your bank account number.");
       String bankAccountNumber = console.nextLine();
       System.out.println("Enter an amount for this transaction.");
       double amount = console.nextDouble();
       System.out.println("Do you want to deposit or withdraw money? W(withdraw) or D(deposit)");
       String option = console.next();
       switch (option) {
           case "W" :
               new AccountManager().withdraw(bankAccountNumber, amount);
               break;
           case "D" :
               new AccountManager().deposit(bankAccountNumber, amount);
               break;
       }
   }
    //focuses on printing details for the account
   void printDetails() {
       var console = new Scanner(System.in);
       System.out.println("What is the bank account number of the account you want to access?");
       String bankAccountNumber = console.nextLine();
       new AccountManager().printAccountDetails(bankAccountNumber);
   }
   //focuses on printing the transaction history for an account
   void transactionHistory() {
       Scanner console = new Scanner(System.in);
       System.out.println("Enter your bank account number.");
       String bankAccountNumber = console.nextLine();
       new AccountManager().printTransactionDetails(bankAccountNumber);
   }


}
