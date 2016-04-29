package product.presisco.yourdrivers.DataModel;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by presisco on 2016/4/25.
 */
public class Category implements Serializable {
    public static final int NAV_TITLE = 0;
    public static final int NAV_TYPE = 1;
    public static final int NAV_ID = 2;
    private String[] properties;

    public Category(String title, String type, String id) {
        properties = new String[]{title, type, id};
    }

    public String getTitle() {
        return properties[NAV_TITLE];
    }

    public String getType() {
        return properties[NAV_TYPE];
    }

    public String getId() {
        return properties[NAV_ID];
    }
}
