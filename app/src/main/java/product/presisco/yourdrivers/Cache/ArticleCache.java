package product.presisco.yourdrivers.Cache;

import android.content.Context;

import product.presisco.yourdrivers.DataModel.Article;

/**
 * Created by presisco on 2016/5/30.
 */
public class ArticleCache extends BaseCache<Article> {
    protected static final String TABLE_NAME = "article";
    protected static final String[] KEYS = {"tid", "title", "writer", "date", "comm_link", "ref_name", "ref_link"};

    public ArticleCache(Context context) {
        super(context, TABLE_NAME, KEYS);
    }

    public static ArticleCache getInstance(Context context) {
        if (instance == null)
            instance = new ArticleCache(context);
        return (ArticleCache) instance;
    }

    public boolean have(Article data) {
        String filter = KEYS[0] + " = '#'";
        filter.replace("#", data.header.tid);
        return have(filter);
    }

    @Override
    protected Article createItem(String[] data) {
        Article item = new Article();
        item.header.tid = data[0];
        item.header.title = data[1];
        item.header.writer = data[2];
        item.header.date = data[3];
        item.comm_link = data[4];
        item.ref_name = data[5];
        item.ref_link = data[6];
        return null;
    }

    @Override
    protected String[] fromItem(Article item) {
        String[] data = new String[KEYS.length];
        data[0] = item.header.tid;
        data[1] = item.header.title;
        data[2] = item.header.writer;
        data[3] = item.header.date;
        data[4] = item.comm_link;
        data[5] = item.ref_name;
        data[6] = item.ref_link;
        return data;
    }
}
