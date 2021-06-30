package hanifzufarrafif.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity Class untuk aplikasi job (1 a.)
 *
 * @author Hanif Zufar Rafif
 * @version 1.0
 * @since 25 Juni 2021
 */
public class JobBatalRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/invoice/invoiceStatus/";
    private Map<String, String> params;

    /**
     * Constructor untuk melakukan pengiriman request edit status ke server
     * @param id untuk id job
     * @param status dalam status batal invoice
     * @param listener volley request URL
     *
     */
    public JobBatalRequest(String id, String status, Response.Listener<String> listener) {
        super(Method.PUT, URL + id, listener, null);
        params = new HashMap<>();
        params.put("id",id);
        params.put("status", status);
    }

    /**
     * Mendapatkan params yang telah diput di konstruktor
     * @return params
     *
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
