package src.main.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import src.main.model.account.Account;
import src.main.model.account.Chequing;
import src.main.model.account.Taxable;

public class Bank {

    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

    public Bank() {
        this.accounts = new ArrayList<Account>();
        this.transactions = new ArrayList<Transaction>();
    }

    public Transaction[] getTransactions(String accountId) {
        List<Transaction> list = this.transactions.stream()
                .filter((transaction) -> transaction.getId().equals(accountId))// this will filter our transaction
                // arraylist
                .collect(Collectors.toList());// this will collect all the filter object and we are storing inside list.
                                              // we dont have a method in collectors to convert into arrayList

        return list.toArray(new Transaction[list.size()]);// this will return the array of custom size based on the list
                                                          // and not of fixed size
    }

    public Account getAccount(String transactionId) {
        return accounts.stream()
                .filter((account) -> account.getId().equals(transactionId))
                .findFirst() // in the stream there will be single account that matches the transaction id
                .orElse(null);
    }

    public void addAccount(Account account) {
        // this.accounts.add(new Account(account)); this give error bcoz account is
        // abstract class and we cannot create object of abstract class that's why we
        // have to use clone method
        this.accounts.add(account.clone());// returns the copy of the object that called means if object is chequing the
                                           // clone method of checking is called
    }

    private void addTransaction(Transaction transaction) {// private bcoz only bank can do transaction
        this.transactions.add(new Transaction(transaction));
    }

    public void executeTransaction(Transaction transaction) {
        switch (transaction.getType()) {
            case WITHDRAW:
                withdrawTransaction(transaction);
                break;
            case DEPOSIT:
                depositTransaction(transaction);
                break;
        }
    }

    private void withdrawTransaction(Transaction transaction) {
        // withdraw returns boolean means if
        // succed it will return true
        if (getAccount(transaction.getId()).withdraw(transaction.getAmount())) {
            addTransaction(transaction);
        }
    }

    private void depositTransaction(Transaction transaction) {
        getAccount(transaction.getId()).deposit(transaction.getAmount());
        addTransaction(transaction);
    }

    private double getIncome(Taxable account) {
        Transaction[] transactions = getTransactions(((Chequing) account).getId());// taxable method doesn't have any
                                                                                   // getId() method

        return Arrays.stream(transactions).mapToDouble((transaction) -> {
            switch (transaction.getType()) {
                case WITHDRAW:
                    return -transaction.getAmount();
                case DEPOSIT:
                    return transaction.getAmount();

                default:
                    return 0;
            }
        }).sum();
    }

    public void deductTaxes() {
        for (Account account : accounts) {
            if (Taxable.class.isAssignableFrom(account.getClass())) {// this will chck if our account is implementing
                                                                     // taxable interface or not ie. if chequing account
                                                                     // thn it will return true
                Taxable taxable = (Taxable) account;// type cast the account
                taxable.tax(getIncome(taxable));

            }
        }
    }

}
