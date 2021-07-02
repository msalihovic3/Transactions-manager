package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Color.parseColor;
import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity  implements TransactionListFragment.OnItemClick ,BudgetFragment.OnItemClickEdit ,GraphsFragment.OnItemClickGraphs,TransactionDetailFragment.OnItemClickDetail {

    private boolean twoPaneMode;
    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(ConnectivityBroadcastReceiver.provjera+"PRVO");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);
        if (details != null) {
            twoPaneMode = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment) fragmentManager.findFragmentById(R.id.transaction_detail);
            if (true) {
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail, detailFragment)
                        .commit();
            }
        } else {

            twoPaneMode = false;
        }
        TransactionListFragment  listFragment = (TransactionListFragment) getSupportFragmentManager().findFragmentById(R.id.transaction_list);
        if (listFragment == null) {

            listFragment = new TransactionListFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.transaction_list, listFragment)
                    .commit();
        } else {

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }



    @Override
    public void onPause() {

        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onItemClicked(Transaction transaction, boolean a) {
        Bundle arguments = new Bundle();
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        if(a==false && twoPaneMode==true){
            arguments.putParcelable("transaction", null);
            detailFragment.setArguments(null);
        }else {
        arguments.putParcelable("transaction", transaction);}


        detailFragment.setArguments(arguments);
        if (twoPaneMode) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        } else {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemClickedLeft(Account account, String a) {
        Bundle arguments = new Bundle();
        if(a.equals("LEFT")) {
            BudgetFragment fragmentBudget = new BudgetFragment();
            arguments.putParcelable("account", account);
            fragmentBudget.setArguments(arguments);
            if (twoPaneMode == false) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, fragmentBudget)
                        .addToBackStack(null)
                        .commit();
            }
        }else if(a.equals("RIGHT")){

            GraphsFragment graphsFragment = new GraphsFragment();
            arguments.putParcelable("account", account);
            graphsFragment.setArguments(arguments);
            if (twoPaneMode == false) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, graphsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }




    @Override
    public void onItemClickedDelete(Transaction transaction) {


        Bundle arguments = new Bundle();

        if (twoPaneMode) {
            TransactionListFragment listFragment = (TransactionListFragment) getSupportFragmentManager().findFragmentById(R.id.transaction_list);
            TransactionDetailFragment detailFragment=new TransactionDetailFragment();
            arguments.putParcelable("transaction", transaction);

            detailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, listFragment).replace(R.id.transaction_detail, detailFragment).addToBackStack(null)
                    .commit();
        }else{
            TransactionListFragment transactionListFragment=new TransactionListFragment();
          //  transactionListFragment.setAccount(account);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, transactionListFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onItemClickedEdit(Account account) {
        Bundle arguments = new Bundle();

        BudgetFragment budgetFragment = new BudgetFragment();
       arguments.putParcelable("account",account);
        budgetFragment.setArguments(arguments);

        if (twoPaneMode) {

        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, budgetFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onClickView(Account account, String a) {
        Bundle arguments = new Bundle();
        if(a.equals("LEFT")) {
            GraphsFragment fragmentBudget = new GraphsFragment();
            arguments.putParcelable("account", account);
            fragmentBudget.setArguments(arguments);
            if (twoPaneMode == false) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, fragmentBudget)
                        .addToBackStack(null)
                        .commit();
            }
        }else if(a.equals("RIGHT")){
            TransactionListFragment transactionListFragment = new TransactionListFragment();
            arguments.putParcelable("account", account);
            transactionListFragment.setArguments(arguments);

            if (twoPaneMode == false) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, transactionListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onItemClickedSave(Transaction transaction) {
        Bundle arguments = new Bundle();

        if (twoPaneMode) {
            TransactionListFragment listFragment = (TransactionListFragment) getSupportFragmentManager().findFragmentById(R.id.transaction_list);
            TransactionDetailFragment detailFragment=new TransactionDetailFragment();
            arguments.putParcelable("transaction", transaction);
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, listFragment).replace(R.id.transaction_detail, detailFragment).addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onItemClickedSave2(Transaction transaction) {
        Bundle arguments = new Bundle();

        if (twoPaneMode) {
            TransactionListFragment listFragment = (TransactionListFragment) getSupportFragmentManager().findFragmentById(R.id.transaction_list);
            TransactionDetailFragment detailFragment=new TransactionDetailFragment();
            arguments.putParcelable("transaction", transaction);
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list, listFragment).replace(R.id.transaction_detail, detailFragment).addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onClickViewGraphs(Account account, String a){
        System.out.println("RIGHHHHT");
        Bundle arguments = new Bundle();
        if(a.equals("LEFT")) {
            TransactionListFragment fragmentBudget = new TransactionListFragment();
            arguments.putParcelable("account", account);
          //  fragmentBudget.setAccount(account);
            if (twoPaneMode == false) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, fragmentBudget)
                        .addToBackStack(null)
                        .commit();
            }
        }else {
            System.out.println("RIGHHHHT");
            BudgetFragment fragmentBudget = new BudgetFragment();
            arguments.putParcelable("account", account);
            fragmentBudget.setAccount(account);

            if (twoPaneMode == false) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list, fragmentBudget)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }





}