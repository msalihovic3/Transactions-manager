package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public interface IModelTransactionInteractor {

   //  ArrayList<Transaction> get();
     void set(ArrayList<Transaction> lista);
    public Cursor getMovieCursor(Context context);
}
