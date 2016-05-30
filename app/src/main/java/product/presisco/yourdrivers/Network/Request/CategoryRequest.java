package product.presisco.yourdrivers.Network.Request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Category;

/**
 * Created by presisco on 2016/5/28.
 */
public class CategoryRequest extends ExtendedRequest<List<Category>> {
    public static final String TAG = CategoryRequest.class.getSimpleName();

    private static final String ID_CATEGORY = "nav";
    private static final String ID_SUBCATEGORY = "subnav";

    public CategoryRequest(OnLoadCompleteListener<List<Category>> loadCompleteListener, Response.ErrorListener listener) {
        super(Method.GET, loadCompleteListener, listener);
    }

    private static String getId(String link) {
        return link.substring(link.indexOf("=") + 1);
    }

    private static String getType(String link) {
        return link.substring(link.indexOf("?") + 1, link.indexOf("="));
    }

    @Override
    protected Response<List<Category>> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(getNavTypesFromEle(Jsoup.parse(getStringFromResponse(networkResponse)))
                , HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private List<Category> getNavTypesFromEle(Document doc) {
        List<Category> categories = new ArrayList<>();

        for (Element ele : doc.getElementById(ID_CATEGORY)
                .getElementsByTag("ul").first().children()) {
            String link = ele.attr("href");
            categories.add(new Category(ele.text(), getType(link), getId(link)));
        }
        return categories;
    }
}
