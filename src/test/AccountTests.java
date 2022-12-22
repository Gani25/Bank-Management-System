package src.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import src.main.model.account.Account;
import src.main.model.account.Chequing;
import src.main.model.account.Loan;
import src.main.model.account.Savings;
import src.main.model.account.Taxable;

public class AccountTests {

    Account[] accounts;

    @Before
    public void setup() {
        accounts = new Account[] { new Chequing("f84c43f4-a634-4c57-a644-7602f8840870", "Michael Scott", 1524.51),
                new Savings("ce07d7b3-9038-43db-83ae-77fd9c0450c9", "Saul Goodman", 2241.60),
                new Loan("4991bf71-ae8f-4df9-81c1-9c79cff280a5", "Phoebe Buffay", 2537.31)
        };
    }

    @Test
    public void withdraw() {
        // withdrawing from checking account which is at index 0
        accounts[0].withdraw(1440);
        assertEquals(84.51, accounts[0].getBalance());
    }

    @Test
    public void overdraft() {
        accounts[0].withdraw(1534.43);
        assertEquals(-15.42, accounts[0].getBalance());
    }

    @Test
    public void overdraftLimit() { // overdraft limit is 200$

        accounts[0].withdraw(1726);

        assertEquals(1524.51, accounts[0].getBalance()); // if limit exceed than withdraw fail
    }

    @Test
    public void withdrawalFee() {
        // withdrawal fee is 5$ from saving account
        accounts[1].withdraw(100);
        assertEquals(2136.60, accounts[1].getBalance());
    }

    @Test
    public void withdrawalInterest() {
        accounts[2].withdraw(2434.31);// loan account already in debt $2537.31
        assertEquals(5020.31, accounts[2].getBalance());
    }

    @Test
    public void withdrawalLimit() {
        accounts[2].withdraw(7463.69);
        assertEquals(2537.31, accounts[2].getBalance());
    }

    @Test
    public void deposit() {// same for saving and chequing
        accounts[0].deposit(5000);
        assertEquals(6524.51, accounts[0].getBalance());
    }

    @Test
    public void loanDeposit() {
        accounts[2].deposit(1000);
        assertEquals(1537.31, accounts[2].getBalance());
    }

    @Test
    public void incomeTest() {
        double amount = 4000;
        accounts[0].deposit(amount);
        // account is not showing tax method so we have to typecast into Taxable
        ((Taxable) accounts[0]).tax(amount);
        assertEquals(5374.51, accounts[0].getBalance());
    }
}
