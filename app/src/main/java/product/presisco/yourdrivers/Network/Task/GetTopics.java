package product.presisco.yourdrivers.Network.Task;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.NavType;
import product.presisco.yourdrivers.DataModel.SubNavType;
import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.DataModel.Viewpoint;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Utils;

/**
 * Created by presisco on 2016/4/20.
 */
public class GetTopics extends AsyncTask<String, Void, Integer> {
    public static final String MODE_GET_MORE = "get_more";
    public static final String MODE_REFRESH = "refresh";
    private static final String CLASSNAME_TOP = "zhiding bor_sy_2";
    private static final String CLASSNAME_TITLE = "newst";
    private static final String CLASSNAME_INTRO = "newsin";
    private static final String CLASSNAME_ICON = "newsimg";
    private static final String CLASSNAME_ICONS = "newsimg1";
    private static final String CLASSNAME_TOPIC = "newstnopic";
    private static final String CLASSNAME_TNAME = "tname";
    private static final String CLASSNAME_TTIME = "ttime";
    private static final String CLASSNAME_COMMENT = "tpinglun";
    private static final String CLASSNAME_NAV_TITLE = "left";
    private static final String ID_NEWSLIST = "mynewslist";
    private static final String ID_VIEWPOINTS = "mySwipe";
    private static final String ID_NAV = "nav";
    private static final String CLASSNAME_SUBNAV = "subnav";
    private static final String CLASSNAME_NAV = "nav";
    private static final String ATTR_DATA_ID = "data-id";

    private static final String VALUE_AD = "推广";
    private static final String VALUE_TOP = "置顶";

    private OnLoadTopicsCompleteListener mOnLoadTopicsCompleteListener;
    private OnLoadViewpointsCompleteListener mOnLoadViewpointsCompleteListener;
    private OnLoadNavCompleteListener mOnLoadNavCompleteListener;

    private List<Topic> mTopics = null;
    private List<Viewpoint> mViewPoints = null;
    private List<NavType> mNavTypes = null;
    private List<SubNavType> mSubNavTypes = null;

    private static String getRefLinkFromST(Element ele) {
        return ele.child(0).attr(Constants.ATTR_LINK);
    }

    private static String getTitleFromST(Element ele) {
        return ele.child(0).text();
    }

    private static String[] getIconsFromIMG(Element ele) {
        Elements imgs = ele.getElementsByTag("img");
        String[] img_path = new String[imgs.size()];
        int i = 0;
        for (Element img : imgs) {
            img_path[i++] = img.attr("src");
        }
        return img_path;
    }

    private List<Topic> getTopicsFromEle(Element parent) {
        Elements eles = parent.children();
        List<Topic> topics = new ArrayList<>();
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
                topic.icon = getIconsFromIMG(ele);
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
                    topic.icon = getIconsFromIMG(ele);
                }
                if (childs.hasClass(CLASSNAME_ICONS)) {
                    topic.icon = getIconsFromIMG(ele);
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
        return topics;
    }

    private List<Viewpoint> getViewpointsFromEle(Element parent) {
        Elements eles = parent.child(0).children();
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

    private List<NavType> getNavTypesFromEle(Element parent) {
        Elements eles = parent.getElementsByTag("a");
        List<NavType> navTypes = new ArrayList<>();
        for (Element ele : eles) {
            NavType navType = new NavType();
            navType.title = ele.text();
            String tmp = ele.attr(Constants.ATTR_LINK);
            int index = tmp.indexOf("=");
            if (index != -1) {
                navType.tid = tmp.substring(index + 1);
            } else {
                navType.tid = "0";
            }
            Log.d("parse", "title:" + navType.title + "/tid:" + navType.tid);
        }
        return navTypes;
    }

    private List<SubNavType> getSubNavTypesFromEle(Element parent) {
        return null;
    }

    public GetTopics setOnLoadTopicsCompleteListener(OnLoadTopicsCompleteListener l) {
        mOnLoadTopicsCompleteListener = l;
        return this;
    }

    public GetTopics setOnLoadViewpointsCompleteListener(OnLoadViewpointsCompleteListener l) {
        mOnLoadViewpointsCompleteListener = l;
        return this;
    }

    public GetTopics setOnLoadNavCompleteListener(OnLoadNavCompleteListener l) {
        mOnLoadNavCompleteListener = l;
        return this;
    }

    /**
     * Load contents from host
     *
     * @param params params[0] for Tid and params[1] for minid
     * @return
     */
    @Override
    protected Integer doInBackground(String... params) {
        int result = 0;
        try {
            Document doc = null;
            Elements eles = null;
            if (params.length < 2) {
                Log.d("GetTopics", "wrong start params");
                return -1;
            }
            List<Pair> url_params = new LinkedList<>();
            switch (params[0]) {
                case MODE_GET_MORE:
                    url_params.add(new Pair("Tid", params[1]));
                    url_params.add(new Pair("type", "1"));
                    url_params.add(new Pair("minid", params[2]));
                    doc = Jsoup.parse(Utils.getURLWithParams(Constants.MOBILE_WEB_HOST + Constants.MOBILE_GET_MORE_LIST, url_params), 5000);
                    if (mOnLoadTopicsCompleteListener != null) {
                        mTopics = getTopicsFromEle(doc.getElementsByTag("body").first());
                    }
                    break;
                case MODE_REFRESH:
                    url_params.add(new Pair("tid", params[1]));
                    doc = Jsoup.parse(Utils.getURLWithParams(Constants.MOBILE_WEB_HOST + Constants.MOBILE_WEB_NAV, url_params), 5000);
                    if (mOnLoadTopicsCompleteListener != null) {
                        mTopics = getTopicsFromEle(doc.getElementById(ID_NEWSLIST));
                    }
                    if (mOnLoadViewpointsCompleteListener != null) {
                        mViewPoints = getViewpointsFromEle(doc.getElementById(ID_VIEWPOINTS));
                    }
                    if (mOnLoadNavCompleteListener != null) {
                        mNavTypes = getNavTypesFromEle(doc.getElementById(ID_NAV));
                        mSubNavTypes = getSubNavTypesFromEle(doc.getElementsByClass(CLASSNAME_SUBNAV).first());
                    }
                    break;
                default:
                    Log.d("GetTopics", "unrecognizable mode:" + params[0]);
                    result = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);
        if (mOnLoadTopicsCompleteListener != null) {
            mOnLoadTopicsCompleteListener.onLoadComplete(mTopics);
        }
        if (mOnLoadViewpointsCompleteListener != null) {
            mOnLoadViewpointsCompleteListener.onLoadComplete(mViewPoints);
        }
        if (mOnLoadNavCompleteListener != null) {
            mOnLoadNavCompleteListener.onLoadComplete(mNavTypes, mSubNavTypes);
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

    public interface OnLoadTopicsCompleteListener {
        void onLoadComplete(List<Topic> src);
    }

    public interface OnLoadViewpointsCompleteListener {
        void onLoadComplete(List<Viewpoint> src);
    }

    public interface OnLoadNavCompleteListener {
        void onLoadComplete(List<NavType> src, List<SubNavType> src_sub);
    }
}
