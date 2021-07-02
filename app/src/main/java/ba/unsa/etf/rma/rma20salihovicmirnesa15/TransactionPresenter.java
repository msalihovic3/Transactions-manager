package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static ba.unsa.etf.rma.rma20salihovicmirnesa15.TransactionsModel.transactions;
import static java.util.Calendar.WEEK_OF_MONTH;
import static java.util.Calendar.getInstance;

public class TransactionPresenter implements ITransactionPresenter ,ModelTransactionInteractor.OnMoviesSearchDone{


    private ITransactionListView view;
    private IModelTransactionInteractor interactor;
    private Context context;
    //pomocni
    ArrayList<Transaction> novi;
    private ArrayList<Transaction> odabrani;
    private ArrayList<Transaction> sort=new ArrayList<>();

    public TransactionPresenter(ITransactionListView view, Context context) {
            this.view       = view;
            this.interactor = new ModelTransactionInteractor();
            this.context    = context;
            this.odabrani=new ArrayList<>();

        }
    public TransactionPresenter(Context context) {

        this.interactor = new ModelTransactionInteractor();
        this.context    = context;
        this.odabrani=new ArrayList<>();

    }
        @Override
        public void refreshTransaction() {


            view.notifyTransactionListDataSetChanged();
        }

    @Override
    public ArrayList get(){

    return null;
    }



