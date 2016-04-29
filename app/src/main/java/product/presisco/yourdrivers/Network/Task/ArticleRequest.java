package product.presisco.yourdrivers.Network.Task;

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

import product.presisco.yourdrivers.DataModel.Article;

/**
 * Created by presisco on 2016/4/29.
 */
public class ArticleRequest extends Request<Article> {
    private OnLoadCompleteListener mListener;

    public ArticleRequest(int method, String url, OnLoadCompleteListener loadCompleteListener, Response.ErrorListener listener) {
        super(method, url, listener);
        mListener = loadCompleteListener;
    }

    @Override
    protected Response<Article> parseNetworkResponse(NetworkResponse networkResponse) {
        Article parsed = new Article();
        String raw;
        try {
            raw = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            raw = new String(networkResponse.data);
        }
        Document doc = Jsoup.parse(raw);
        Element bodyEle = doc.body();
        addArticleHead(parsed, bodyEle.getElementsByClass("news_content").first());
        addArticleContent(parsed, bodyEle.getElementById("content"));
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    private void addArticleHead(Article art, Element headEle) {
        art.title = headEle.getElementsByClass("news_t").first().text();
        Elements writerndate = headEle.getElementsByClass("news_t1").first().child(0).children();
        art.writer = writerndate.first().text();
        art.date = writerndate.get(2).text();
    }

    private void addArticleContent(Article art, Element src) {
        Elements contentEles = src.getElementsByTag("p");
        List<String> images = new ArrayList<>();
        boolean isMergingImgs = false;
        String textBuff = "";
        for (Element ele : contentEles) {
            if (ele.hasText()) {
                if (isMergingImgs) {
                    art.contents.add(new Article.Images(images.toArray(new String[0])));
                    images.clear();
                    isMergingImgs = false;
                } else {
                    textBuff = ele.text();
                    if (ele.getElementsByTag("strong") != null) {
                        textBuff += ele.getElementsByTag("strong").text();
                    }
                }
            } else {
                if (!isMergingImgs) {
                    art.contents.add(new Article.Text(textBuff));
                    isMergingImgs = true;
                }
                images.add(ele.getElementsByTag("img").first().attr("src"));
            }
        }
        if (isMergingImgs) {
            art.contents.add(new Article.Images(images.toArray(new String[0])));
            images.clear();
        } else {
            art.contents.add(new Article.Text(textBuff));
        }
        images = null;
        textBuff = null;
        Element footerEle = src.getElementsByTag("div").get(1);
        if (footerEle != null) {
            art.isMultPage = true;
            art.all_link = footerEle.child(0).attr("href");
            art.prev_link = footerEle.child(1).child(0).attr("href");
            art.next_link = footerEle.child(1).child(1).attr("href");
        }
    }

    @Override
    protected void deliverResponse(Article article) {
        if (mListener != null) {
            mListener.onLoadComplete(article);
        }
    }

    public interface OnLoadCompleteListener {
        void onLoadComplete(Article art);
    }
}
