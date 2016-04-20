package product.presisco.yourdrivers.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by presisco on 2016/4/20.
 */
public class Utils {
    public static String getFullStringFromConnection(InputStream is, String format) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(is, format));
        StringBuffer resultBuff = new StringBuffer();
        String line = "";
        while ((line = buff.readLine()) != null) {
            resultBuff.append(line);
        }
        return resultBuff.toString();
    }
}
