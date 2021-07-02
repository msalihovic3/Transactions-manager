package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.os.Parcelable;

interface IAccountPresenter {
    double getLimit();
     Double getGlobal();

    public void refreshTransaction();

    void setBunget(double budzet);

   

    void setAccount(Parcelable account);

    Account getAccount();
    public void searchMovies(String query);
    void updateAccount(String toString, String toString1, String toString2);

    void updateBudget(double budzet2);

    void updateAccountBaza(String toString, String toString1, String toString2);
}
