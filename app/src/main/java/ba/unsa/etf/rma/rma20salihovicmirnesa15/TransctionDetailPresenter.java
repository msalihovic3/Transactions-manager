package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARINCOME;
import static ba.unsa.etf.rma.rma20salihovicmirnesa15.Type.REGULARPAYMENT;

class TransctionDetailPresenter implements ITransactionDetailPresenter ,TransactionDetailInteractor.OnMovieSearchDone {
    private Context context;
    private ModelTransactionInteractor interactor;
    private ModelAccountInteractor interactorAccount;
    private TransactionDetailInteractor interactorTransaction;
    private Activity view;
    private Transaction transaction;
    private ArrayList<Transaction> transactions;
    private Account account;

    public TransctionDetailPresenter(Context context, Activity activity) {
        this.context = context;
        this.view = activity;
        this.interactor = new ModelTransactionInteractor();
        this.interactorTransaction=new TransactionDetailInteractor();
        this.interactorAccount=new ModelAccountInteractor();
    }

    public TransctionDetailPresenter(Context context) {
        this.context = context;

        this.interactor = new ModelTransactionInteractor();
        this.interactorTransaction=new TransactionDetailInteractor();
        this.interactorAccount=new ModelAccountInteractor();
    }

    public Transaction getTransaction(){

        return transaction;
    }
    @Override
    public void searchDetail(String query){
        new TransactionDetailInteractor((TransactionDetailInteractor.OnMovieSearchDone)
                this).execute(query);
    }

    @Override
    public void onDone(Transaction result, ArrayList<Transaction> trans) {
        if(result!=null){
        transaction = result;}
        else{
            transactions=TransactionsModel.transactions;
        }
        transactions=trans;
    }
    @Override
    public  double obracunTotalLimita() {
       double newTotalLimit=0;
       if(ConnectivityBroadcastReceiver.provjera==false) transactions=TransactionsModel.transactions;
        for(Transaction trans: transactions){
            if(trans.getType().name().equals("REGULARPAYMENT") || trans.getType().name().equals("INDIVIDUALPAYMENT")){
                newTotalLimit-=trans.getAmount();
            }else{
            newTotalLimit+=trans.getAmount();}
        }
        return newTotalLimit;
    }

    public Account getAccount(){return interactorAccount.get();}
    @Override
    public  double obracunMounthLimita(String toString) throws ParseException {
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(toString));
        int month=cal.get(Calendar.MONTH);
        month=month+1;

