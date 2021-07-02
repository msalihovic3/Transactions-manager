package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TransactionDetailInteractor
    extends AsyncTask<String, Integer, Void> {
        Transaction transaction;
        private OnMovieSearchDone caller;
        ArrayList<Transaction> transactions;

    public TransactionDetailInteractor (OnMovieSearchDone p) {
            caller = p;
            transactions=new ArrayList<>();
        };

    public TransactionDetailInteractor() {
        transactions=new ArrayList<>();
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
        System.out.println(strings[0]+"PROvjera");
            if(strings[0].equals("TRANSAKCIJE")) {
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
                            System.out.println(id + date + endDate + itemDescription + amount + interval);
                            if ((typeId == 1 || typeId == 2) && endDate.equals("null")) {
                                //nista se ne spasava
                            } else
                                transactions.add(new Transaction(cal.getTime(), amount, title, typeId, itemDescription, interval, endDate, id));
                        }

                        j++;
                    }

                    System.out.println(transactions);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                String json = "";
                String query = "";
                if (strings[0].contains("DELETE")) {
                    for (String id : strings[0].split("DELETE")) {
                        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/39c75b2f-ee79-4576-88fb-8d155d5d2104/transactions/" + id;
                        System.out.println(url1 + "MIKII");
                        URL url = null;
                        try {
                            url = new URL(url1);

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setInstanceFollowRedirects(false);
                            connection.setRequestMethod("DELETE");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("charset", "utf-8");
                            connection.setUseCaches(false);
                            System.out.println("Response code: " + connection.getResponseCode());

                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String line, responseText = "";
                            while ((line = br.readLine()) != null) {
                                System.out.println("LINE: " + line);
                                responseText += line;
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("update1");
                    if (strings[0].contains("idTransakcije")) {
                        System.out.println("update");
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
            }
            return null;
        }



    public void save(Transaction transaction, Context context) {

        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionURI = Uri.parse("content://rma.provider.transaction/elements");

        ContentValues values = new ContentValues();


        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId());
        System.out.println(transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        System.out.println(transaction.getType().name());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getType().name());
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(transaction.getDate())+"MINA");
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, sdf.format(transaction.getDate())); //treba format odrediti
        System.out.println(transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION, transaction.getItemDescription());
        System.out.println(transaction.getEndDate());
        if(transaction.getEndDate()!=null){
        values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, sdf.format(transaction.getEndDate()));}else{
            values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE,"null");
        }
        System.out.println(transaction.getText());
        values.put(TransactionDBOpenHelper.TRANSACTION_TEXT, transaction.getText());

        cr.insert(transactionURI,values);

    }

    public void updateBaza(Transaction transaction, Context context) {

        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionURI = Uri.parse("content://rma.provider.transaction/elements/#");

        ContentValues values = new ContentValues();



        System.out.println(transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        System.out.println(transaction.getType().name());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getType().name());
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(transaction.getDate())+"MINA");
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, sdf.format(transaction.getDate())); //treba format odrediti
        System.out.println(transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION, transaction.getItemDescription());
        System.out.println(transaction.getEndDate());
        if(transaction.getEndDate()!=null){
            values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, sdf.format(transaction.getEndDate()));}else{

            values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE,"null");
        }
        System.out.println(transaction.getText()+transaction.getId()+"U BAZIIIII");
        values.put(TransactionDBOpenHelper.TRANSACTION_TEXT, transaction.getText());

        //cr.update(transactionURI,values, String.valueOf(transaction.getId()),null);
        cr.update(transactionURI, values,  " = ?",
                new String[] {String.valueOf(transaction.getId())});

    }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            caller.onDone(transaction, transactions);
        }

    public void deleteTable(Transaction transaction,Context context) {

        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionURI = Uri.parse("content://rma.provider.transactionDelete/elements");

        ContentValues values = new ContentValues();

        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId());
        System.out.println(transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        System.out.println(transaction.getType().name());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getType().name());
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(transaction.getDate())+"MINA");
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, sdf.format(transaction.getDate())); //treba format odrediti
        System.out.println(transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION, transaction.getItemDescription());
        System.out.println(transaction.getEndDate());
        if(transaction.getEndDate()!=null){
            values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, sdf.format(transaction.getEndDate()));}else{
            values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE,"null");
        }
        System.out.println(transaction.getText()+"deleteeeeee"+transaction.getId());


        cr.insert(transactionURI,values);

    }

    public void undoDelete(int id, Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionURI = Uri.parse("content://rma.provider.transactionDelete/elements/#");

        cr.delete(transactionURI, String.valueOf(id),
                null);

    }

    public interface OnMovieSearchDone{
            void onDone(Transaction result, ArrayList<Transaction> transactions);

        }
}
