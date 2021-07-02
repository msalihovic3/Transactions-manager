package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class AccountPresenter  implements IAccountPresenter,AccountInteractor.OnAccountDone{

    //  private ITransactionListView view;
    private AccountInteractor interactor;
    private ITransactionListView view;
    private Context context;
    private Account account;
    //pomocni


    public AccountPresenter(Context context,ITransactionListView view) {
        this.view       =  view;
        this.interactor = new AccountInteractor();
        this.context    = context;
        //this.account=interactor.get();
    }

    public AccountPresenter(Context applicationContext) {
        this.context=applicationContext;
    }

    @Override
    public Double getGlobal(){
        return AccountModel.account.getBudget();
    }

    @Override
    public void refreshTransaction() {
           //System.out.println(interactor.get().getBudget());

    }

    @Override
    public void searchMovies(String query){
        new AccountInteractor((AccountInteractor.OnAccountDone)
                this).execute(query);
    }


    @Override
    public void setBunget(double budzet) {
        account.setBudget(budzet);
      //  interactor.get().setBudget(budzet);
      //  interactor.setBunget(budzet);
    }

    @Override
    public void setAccount(Parcelable account) {
        this.account=(Account) account;
       // interactor.set((Account)account);
    }

    @Override
    public Account getAccount() {
        return AccountModel.account;
    }

    @Override
    public void updateAccount(String toString, String toString1, String toString2) {
             Account ac=new Account(Double.parseDouble(toString),Double.parseDouble(toString1),Double.parseDouble(toString2));
             account=ac;
             AccountModel.account=ac;
             //interactor.set(ac);
    }

    @Override
    public void updateAccountBaza(String toString, String toString1, String toString2) {
        Account ac=new Account(Double.parseDouble(toString),Double.parseDouble(toString1),Double.parseDouble(toString2));
        account=ac;
        AccountModel.account=ac;
        interactor.addAccountUpdate(AccountModel.account,context);
        //interactor.set(ac);
    }
    @Override
    public void updateBudget(double budzet2) {
        AccountModel.account.setBudget(budzet2);
        interactor.addAccountUpdate(AccountModel.account,context);
    }

    @Override
    public double getLimit(){
        return account.getTotalLimit();
    }


    @Override
    public void onDone(Account results) {
        if(ConnectivityBroadcastReceiver.provjera){
        view.setAccount(results);
        view.notifyTransactionListDataSetChanged();}else{
            AccountModel.account=results;
        }

    }
}
