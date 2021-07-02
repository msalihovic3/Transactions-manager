package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.getInstance;

public class ModelTransactionInteractor extends AsyncTask<String, Integer, Void> implements IModelTransactionInteractor {


        boolean provjera=true;
        private String tmdb_api_key="";
        ArrayList<Transaction> transactions;
        private OnMoviesSearchDone caller;
        String oznaka;
    public ModelTransactionInteractor(OnMoviesSearchDone p) {
            caller = p;
            transactions = new ArrayList<Transaction>();
        };

    public ModelTransactionInteractor() {
        transactions = new ArrayList<Transaction>();
    }

    public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            return sb.toString();
        }

        @Override
        protected Void doInBackground(String... strings) {
if(strings[0].equals("")) {
    String page = "?page=";

    int j = 0;
    try {

        boolean provjera = true;
        while (provjera) {
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/39c75b2f-ee79-4576-88fb-8d155d5d2104/transactions";

            url1 += page + j;
            System.out.println(url1);
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);

            JSONObject jo = new JSONObject(result);
            JSONArray results = jo.getJSONArray("transactions");
            provjera = false;
            for (int i = 0; i < results.length(); i++) {
                provjera = true;
                System.out.println(result.length());
                JSONObject transaction = results.getJSONObject(i);
                String title = transaction.getString("title");
                Integer id = transaction.getInt("id");
                String date = transaction.getString("date");
                String endDate = transaction.getString("endDate");
                String itemDescription = transaction.getString("itemDescription");
                Double amount = transaction.getDouble("amount");
                String interval = transaction.getString("transactionInterval");
                Integer typeId = transaction.getInt("TransactionTypeId");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.FRANCE);
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(date));
                System.out.println(id + date + endDate + itemDescription + amount + interval+"staaaaa");
                if ((typeId == 1 || typeId == 2) && endDate.equals("null")) {
                    //nista se ne spasava
                } else
                    transactions.add(new Transaction(cal.getTime(), amount, title, typeId, itemDescription, interval, endDate, id));
            }
            System.out.println(transactions+"SVE");
            TransactionsModel.set(transactions);

            j++;
        }
        oznaka = strings[0];
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }
}else{
    String query="";
    String json="";
    if (strings[0].contains("idTransakcije")) {
        boolean prva = true;
        for (String a : strings[0].split("idTransakcije")) {
            if (prva) {
                query += "/" + a;
                prva = false;
            } else {
                json = a;
            }

        }
    } else {
        json = strings[0];
    }
   provjera=false;

    String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/39c75b2f-ee79-4576-88fb-8d155d5d2104/transactions" + query;
    System.out.println(strings[0] + "UNUTAR" + json + " " + query);
    try {

        URL url = new URL(url1);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");


        try (OutputStream wr = urlConnection.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            wr.write(input, 0, input.length);
        }
        ;

        StringBuilder sb = new StringBuilder();
        int HttpResult = urlConnection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
        } else {
            System.out.println(urlConnection.getResponseMessage());
        }


    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            if(provjera){ ;
            caller.onDone(transactions,oznaka);
            }
        }

        public interface OnMoviesSearchDone{
            public void onDone(ArrayList<Transaction> results, String a);
        }



   @Override
   public Cursor getMovieCursor(Context context) {
       ContentResolver cr = context.getApplicationContext().getContentResolver();
       String[] kolone = new String[]{
               TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID,
               TransactionDBOpenHelper.TRANSACTION_ID,
               TransactionDBOpenHelper.TRANSACTION_TITLE,
               TransactionDBOpenHelper.TRANSACTION_TYPE,
               TransactionDBOpenHelper.TRANSACTION_DATE,
               TransactionDBOpenHelper.TRANSACTION_INTERVAL ,
               TransactionDBOpenHelper.TRANSACTION_AMOUNT ,
               TransactionDBOpenHelper.TRANSACTION_DESCRIPTION ,
               TransactionDBOpenHelper.TRANSACTION_ENDDATE,
               TransactionDBOpenHelper.TRANSACTION_TEXT

       };


       Uri adresa = Uri.parse("content://rma.provider.transaction/elements");
       String where = null;
       String whereArgs[] = null;
       String order = null;

       Cursor cur = cr.query(adresa,kolone,where,whereArgs,order);

       return cur;
   }

    public Cursor getDeleteCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID,
                TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL ,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT ,
                TransactionDBOpenHelper.TRANSACTION_DESCRIPTION ,
                TransactionDBOpenHelper.TRANSACTION_ENDDATE

        };


        Uri adresa = Uri.parse("content://rma.provider.transactionDelete/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;

        Cursor cur = cr.query(adresa,kolone,where,whereArgs,order);

        return cur;
    }

   public void deleteBase(Context context){
       ContentResolver cr = context.getApplicationContext().getContentResolver();
       Uri adresa = Uri.parse("content://rma.provider.transaction/elements");
       cr.delete(adresa,null,null);
       ContentResolver crDelete = context.getApplicationContext().getContentResolver();
       Uri adresanova = Uri.parse("content://rma.provider.transactionDelete/elements");
       crDelete.delete(adresanova,null,null);
       ContentResolver crAccount = context.getApplicationContext().getContentResolver();
       Uri adresaAc = Uri.parse("content://rma.provider.account/elements");
       crAccount.delete(adresaAc,null,null);
   }
    @Override
    public void set(ArrayList<Transaction> lista) {
        TransactionsModel.set(lista);
    }







}
