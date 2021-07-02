package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARINCOME;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARPAYMENT;

public class TransactionDetailFragment extends Fragment implements ITransactionListView {
    private EditText idTitle;
    private EditText idType;
    private EditText idDescription;
    private EditText idDate;
    private EditText idInterval;
    private EditText idEndDate;
    private EditText idAmount;
    private TextView idOffline;
    private Button save;
    private Button delete;
    private Integer idTransakcije;
    private AccountModel accountModel=new AccountModel();
    private Account account;
    private String code;
    private TransctionDetailPresenter transctionDetailPresenter;
    private IAccountPresenter accountPresenter;
    double budzet=0;
    Transaction transaction;

    public IAccountPresenter getAccountPresenter() {
        if (accountPresenter == null) {

            accountPresenter = new AccountPresenter( getActivity(),this);
        }

        return accountPresenter;
    }

    public ITransactionDetailPresenter getPresenter() {
        if (transctionDetailPresenter == null) {
            transctionDetailPresenter = new TransctionDetailPresenter(getActivity(),getActivity());
        }
        return transctionDetailPresenter;
    }

    private OnItemClickDetail onItemClickDetail;

    @Override
    public void setTransaction(ArrayList<Transaction> transaction) {

    }

    @Override
    public void notifyTransactionListDataSetChanged() {

    }

    @Override
    public void setAccount(Account account) {


        this.account=account;


        System.out.println(transaction+"zzz"+account);

        if(idType.getText().toString().equals("REGULARPAYMENT") ||idType.getText().toString().equals("INDIVIDUALPAYMENT")) {
            if(transaction==null) budzet = account.getBudget();
            else {

                budzet = account.getBudget() + Double.parseDouble(idAmount.getText().toString());
                System.out.println(account.getBudget() + "   " + idAmount.getText() +"   " + budzet);


            }

        }else{
            System.out.println(transaction+"zzz");
            if(transaction==null) {

                budzet = account.getBudget();
                System.out.println(account.getBudget() + "   " + idAmount.getText() +" ako je null  " + budzet);

            } else {

                budzet = account.getBudget() - Double.parseDouble(idAmount.getText().toString());
                System.out.println(account.getBudget() + "   " + idAmount.getText() +"   " + budzet);
            }



        }


    }

    public interface OnItemClickDetail {
        public void onItemClickedDelete(Transaction transaction);


