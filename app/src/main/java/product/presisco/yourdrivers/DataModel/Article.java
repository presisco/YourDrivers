package product.presisco.yourdrivers.DataModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by presisco on 2016/4/29.
 */
public class Article {
    public List<Node> contents;
    public Header header;
    public String comm_link;
    public boolean isMultPage = false;
    public String prev_link = "";
    public String next_link = "";
    public String all_link = "";
    public String ref_name = "";
    public String ref_link = "";

    public Article() {
        header = new Header();
        contents = new LinkedList<>();
    }

    public static class Node {
        public String type = "";

        public Node(String childType) {
            type = childType;
        }

        public static String getLogTag() {
            return "Node";
        }
    }

    public static class Text extends Node {
        public static final String TAG = Text.class.getSimpleName();

        public String text = "";

        public Text(String t) {
            super(TAG);
            text = t;
        }
    }

    public static class Image extends Node {
        public static final String TAG = Image.class.getSimpleName();

        public String img_link = null;

        public Image(String url) {
            super(TAG);
            img_link = url;
        }
    }

    public static class Header {
        public String tid;
        public String title;
        public String writer;
        public String date;
    }
}
