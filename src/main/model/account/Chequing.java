package src.main.model.account;

public class Chequing extends Account implements Taxable {

    private static final double OVERDRAFT_FEE = 5.5;
    private static final double OVERDRAFT_LIMIT = -200;

    private static final double TAXABLE_INCOME = 3000;
    private static final double TAX_RATE = 0.15;

    public Chequing(String id, String name, double balance) {
        super(id, name, balance);
    }

    public Chequing(Chequing source) {
        super(source);
    }

    @Override
    public Account clone() {
        return new Chequing(this);// And the object we're creating, it's going to get all of its values from the
                                  // current object that's calling. this method, which would be this.
    }

    @Override
    public void deposit(double amount) {
        super.setBalance(super.round(super.getBalance() + amount));
        // logic will be same in saving account but in loan when user deposit the debt
        // decreases

    }

    @Override
    public boolean withdraw(double amount) {
        if ((super.getBalance() - amount) < OVERDRAFT_LIMIT) {
            return false;// if this if execute thn we dont want bellow code to execute that's why
            // returning false from this if
        } else if ((super.getBalance() - amount) < 0) {
            // this will work if user tries to withdraw more than balance
            super.setBalance(super.round(super.getBalance() - amount - OVERDRAFT_FEE));

        } else {
            // 2 decimal digit
            super.setBalance(super.round(super.getBalance() - amount));

        }
        return true;// if any of the elseif or else execute it will return true
    }

    @Override
    public void tax(double income) {
        // if income < 3000 it will return 0
        double tax = Math.max(0, income - TAXABLE_INCOME) * TAX_RATE;
        super.setBalance(super.round(super.getBalance() - tax));
    }

}