    @Override
    public void sortListTransaction(String a) {


            if(a.equals("Price - Ascending")){

                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        int s=p1.getAmount().compareTo(p2.getAmount());

                        return s;
                    }
                });

            }else if(a.equals("Price - Descending")){
                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        return -p1.getAmount().compareTo(p2.getAmount());
                    }
                });
            }else if(a.equals("Title - Ascending")){
                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        return p1.getTitle().compareTo(p2.getTitle());
                    }
                });
            }else if(a.equals("Title - Descending")){
                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        return -p1.getTitle().compareTo(p2.getTitle());
                    }
                });
            }else if (a.equals("Date - Ascending")){
                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        return p1.getDate().compareTo(p2.getDate());
                    }
                });
            }else if(a.equals("Date - Descending")){
                Collections.sort(sort, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction p1, Transaction p2) {
                        return -p1.getDate().compareTo(p2.getDate());
                    }
                });
            }


        view.setTransaction(sort);
        view.notifyTransactionListDataSetChanged();
    }


    @Override
    public void filterListTransaction(String selectedItemText) {


        ArrayList<Transaction> numbers = new ArrayList<>();
        if(!selectedItemText.equals("Filter by") && !selectedItemText.equals("ALL")){
            for(Transaction transaction: novi){
                if(transaction.getType().name().equals(selectedItemText)){
              numbers.add(transaction);
            }
        }
            sort=numbers;
            odabrani=numbers;
            view.setTransaction(numbers);
            System.out.println(novi);
        }else if (selectedItemText.equals("ALL")){
            for(Transaction transaction: novi)
                numbers.add(transaction);
            sort=numbers;
            view.setTransaction(numbers);
            odabrani=numbers;

        }
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void monthTransaction(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date zadani=cal.getTime();  //mjesec sa kojim uporedujem
        int mjesecZadanog=cal.get(Calendar.MONTH);
        int godinaZadanog=cal.get(Calendar.YEAR);

        ArrayList<Transaction> numbers = new ArrayList<>();
        if(!ConnectivityBroadcastReceiver.provjera) odabrani=TransactionsModel.transactions;
        for(Transaction transaction: odabrani){
                //date prvo
            Date dateTransaction1=transaction.getDate();
            cal.setTime(dateTransaction1);
            Date date1 = cal.getTime();   //pocetni datum
            Calendar datum=getInstance();
            datum.setTime(date1);
            if(transaction.getType().name().equals("REGULARINCOME") || transaction.getType().name().equals("REGULARPAYMENT") ){

                Boolean pomocna=true;

                if(mjesecZadanog==datum.get(Calendar.MONTH) && godinaZadanog==datum.get(Calendar.YEAR))

                    numbers.add(transaction);
                while(pomocna) {
                    datum.add(Calendar.DAY_OF_YEAR, Integer.parseInt(transaction.getTransactionInterval()));
                    System.out.println(date1+"ULAZIIIIII"+transaction.getEndDate()+"NOVI"+datum.getTime());

                    if(datum.getTime().after(date1) && datum.getTime().before(transaction.getEndDate()) ){
                        if(mjesecZadanog==datum.get(Calendar.MONTH) && godinaZadanog==datum.get(Calendar.YEAR))
                     //   System.out.println(date1+"ULAZIIIIII"+transaction.getEndDate()+"NOVI"+datum.getTime());
                        numbers.add(transaction);
                    }else
                        pomocna=false;
                }

            }else if(mjesecZadanog==datum.get(Calendar.MONTH) && godinaZadanog==datum.get(Calendar.YEAR))
                numbers.add(transaction);
        }


        sort=numbers;
        view.setTransaction(numbers);
        view.notifyTransactionListDataSetChanged();
    }

    public ArrayList getEntriesMounth(String nacin ) {
        if(!ConnectivityBroadcastReceiver.provjera) novi=TransactionsModel.transactions;

        ArrayList barEntries = new ArrayList<>();
        float suma=0;

        for(int i=0; i<12; i++){

            for (Transaction transaction: novi) {//interactor.get


                long brojDanaIzmedju=0;
                if (transaction.getType().name().equals("REGULARINCOME") || transaction.getType().name().equals("REGULARPAYMENT")) {
                    brojDanaIzmedju= transaction.getEndDate().getTime() - transaction.getDate().getTime();
                    brojDanaIzmedju= (int) TimeUnit.DAYS.convert(brojDanaIzmedju, TimeUnit.MILLISECONDS);
               }
                int interval;
                System.out.println(transaction.getTransactionInterval());
                if(transaction.getTransactionInterval().equals("null")) interval=0;
                else{
                interval=Integer.parseInt(transaction.getTransactionInterval());
                }


                if (nacin.equals("potrošnja")){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(Calendar.MONTH)==i && datum2.get(Calendar.YEAR)==godina  ){

                        suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina ){
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina) {
                                suma += transaction.getAmount();

                                }
                        }
                    }
                } else if (nacin.equals("zarada") ){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALINCOME") ||  transaction.getType().name().equals("PURCHASE")){
                        if( datum2.get(Calendar.MONTH)==i && datum2.get(Calendar.YEAR)==godina){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.MONTH)==i  && datum.get(Calendar.YEAR)==godina){
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina) {
                                    suma += transaction.getAmount();
                                }
                        }
                    }

                } else if (nacin.equals("ukupno")) {
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(Calendar.MONTH)==i && datum2.get(Calendar.YEAR)==godina){

                            suma -= transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.MONTH)==i  && datum.get(Calendar.YEAR)==godina){
                                suma -= transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina) {
                                    suma -= transaction.getAmount();

                                }
                        }
                    } else if(transaction.getType().name().equals("INDIVIDUALINCOME") ||  transaction.getType().name().equals("PURCHASE")){
                        if( datum2.get(Calendar.MONTH)==i && datum2.get(Calendar.YEAR)==godina){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina){
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.MONTH)==i && datum.get(Calendar.YEAR)==godina) {
                                    suma += transaction.getAmount();

                                }
                        }
                    }
                }

            }


            barEntries.add(new BarEntry( i, suma));
            suma=0;
        }

        return barEntries;
    }

    @Override
    public ArrayList getEntriesWeek(String nacin){
        if(!ConnectivityBroadcastReceiver.provjera) novi=TransactionsModel.transactions;

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());

        ArrayList barEntries = new ArrayList<>();
        float suma=0;
        Calendar cal = Calendar.getInstance();


        for(int i=0; i<5; i++) {

            for (Transaction transaction : novi) {// interactor.get() je ilo
                cal.setTime(transaction.getDate());

                long brojDanaIzmedju=0;
                if (transaction.getType().name().equals("REGULARINCOME") || transaction.getType().name().equals("REGULARPAYMENT")) {

                      brojDanaIzmedju= transaction.getEndDate().getTime() - transaction.getDate().getTime();
                        brojDanaIzmedju= (int) TimeUnit.DAYS.convert(brojDanaIzmedju, TimeUnit.MILLISECONDS);

                }
                int interval;
                if(transaction.getTransactionInterval().equals("null")) interval=0;
                else{
                    interval=Integer.parseInt(transaction.getTransactionInterval());
                }
                if (nacin.equals("potrošnja") ){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(WEEK_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum = getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR, broj);

                            if ((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) && datum.get(WEEK_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if (datum.get(WEEK_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                    suma += transaction.getAmount();

                                }
                        }
                    }

                } else if (nacin.equals("zarada")){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALINCOME") || transaction.getType().name().equals("PURCHASE") ){
                        if( datum2.get(WEEK_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum = getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR, broj);

                            if ((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) && datum.get(WEEK_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate())) {
                                if (datum.get(WEEK_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                    suma += transaction.getAmount();

                                }
                            }
                        }
                    }
                } else if (nacin.equals("ukupno") ) {
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(WEEK_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma -= transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(WEEK_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec ){
                                suma -= transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(WEEK_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec) {
                                    suma -= transaction.getAmount();

                                }
                        }
                    }else if(transaction.getType().name().equals("INDIVIDUALINCOME") || transaction.getType().name().equals("PURCHASE") ){
                        if( datum2.get(WEEK_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(WEEK_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec ){
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(WEEK_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec) {
                                    suma += transaction.getAmount();
                                }
                        }
                    }

                }

            }

            barEntries.add(new BarEntry((float)i , suma));
                suma = 0;
            }
            return barEntries;

    }

    public ArrayList getEntriesDay(String nacin ) {

        if(!ConnectivityBroadcastReceiver.provjera) novi=TransactionsModel.transactions;
        SimpleDateFormat sdf = new SimpleDateFormat("D", Locale.getDefault());

        ArrayList barEntries = new ArrayList<>();
        float suma=0;
        Calendar cal=getInstance();
        int trenutni=cal.get(Calendar.MONTH);
        trenutni=trenutni+1;
        for(int i=0; i<31; i++){
            for (Transaction transaction : novi) {//interactor.get() je bilo
            //    cal.setTime(transaction.getDate());

                long brojDanaIzmedju=0;
                if (transaction.getType().name().equals("REGULARINCOME") || transaction.getType().name().equals("REGULARPAYMENT")) {

                    brojDanaIzmedju= transaction.getEndDate().getTime() - transaction.getDate().getTime();
                    brojDanaIzmedju= (int) TimeUnit.DAYS.convert(brojDanaIzmedju, TimeUnit.MILLISECONDS);

                }
                int interval;
                if(transaction.getTransactionInterval().equals("null")) interval=0;
                else{
                    interval=Integer.parseInt(transaction.getTransactionInterval());
                }
                if (nacin.equals("potrošnja") ){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(Calendar.DAY_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum = getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR, broj);

                            if ((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) && datum.get(Calendar.DAY_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if (datum.get(Calendar.DAY_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                    suma += transaction.getAmount();

                                }
                        }
                    }

                } else if (nacin.equals("zarada")){
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());
                    if(transaction.getType().name().equals("INDIVIDUALINCOME") || transaction.getType().name().equals("PURCHASE") ){
                        if( datum2.get(Calendar.DAY_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum = getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR, broj);

                            if ((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) && datum.get(Calendar.DAY_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate())) {
                                if (datum.get(Calendar.DAY_OF_MONTH) == i + 1 && datum.get(Calendar.YEAR) == godina && datum.get(Calendar.MONTH) == mjesec) {
                                    suma += transaction.getAmount();

                                }
                            }
                        }
                    }
                } else if (nacin.equals("ukupno") ) {
                    Calendar datum2=getInstance();
                    int godina=datum2.get(Calendar.YEAR);
                    int mjesec=datum2.get(WEEK_OF_MONTH);
                    datum2.setTime(transaction.getDate());

                    if(transaction.getType().name().equals("INDIVIDUALPAYMENT")){
                        if( datum2.get(Calendar.DAY_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma -= transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARPAYMENT") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.DAY_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec ){
                                suma -= transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.DAY_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec) {
                                    suma -= transaction.getAmount();

                                }
                        }
                    }else if(transaction.getType().name().equals("INDIVIDUALINCOME") || transaction.getType().name().equals("PURCHASE") ){
                        if( datum2.get(Calendar.DAY_OF_MONTH)==i+1 && datum2.get(Calendar.YEAR)==godina && datum2.get(Calendar.MONTH)==mjesec){

                            suma += transaction.getAmount();
                        }
                    } else if( (transaction.getType().name().equals("REGULARINCOME") )){

                        for(int broj=0; broj<=brojDanaIzmedju; broj+=interval) {

                            Calendar datum=getInstance();
                            datum.setTime(transaction.getDate());
                            datum.add(Calendar.DAY_OF_YEAR,broj);

                            if((datum.getTime().after(transaction.getDate()) && datum.getTime().before(transaction.getEndDate())) &&   datum.get(Calendar.DAY_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec ){
                                suma += transaction.getAmount();

                            } else if (datum.getTime().equals(transaction.getDate()) || datum.getTime().equals(transaction.getEndDate()))
                                if(datum.get(Calendar.DAY_OF_MONTH)==i+1 && datum.get(Calendar.YEAR)==godina && datum.get(Calendar.MONTH)==mjesec) {
                                    suma += transaction.getAmount();
                                }
                        }
                    }

                }

            }

            barEntries.add(new BarEntry( i, suma));
            suma=0;
        }

        return barEntries;
    }


    private void setOdabrani(ArrayList<Transaction> odabrani) {
        this.odabrani = odabrani;
    }

    @Override
    public void searchTransaction(String query){
        
        new ModelTransactionInteractor((ModelTransactionInteractor.OnMoviesSearchDone)
                this).execute(query);
    }

    @Override
    public void onDone(ArrayList<Transaction> results, String a) {
        novi=results; //odraditi poslove sortiranja
        sort=results;
        odabrani=results;
        view.setTransaction(results);
        view.notifyTransactionListDataSetChanged();
    }

    public void setAdapter() {
            if(!ConnectivityBroadcastReceiver.provjera){
                System.out.println(TransactionsModel.transactions);
            view.setTransaction(TransactionsModel.transactions);}

    }
}
