package product.presisco.yourdrivers.DataModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by presisco on 2016/4/20.
 */
public class Topic implements Serializable {
    public String id;
    public String title;
    public String[] icon;
    public int comments_count;
    public Date date;
    public boolean isTop;
    public String writer;

    public Topic(String _id, String _title, String[] _icon, String fromNow, int _comments_count, boolean _isTop, String _writer) {
        id = _id;
        title = _title;
        icon = _icon;
        date = getDateFromRelative(fromNow);
        comments_count = _comments_count;
        isTop = _isTop;
        writer = _writer;
    }

    public static Date getDateFromRelative(String fromNow) {
        return null;
    }
}