        void onItemClickedSave(Transaction transaction);
        void onItemClickedSave2(Transaction transaction);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View fragmentMenager = inflater.inflate(R.layout.fragment_detail, container, false);
        if (getArguments() != null && getArguments().containsKey("transaction")) {
            getPresenter().setTransaction(getArguments().getParcelable("transaction"));

            save = (Button) fragmentMenager.findViewById(R.id.idSave);
            delete = (Button) fragmentMenager.findViewById(R.id.idDelete);
            idTitle = (EditText) fragmentMenager.findViewById(R.id.idTitle);
            idDate = (EditText) fragmentMenager.findViewById(R.id.idDate);
            idAmount = (EditText) fragmentMenager.findViewById(R.id.idAmount);
            idEndDate = (EditText) fragmentMenager.findViewById(R.id.idEndDate);
            idDescription = (EditText) fragmentMenager.findViewById(R.id.idDescription);
            idInterval = (EditText) fragmentMenager.findViewById(R.id.idInterval);
            idType = (EditText) fragmentMenager.findViewById(R.id.idType);
            idOffline = (TextView) fragmentMenager.findViewById(R.id.idOffline);
            transaction = getPresenter().getTransaction();

            onItemClickDetail= (OnItemClickDetail) getActivity();
            if (transaction!=null) {
                //namekey=transaction.getTitle();

                idTitle.setText(transaction.getTitle());
                idType.setText(transaction.getType().name());
                idAmount.setText(transaction.getAmount().toString());
                idInterval.setText(transaction.getTransactionInterval());
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                idDate.setText(f.format(transaction.getDate()));

                idTransakcije=transaction.getId();
                System.out.println(idTransakcije+"IDDDDD");

                if (transaction.getEndDate() == null) {
                    idEndDate.setText(null);
                } else {
                    idEndDate.setText(f.format(transaction.getEndDate()));
                }
                idDescription.setText(transaction.getItemDescription());
                oznacavanjePromjene();
                //oduzimam prvo budzet da mogla registorvati promjene
                if(ConnectivityBroadcastReceiver.provjera){
                getAccountPresenter().searchMovies("");
                    idOffline.setText("");
                transctionDetailPresenter.searchDetail("TRANSAKCIJE");}else{
                     budzet=getAccountPresenter().getGlobal();
                    if(transaction.getText().equals("")){
                        idOffline.setText("Offline izmjena");
                    }else{
                    idOffline.setText(transaction.getText());}
                }
                if(transaction.getText().equals("Offline brisanje"))
                    delete.setText("Undo");

                delete.setOnClickListener(deleteOnClickListener);
                save.setOnClickListener(saveOnClickListener);
            }else{
                if(ConnectivityBroadcastReceiver.provjera){
                    getAccountPresenter().searchMovies("");
                    transctionDetailPresenter.searchDetail("TRANSAKCIJE");
                }else{

                }

                delete.setEnabled(false);
                save.setOnClickListener(save2OnClickListener);
            }

            if(ConnectivityBroadcastReceiver.provjera==false) {
                account = getAccountPresenter().getAccount();

                    if (idType.getText().toString().equals("REGULARPAYMENT") || idType.getText().toString().equals("INDIVIDUALPAYMENT")) {
                        if (transaction == null) budzet = account.getBudget();
                        else {
                            budzet = account.getBudget() + Double.parseDouble(idAmount.getText().toString());
                            System.out.println(account.getBudget() + "   " + idAmount.getText() + "   " + budzet);
                        }

                    } else {
                        System.out.println(transaction + "zzzTRANSAKCIJA");
                        if (transaction == null) {

                            budzet = account.getBudget();
                            System.out.println(account.getBudget() + "   " + idAmount.getText() + " ako je null  " + budzet);

                        } else {
                            System.out.println(transaction + "zzzTRANSAKCIJA"+budzet);
                            budzet = account.getBudget() - Double.parseDouble(idAmount.getText().toString());
                            System.out.println(account.getBudget() + "   " + idAmount.getText() + "   " + budzet);
                        }

                }
            }
        }

        return fragmentMenager;
    }

    private int odreditiIdTypa(String type){
        if(type.equals("REGULARPAYMENT")) return 1;
        else if(type.equals("REGULARINCOME")) return 2;
        else if(type.equals("PURCHASE")) return 3;
        else if(type.equals("INDIVIDUALINCOME")) return 4;
        else
           return 5;
    }

