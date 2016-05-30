package product.presisco.yourdrivers.Cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by presisco on 2016/5/30.
 */
public abstract class BaseCache<T> {
    protected static final String SQL_SEPARATOR = ",";
    protected static final String LINK_SEPERATOR = ";";
    static BaseCache instance = null;
    AppDBHelper dbHelper = null;
    private String[] keys = null;
    private String[] workspace = null;
    private String table_name = "";

    public BaseCache(Context context, String _tablename, String[] _keys) {
        dbHelper = AppDBHelper.getInstance(context);
        keys = _keys;
        workspace = new String[keys.length];
        table_name = _tablename;
        String sql_create = "create table # if not exists ( $ );";
        sql_create = sql_create.replace("#", table_name);
        sql_create = sql_create.replace("$", genKeySet());
        dbHelper.getWritableDatabase().execSQL(sql_create);
    }

    protected static String[] string2Array(String src) {
        List<String> array = new LinkedList<>();
        int base = 0;
        int pos = src.indexOf(LINK_SEPERATOR, base);
        while (pos > -1) {
            array.add(src.substring(base, pos));
            base = pos + 1;
            pos = src.indexOf(LINK_SEPERATOR, base);
        }
        return array.toArray(new String[0]);
    }

    protected static String array2String(String[] src) {
        return TextUtils.join(LINK_SEPERATOR, src);
    }

    private String genKeySet() {
        for (int i = 0; i < keys.length; ++i) {
            keys[i].concat(" text");
        }
        keys[0].concat(" primary key");
        return TextUtils.join(SQL_SEPARATOR, keys);
    }

    public boolean have(String filter) {
        Cursor cursor = dbHelper.getReadableDatabase().query(table_name, new String[]{keys[0]}, filter, null, null, null, null);
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    protected abstract T createItem(String[] data);

    protected abstract String[] fromItem(T item);

    public List<T> getAll() {
        List<T> data = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(table_name, keys, null, null, null, null, null);
        while (cursor.moveToNext()) {
            data.add(createItem(cursor2array(cursor)));
        }
        return data;
    }

    private String[] cursor2array(Cursor cursor) {
        for (int i = 0; i < keys.length; ++i) {
            workspace[i] = cursor.getString(cursor.getColumnIndex(keys[i]));
        }
        return workspace;
    }

    public void putAll(List<T> data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 0; i < data.size(); ++i) {
            db.insert(table_name, null, item2values(data.get(i)));
        }
    }

    public void put(T item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(table_name, null, item2values(item));
    }

    private ContentValues item2values(T item) {
        ContentValues values = new ContentValues();
        String[] data = fromItem(item);
        for (int i = 0; i < keys.length; ++i) {
            values.put(keys[i], data[i]);
        }
        return values;
    }

    public void del() {
    }

}
