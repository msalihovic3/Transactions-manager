package ba.unsa.etf.rma.rma20salihovicmirnesa15;

public class AccountModel {
    public static Account account=new Account(20770, 4000, 2000);
    public AccountModel(){
    }

    public static void set(double budzet) {
        account.setBudget(budzet);
    }

    public  Account getAccount(){
        return account;
    }
    public static void setAccount(Account account1){
        account=account1;
    }
}
