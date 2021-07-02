package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BudgetFragment extends Fragment implements ITransactionListView {
    private TextView idBudget;
    private EditText idTotalLimit;
    private EditText idMounthLimit;
    private IAccountPresenter accountPresenter;
    private Button idEdit;

    public IAccountPresenter getPresenter() {
        if (accountPresenter == null) {
            accountPresenter = new AccountPresenter(  getActivity(),this);

        }

        return accountPresenter;
    }

    private OnItemClickEdit onItemClickEdit;

    @Override
    public void setTransaction(ArrayList<Transaction> transaction) {

    }

    @Override
    public void notifyTransactionListDataSetChanged() {

    }

    @Override
    public void setAccount(Account account) {
    //    System.out.println(account+"minaa");
        if(account!=null) {
            idBudget.setText(account.getBudget().toString());
            idTotalLimit.setText(account.getTotalLimit().toString());
            idMounthLimit.setText(account.getMounthLimit().toString());
        }
    }

    public interface OnItemClickEdit {
        public void onItemClickedEdit(Account transaction);
        public void onClickView(Account account,String a);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
Account vrijednost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        idBudget = fragmentView.findViewById(R.id.idBudgetFrag);
        idTotalLimit = fragmentView.findViewById(R.id.idTotalLimitFrag);
        idMounthLimit = fragmentView.findViewById(R.id.idMounthFrag);
        idEdit=fragmentView.findViewById(R.id.idEdit);
        onItemClickEdit= (OnItemClickEdit) getActivity();
        if(ConnectivityBroadcastReceiver.provjera){
         getPresenter().searchMovies("");}
        else{
           Account account= getPresenter().getAccount();
            idBudget.setText(account.getBudget().toString());
            idTotalLimit.setText(account.getTotalLimit().toString());
            idMounthLimit.setText(account.getMounthLimit().toString());
        }
        idEdit.setOnClickListener(editOnClickListener);

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {

                onItemClickEdit.onClickView(getPresenter().getAccount(),"LEFT");
            }
            @Override
            public void onSwipeRight() {

                onItemClickEdit.onClickView(getPresenter().getAccount(),"RIGHT");
            }

        });


        return fragmentView;
    }


    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent transactionDetailIntent = new Intent(getActivity(),
                    TransactionDetailFragment.class);

            //accountPresenter.updateAccount(idBudget.getText().toString(),idTotalLimit.getText().toString(),idMounthLimit.getText().toString());

            String jsonInputString = "{\"totalLimit\": "+idTotalLimit.getText().toString()+",\"monthLimit\": "+idMounthLimit.getText().toString()+"}";

            if(ConnectivityBroadcastReceiver.provjera) {
                getPresenter().searchMovies(jsonInputString);
            }else{
                getPresenter().updateAccountBaza(idBudget.getText().toString(),idTotalLimit.getText().toString(),idMounthLimit.getText().toString());
                System.out.println("mmmmmmmmmmmmmmmmmmmmmmmm");
            }
            onItemClickEdit.onItemClickedEdit(accountPresenter.getAccount());



        }
    };
}


