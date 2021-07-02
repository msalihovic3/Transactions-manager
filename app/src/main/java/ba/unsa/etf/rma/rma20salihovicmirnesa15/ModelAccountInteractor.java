package ba.unsa.etf.rma.rma20salihovicmirnesa15;


public class ModelAccountInteractor {

    public Account get() {

        return AccountModel.account;

    }


    public void set(Account account) {
        AccountModel.setAccount(account);
    }

}
