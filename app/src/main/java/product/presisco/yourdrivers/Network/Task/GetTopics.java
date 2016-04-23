package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Utils;

/**
 * Created by presisco on 2016/4/20.
 */
public class GetTopics extends AsyncTask<String, Void, List<Topic>> {
    public static final String MODE_GET_MORE = "get_more";
    private static final String CLASSNAME_TOP = "zhiding bor_sy_2";
    private static final String CLASSNAME_TITLE = "newst";
    private static final String CLASSNAME_INTRO = "newsin";
    private static final String CLASSNAME_ICON = "newsimg";
    private static final String CLASSNAME_ICONS = "newsimg1";
    private static final String CLASSNAME_TOPIC = "newstnopic";
    private static final String CLASSNAME_TNAME = "tname";
    private static final String CLASSNAME_TTIME = "ttime";
    private static final String CLASSNAME_COMMENT = "tpinglun";
    private static final String CLASSNAME_NEWSLIST = "mynewslist";
    private static final String ATTR_DATA_ID = "data-id";

    private static final String VALUE_AD = "推广";
    private static final String VALUE_TOP = "置顶";

    private OnLoadCompleteListener mOnLoadCompleteListener;

    private static String getRefLinkFromST(Element ele) {
        return ele.child(0).attr(Constants.ATTR_LINK);
    }

    private static String getTitleFromST(Element ele) {
        return ele.child(0).text();
    }

    private static List<String> getIconsFromIMG(Element ele) {
        List<String> icons = new ArrayList<>();
        for (Element img : ele.child(0).children()) {
            icons.add(img.attr("src"));
        }
        return icons;
    }

    public GetTopics setOnLoadCompleteListener(OnLoadCompleteListener l) {
        mOnLoadCompleteListener = l;
        return this;
    }

    /**
     * Load contents from host
     *
     * @param params params[0] for Tid and params[1] for minid
     * @return
     */

    @Override
    protected List<Topic> doInBackground(String... params) {
        List<Topic> topics = new ArrayList<>();
        try {
            Document doc = null;
            Elements eles = null;
            if (params.length == 2) {
                List<Pair> url_params = new ArrayList<>();
                url_params.add(new Pair("Tid", params[0]));
                url_params.add(new Pair("type", "1"));
                url_params.add(new Pair("minid", params[1]));
                doc = Jsoup.parse(Utils.getURLWithParams(Constants.MOBILE_WEB_HOST + Constants.MOBILE_GET_MORE_LIST, url_params), 5000);
                eles = doc.getElementsByTag("body").first().children();
            } else {
                doc = Jsoup.parse(new URL(Constants.MOBILE_WEB_HOST), 5000);
                eles = doc.getElementById(CLASSNAME_NEWSLIST).children();
            }
            for (Element ele : eles) {
                String display = "";
                Topic topic = new Topic();
                Elements childs = ele.children();
                if (childs.size() > 3) {
                    if (childs.get(3).text().equals(VALUE_AD)) {
                        Log.d("Parse", "Dump ad");
                        continue;
                    }
                    topic.id = "0";
                    Element tmp = ele.getElementsByClass(CLASSNAME_TITLE).first();
                    topic.link = getRefLinkFromST(tmp);
                    topic.title = getTitleFromST(tmp);
                    topic.icon = getIconsFromIMG(ele.getElementsByClass(CLASSNAME_ICON).first());
                    topic.date = null;
                    topic.isTop = true;
                    topic.writer = "";
                    topics.add(topic);
                } else if (ele.hasAttr(ATTR_DATA_ID)) {
                    Element tmp;
                    topic.isTop = false;
                    topic.id = ele.attr(ATTR_DATA_ID);
                    if (childs.hasClass(CLASSNAME_TOPIC)) {
                        tmp = ele.getElementsByClass(CLASSNAME_TOPIC).first();
                    } else {
                        tmp = ele.getElementsByClass(CLASSNAME_TITLE).first();
                    }
                    topic.link = getRefLinkFromST(tmp);
                    topic.title = getTitleFromST(tmp);
                    if (childs.hasClass(CLASSNAME_ICON)) {
                        topic.icon = getIconsFromIMG(ele.getElementsByClass(CLASSNAME_ICON).first());
                    }
                    if (childs.hasClass(CLASSNAME_ICONS)) {
                        topic.icon = getIconsFromIMG(ele.getElementsByClass(CLASSNAME_ICONS).first());
                    }
                    topic.writer = ele.getElementsByClass(CLASSNAME_TNAME).text();
                    topic.date = Topic.getDateFromRelative(ele.getElementsByClass(CLASSNAME_TTIME).text());
                    tmp = ele.getElementsByClass(CLASSNAME_COMMENT).first().child(0);
                    topic.comment_link = tmp.attr(Constants.ATTR_LINK);
                    topic.comments_count = tmp.text();
                    topics.add(topic);
                } else {
                    Log.d("Parse", "unrecognized content");
                    continue;
                }

                if (topic.isTop) {
                    display = topic.title;
                } else {
                    display = topic.id + "/" + topic.title;
                }
                Log.d("Parse", display);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }

    @Override
    protected void onPostExecute(List<Topic> s) {
        super.onPostExecute(s);
        if (mOnLoadCompleteListener != null) {
            mOnLoadCompleteListener.onLoadComplete(s);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public interface OnLoadCompleteListener {
        void onLoadComplete(List<Topic> src);
    }
}
