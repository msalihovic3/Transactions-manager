package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class AdapterTransaction extends ArrayAdapter<Transaction>  {

    private int resource;
    private UUID id;
    public TextView title;
    public TextView amount;
    public ImageView icon;

    public AdapterTransaction(@NonNull Context context, int _resource, ArrayList<Transaction> items) {
        super(context, _resource,items);
        resource = _resource;
    }

    public void setTransaction(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> nulic=new ArrayList<>();
        this.clear();
       // this.addAll(nulic);
        this.addAll(transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().
                    getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        Transaction transaction = getItem(position);

        title = newView.findViewById(R.id.title);
        amount = newView.findViewById(R.id.amount);
        icon = newView.findViewById(R.id.icon);

        title.setText(transaction.getTitle());
        amount.setText(transaction.getAmount().toString());

        String type = transaction.getType().toString();
        try {
            Class res = R.drawable.class;
            Field field = res.getField(type);
            int drawableId = field.getInt(null);
            icon.setImageResource(drawableId);
        } catch (Exception e) {
            icon.setImageResource(R.drawable.left);
        }

        return newView;
    }


    public Transaction getTransaction(int position) {
        return this.getItem(position);
    }
}