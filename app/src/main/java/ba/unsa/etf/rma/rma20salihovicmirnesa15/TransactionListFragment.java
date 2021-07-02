package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class TransactionListFragment  extends Fragment implements ITransactionListView  {

    private TextView budget;
    private TextView totalLimit;
    private TextView textDate;
    private Button idAddtransaction;
    private ListView listTransaction;
    private Spinner fileterSpinner;
    private Spinner sortSpinner;
    private ImageView idLeft;
    private ImageView idRight;
    private SpinerFilterAdapter spinerAdapterFilter;
    private AccountModel accountModel=new AccountModel();
    //  private Account account ;
    private String[] tipovi={"Filter by","INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME", "REGULARINCOME","ALL"};
    private String[] sortiranje={ "Sort by","Price - Ascending" ,"Price - Descending","Title - Ascending","Title - Descending","Date - Ascending","Date - Descending"};
    private int[] image={R.drawable.filter,R.drawable.prvi,R.drawable.drugi,R.drawable.treci,R.drawable.cetvrti,R.drawable.peti,R.drawable.filter};

    private TransactionPresenter transactionPresenter;
    private IAccountPresenter accountPresenter;
    private AdapterTransaction transactionAdapter;
    private ArrayAdapter<String> sortAdapter;
    private boolean a=ConnectivityBroadcastReceiver.provjera;

    public TransactionPresenter getPresenter() {
        if (transactionPresenter == null) {

            transactionPresenter = new TransactionPresenter(this, getActivity());
        }

        return transactionPresenter;
    }

    public IAccountPresenter getAccountPresenter() {
        if (accountPresenter == null) {

            accountPresenter = new AccountPresenter(getActivity(),this);
        }

        return accountPresenter;
    }

    private OnItemClick onItemClick;
    public interface OnItemClick {
        public void onItemClicked(Transaction transaction, boolean a);
         void onItemClickedLeft(Account account,String a);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView=inflater.inflate(R.layout.fragment_list, container, false);
        transactionAdapter=new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());
        listTransaction= fragmentView.findViewById(R.id.listTrasaction);

        listTransaction.setAdapter(transactionAdapter);

        System.out.println(a+"PROVJERA");
        if(ConnectivityBroadcastReceiver.provjera) {
            getPresenter().searchTransaction("");

        }else{
            System.out.println(ConnectivityBroadcastReceiver.provjera+"transakcije");
            getPresenter().setAdapter();
        }

        listTransaction.setOnItemClickListener(listItemClickListener);
        onItemClick= (OnItemClick) getActivity();
        Intent intent = getActivity().getIntent();

        budget = fragmentView.findViewById(R.id.budget);
        totalLimit =fragmentView.findViewById((R.id.totalLimit));
        textDate = fragmentView.findViewById(R.id.textDate);
        idAddtransaction = fragmentView.findViewById(R.id.idAddTransaction);
        listTransaction = fragmentView.findViewById(R.id.listTrasaction);
        sortSpinner = fragmentView.findViewById(R.id.sortspiner);
        fileterSpinner = fragmentView.findViewById(R.id.filterSpinner);
        idLeft = fragmentView.findViewById(R.id.idLeft);
        idRight = fragmentView.findViewById(R.id.idRigth);

        if(ConnectivityBroadcastReceiver.provjera){
            System.out.println("RACUUUUN");
        getAccountPresenter().searchMovies("");

        }else{
          Account account=getAccountPresenter().getAccount();
            budget.setText("Global amount: " + account.getBudget());
            totalLimit.setText("Limit: " + account.getTotalLimit());

        }
       /* budget.setText("Global amount: " + accountPresenter.getGlobal());
        totalLimit.setText("Limit: " + accountPresenter.getLimit());*/
        SimpleDateFormat f = new SimpleDateFormat("MMMM, yyyy");
        Calendar cal=Calendar.getInstance();

        textDate.setText(f.format(cal.getTime()));

        if(ConnectivityBroadcastReceiver.provjera) {
            SpinerFilterAdapter filterAdapter = new SpinerFilterAdapter(getActivity(), tipovi, image);
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            fileterSpinner.setAdapter(filterAdapter);

            fileterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Get the spinner selected item text
                    String selectedItemText = (String) tipovi[i];
                    transactionAdapter = new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());

                    transactionPresenter.filterListTransaction(selectedItemText);
                    //transactionAdapter.setTransaction(a);
                    listTransaction.setAdapter(transactionAdapter);
                    sortSpinner.setSelection(0);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            sortAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, sortiranje);
            sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortSpinner.setAdapter(sortAdapter);

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Get the spinner selected item text
                    String selectedItemText = (String) adapterView.getItemAtPosition(i);

                    transactionAdapter = new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());

                    transactionPresenter.sortListTransaction(selectedItemText);
                    listTransaction.setAdapter(transactionAdapter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        idLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = textDate.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(date));
                    cal.add(Calendar.MONTH, -1);
                    Date newdate = cal.getTime();
                    sortSpinner.setSelection(0);
                    SimpleDateFormat f = new SimpleDateFormat("MMMM, yyyy");
                    textDate.setText(f.format(newdate));
                    transactionAdapter = new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());
                    getPresenter().monthTransaction(f.format(newdate));
                    listTransaction.setAdapter(transactionAdapter);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        idRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = textDate.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(date));
                    cal.add(Calendar.MONTH, 1);
                    Date newdate = cal.getTime();
                    sortSpinner.setSelection(0);
                    SimpleDateFormat f = new SimpleDateFormat("MMMM, yyyy");
                    textDate.setText(f.format(newdate));

                    transactionAdapter = new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());
                    getPresenter().monthTransaction(f.format(newdate));
                    listTransaction.setAdapter(transactionAdapter);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        listTransaction.setOnItemClickListener(listItemClickListener);

        idAddtransaction.setOnClickListener(addOnClickListener);

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
                  onItemClick.onItemClickedLeft(getAccountPresenter().getAccount(),"LEFT");
            }
            @Override
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
                onItemClick.onItemClickedLeft(getAccountPresenter().getAccount(),"RIGHT");
            }
        });

      //  budget.setText("Global amount: " + accountPresenter.getGlobal());
        return fragmentView;
    }


    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent transactionDetailIntent = new Intent(getActivity(),
                    TransactionDetailFragment.class);
            onItemClick.onItemClicked(null,true);
        }
    };

    @Override
    public void setTransaction(ArrayList<Transaction> transactions) {
        transactionAdapter.setTransaction(transactions);
    }

    private View lastaTauch;
    private String ime;
    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAccount(Account account) {

        getAccountPresenter().setAccount(account);
        budget.setText("Global amount: " + account.getBudget());
        totalLimit.setText("Limit: " + account.getTotalLimit());
    }

    private static int save = -1;
    private static  boolean boja=false;

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent transactionDetailIntent = new Intent(getActivity(),
                    TransactionDetailFragment.class);
            Transaction transaction = transactionAdapter.getTransaction(position);

            if(save==position && boja==true){
                if(lastaTauch!=null)
            lastaTauch.setBackgroundColor(Color.WHITE);
              boja=false;
            }
            else{
                if(lastaTauch!=null)
                lastaTauch.setBackgroundColor(Color.WHITE);
                view.setBackgroundColor(Color.BLUE);
                boja=true;
            }
            lastaTauch=view;
            save = position;

            if(boja ==false){
                onItemClick.onItemClicked(transaction,false);
            }else {
                onItemClick.onItemClicked(transaction,true);}
        }
    };


    public void onChanse(Account account) {
        getAccountPresenter().setAccount(account);
        transactionAdapter = new AdapterTransaction(getActivity(), R.layout.element_transaction, new ArrayList<Transaction>());
        getPresenter().refreshTransaction();
        listTransaction.setAdapter(transactionAdapter);
        budget.setText("Global amount: "+accountPresenter.getGlobal().toString());
        transactionAdapter.notifyDataSetChanged();





    }
}
