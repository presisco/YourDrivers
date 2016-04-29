package product.presisco.yourdrivers.Network.Task;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import product.presisco.yourdrivers.DataModel.Category;
import product.presisco.yourdrivers.DataModel.Headline;
import product.presisco.yourdrivers.Network.Constants;

/**
 * Created by presisco on 2016/5/28.
 */
public class HeadlineRequest extends ExtendedRequest<List<Headline>> {
    public static final String TAG = HeadlineRequest.class.getSimpleName();

    private static final String CLASSNAME_NEWSLIST = "newslist";
    private static final String CLASSNAME_STICKY = "zhiding bor_sy_2";
    private static final String ID_WRAPPER = "mynewslist";
    private static final String TYPE_STICKY = "sticky";
    private static final String TYPE_TITLE1 = "title1";
    private static final String TYPE_TITLE2 = "title2";
    private static final String TYPE_SCRIPT = "script";
    private static final String TYPE_UNKNOWN = "unknown";
    private static final String TYPE_AD = "ad";
    private static final String ATTR_AD = "id";
    private static final String ATTR_NORMAL_TITLE = "data-id";

    public HeadlineRequest(int method, Category category, String max_id, OnLoadCompleteListener<List<Headline>> onLoadCompleteListener, Response.ErrorListener listener) {
        super(method, Constants.MOBILE_WEB_HOST + Constants.MOBILE_GET_MORE_LIST, onLoadCompleteListener, listener);
        Map<String, String> params = new HashMap<>();
        switch (category.getType()) {
            case Constants.NEWS_CATEGORY:
                params.put(category.getType(), category.getId());
                params.put("type", "1");
                break;
            case Constants.NEWS_SUBCATEGORY:
                params.put(Constants.NEWS_SUBCATEGORY, category.getId());
                params.put(Constants.NEWS_SUBCATEGORY_2, "");
                break;
            case Constants.NEWS_SUBCATEGORY_2:
                params.put(Constants.NEWS_SUBCATEGORY, "");
                params.put(Constants.NEWS_SUBCATEGORY_2, category.getId());
                break;
        }
        params.put("minid", max_id);
        setParams(params);
    }

    private static void setTitleAndLink(Headline headline, Element ele) {
        headline.title = ele.text();
        headline.link = ele.child(0).attr("href");
    }

    private static void setFooter(Headline headline, Element ele) {
        headline.date = null;
        headline.writer = ele.getElementsByClass("tname").text();
        headline.comment_link = ele.getElementsByClass("tpinglun").first().getElementsByTag("a").attr("href");
        headline.comments_count = ele.getElementsByClass("tpinglun").text();
    }

    private static String[] getIcons(Element ele) {
        Elements imgs = ele.getElementsByTag("img");
        String[] img_path = new String[imgs.size()];
        int i = 0;
        for (Element img : imgs) {
            img_path[i++] = img.attr("src");
        }
        return img_path;
    }

    private static String getType(Element ele) {
        if (ele.hasAttr(ATTR_NORMAL_TITLE)) {
            if (ele.children().hasClass("newstnopic"))
                return TYPE_TITLE1;
            else
                return TYPE_TITLE2;
        }
        if (ele.hasAttr(ATTR_AD)) {
            return TYPE_AD;
        }
        if (ele.hasClass(CLASSNAME_STICKY)) {
            return TYPE_STICKY;
        }
        if (ele.hasClass("script")) {
            return TYPE_SCRIPT;
        }
        return TYPE_UNKNOWN;
    }

    @Override
    protected Response<List<Headline>> parseNetworkResponse(NetworkResponse networkResponse) {
        String raw;
        try {
            raw = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            raw = new String(networkResponse.data);
        }

        return Response.success(getHeadlines(Jsoup.parse(raw).body()), HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private List<Headline> getHeadlines(Element parent) {
        Elements eles = parent.children();
        List<Headline> headlines = new ArrayList<>();
        for (Element ele : eles) {
            Headline headline = new Headline();
            switch (getType(ele)) {
                case TYPE_STICKY:
                    headline.id = "";
                    setTitleAndLink(headline, ele.getElementsByClass("newst").first());
                    headline.icon = getIcons(ele);
                    headline.date = null;
                    headline.sticky = true;
                    headline.writer = "";
                    break;
                case TYPE_TITLE1:
                    headline.id = ele.attr(ATTR_NORMAL_TITLE);
                    setTitleAndLink(headline, ele.getElementsByClass("newstnopic").first());
                    headline.icon = getIcons(ele);
                    headline.date = null;
                    headline.sticky = false;
                    setFooter(headline, ele.getElementsByTag("ul").first());
                    break;
                case TYPE_TITLE2:
                    headline.id = ele.attr(ATTR_NORMAL_TITLE);
                    setTitleAndLink(headline, ele.getElementsByClass("newst").first());
                    headline.icon = getIcons(ele);
                    headline.date = null;
                    headline.sticky = false;
                    setFooter(headline, ele.getElementsByTag("ul").first());
                    break;
                case TYPE_AD:
                    Log.d("Parse", "Dump ad");
                    continue;
                case TYPE_SCRIPT:
                    Log.d("Parse", "Dump script");
                    continue;
                case TYPE_UNKNOWN:
                    Log.d("Parse", "unknown content");
                    continue;
            }
            headlines.add(headline);
        }
        return headlines;
    }

}
