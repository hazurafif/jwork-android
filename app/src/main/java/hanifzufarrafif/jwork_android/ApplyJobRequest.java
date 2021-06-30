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
public class ApplyJobRequest extends StringRequest {
    //private static final String URL = "http://10.0.2.2:8080/invoice";
    private static final String URL_Ewallet = "http://10.0.2.2:8080/invoice/createEWalletPayment";
    private static final String URL_Bank = "http://10.0.2.2:8080/invoice/createBankPayment";
    private Map<String, String> params;

    /**
     * konstruktor ApplyJobRequest dengan ewallet payment
     * @param jobList
     * @param jobseekerID
     * @param refferalCode
     * @param listener
     */
    public ApplyJobRequest(String jobList, String jobseekerID, String refferalCode, Response.Listener<String> listener) {
        super(Method.POST, URL_Ewallet, listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobList);
        params.put("jobseekerId", jobseekerID);
        params.put("referralCode", refferalCode);
    }

    /**
     * konstruktor ApplyJobRequest dengan bank payment
     * @param jobList
     * @param jobseekerID
     * @param listener
     */
    public ApplyJobRequest(String jobList, String jobseekerID, Response.Listener<String> listener) {
        super(Method.POST, URL_Bank, listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobList);
        params.put("jobseekerId", jobseekerID);
        params.put("adminFee", "2500");
    }


    /**
     * Mendaoatkan params yang telah diput di konstruktor
     * @return @throws AuthFailureError
     *
     */
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
