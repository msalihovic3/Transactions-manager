package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARINCOME;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARPAYMENT;

public interface ITransactionDetailPresenter {

     Transaction getTransaction();

     double obracunTotalLimita();

     double obracunMounthLimita(String toString) throws ParseException;

     boolean provjeraBudzeta(String date) throws ParseException;

     void addTransaction(int id, String toString, String amount, String title, String type, String itemDescription, String intrval, String endDate,String text) throws ParseException;
      ArrayList<Transaction> deleteTransaction(int name,String s);
      void updateTransaction(int id, String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate,String text) throws ParseException;
    public void searchDetail(String query);
    void setTransaction(Parcelable transaction);
}
