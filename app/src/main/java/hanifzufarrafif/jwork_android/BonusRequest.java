package hanifzufarrafif.jwork_android;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Activity Class untuk aplikasi job (1 a.)
 *
 * @author Hanif Zufar Rafif
 * @version 1.0
 * @since 25 Juni 2021
 */
public class BonusRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/bonus/";
    private Map<String, String> params;

    /**
     * konstruktor untuk BonusRequest yang akan dikirimkan ke server
     * @param referralCode
     * @param listener
     */
    public BonusRequest(String referralCode, Response.Listener<String> listener) {
        super(Method.GET, URL+referralCode, listener, null);
    }

    /**
     * Mendapatkan params yang telah diput di konstruktor
     * @return params
     */
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}

