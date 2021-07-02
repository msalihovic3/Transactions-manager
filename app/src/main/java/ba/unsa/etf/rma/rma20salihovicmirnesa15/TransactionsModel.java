package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Intent;
import android.text.Editable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.PURCHASE;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARINCOME;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARPAYMENT;


public class TransactionsModel {


    public static ArrayList<Transaction> transactions = new ArrayList<Transaction>() {
        {

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse("3/3/2020"));
                     Date date1=cal.getTime();
                     cal.setTime(sdf.parse("1/3/2021"));
                add(new Transaction(date1, 657,
                        "Fotoaparat", INDIVIDUALPAYMENT, "kupovina fotoaparata",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("3/6/2020"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("22/11/2020"));
                add(new Transaction(date1, 350,
                        "Baterija", PURCHASE, "baterija za laptop",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("1/4/2020"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("1/3/2021"));
                add(new Transaction(date1, 130,
                        "Stipendija", REGULARINCOME, "Kantonalna stipendija",
                        30,cal.getTime()));

                cal.setTime(sdf.parse("1/4/2019"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("15/6/2020"));
                add(new Transaction(date1, 400,
                        "kirija", REGULARPAYMENT, "uplata za stan",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("3/2/2020"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("1/3/2022"));
                add(new Transaction(date1, 75,
                        "kurs", INDIVIDUALPAYMENT, "uplata za knjige",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("3/5/2020"));
                add(new Transaction(cal.getTime(), 150,
                        "takmicenje", INDIVIDUALINCOME, "nagrada",
                        25,null));

                cal.setTime(sdf.parse("1/1/2020"));
                add(new Transaction(cal.getTime(), 50,
                        "transakcija1", PURCHASE, "maske za lice",
                        25,null));
                cal.setTime(sdf.parse("3/12/2019"));
                add(new Transaction(cal.getTime(), 60,
                        "obrazovanje", INDIVIDUALPAYMENT, "knjiga za nastavu",
                        25,null));

                cal.setTime(sdf.parse("22/4/2020"));
                add(new Transaction(cal.getTime(), 350,
                        "lijekovi", PURCHASE, "za potrebe bolnice",
                        25,null));

                cal.setTime(sdf.parse("10/4/2020"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("15/6/2020"));
                add(new Transaction(date1, 2000,
                        "plata", REGULARINCOME, "uplata firme",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("30/4/2020"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("13/12/2020"));
                add(new Transaction(date1, 779,
                        "laptop", REGULARPAYMENT, "Lenovo",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("13/12/2020"));
                add(new Transaction(cal.getTime(), 90,
                        "tretman lica", INDIVIDUALPAYMENT, "uplata na racun firme",
                        25,null));

                cal.setTime(sdf.parse("16/6/2019"));
                add(new Transaction(cal.getTime(), 1500,
                        "prodaja", INDIVIDUALINCOME, "uplata na racun",
                        25,null));

                cal.setTime(sdf.parse("1/6/2021"));
                add(new Transaction(cal.getTime(), 50,
                        "odjeca", PURCHASE, "kupovina",
                        25,null));

                cal.setTime(sdf.parse("14/7/2020"));
                add(new Transaction(cal.getTime(), 657,
                        "uplatak", INDIVIDUALINCOME, "praznik",
                        25,null));
                cal.setTime(sdf.parse("13/12/2019"));
                add(new Transaction(cal.getTime(), 350,
                        "zvucnik", PURCHASE, "dostava",
                        25,null));

                cal.setTime(sdf.parse("1/11/2019"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("11/5/2020"));
                add(new Transaction(date1, 50,
                        "stipedija2", REGULARINCOME, "poslovna saradnja",
                        25,cal.getTime()));

                cal.setTime(sdf.parse("1/9/2019"));
                date1=cal.getTime();
                cal.setTime(sdf.parse("1/4/2020"));
                add(new Transaction(date1, 50,
                        "struja", REGULARPAYMENT, "gradjanske obaveze",
                        30,cal.getTime()));

                cal.setTime(sdf.parse("1/11/2019"));
                add(new Transaction(cal.getTime(), 200,
                        "punjac", INDIVIDUALPAYMENT, "za laptop",
                        25,null));

                cal.setTime(sdf.parse("17/4/2020"));
                add(new Transaction(cal.getTime(), 230,
                        "transakcija3", INDIVIDUALINCOME, "za programere",
                        25,null));

                cal.setTime(sdf.parse("1/6/2020"));
                add(new Transaction(cal.getTime(), 80,
                        "cipele", PURCHASE, "odjeca i obuca",
                        25,null));

        } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };



    public  static void set(ArrayList<Transaction> lista) {
        transactions=lista;
    }
}
