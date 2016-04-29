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

    public ViewPointRequest(int method, OnLoadCompleteListener<List<Viewpoint>> onLoadCompleteListener, Response.ErrorListener listener) {
        super(method, Constants.MOBILE_WEB_HOST, onLoadCompleteListener, listener);
    }

    @Override
    protected Response<List<Viewpoint>> parseNetworkResponse(NetworkResponse networkResponse) {
        String raw = "";
        try {
            raw = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            raw = new String(networkResponse.data);
        }
        return Response.success(getViewpointsFromEle(Jsoup.parse(raw).getElementsByClass(CLASSNAME_VIEWPOINT).first())
                , HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private List<Viewpoint> getViewpointsFromEle(Element parent) {
        //Elements eles = parent.child(0).child(0).children();
        Elements eles = parent.getElementsByClass(CLASSNAME_WRAPPER).first().children();
        List<Viewpoint> vps = new ArrayList<>();
        for (Element ele : eles) {
            Viewpoint vp = new Viewpoint();
            vp.title = ele.getElementsByClass("left").text();
            vp.link = ele.getElementsByTag("a").attr(Constants.ATTR_LINK);
            vp.icon = ele.getElementsByTag("img").attr("src");
            vps.add(vp);
            Log.d("parse", "title:" + vp.title + "/link:" + vp.link);
        }
        return vps;
    }
}
