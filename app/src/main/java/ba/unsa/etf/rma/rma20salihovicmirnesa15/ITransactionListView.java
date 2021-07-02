package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import java.util.ArrayList;

public interface ITransactionListView {

        void setTransaction(ArrayList<Transaction> transaction);
        void notifyTransactionListDataSetChanged();
        void setAccount(Account account);


}
