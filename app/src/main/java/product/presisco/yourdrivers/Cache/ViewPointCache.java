package product.presisco.yourdrivers.Cache;

import android.content.Context;
import android.view.View;

import product.presisco.yourdrivers.DataModel.Viewpoint;

/**
 * Created by presisco on 2016/5/30.
 */
public class ViewPointCache extends BaseCache<Viewpoint> {
    protected static final String TABLE_NAME = "viewpoint";
    protected static final String[] KEYS = {"id", "link", "icon", "title"};

    public ViewPointCache(Context context) {
        super(context, TABLE_NAME, KEYS);
    }

    public static ViewPointCache getInstance(Context context) {
        if (instance == null)
            instance = new ViewPointCache(context);
        return (ViewPointCache) instance;
    }

    public boolean have(Viewpoint data) {
        String filter = "id = '#'";
        filter.replace("#", data.id);
        return have(filter);
    }

    @Override
    protected Viewpoint createItem(String[] data) {
        Viewpoint viewpoint = new Viewpoint();
        viewpoint.id = data[0];
        viewpoint.link = data[1];
        viewpoint.icon = data[2];
        viewpoint.title = data[3];
        return null;
    }

    @Override
    protected String[] fromItem(Viewpoint item) {
        String[] data = new String[KEYS.length];
        data[0] = item.id;
        data[1] = item.link;
        data[2] = item.icon;
        data[3] = item.title;
        return data;
    }
}
