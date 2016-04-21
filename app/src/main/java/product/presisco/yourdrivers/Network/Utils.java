package product.presisco.yourdrivers.Network;

import android.text.TextUtils;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by presisco on 2016/4/20.
 */
public class Utils {
    public static URL getURLWithParams(String path, List<Pair> params) throws Exception {
        String full = path;
        if (params == null || params.size() == 0) {
            return new URL(path);
        }
        path = path + "?" + getFormParams(params);
        return new URL(path);
    }

    public static String getFullStringFromConnection(InputStream is, String format) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(is, format));
        StringBuffer resultBuff = new StringBuffer();
        String line = "";
        while ((line = buff.readLine()) != null) {
            resultBuff.append(line);
        }
        return resultBuff.toString();
    }

    public static String getFormParams(List<Pair> orig_params) throws Exception {
        List<String> cooked_params = new ArrayList<>();
        for (Pair pair : orig_params) {
            cooked_params.add(pair.first + "=" + URLEncoder.encode((String) pair.second, "UTF-8"));
        }
        return TextUtils.join("&", cooked_params);
    }
}
