package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import java.text.ParseException;
import java.util.ArrayList;

interface ITransactionPresenter {
    void refreshTransaction();
    void sortListTransaction(String a);

    void monthTransaction(String selectedItemText) throws ParseException;
    void filterListTransaction(String selectedItemText);
ArrayList get();
    ArrayList getEntriesMounth(String nacin);
    ArrayList getEntriesDay(String nacin);
    ArrayList getEntriesWeek(String nacin);
    public void searchTransaction(String query);
}
