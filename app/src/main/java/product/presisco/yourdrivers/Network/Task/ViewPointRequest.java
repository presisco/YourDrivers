package product.presisco.yourdrivers.Network.Task;

import android.util.Log;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Viewpoint;
import product.presisco.yourdrivers.Network.Constants;

/**
 * Created by presisco on 2016/5/28.
 */
public class ViewPointRequest extends ExtendedRequest<List<Viewpoint>> {
    public static final String TAG = ViewPointRequest.class.getSimpleName();
    private static final String CLASSNAME_VIEWPOINT = "shidian";
    private static final String CLASSNAME_WRAPPER = "swipe-wrap";

    public ViewPointRequest(OnLoadCompleteListener<List<Viewpoint>> onLoadCompleteListener, Response.ErrorListener listener) {
        super(Method.GET, onLoadCompleteListener, listener);
    }

    @Override
    protected Response<List<Viewpoint>> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(getViewpointsFromEle(
                Jsoup.parse(getStringFromResponse(networkResponse))
                        .getElementsByClass(CLASSNAME_VIEWPOINT).first())
                , HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private List<Viewpoint> getViewpointsFromEle(Element parent) {
        //Elements eles = parent.child(0).child(0).children();
        Elements eles = parent.getElementsByClass(CLASSNAME_WRAPPER).first().children();
        List<Viewpoint> vps = new ArrayList<>();
        for (Element ele : eles) {
            Viewpoint vp = new Viewpoint();
            vp.title = ele.getElementsByClass("left").text();
            String link = ele.getElementsByTag("a").attr(Constants.ATTR_LINK);
            vp.link = link;
            vp.id = link.substring(link.lastIndexOf("/") + 1, link.indexOf(".html"));
            vp.icon = ele.getElementsByTag("img").attr("src");
            vps.add(vp);
            Log.d("parse", "title:" + vp.title + "/link:" + vp.link);
        }
        return vps;
    }
}
