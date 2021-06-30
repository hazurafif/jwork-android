package hanifzufarrafif.jwork_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity Class untuk aplikasi job (1 a.)
 *
 * @author Hanif Zufar Rafif
 * @version 1.0
 * @since 25 Juni 2021
 */
public class ApplyJobActivity extends AppCompatActivity {
    /**
     * private variabel (1 b.)
     */
    private int jobseekerID, jobId, bonus;
    private String jobName, jobCategory, selectedPayment;
    private double jobFee;

    //Request Class untuk melakukan request ke server jwork (untuk 1 i.)
    ApplyJobRequest applyJobRequest;

    /**
     * Method onCreate untuk inisiasi dalam pembuatan layout activity_apply_job.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inisiasi view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        /**
         * inisiasi komponen (1 d.)
         */
        TextView etJob_name = findViewById(R.id.job_name);
        TextView etJob_category = findViewById(R.id.job_category);
        TextView etJob_fee = findViewById(R.id.job_fee);
        RadioGroup etRadioGroup = findViewById(R.id.radioGroup);
        TextView etTextCode = findViewById(R.id.textCode);
        EditText etReferral_code = findViewById(R.id.referral_code);
        TextView etTotal_fee = findViewById(R.id.total_fee);
        Button btnApply = findViewById(R.id.btnApply);
        Button hitung = findViewById(R.id.hitung);

        //Pengambilan intent dari MainActivity
        Intent intent = getIntent();
        /**
         * Pengambilan putEkstra dari MainActivity (1 e.)
         */
        jobseekerID = intent.getIntExtra("jobseekerID", 0);
        jobId = intent.getIntExtra("jobID", 0);
        jobName = intent.getStringExtra("jobName");
        jobCategory = intent.getStringExtra("jobCategory");
        jobFee = intent.getIntExtra("jobFee", 0);

        //Buat btnApply, TextView textCode, dan EditText referral_code menghilang/tidak terlihat (1 e.)
        btnApply.setVisibility(View.INVISIBLE);
        etTextCode.setVisibility(View.INVISIBLE);
        etReferral_code.setVisibility(View.INVISIBLE);


        /**
         * Set komponen pada job name, category, fee dan total fee sesuai dengan data yang telah diambil
         * dari Main Activity dan berikan nilai (1 e.)
         */
        etJob_name.setText(jobName);
        etJob_category.setText(jobCategory);
        etJob_fee.setText(Double.toString(jobFee));

        etTotal_fee.setText("0");

        /**
         * Method etRadioGroup yang bekerja saat radio button dipilih (1 f.)
         */
        etRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = findViewById(checkedId);
                switch (checkedId) {
                    case R.id.ewallet:
                        etTextCode.setVisibility(View.VISIBLE);
                        etReferral_code.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bank:
                        etTextCode.setVisibility(View.INVISIBLE);
                        etReferral_code.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        /**
         * Method hitung untuk counting yang diaktivasi jika tombol hitung diklik (1 h.)
         */
        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = etRadioGroup.getCheckedRadioButtonId();
                switch (checkedId) {
                    case R.id.ewallet:

                        String refCode = etReferral_code.getText().toString();
                        final Response.Listener<String> bonusResponse = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (refCode.isEmpty()) {
                                    Toast.makeText(ApplyJobActivity.this, "No referral code applied!", Toast.LENGTH_SHORT).show();
                                    etTotal_fee.setText(Double.toString(jobFee));
                                } else {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        //Get Discount Price
                                        int extraFee = jsonResponse.getInt("extraFee");
                                        int minTotalFee = jsonResponse.getInt("minTotalFee");
                                        boolean bonusStatus = jsonResponse.getBoolean("active");

                                        if (!bonusStatus) {
                                            Toast.makeText(ApplyJobActivity.this, "This bonus is unavailable!", Toast.LENGTH_SHORT).show();
                                        } else if (bonusStatus) {
                                            if (jobFee < extraFee || jobFee < minTotalFee) {
                                                Toast.makeText(ApplyJobActivity.this, "Referral code is invalid!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ApplyJobActivity.this, "Promo code applied!", Toast.LENGTH_SHORT).show();
                                                //Set Total Price
                                                etTotal_fee.setText(Double.toString(jobFee));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(ApplyJobActivity.this, "Referral code not found!", Toast.LENGTH_SHORT).show();
                                        etTotal_fee.setText(Double.toString(jobFee));
                                    }
                                }

                            }
                        };
                        Response.ErrorListener errorPromo = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "Error Occured", error);
                            }
                        };
                        //Volley Request for Promo Request
                        BonusRequest bonusRequest = new BonusRequest(refCode, bonusResponse);
                        RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                        queue.add(bonusRequest);
                        break;

                    case R.id.bank:
                        etTotal_fee.setText(Double.toString(jobFee));
                        break;
                }
                hitung.setVisibility(View.INVISIBLE);
                btnApply.setVisibility(View.VISIBLE);
            }
        });

        /**
         * Method onClickListener untuk tombol apply (1 i.)
         */
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioId = etRadioGroup.getCheckedRadioButtonId();
                ApplyJobRequest request = null;

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(ApplyJobActivity.this, "Applied!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ApplyJobActivity.this, "Apply failed!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ApplyJobActivity.this, "Order failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                if (selectedRadioId == R.id.bank) {
                    request = new ApplyJobRequest(String.valueOf(jobId), String.valueOf(jobseekerID), responseListener);
                    RequestQueue q = Volley.newRequestQueue(ApplyJobActivity.this);
                    q.add(request);
                } else if (selectedRadioId == R.id.ewallet) {
                    request = new ApplyJobRequest(String.valueOf(jobId), String.valueOf(jobseekerID), etReferral_code.getText().toString(), responseListener);
                    RequestQueue q = Volley.newRequestQueue(ApplyJobActivity.this);
                    q.add(request);
                }
            }
        });
    }
}