        double newMounthLimit=0;
        for(Transaction trans: transactions){
            cal.setTime(trans.getDate());
            int transMont=cal.get(Calendar.MONTH);
            transMont+=1;
            if(transMont==month){
                newMounthLimit+=trans.getAmount();}
        }
        return newMounthLimit;
    }

    @Override
    public boolean provjeraBudzeta(String date) throws ParseException {
       account=interactorAccount.get();
        double newTotalLimit;
        newTotalLimit=obracunTotalLimita();
        double newMounthLimit;
        newMounthLimit=obracunMounthLimita(date);

        if (newMounthLimit > account.getMounthLimit() || newTotalLimit > account.getTotalLimit()) {

            return true;
        }
        else return false;

    }

    @Override
    public  void addTransaction(int id,String date, String amount, String title, String type, String itemDescription, String intrval, String endDate,String text) throws ParseException {

        Date endDatenew=null;
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(date));
        Date newdate = cal.getTime();
        if(type.equals(REGULARINCOME.name()) || type.equals(REGULARPAYMENT.name()) ){
            cal.setTime(sdf.parse(endDate));
            endDatenew=cal.getTime();}else{
            intrval="0";
        }
            Transaction newTransaction=new Transaction(id,newdate,Double.parseDouble(amount),title,Type.valueOf(type),itemDescription,Integer.parseInt(intrval),endDatenew,text);
            TransactionsModel.transactions.add(newTransaction);//mijenja u presenteru samo
        System.out.println(TransactionsModel.transactions+"MINA"+newTransaction.getId()+newTransaction.getText());
            interactorTransaction.save(newTransaction,context.getApplicationContext());



    }


    public  void addTransactionUpdate(int id,String date, String amount, String title, String type, String itemDescription, String intrval, String endDate,String text) throws ParseException {

        Date endDatenew=null;
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(date));
        Date newdate = cal.getTime();
        if(type.equals(REGULARINCOME.name()) || type.equals(REGULARPAYMENT.name()) ){
            cal.setTime(sdf.parse(endDate));
            endDatenew=cal.getTime();}else{
            intrval="0";
        }

        Transaction newTransaction=new Transaction(id,newdate,Double.parseDouble(amount),title,Type.valueOf(type),itemDescription,Integer.parseInt(intrval),endDatenew,text);

        System.out.println(TransactionsModel.transactions+"MINA"+newTransaction.getId()+newTransaction.getText());
        interactorTransaction.save(newTransaction,context.getApplicationContext());



    }
    @Override
    public  ArrayList<Transaction> deleteTransaction(int id,String s){

        ArrayList<Transaction> novo=TransactionsModel.transactions;
        for(Transaction trans: novo){

            if(trans.getId()==id){
                if(trans.getText()=="Offline dodavanje"){
                    trans.setText("Offline brisanje");
                    interactorTransaction.updateBaza(trans,context.getApplicationContext());
                }
              trans.setText("Offline brisanje");
               System.out.println(trans.getText()+trans.getId()+"prvooo");
               interactorTransaction.deleteTable(trans,context.getApplicationContext());
                break;

            }

        }
        TransactionsModel.transactions=novo;
        return null;
    }

    public void promjenaBudzeta(double a){
        interactorAccount.get().setBudget(a);
    }

    @Override
    public  void updateTransaction(int id, String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate,String text) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        Date newdate = cal.getTime();
        ArrayList<Transaction> novo=TransactionsModel.transactions;
        for(Transaction trans: novo){
            if(trans.getId()==id){
                trans.setDate(newdate);
                trans.setAmount(Double.parseDouble(amount));

                trans.setTitle(title);
                trans.setType(Type.valueOf(type));
                trans.setItemDescription(itemDescription);
                //  trans.setEndDate(newEndaDate);
                trans.setTransactionInterval(Integer.parseInt(transactionInterval));

                break;

            }
            TransactionsModel.transactions=novo;
        }
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction)transaction;
    }

    public  void updateTransactionBaza(int id,String date, String amount, String title, String type, String itemDescription, String intrval, String endDate,String text) throws ParseException {
        Date endDatenew=null;
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(date));
        Date newdate = cal.getTime();
        if(type.equals(REGULARINCOME.name()) || type.equals(REGULARPAYMENT.name()) ){
            cal.setTime(sdf.parse(endDate));
            endDatenew=cal.getTime();}else{
            intrval="0";
        }

      /*  if( text.equals("") || text.equals("offline dodavanje")){
            Transaction newTransaction=new Transaction(id,newdate,Double.parseDouble(amount),title,Type.valueOf(type),itemDescription,Integer.parseInt(intrval),endDatenew,"Offline Izmjena");
         //mijenja u presenteru samo
            //interactor.set(transactions);
            System.out.println(TransactionsModel.transactions+"MINA"+newTransaction.getText());
           interactorTransaction.save(newTransaction,context.getApplicationContext());
        }else{*/

            Transaction newTransaction=new Transaction(id,newdate,Double.parseDouble(amount),title,Type.valueOf(type),itemDescription,Integer.parseInt(intrval),endDatenew,text);
           //mijenja u presenteru samo
            System.out.println("mmmmmmmmmmmmmmmmmmm"+newTransaction.getText());
            interactorTransaction.updateBaza(newTransaction,context.getApplicationContext());


    }

    public void undoDelete(int id) {
        ArrayList<Transaction> novo=TransactionsModel.transactions;

        for(Transaction trans: novo){

            if(trans.getId()==id){
                if(trans.getText()=="Offline brisanje"){
                    trans.setText("Offline izmjena");
                    interactorTransaction.updateBaza(trans,context.getApplicationContext());
                }
                break;

            }

        }
        interactorTransaction.undoDelete(id,context.getApplicationContext());
    }
}
