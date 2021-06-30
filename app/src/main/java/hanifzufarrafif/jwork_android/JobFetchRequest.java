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
public class JobFetchRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/invoice/jobseeker/";
    private Map<String, String> params;

    /**
     * konstruktor yang akan dilemparkan ke server untuk mengambil Job/Invoice berdasarkan jobseeker
     * @param jobseekerId
     * @param listener
     */
    public JobFetchRequest(String jobseekerId, Response.Listener<String> listener){
        super(Method.GET, URL+jobseekerId, listener, null);
        params = new HashMap<>();
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
