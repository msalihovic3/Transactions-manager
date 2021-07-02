package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class ConnectivityBroadcastReceiver extends BroadcastReceiver {
    public static boolean provjera=true;

    ModelTransactionInteractor mod=new ModelTransactionInteractor();
    AccountInteractor account=new AccountInteractor();
    static int alert=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {

                Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
                toast.show();
                provjera=false;
                System.out.println(provjera+"DIS");


            }
            else {
                Cursor cursor=mod.getMovieCursor(context);
                Cursor accountCursor=account.getAccountCursor(context);
                AccountPresenter accountPresenter=new AccountPresenter(context.getApplicationContext());
                ArrayList<String> accountJsons=new ArrayList<>();
              accountJsons=addAccount(accountCursor);
            for(String accountJson: accountJsons){
                System.out.println(accountJson);
                accountPresenter.searchMovies(accountJson);
            }
                TransactionPresenter presenter=new TransactionPresenter(context.getApplicationContext());

                ArrayList<String> jsons=addTransakcije(cursor);
                for(String json: jsons){
                    System.out.println(json);
            presenter.searchTransaction(json);
                    }

            Cursor cursorDelete=mod.getDeleteCursor(context);
                TransctionDetailPresenter presenter1=new TransctionDetailPresenter(context);
            ArrayList<String> jsonsDelete=deleteTransakcije(cursorDelete);
            for(String json: jsonsDelete){
                presenter1.searchDetail(json);
            }

                Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                toast.show();
                mod.deleteBase(context);
                provjera = true;
                System.out.println(provjera + "CON");
              // presenter.refreshTransaction();
            AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setTitle("");
            alertDialog1.setMessage("POTREBNO JE SACEKATI ODREĐENI VREMENSKI PERIOD DA SE PODACI UČITAJU SA SERVISA.");
            alertDialog1.show();
            if(alert!=0){

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("");
            alertDialog.setMessage("Potrebno je refresh prozora uraditi tako sto odemo na drugi VIEW i vratimo se na prvobitni.");
            alertDialog.show();}
            alert+=1;


    }}

    private ArrayList<String> addAccount(Cursor accountCursor) {
        ArrayList<String> jsons=new ArrayList<>();
        String json="";

        if(accountCursor.moveToFirst()) {

            System.out.println("PRAZNO");
            do {
                int idBudget = accountCursor.getColumnIndexOrThrow(TransactionDBOpenHelper.BUDGET);
                int idTotal = accountCursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TOTAL);
                int idMounth = accountCursor.getColumnIndexOrThrow(TransactionDBOpenHelper.MOUNTH);
                //  int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);

                String jsonInputString = "{\"budget\": "+accountCursor.getDouble(idBudget)+"}";
                json = "{ \"budget\": " +accountCursor.getDouble(idBudget) + ", \"totalLimit\": " + accountCursor.getDouble(idTotal) +  ",\"monthLimit\": " + +accountCursor.getDouble(idMounth) + "}";

                jsons.add(json);
                System.out.println(json+"CURSOR");

            } while (accountCursor.moveToNext());
        }else{

        }
        return  jsons;
    }

    private ArrayList<String> deleteTransakcije(Cursor cursor) {
        ArrayList<String> jsons=new ArrayList<>();
        String json="";

        if(cursor.moveToFirst()) {

            System.out.println("PRAZNO");
            do {
                int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
              //  int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
                json=cursor.getInt(idPos)+"DELETE";
                    jsons.add(json);
                    System.out.println(json+"CURSOR");

            } while (cursor.moveToNext());
        }else{

        }
            return  jsons;
    }

    private int odreditiIdTypa(String type){
        if(type.equals("REGULARPAYMENT")) return 1;
        else if(type.equals("REGULARINCOME")) return 2;
        else if(type.equals("PURCHASE")) return 3;
        else if(type.equals("INDIVIDUALINCOME")) return 4;
        else
            return 5;
    }

    protected ArrayList<String> addTransakcije(Cursor cursor) {
        String quety=""; //sta treba poslati sa servis;
        String json="";
        ArrayList<String > jsons=new ArrayList<>();
       // System.out.println("PRAZNO");
        if(cursor.moveToFirst()) {

            System.out.println("PRAZNO");
            do{
                int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
                int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
                int dataPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE);
                int typePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE);
                int amountPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
                int intervalPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERVAL);
                int descriptionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION);
                int endDatePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ENDDATE);
                int textPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TEXT);

                System.out.println(cursor.getInt(idPos)+cursor.getString(titlePos)+ cursor.getInt(intervalPos)+
                        cursor.getString(dataPos)+cursor.getString(typePos)+ cursor.getDouble(amountPos)+
                        cursor.getInt(idPos)+ cursor.getString(descriptionPos)+cursor.getString(endDatePos));

                SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.FRANCE);

                if (!cursor.getString(endDatePos).equals("null")) {
                    System.out.println(cursor.getString(endDatePos));
                    //pretvaranje date u format za json
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(cursor.getString(dataPos)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date newdate = cal.getTime();
                    String datumpoc=sdf2.format(newdate);
                    //pretvaranje endDate u format za json
                    try {
                        cal.setTime(sdf.parse(cursor.getString(endDatePos)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String endDate=sdf2.format(cal.getTime());

                    //json format podataka
                    System.out.println("UPDATE TRANS"+cursor.getString(textPos));
                    if(cursor.getString(textPos).equals("Offline izmjena")){

                       json = cursor.getInt(idPos)+"idTransakcije"+"{ \"date\": \"" + datumpoc + "\", \"title\": \"" + cursor.getString(titlePos) + "\",\"amount\": " + cursor.getDouble(amountPos) + ",\"endDate\": \"" + endDate + "\",\"itemDescription\": \"" + cursor.getString(descriptionPos) + "\",\"transactionInterval\": " + cursor.getInt(intervalPos) + ",\"typeId\": " + odreditiIdTypa(cursor.getString(typePos)) + "}";
                        System.out.println("UPDATE TRANS"+json);

                    }else if(cursor.getString(textPos).equals("Offline brisanje")){

                    }
                    else {
                        json = "{ \"date\": \"" + datumpoc + "\", \"title\": \"" + cursor.getString(titlePos) + "\",\"amount\": " + cursor.getDouble(amountPos) + ",\"endDate\": \"" + endDate + "\",\"itemDescription\": \"" + cursor.getString(descriptionPos) + "\",\"transactionInterval\": " + cursor.getInt(intervalPos) + ",\"typeId\": " + odreditiIdTypa(cursor.getString(typePos)) + "}";

                    }
                    jsons.add(json);
                }else{

                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(cursor.getString(dataPos)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date newdate = cal.getTime();
                    String datumpoc=sdf2.format(newdate);
                    if(cursor.getString(textPos).equals("Offline izmjena")){

                        json = cursor.getInt(idPos)+"idTransakcije"+"{ \"date\": \"" + datumpoc + "\", \"title\": \"" + cursor.getString(titlePos) + "\",\"amount\": " + cursor.getDouble(amountPos) + ",\"endDate\": " + null + ",\"itemDescription\": \"" + cursor.getString(descriptionPos) + "\",\"transactionInterval\": " + cursor.getInt(intervalPos) + ",\"typeId\": " + odreditiIdTypa(cursor.getString(typePos)) + "}";

                        System.out.println("UPDATE TRANS"+json);

                    }
                    else {

                        json= "{ \"date\": \"" + datumpoc + "\", \"title\": \"" + cursor.getString(titlePos) + "\",\"amount\": " + cursor.getDouble(amountPos) + ",\"endDate\": " + null + ",\"itemDescription\": \"" + cursor.getString(descriptionPos) + "\",\"transactionInterval\": " + cursor.getInt(intervalPos) + ",\"typeId\": " + odreditiIdTypa(cursor.getString(typePos)) + "}";
                        System.out.println("Dodavanje"+json+cursor.getString(textPos));
                    }
                    jsons.add(json);
                }

            }while (cursor.moveToNext());
        }
        return jsons;
    }


}
