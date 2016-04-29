package product.presisco.yourdrivers.Network.Task;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import product.presisco.yourdrivers.DataModel.Comment;

/**
 * Created by presisco on 2016/5/28.
 */
public class CommentsRequest extends Request<Comment> {
    public CommentsRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected void deliverResponse(Comment comment) {

    }

    @Override
    protected Response<Comment> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }
}
