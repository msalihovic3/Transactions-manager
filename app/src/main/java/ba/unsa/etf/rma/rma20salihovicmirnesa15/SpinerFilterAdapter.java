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

public class SpinerFilterAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;

    Context mContext;

    public SpinerFilterAdapter(@NonNull Context context, String[] titles, int[] images) {
        super(context, R.layout.spiner_filter);
        this.spinnerTitles = titles;
        this.spinnerImages = images;

        this.mContext = context;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
        TextView mPopulation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spiner_filter, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.idIconFilter);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.idTitleFilter);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);


        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    @Override
    public int getCount() {
        return spinnerTitles.length;
    }
}