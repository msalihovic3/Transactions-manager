package ba.unsa.etf.rma.rma20salihovicmirnesa15;


import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

public class Transaction implements Parcelable{

    private Date date;
    private Double amount;
    private String title;
    private Type type;
    private String itemDescription;
    private int transactionInterval;
    private Date endDate;
    private Integer id;
    private String text="";
    private static int brojc=1;
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Transaction(Integer id,Date date, double amount, String title, Type type, String itemDescription, int transactionInterval, Date endDate,String text) throws ParseException {
       if(id==0){

           this.id=brojc;
           brojc+=1;
       }else{
        this.id=id;}
        this.date = date;
        this.amount = amount;
        if(title.length()<3 || title.length()>15) throw new IllegalArgumentException("Nije korektno");
        else {
            this.title = title;
        }
        this.type = type;
        if(type.toString().equals("prvi") || type.toString().equals("cetvrti")){
            this.itemDescription=null;
        }else{
            this.itemDescription = itemDescription;
        }
        if(type.toString().equals("drugi") || type.toString().equals("peti")){

            this.transactionInterval=transactionInterval;
            this.endDate = endDate;

        }
        else {
            this.transactionInterval= 0;
            this.endDate = null;
        }

        this.text=text;

    }
    public Transaction(Date date, Double amount, String title, Integer type, String itemDescription, String transactionInterval, String endDate, Integer id) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.FRANCE);
        Calendar cal = Calendar.getInstance();
       // cal.setTime(sdf.parse(date));

        Date date1 = cal.getTime();
        this.date = date;
        this.amount = amount;
        this.title = title;
        if(type==1) this.type =Type.REGULARPAYMENT;
        else if(type==2) this.type =Type.REGULARINCOME;
        else if(type==3) this.type =Type.PURCHASE;
        else if(type==4) this.type =Type.INDIVIDUALINCOME;
        else
        this.type =Type.INDIVIDUALPAYMENT ;//nije ureduuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu
        this.itemDescription = itemDescription;

        if(!transactionInterval.equals("null")){
        this.transactionInterval = Integer.parseInt(transactionInterval);
        }

        if ( !endDate.equals("null")) {

        cal.setTime(sdf.parse(endDate));
        this.endDate = cal.getTime();
        }
        this.id = id;
    }

    protected Transaction(Parcel in) throws ParseException {

        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(in.readString()));
        date = cal.getTime();
        amount = in.readDouble();
        title = in.readString();
        type = Type.valueOf(in.readString());
        itemDescription = in.readString();
        transactionInterval = in.readInt();
        cal.setTime(sdf.parse(in.readString()));
        endDate=cal.getTime();
    }

    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            try {
                return new Transaction(in);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public Transaction(Date date, double amount, String title, Type type, String itemDescription, int transactionInterval, Date endDate) throws ParseException {
        this.date = date;
        this.amount = amount;
        if(title.length()<3 || title.length()>15) throw new IllegalArgumentException("Nije korektno");
        else {
            this.title = title;
        }
        this.type = type;
        if(type.toString().equals("peti") || type.toString().equals("cetvrti")){
            this.itemDescription=null;
        }else{
        this.itemDescription = itemDescription;
        }
        if(type.toString().equals("drugi") || type.toString().equals("peti")){

            this.transactionInterval=transactionInterval;
            this.endDate = endDate;

        }
        else {
            this.transactionInterval= 0;
            this.endDate = null;
        }



    }

    @NonNull
    @Override
    public String toString() {
        return title+id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String  getTransactionInterval() {
        return ""+transactionInterval;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        dest.writeString(sdf.format(date));
        dest.writeDouble(amount);
        dest.writeString(title);
        dest.writeString(type.name());
        dest.writeString(itemDescription);
        dest.writeInt(transactionInterval);
        dest.writeString(sdf.format(endDate));

    }
}
