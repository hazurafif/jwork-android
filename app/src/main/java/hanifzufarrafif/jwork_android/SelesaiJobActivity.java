package hanifzufarrafif.jwork_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity Class untuk aplikasi job (1 a.)
 *
 * @author Hanif Zufar Rafif
 * @version 1.0
 * @since 25 Juni 2021
 */
public class SelesaiJobActivity extends AppCompatActivity {

    //inisiasi variabel pada layout
    TextView tvInvoice_Id, staticJobseekerName, staticInvoiceDate, staticPaymentType, staticInvoiceStatus,  staticJobName, staticTotalFee, staticReferralCode, tvJob_name;
    TextView tvReferral_code, tvInvoice_date, tvPayment_type, tvInvoice_status, tvJob_fee, tvTotal_fee, tvJobseeker_name;
    Button cancel_button, finish_button;

    //Instance Variable
    String jobName, jobseekerName, invoiceDate, referralCode, paymentType;
    int jobseekerID, jobFee, totalFee, currentInvoiceId, adminFee, discount;

    /**
     * Method onCreate untuk inisiasi dalam pembuatan activity_selesai_job.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inisiasi view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_job);

        //inisiasi komponen
        tvInvoice_Id = findViewById(R.id.invoice_Id);
        staticJobseekerName = findViewById(R.id.staticJobseekerName);
        staticJobName = findViewById(R.id.staticJobName);
        staticInvoiceDate = findViewById(R.id.staticInvoiceDate);
        staticInvoiceStatus = findViewById(R.id.staticInvoiceStatus);
        staticPaymentType = findViewById(R.id.staticPaymentType);
        staticReferralCode = findViewById(R.id.staticReferralCode);
        staticTotalFee = findViewById(R.id.staticTotalFee);
        tvReferral_code = findViewById(R.id.referral_code);
        tvJobseeker_name = findViewById(R.id.jobseeker_name);
        tvInvoice_date = findViewById(R.id.invoice_date);
        tvInvoice_status = findViewById(R.id.invoice_status);
        tvPayment_type = findViewById(R.id.payment_type);
        tvJob_fee = findViewById(R.id.job_fee);
        tvTotal_fee = findViewById(R.id.total_fee);
        cancel_button = findViewById(R.id.cancel_button);
        finish_button = findViewById(R.id.finish_button);
        tvJob_name = findViewById(R.id.job_name);

        /**
         * ubah semua komponen menjadi tak terlihat ( 3 g.)
         */
        tvInvoice_Id.setText("No on-going orders");
        staticJobseekerName.setVisibility(View.GONE);
        staticJobName.setVisibility(View.GONE);
        staticInvoiceDate.setVisibility(View.GONE);
        staticInvoiceStatus.setVisibility(View.GONE);
        staticPaymentType.setVisibility(View.GONE);
        staticReferralCode.setVisibility(View.GONE);
        staticTotalFee.setVisibility(View.GONE);
        tvJobseeker_name.setVisibility(View.GONE);
        tvTotal_fee.setVisibility(View.GONE);
        cancel_button.setVisibility(View.GONE);
        finish_button.setVisibility(View.GONE);
        tvInvoice_date.setVisibility(View.GONE);
        tvInvoice_status.setVisibility(View.GONE);
        tvPayment_type.setVisibility(View.GONE);
        tvReferral_code.setVisibility(View.GONE);
        tvJob_fee.setVisibility(View.GONE);
        tvJob_name.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobseekerName = extras.getString("jobseekerName");
            jobseekerID = extras.getInt("jobseekerId");
            currentInvoiceId = extras.getInt("currentInvoiceId");
        }
        //panggil method fetchJob()
        runOnUiThread(this::fetchJob);

        Log.d("currentInvoiceId", String.valueOf(currentInvoiceId));
        /**
         * buat conditional statement untuk fetch ( 3 j.)
         */
        //Method yang dijalankan saat cancel_button/tombol batal diklik
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(SelesaiJobActivity.this, "This invoice is cancelled", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("jobseekerID", jobseekerID);
                                intent.putExtra("jobseekerName", jobseekerName);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiJobActivity.this);
                            builder.setMessage("Please try again").create().show();
                        }
                    }
                };

                JobBatalRequest request = new JobBatalRequest(String.valueOf(currentInvoiceId), "Cancelled", responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(request);
            }
        });

        //Method yang dijalankan saat finish_button/tombol selesai diklik
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(SelesaiJobActivity.this, "This invoice is finished", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("jobseekerID", jobseekerID);
                                intent.putExtra("jobseekerName", jobseekerName);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiJobActivity.this);
                            builder.setMessage("Operation Failed! Please try again").create().show();
                        }
                    }
                };
                JobSelesaiRequest request = new JobSelesaiRequest(String.valueOf(currentInvoiceId), "Finished", responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(request);
            }
        });

    }

    /**
     * method fetchJob untuk mekanisme request dan queue data invoice dari jobseeker (3 h.)
     */
    public void fetchJob() {
        //Response.Listener<String> responseListener = new Response.Listener<String>() {
        Response.Listener<String> responseListener = response -> {
            //@RequiresApi(api = Build.VERSION_CODES.N)
            //@Override
            /**
             * conditional statement jika fetch dihasilkan (semua komponen layout tidak terlihat)/
             * tidak dihasilkan (semua komponen layout terlihat)
             * 3 (i.)
             */
            try {
                JSONArray jsonResponse = new JSONArray(response);
                if (jsonResponse != null) {
                    Toast.makeText(SelesaiJobActivity.this, "Bank Payment", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject invoice = jsonResponse.getJSONObject(i);
                        JSONArray jobs = invoice.getJSONArray("jobs");
                        String invoiceStatus = invoice.getString("invoiceStatus");
                        if (invoiceStatus.equals("OnGoing")) {
                            for (int j = 0; j < jobs.length(); j++) {
                                JSONObject job = jobs.getJSONObject(j);
                                jobName = job.getString("name");
                                jobFee = job.getInt("fee");
                                tvJob_name.setText(jobName);
                                tvJob_fee.setText("Rp. " + jobFee);
                            }

                            staticJobseekerName.setVisibility(View.VISIBLE);
                            staticInvoiceDate.setVisibility(View.VISIBLE);
                            staticJobName.setVisibility(View.VISIBLE);
                            staticInvoiceStatus.setVisibility(View.VISIBLE);
                            staticPaymentType.setVisibility(View.VISIBLE);
                            staticTotalFee.setVisibility(View.VISIBLE);
                            tvJobseeker_name.setVisibility(View.VISIBLE);
                            tvInvoice_date.setVisibility(View.VISIBLE);
                            tvPayment_type.setVisibility(View.VISIBLE);
                            tvInvoice_status.setVisibility(View.VISIBLE);
                            tvJob_name.setVisibility(View.VISIBLE);
                            tvJob_fee.setVisibility(View.VISIBLE);
                            tvTotal_fee.setVisibility(View.VISIBLE);
                            cancel_button.setVisibility(View.VISIBLE);
                            finish_button.setVisibility(View.VISIBLE);

                            tvInvoice_Id.setText("Invoice ID: " + currentInvoiceId);
                            tvJobseeker_name.setText(jobseekerName);
                            tvInvoice_status.setText(invoiceStatus);

                            invoiceDate = invoice.getString("date");
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = inputFormat.parse(invoiceDate);
                            Locale indonesia = new Locale("in", "ID");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", indonesia);
                            invoiceDate = outputFormat.format(date);

                            tvInvoice_date.setText(invoiceDate);
                            paymentType = invoice.getString("paymentType");
                            tvPayment_type.setText(paymentType);
                            totalFee = invoice.getInt("totalFee");
                            tvTotal_fee.setText("" + totalFee);

                            switch (paymentType) {
                                case "BankPayment":
                                    adminFee = invoice.getInt("adminFee");
                                    tvReferral_code.setVisibility(View.GONE);
                                    staticReferralCode.setVisibility(View.GONE);
                                    break;
                                case "EWalletPayment":
                                    JSONObject referral = invoice.getJSONObject("referral");
                                    referralCode = referral.getString("code");
                                    if (referral.isNull("code")) {
                                        tvReferral_code.setVisibility(View.GONE);
                                        staticReferralCode.setVisibility(View.GONE);
                                    } else {
                                        discount = referral.getInt("discount");
                                        tvReferral_code.setVisibility(View.VISIBLE);
                                        staticReferralCode.setVisibility(View.VISIBLE);
                                        tvReferral_code.setText(referralCode);
                                    }
                                    break;
                            }
                        } else {
                            tvInvoice_Id.setText("No on-going orders");
                            staticJobseekerName.setVisibility(View.GONE);
                            staticJobName.setVisibility(View.GONE);
                            staticInvoiceDate.setVisibility(View.GONE);
                            staticInvoiceStatus.setVisibility(View.GONE);
                            staticPaymentType.setVisibility(View.GONE);
                            staticReferralCode.setVisibility(View.GONE);
                            staticTotalFee.setVisibility(View.GONE);
                            tvReferral_code.setVisibility(View.GONE);
                            tvJobseeker_name.setVisibility(View.GONE);
                            tvInvoice_date.setVisibility(View.GONE);
                            tvInvoice_status.setVisibility(View.GONE);
                            tvPayment_type.setVisibility(View.GONE);
                            tvJob_fee.setVisibility(View.GONE);
                            tvJob_name.setVisibility(View.GONE);
                            tvTotal_fee.setVisibility(View.GONE);
                            cancel_button.setVisibility(View.GONE);
                            finish_button.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (JSONException | ParseException e) {
                Toast.makeText(SelesaiJobActivity.this, ""+currentInvoiceId, Toast.LENGTH_LONG).show();
            }
        };

        JobFetchRequest request = new JobFetchRequest(Integer.toString(jobseekerID), responseListener);
        RequestQueue queue = new Volley().newRequestQueue(SelesaiJobActivity.this);
        queue.add(request);
    }


}