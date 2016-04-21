package product.presisco.yourdrivers.DataModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by presisco on 2016/4/20.
 */
public class Topic implements Serializable {
    public String id;
    public String title;
    public String icon;
    public long date_millis;

    public Topic(String _id, String _title, String _icon, long _date_millis) {
        id = _id;
        title = _title;
        icon = _icon;
        date_millis = _date_millis;
    }
}
