package product.presisco.yourdrivers.DataModel;

import java.util.List;

/**
 * Created by presisco on 2016/5/28.
 */
public class CommentSet {
    public List<Comment> All;
    public List<Counter> Count;

    public static class Comment {
        public String ID;
        public String Cid;
        public String TitleID;
        public String UserName;
        public String UserID;
        public String Content;
        public String IPAdd;
        public String IPAddCity;
        public String PostDate;
        public String Support;
        public String Oppose;
        public String RevertID;
        public String RevertContent;
        public String RevertUsername;
        public String Cfrom;
        public String Floor;
    }

    public static class Counter {
        public String ReviewCount;
    }

}