    private View.OnClickListener save2OnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("action", "add");
             if(!ConnectivityBroadcastReceiver.provjera)  idOffline.setText("Offline dodavanje");
            if (validirajPolja()) {
                try {
                    SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.FRANCE);

                    if (!idEndDate.getText().toString().equals("")) {
                        System.out.println(idEndDate.getText().toString());
                        //pretvaranje date u format za json
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(idDate.getText().toString()));
                        Date newdate = cal.getTime();
                        String datumpoc=sdf2.format(newdate);
                        //pretvaranje endDate u format za json
                        cal.setTime(sdf.parse(idEndDate.getText().toString()));
                        String endDate=sdf2.format(cal.getTime());

                        //json format podataka
                        if(ConnectivityBroadcastReceiver.provjera){
                        String jsonInputString = "{ \"date\": \""+datumpoc+"\", \"title\": \""+idTitle.getText().toString()+"\",\"amount\": "+idAmount.getText().toString()+",\"endDate\": \""+endDate+"\",\"itemDescription\": \""+idDescription.getText().toString()+"\",\"transactionInterval\": "+idInterval.getText().toString()+",\"typeId\": "+odreditiIdTypa(idType.getText().toString())+"}";
                        transctionDetailPresenter.searchDetail(jsonInputString);}
                        else{
                            idOffline.setText("Offline dodavanje");
                            //ako nema konekcije
                            transctionDetailPresenter.addTransaction(0,idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), idEndDate.getText().toString(),"Offline dodavanje");

                        }
                    } else {

                        //pretvaranje date u format za json
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(idDate.getText().toString()));
                        String datumpoc = sdf2.format(cal.getTime());

                        if(ConnectivityBroadcastReceiver.provjera){
                        //json format podataka
                        String jsonInputString = "{ \"date\": \"" + datumpoc + "\", \"title\": \"" + idTitle.getText().toString() + "\",\"amount\": " + idAmount.getText().toString() + ",\"endDate\": " + null + ",\"itemDescription\": \"" + idDescription.getText().toString() + "\",\"transactionInterval\": " + null + ",\"typeId\": " + odreditiIdTypa(idType.getText().toString()) + "}";
                        transctionDetailPresenter.searchDetail(jsonInputString);
                    }else{
                            idOffline.setText("Offline dodavanje");
                            //ako nema konekcije
                            transctionDetailPresenter.addTransaction(0,idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), null,"Offline dodavanje");

                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    if (transctionDetailPresenter.provjeraBudzeta(idDate.getText().toString())) {


                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("ADD");
                        alertDialog.setMessage("Are you sure for this change?");
                        alertDialog.show();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //registorvanje promjena sa novim unosom
                String a = idType.getText().toString().toUpperCase();
                if (a.equals("REGULARPAYMENT") || a.equals("INDIVIDUALPAYMENT")) {
                    double budzet2 = budzet- Double.parseDouble(idAmount.getText().toString());
                    String jsonInputString = "{\"budget\": "+budzet2+"}";

                    if(ConnectivityBroadcastReceiver.provjera) {
                        getAccountPresenter().searchMovies(jsonInputString);
                    }else{
                        getAccountPresenter().updateBudget(budzet2);
                    }

                } else {
                    double budzet2 = budzet + Double.parseDouble(idAmount.getText().toString());
                    String jsonInputString = "{\"budget\": "+budzet2+"}";
                    if(ConnectivityBroadcastReceiver.provjera) {
                        getAccountPresenter().searchMovies(jsonInputString);
                    }else{
                        getAccountPresenter().updateBudget(budzet2);
                    }
                }
                onItemClickDetail.onItemClickedSave2(getPresenter().getTransaction());
            }


        }

        ;
    };


    private View.OnClickListener saveOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("action", "update");
            SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.FRANCE);



            if (!idEndDate.getText().toString().equals("")) {
                System.out.println(idEndDate.getText().toString());
                //pretvaranje date u format za json
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sdf.parse(idDate.getText().toString()));

                Date newdate = cal.getTime();
                String datumpoc=sdf2.format(newdate);
                //pretvaranje endDate u format za json
                cal.setTime(sdf.parse(idEndDate.getText().toString()));
                String endDate=sdf2.format(cal.getTime());

                //json format podataka
                String jsonInputString =transaction.getId()+"idTransakcije"+"{ \"date\": \""+datumpoc+"\", \"title\": \""+idTitle.getText().toString()+"\",\"amount\": "+idAmount.getText().toString()+",\"endDate\": \""+endDate+"\",\"itemDescription\": \""+idDescription.getText().toString()+"\",\"transactionInterval\": "+idInterval.getText().toString()+",\"TransactionTypeId\": "+odreditiIdTypa(idType.getText().toString())+"}";

                    System.out.println(jsonInputString+"cccc");
                    if(ConnectivityBroadcastReceiver.provjera){
                        System.out.println("NEMAKONEKCIJE");
                   transctionDetailPresenter.searchDetail(jsonInputString);
                    }
                    else {

                        if(transaction.getText().equals(""))  {
                            System.out.println("NEMAKONEKCIJE prvi put");
                            transctionDetailPresenter.addTransactionUpdate(transaction.getId(),idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), idEndDate.getText().toString(),"Offline izmjena");
                        }

                        else {
                            System.out.println("NEMAKONEKCIJE dodavana");

                            transctionDetailPresenter.updateTransactionBaza(transaction.getId(),idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), idEndDate.getText().toString(),transaction.getText());
                        }
                        idOffline.setText("Offline izmjena");
                        transctionDetailPresenter.updateTransaction(transaction.getId(), idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), idEndDate.getText().toString(),transaction.getText());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                } else {

                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sdf.parse(idDate.getText().toString()));

                    Date newdate = cal.getTime();
                    String datumpoc=sdf2.format(newdate);

                    //json format podataka
                    String jsonInputString = transaction.getId()+"idTransakcije"+"{ \"date\": \""+datumpoc+"\", \"title\": \""+idTitle.getText().toString()+"\",\"amount\": "+idAmount.getText().toString()+",\"endDate\": "+null+",\"itemDescription\": \""+idDescription.getText().toString()+"\",\"transactionInterval\": "+null+",\"TransactionTypeId\": "+odreditiIdTypa(idType.getText().toString())+"}";

                    System.out.println(jsonInputString);
                    if(ConnectivityBroadcastReceiver.provjera){

                    transctionDetailPresenter.searchDetail(jsonInputString); }
                else{
                        System.out.println("NEMAKONEKCIJE"+transaction.getText());
                    if(transaction.getText().equals("")){
                        System.out.println("ako je transakcije tek izmijenjena  i nije blo promjena na njoj niti je dodana tek");
                        transctionDetailPresenter.addTransactionUpdate(transaction.getId(),idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), null,"Offline izmjena");}
                    else {
                        System.out.println("NEMAKONEKCIJE");
                            transctionDetailPresenter.updateTransactionBaza(transaction.getId(),idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), null,transaction.getText());
                    }
                        transctionDetailPresenter.updateTransaction(transaction.getId(), idDate.getText().toString(), idAmount.getText().toString(), idTitle.getText().toString(), idType.getText().toString(), idDescription.getText().toString(), idInterval.getText().toString(), null,transaction.getText());
                        idOffline.setText("Offline izmjena");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                }



            idTitle.setBackgroundColor(Color.WHITE);
            idDate.setBackgroundColor(Color.WHITE);
            idAmount.setBackgroundColor(Color.WHITE);
            idDescription.setBackgroundColor(Color.WHITE);
            idInterval.setBackgroundColor(Color.WHITE);
            idEndDate.setBackgroundColor(Color.WHITE);
            idType.setBackgroundColor(Color.WHITE);
            try {

                if (transctionDetailPresenter.provjeraBudzeta(idDate.getText().toString())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("UPDATE");
                    alertDialog.setMessage("Are you sure for this change?");
                    alertDialog.show();

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String a = idType.getText().toString().toUpperCase();
            if (a.equals("REGULARPAYMENT") || a.equals("INDIVIDUALPAYMENT")) {
                double budzet2 = budzet - Double.parseDouble(idAmount.getText().toString());
                System.out.println(budzet+"  BUDZET  "+ budzet2+"trenutni "+Double.parseDouble(idAmount.getText().toString()));
                String jsonInputString = "{\"budget\": "+budzet2+"}";
                if(ConnectivityBroadcastReceiver.provjera) {
                    getAccountPresenter().searchMovies(jsonInputString);
                }else{
                    getAccountPresenter().updateBudget(budzet2);
                }

            } else {
                double budzet2 = budzet+ Double.parseDouble(idAmount.getText().toString());
                System.out.println(budzet+"  BUDZET  "+ budzet2);
                String jsonInputString = "{\"budget\": "+budzet2+"}";
                if(ConnectivityBroadcastReceiver.provjera) {
                    getAccountPresenter().searchMovies(jsonInputString);
                }else{
                    getAccountPresenter().updateBudget(budzet2);
                }

            }
            onItemClickDetail.onItemClickedSave(getPresenter().getTransaction());

        }
    };


    private View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            final Intent transactionDetailIntent = new Intent(getActivity(),
                    TransactionListFragment.class);

            if (delete.getText().toString().equals("Undo")) {
                System.out.println("dugme"+delete.getText().toString()+"ccccccccccccc");
                transctionDetailPresenter.undoDelete(transaction.getId());
                if (idType.getText().toString().equals("REGULARPAYMENT") || idType.getText().toString().equals("INDIVIDUALPAYMENT")) {
                    double budzet2 = budzet - 2*Double.parseDouble(idAmount.getText().toString());
                    System.out.println(budzet+"  BUDZET  "+ budzet2+"trenutni "+Double.parseDouble(idAmount.getText().toString()));
                    getAccountPresenter().updateBudget(budzet2);


                } else {
                    double budzet2 = budzet+ 2*Double.parseDouble(idAmount.getText().toString());
                    System.out.println(budzet+"  BUDZET  "+ budzet2);


                        getAccountPresenter().updateBudget(budzet2);


                }

                delete.setText("Delete");
            } else {
                System.out.println("dugme"+delete.getText().toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle("Delete");
                dialog.setMessage("Are you sure you want to delete this entry?");

                if(transaction.getText().equals("Offline dodavanje")){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Izbrisana je transakcija koja je dodana u offline rezimu rada. n" +
                            "Nije moguc njen povratak na dugmetu UNDO");
                    alertDialog.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Izbrisana je transakcija u offline rezimu rada. n" +
                            "Moguc njen povratak na dugmetu UNDO");
                    alertDialog.show();
                }
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        Intent intent = new Intent();
                        intent.putExtra("action", "delete");


                        //transctionDetailPresenter.deleteTransaction(getPresenter().getTransaction().getTitle());
                        // transctionDetailPresenter.searchMovie(getPresenter().getTransaction().getId().toString());

                        // getAccountPresenter().setBunget(budzet);

                        System.out.println(budzet + "BUDZET NOVI");
                        String jsonInputString = "{\"budget\": " + budzet + "}";
                        if (ConnectivityBroadcastReceiver.provjera) {
                            getAccountPresenter().searchMovies(jsonInputString);

                            System.out.println(idTransakcije + "DELETE");
                            transctionDetailPresenter.searchDetail(idTransakcije + "DELETE");
                        } else {

                                getAccountPresenter().updateBudget(budzet);

                            transctionDetailPresenter.deleteTransaction(idTransakcije, transaction.getText());
                            delete.setText("Undo");
                        }
                        //transctionDetailPresenter.promjenaBudzeta(budzet);

                        onItemClickDetail.onItemClickedDelete(null);

                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                onItemClickDetail.onItemClickedDelete(getPresenter().getTransaction());
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();


            }
        }

        };

    private void oznacavanjePromjene(){

        idTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idTitle.setBackgroundColor(Color.GREEN);
            }
        });

        idDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idDate.setBackgroundColor(Color.GREEN);
            }
        });
        idAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idAmount.setBackgroundColor(Color.GREEN);
            }
        });
        idType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idType.setBackgroundColor(Color.GREEN);
            }
        });
        idDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idDescription.setBackgroundColor(Color.GREEN);
            }
        });
        idInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idInterval.setBackgroundColor(Color.GREEN);
            }
        });
        idEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                idEndDate.setBackgroundColor(Color.GREEN);
            }
        });

    }



    public boolean validirajPolja() {
        boolean tacno = true;
        if (idTitle.getText().toString().equals("") || idTitle.getText().toString().length() < 3 || idTitle.getText().toString().length() > 15 ) {
            idTitle.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            idTitle.setBackgroundColor(Color.WHITE);
        }

        String a = idType.getText().toString().toUpperCase();
        if (idType.getText().toString().equals("") || (!a.equals("INDIVIDUALPAYMENT") && !a.equals("REGULARPAYMENT") && !a.equals("PURCHASE") && !a.equals("INDIVIDUALINCOME") && !a.equals("REGULARINCOME"))) {
            idType.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            idType.setBackgroundColor(Color.WHITE);
        }

        idType.setText(a);

        //validacija datuma

        if (idDate.getText().toString().equals("")) {
            idDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            String datePoocetna = idDate.getText().toString();
            int broja = 0;
            boolean provjera = true;
            for (String w : datePoocetna.split("/", 0)) {
                broja++;
                for (int i = 0; i < w.length(); i++) {

                    char karatkte = w.charAt(i);
                    //ako se dijelovi datuma sastoje od nekih znakova koji nisu cifre
                    if (!(karatkte >= '0' && karatkte <= '9')) {
                        idDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
                        tacno = false;
                        provjera = false;
                        break;
                    }
                }
            }
            //ako se sastoji od vise od 3 rijeci kada se rastavi sa split() funkcijom
            if (broja < 3 || broja > 3) {
                idDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
                tacno = false;
            } else if (provjera) {
                idDate.setBackgroundColor(Color.WHITE);
            }
        }


        if (idAmount.getText().toString().equals("")) {
            idAmount.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            boolean provjera = true;
            String iznos = idAmount.getText().toString();
            for (int i = 0; i < idAmount.getText().toString().length(); i++)
                if (!Character.isDigit(iznos.charAt(i)) && iznos.charAt(i) != '-') {
                    tacno = false;
                    idAmount.setBackgroundColor(Color.parseColor("#FFCDD2"));
                    provjera = false;
                }
            if (provjera)
                idAmount.setBackgroundColor(Color.WHITE);
        }


        if (idDescription.getText().toString().equals("") && !(a.equals("REGULARINCOME") || a.equals("INDIVIDUALINCOME"))) {
            idDescription.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            if(a.equals("REGULARINCOME") || a.equals("INDIVIDUALINCOME")) idDescription.setText(null);
            idDescription.setBackgroundColor(Color.WHITE);
        }
        if (idInterval.getText().toString().equals("") && (a.equals("REGULARINCOME") || a.equals("REGULARPAYMENT"))) {
            idInterval.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else {
            if(!(a.equals("REGULARINCOME") || a.equals("REGULARPAYMENT"))) {idInterval.setText(null);
            idEndDate.setText(null);}
            idInterval.setBackgroundColor(Color.WHITE);
        }

        //provjera da li je ispred pocetnog datuma

        if (idEndDate.getText().toString().equals("") && ((a.equals("REGULARINCOME") || a.equals("REGULARPAYMENT")) || a.equals(""))) {
            idEndDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
            tacno = false;
        } else if (idEndDate.getText().toString().equals("")) {
            idEndDate.setBackgroundColor(Color.WHITE);
        } else {
            boolean provjera = true;
            String datePoocetna = idEndDate.getText().toString();
            int broja = 0;


            for (String w : datePoocetna.split("/", 0)) {
                broja++;
                for (int i = 0; i < w.length(); i++) {

                    char karatkte = w.charAt(i);
                    //ako se dijelovi datuma sastoje od nekih znakova koji nisu cifre
                    if (!(karatkte >= '0' && karatkte <= '9')) {
                        idEndDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
                        tacno = false;
                        provjera = false;
                        break;
                    }
                }

            }

            //ako se sastoji od vise od 3 rijeci kada se rastavi sa split() funkcijom
            if (broja < 3 || broja > 3) {
                idEndDate.setBackgroundColor(Color.parseColor("#FFCDD2"));
                tacno = false;
            } else if (provjera) {
                idEndDate.setBackgroundColor(Color.WHITE);
            }

        }


        return tacno;
    }
}
