package product.presisco.yourdrivers.Cache;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import product.presisco.yourdrivers.DataModel.Headline;

/**
 * Created by presisco on 2016/5/30.
 */
public class HeadlineCache extends BaseCache<Headline> {
    protected static final String TABLE_NAME = "headline";
    protected static final String[] KEYS = {"id", "title", "link", "icons", "writer", "comm_link"};
    protected Set<Headline> headlines;

    public HeadlineCache(Context context) {
        super(context, TABLE_NAME, KEYS);
        headlines = new HashSet<>();
    }

    public static HeadlineCache getInstance(Context context) {
        if (instance == null)
            instance = new HeadlineCache(context);
        return (HeadlineCache) instance;
    }

    public void fillCacheFromDisk() {
        headlines.addAll(super.getAll());
    }

    public List<Headline> getAll() {
        if (headlines == null)
            fillCacheFromDisk();
        return new ArrayList<>(headlines);
    }

    public void putAll(List<Headline> data) {
        headlines.addAll(data);
        super.putAll(data);
    }

    @Override
    protected Headline createItem(String[] data) {
        Headline item = new Headline();
        item.id = data[0];
        item.title = data[1];
        item.link = data[2];
        item.icon = string2Array(data[3]);
        item.writer = data[4];
        item.comment_link = data[5];
        return item;
    }

    @Override
    protected String[] fromItem(Headline item) {
        String[] result = new String[KEYS.length];
        result[0] = item.id;
        result[1] = item.title;
        result[2] = item.link;
        result[3] = array2String(item.icon);
        result[4] = item.writer;
        result[5] = item.comment_link;
        return result;
    }

    public boolean have(Headline data) {
        String filter = KEYS[0] + " = '#'";
        filter.replace("#", data.id);
        return have(filter);
    }
}
