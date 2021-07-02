package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphsFragment extends Fragment implements ITransactionListView {

    private ITransactionPresenter accountPresenter1;
    private IAccountPresenter accountPresenter;
    private Button buttonMounth;
    private Button buttonWeek;
    private Button buttonDay;
    private BarChart barChart;
    private BarChart barChart2;
    private BarChart barChart3;
    private String oznaka;

    public ITransactionPresenter getPresenterTransaction() {
        if (accountPresenter1 == null) {

            accountPresenter1 = new TransactionPresenter(this, getActivity());
        }

        return accountPresenter1;
    }
    public IAccountPresenter getPresenter() {
        if (accountPresenter == null) {

            accountPresenter = new AccountPresenter(  getActivity(),this);
        }

        return accountPresenter;
    }

    private  OnItemClickGraphs onItemClickGraphs;
    public interface OnItemClickGraphs {

        public void onClickViewGraphs(Account a, String m);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragmentView = inflater.inflate(R.layout.fragment_grafic, container, false);


        barChart = fragmentView.findViewById(R.id.IDprvi);
        barChart2 = fragmentView.findViewById(R.id.barChart);
        barChart3 = fragmentView.findViewById(R.id.barChart2);
        buttonDay= fragmentView.findViewById(R.id.idDay);
        buttonMounth= fragmentView.findViewById(R.id.idMounth);
        buttonWeek= fragmentView.findViewById(R.id.idWeek);
        //getPresenterTransaction().searchTransaction(null);
        if(ConnectivityBroadcastReceiver.provjera){
        getPresenterTransaction().searchTransaction("");
        }else {
            getPresenterTransaction();
            oznaka="month";
            graphsMounth();
        }
        oznaka="month";
     //   graphsMounth();
        onItemClickGraphs= (OnItemClickGraphs) getActivity();

        //graphsWeek();
        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
                onItemClickGraphs.onClickViewGraphs(getPresenter().getAccount(),"LEFT");
            }
            @Override
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();

                onItemClickGraphs.onClickViewGraphs(getPresenter().getAccount(),"RIGTH");
            }
        });

        buttonMounth.setOnClickListener(onItemClickMounth);
        buttonWeek.setOnClickListener(onItemClickWeek);
        buttonDay.setOnClickListener(onItemClickDay);
        return fragmentView;
    }

    private View.OnClickListener onItemClickMounth = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            oznaka="month";
            barChart.clear();
            barChart2.clear();
            barChart3.clear();
            graphsMounth();
        }

    };

    private View.OnClickListener onItemClickWeek = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            oznaka="week";
            barChart.clear();
            barChart2.clear();
            barChart3.clear();

            graphsWeek();
        }

    };

    private View.OnClickListener onItemClickDay = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            oznaka="day";
            barChart.clear();
            barChart2.clear();
            barChart3.clear();

            graphsDay();
        }

    };


    void graphsMounth(){

        ArrayList barEntries;
        BarData barData;
        BarDataSet barDataSet;
      //  accountPresenter1.searchTransaction("");

        barEntries =accountPresenter1.getEntriesMounth("potrošnja");
        barDataSet = new BarDataSet(barEntries, "POTROSNJA");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");

        barData= new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));


 System.out.println("Potrošnaj");

        barEntries = accountPresenter1.getEntriesMounth("zarada");
        barDataSet = new BarDataSet(barEntries, "ZARADA");
        barData= new BarData(barDataSet);
        barChart2.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart2.animateY(5000);
        barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        System.out.println("ZARADA");

        barEntries =accountPresenter1.getEntriesMounth("ukupno");
        barDataSet = new BarDataSet(barEntries, "UKUPNO");
        barData= new BarData(barDataSet);
        barChart3.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart3.animateY(5000);
        barChart3.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    }

    void graphsWeek(){

        ArrayList barEntries;
        BarData barData;
        BarDataSet barDataSet;

        barEntries = accountPresenter1.getEntriesWeek("potrošnja");
        barDataSet = new BarDataSet(barEntries, "POTROŠNJA");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        barDataSet.setBarBorderWidth(0.9f);
        barData= new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));



        barEntries = accountPresenter1.getEntriesWeek("zarada");
        barDataSet = new BarDataSet(barEntries, "ZARADA");
        barDataSet.setBarBorderWidth(0.9f);
        barData= new BarData(barDataSet);
        barChart2.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart2.animateY(5000);
        barChart2.getXAxis().setGranularity(1f);

        barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));



        barEntries = accountPresenter1.getEntriesWeek("ukupno");
        barDataSet = new BarDataSet(barEntries, "UKUPNO");
        barDataSet.setBarBorderWidth(0.9f);
        barData= new BarData(barDataSet);
        barChart3.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart3.animateY(5000);
        barChart3.getXAxis().setGranularity(1f);
        barChart3.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

    }

    void graphsDay(){
        ArrayList barEntries = null;
        BarData barData;
        BarDataSet barDataSet;



        barEntries = accountPresenter1.getEntriesDay("potrošnja");
        barDataSet = new BarDataSet(barEntries, "POTROŠNJA");

        barData= new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
        barChart.getXAxis().setGranularity(1f);

        BarData barData2;
        BarDataSet barDataSet2;

        barEntries = accountPresenter1.getEntriesDay("zarada");
        barDataSet2 = new BarDataSet(barEntries, "ZARADA");
        barData2= new BarData(barDataSet2);
        barChart2.setData(barData2);
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart2.animateY(5000);
        barChart2.getXAxis().setGranularity(1f);

        BarData barData3;
        BarDataSet barDataSet3;

        barEntries = accountPresenter1.getEntriesDay("ukupno");
        barDataSet3 = new BarDataSet(barEntries, "UKUPNO");
        barDataSet3.setBarBorderWidth(0.9f);
        barData3= new BarData(barDataSet3);

        barChart3.setData(barData3);
        barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart3.animateY(5000);
        barChart3.getXAxis().setGranularity(1f);

    }

    @Override
    public void setTransaction(ArrayList<Transaction> transaction) {
            if(oznaka.equals("month")) graphsMounth();
            else if(oznaka.equals("week")) graphsWeek();
            else if(oznaka.equals("day")) graphsDay();
    }

    @Override
    public void notifyTransactionListDataSetChanged() {

    }

    @Override
    public void setAccount(Account account) {

    }
}
