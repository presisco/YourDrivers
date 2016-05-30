package product.presisco.yourdrivers.Cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by presisco on 2016/5/30.
 */
public class AppDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cache.db";
    private static AppDBHelper instance = null;

    public AppDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static AppDBHelper getInstance(Context context) {
        if (instance == null)
            instance = new AppDBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop *");
        onCreate(db);
    }

    public interface CreateTableInterface {
        String getSQL();
    }
}
