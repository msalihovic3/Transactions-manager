package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeleteContetntProvider extends ContentProvider {

    private static final int ALLROWS =1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.transactionDelete","elements",ALLROWS);
        uM.addURI("rma.provider.transactionDelete","elements/#",ONEROW);
    }

    TransactionDBOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new TransactionDBOpenHelper(getContext(),
                TransactionDBOpenHelper.DATABASE_NAME,null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArg, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }
        String groupby=null;
        String having=null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)){
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID+"="+idRow);
            default:break;
        }
        squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE_DELETE);
        Cursor cursor = squery.query(database,projection,selection,selectionArg,groupby,having,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)){
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            case ONEROW:
                return "vnd.android.cursor.item/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }
        System.out.println("deleteeeeeeinserttt");
        long id = database.insert(TransactionDBOpenHelper.TRANSACTION_TABLE_DELETE, null, contentValues);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        SQLiteDatabase db;
        try{
            db=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            db=mHelper.getReadableDatabase();
        }
        switch (uM.match(uri)){
            case ALLROWS:
                System.out.println("OnOOOO");
                count = db.delete(TransactionDBOpenHelper.TRANSACTION_TABLE_DELETE, s, strings);

                break;

            case ONEROW:
                System.out.println("OVOOOO");
               /* String id = uri.getPathSegments().get(1);
                count = db.delete( STUDENTS_TABLE_NAME, _ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? "
                        AND (" + selection + ')' : ""), selectionArgs);
                break;*/
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
