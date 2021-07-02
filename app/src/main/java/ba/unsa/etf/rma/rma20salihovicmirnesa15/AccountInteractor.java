package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AccountInteractor extends AsyncTask<String, Integer, Void> implements IAcountInteractor {

    private Account account;
    private OnAccountDone caller;
    private boolean provjera=true;
    public AccountInteractor() {

    }

    public void setAccount(Account account){ AccountModel.setAccount(account);}

    @Override
    public void setBudget(double budzet) {
        AccountModel.set(budzet);
    }

    public AccountInteractor(OnAccountDone p) {
        caller = p;

    };


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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

        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(query!="") {

            UpdateAccount(strings);

        }else
        {
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/39c75b2f-ee79-4576-88fb-8d155d5d2104";
            provjera = true;
            try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);

                JSONObject jsonObject = new JSONObject(rezultat);
                Double budget = jsonObject.getDouble("budget");
                Double total = jsonObject.getDouble("totalLimit");
                Double month = jsonObject.getDouble("monthLimit");
                account = new Account(budget, total, month);
                AccountModel.account = account;
                //setAccount(account);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected Void UpdateAccount(String... params)
    {
        provjera=false;
        URL url = null;
        System.out.println("MOZEEEE"+params[0]);
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/39c75b2f-ee79-4576-88fb-8d155d5d2104";

            url = new URL(url1);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            OutputStream wr = urlConnection.getOutputStream();
            wr.write(params[0].getBytes());
            wr.flush();
            wr.close();
            System.out.println("response code from server" + urlConnection.getResponseCode());
           // return urlConnection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            //return 500;
        }finally {
            urlConnection.disconnect();
        }

        return null;
    }

    public void addAccountUpdate(Account account,Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionURI = Uri.parse("content://rma.provider.account/elements");

        ContentValues values = new ContentValues();

        values.put(TransactionDBOpenHelper.BUDGET, account.getBudget());

        values.put(TransactionDBOpenHelper.TOTAL, account.getTotalLimit());

        values.put(TransactionDBOpenHelper.MOUNTH, account.getMounthLimit());


        cr.insert(transactionURI,values);
    }

    public interface OnAccountDone{
        public void onDone(Account results);
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        if(provjera)
        caller.onDone(account);
    }

   /* @Override
    public Account get() {
        return AccountModel.account;
    }*/

    public Cursor getAccountCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.ACCOUNT_INTERNAL_ID,
                TransactionDBOpenHelper.BUDGET,
                TransactionDBOpenHelper.TOTAL,
                TransactionDBOpenHelper.MOUNTH
        };


        Uri adresa = Uri.parse("content://rma.provider.account/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;

        Cursor cur = cr.query(adresa,kolone,where,whereArgs,order);

        return cur;
    }
    @Override
    public void set(double budzet) {
        AccountModel.set(budzet);
    }
}
