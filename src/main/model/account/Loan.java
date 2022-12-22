package src.main.model.account;

public class Loan extends Account {

    public static final double INTEREST_RATE = 0.02;
    public static final double MAX_DEBT = 10000.0;

    public Loan(String id, String name, double balance) {
        super(id, name, balance);
    }

    public Loan(Loan source) {
        super(source);
    }

    @Override
    public Account clone() {
        return new Loan(this);
    }

    @Override
    public void deposit(double amount) {
        super.setBalance(super.round(super.getBalance() - amount));
    }

    @Override
    public boolean withdraw(double amount) {
        if ((super.getBalance() + amount) > MAX_DEBT) {
            return false;
        }
        // debt increases with loan and additional 2% interest
        super.setBalance(super.round(super.getBalance() + amount + (amount * INTEREST_RATE)));
        return true;
    }
}
