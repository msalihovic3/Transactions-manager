package ba.unsa.etf.rma.rma20salihovicmirnesa15;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RMADataBase.db";
    public static final int DATABASE_VERSION = 2;


    public TransactionDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TransactionDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TRANSACTION_TABLE = "transactions";

    public static final String ACCOUNT_INTERNAL_ID = "id";
    public static final String ACCOUNT_TABLE = "account";
    public static final String BUDGET = "budget";
    public static final String TOTAL = "total";
    public static final String MOUNTH = "mounth";
    public static final String TRANSACTION_TABLE_DELETE = "deleteTransaction";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_INTERNAL_ID = "_id";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_ENDDATE = "endDate";
    public static final String TRANSACTION_INTERVAL = "interval";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TEXT = "text";
    public static final String TRANSACTION_DESCRIPTION = "description";
    public static final String TRANSACTION_TYPE = "type";

    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ("  + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER , "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_TYPE + " TEXT, "
                    + TRANSACTION_DATE + " TEXT, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_AMOUNT + " REAL, "
                    + TRANSACTION_DESCRIPTION + " TEXT, "
                    + TRANSACTION_ENDDATE+ " TEXT, "
                    + TRANSACTION_TEXT + " TEXT); ";


    private static final String TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    private static final String TRANSACTION_TABLE_CREATE_DELETE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE_DELETE + " ("  + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER , "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_TYPE + " TEXT, "
                    + TRANSACTION_DATE + " TEXT, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_AMOUNT + " REAL, "
                    + TRANSACTION_DESCRIPTION + " TEXT, "
                    + TRANSACTION_ENDDATE + " TEXT); ";


    private static final String TRANSACTION_DROP_DELETE = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE_DELETE;

    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ("  + ACCOUNT_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BUDGET + " REAL NOT NULL, "
                    + TOTAL + " REAL, "
                    + MOUNTH + " REAL); ";


    private static final String ACCOUNT_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION_TABLE_CREATE);
        db.execSQL(TRANSACTION_TABLE_CREATE_DELETE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TRANSACTION_DROP);
        db.execSQL(TRANSACTION_DROP_DELETE);
        db.execSQL(ACCOUNT_DROP);
        onCreate(db);
    }
}
