package product.presisco.yourdrivers.Network.Request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import product.presisco.yourdrivers.DataModel.CommentSet;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Utils;

/**
 * Created by presisco on 2016/5/29.
 */
public class CommentRequest extends ExtendedRequest<CommentSet> {
    String tid;
    String type;
    int page;
    String orderby;

    public CommentRequest(String _tid, String _type, int _page, String _orderby, OnLoadCompleteListener<CommentSet> loadCompleteListener, Response.ErrorListener listener) {
        super(Method.POST, Constants.REQUEST_COMMENT, loadCompleteListener, listener);
        tid = _tid;
        type = _type;
        page = _page;
        orderby = _orderby;
    }

    private static String cookContent(String content) {
        content.replaceAll("<br/>", "\n");
        content.replace("&nbsp;", " ");
        return content;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] buff = null;
        Map<String, String> params = new HashMap<>();
        params.put(Constants.REQUEST_COMMENT_TID, tid);
        params.put(Constants.REQUEST_COMMENT_TYPE, type);
        params.put(Constants.REQUEST_COMMENT_PAGE, page + "");
        params.put(Constants.REQUEST_COMMENT_ORDER, orderby);
        try {
            buff = Utils.getFormParams(params).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buff;
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    protected Response<CommentSet> parseNetworkResponse(NetworkResponse networkResponse) {
        Gson gson = new Gson();
        CommentSet commentSet = gson.fromJson(getStringFromResponse(networkResponse), CommentSet.class);
        if (commentSet == null) {
            Log.e(TAG, "get comment data failed");
        }
        return Response.success(commentSet, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
