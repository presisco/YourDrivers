package product.presisco.yourdrivers.DataModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by presisco on 2016/4/20.
 */
public class Headline implements Serializable {
    public String id;
    public String link;
    public String title;
    public String[] icon;
    public String comments_count;
    public String comment_link;
    public Date date;
    public boolean sticky;
    public String writer;

    public Headline() {

    }

    public Headline(String _id, String _link, String _title, String[] _icon, String fromNow, String _comments_count, boolean _isTop, String _writer) {
        id = _id;
        link = _link;
        title = _title;
        icon = _icon;
        date = getDateFromRelative(fromNow);
        comments_count = _comments_count;
        sticky = _isTop;
        writer = _writer;
    }

    public static Date getDateFromRelative(String fromNow) {
        return null;
    }
}
