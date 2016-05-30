package product.presisco.yourdrivers.Network.Request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import product.presisco.yourdrivers.DataModel.Article;
import product.presisco.yourdrivers.Network.Constants;

/**
 * Created by presisco on 2016/5/29.
 */
public class CommentHeaderRequest extends ExtendedRequest<Article.Header> {
    String tid;

    public CommentHeaderRequest(String _tid, OnLoadCompleteListener<Article.Header> loadCompleteListener, Response.ErrorListener listener) {
        super(Method.GET, Constants.REQUEST_COMMENT_HEADER, loadCompleteListener, listener);
        tid = _tid;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.REQUEST_COMMENT_TID, tid);
        return params;
    }

    @Override
    protected Response<Article.Header> parseNetworkResponse(NetworkResponse networkResponse) {
        Article.Header header = new Article.Header();
        header.tid = tid;
        Document doc = Jsoup.parse(getStringFromResponse(networkResponse));
        Element bodyEle = doc.body();
        addArticleHead(header, bodyEle.getElementsByClass("news_content").first());
        return Response.success(header, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private void addArticleHead(Article.Header head, Element headEle) {
        head.title = headEle.getElementsByClass("news_t").first().text();
        Elements writerndate = headEle.getElementsByClass("news_t1").first().child(0).children();
        head.writer = writerndate.first().text();
        head.date = writerndate.get(2).text();
    }
}
