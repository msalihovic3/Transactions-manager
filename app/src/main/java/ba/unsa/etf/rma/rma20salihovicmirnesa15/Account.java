package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Account implements Parcelable {
    private double budget;
    private double totalLimit;
    private double mounthLimit;

    protected Account(Parcel in)  {



        budget = in.readDouble();
        totalLimit = in.readDouble();
        mounthLimit = in.readDouble();

    }


    @NonNull
    @Override
    public String toString() {
        return " "+ budget +"GLOBAL"+totalLimit;
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {

                return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    public Account(double budget, double totalLimit, double mounthLimit) {
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.mounthLimit = mounthLimit;
    }


    public Double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Double getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(double totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Double getMounthLimit() {
        return mounthLimit;
    }

    public void setMounthLimit(double mounthLimit) {
        this.mounthLimit = mounthLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeDouble(budget);
        dest.writeDouble(totalLimit);
        dest.writeDouble(mounthLimit);

    }


}
