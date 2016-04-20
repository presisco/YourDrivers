package product.presisco.yourdrivers.DataModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by presisco on 2016/4/20.
 */
public class Topic implements Serializable {
    public String detail_link;
    public String title;
    public String author;
    public String[] images;
    public int comments_count;
    public String comments_link;
    public Date date;

}
