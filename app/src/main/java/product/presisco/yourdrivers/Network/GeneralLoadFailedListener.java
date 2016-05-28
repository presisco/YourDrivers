package product.presisco.yourdrivers.Network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;

/**
 * Created by presisco on 2016/5/29.
 */
public class GeneralLoadFailedListener implements Response.ErrorListener {
    private Context context;

    public GeneralLoadFailedListener(Context c) {
        context = c;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
