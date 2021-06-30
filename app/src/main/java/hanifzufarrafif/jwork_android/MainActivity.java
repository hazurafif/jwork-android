package hanifzufarrafif.jwork_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity Class untuk aplikasi job (1 a.)
 *
 * @author Hanif Zufar Rafif
 * @version 1.0
 * @since 25 Juni 2021
 */
public class MainActivity extends AppCompatActivity {

    //private variabel
    private ArrayList<Recruiter> listRecruiter = new ArrayList<>();
    private ArrayList<Job> jobIdList = new ArrayList<>();
    private HashMap<Recruiter, ArrayList<Job>> childMapping = new HashMap<>();

    //Adapter dan view untuk inisialisasi pada layout
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    /**
     * buat tombol applied job (3 b.)
     */
    Button btnAppliedJob;
    private static int jobseekerID;
    private static String jobseekerName;
    private static int currentInvoiceId;

    /**
     * Method onCreate untuk inisiasi dalam pembuatan layout activity_main.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inisiasi view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Proses pengambilan intent dari LoginActivity 1 b.)
         */
        Intent intent = getIntent();
        //Pengambilan putEkstra dari LoginActivity
        int jobseekerID = intent.getIntExtra("jobseekerID", 0);
        String jobseekerName = intent.getStringExtra("jobseekerName");

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnAppliedJob = findViewById(R.id.btnAppliedJob);

        //Menjalankan method refreshList pada sebuah Thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshList();
            }
        });

        /**
         * method expandable list  yang kan dijalankan jika mengklik salah satu job yang ada di expandView (1 c.)
         */
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                /**
                 * ketika diklik berpindah ke applyjobactivty, dan berikan pass atau putExtra (1 d.)
                 */
                Intent intent = new Intent(MainActivity.this, ApplyJobActivity.class);
                //potongan kode (1 c.)
                Job selectedJob = childMapping.get(listRecruiter.get(groupPosition)).get(childPosition);

                intent.putExtra("jobID", selectedJob.getId());
                intent.putExtra("jobName", selectedJob.getName());
                intent.putExtra("jobCategory", selectedJob.getCategory());
                intent.putExtra("jobFee", selectedJob.getFee());
                intent.putExtra("jobseekerID", jobseekerID);
                startActivity(intent);
                return true;
            }
        });
        //jika tombol applied job ditekan maka activity dipindahkan ke  SelesaiJobActivity (3 b.)
        btnAppliedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelesaiJobActivity.class);
                intent.putExtra("jobseekerID", jobseekerID);
                intent.putExtra("jobseekerName", jobseekerName );
                startActivity(intent);
            }
        });

    }

    protected void refreshList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    if (jsonResponse != null) {
                        for (int i = 0; i < jsonResponse.length(); i++){
                            JSONObject job = jsonResponse.getJSONObject(i);
                            JSONObject recruiter = job.getJSONObject("recruiter");
                            JSONObject location = recruiter.getJSONObject("location");

                            String city = location.getString("city");
                            String province = location.getString("province");
                            String description = location.getString("description");

                            Location location1 = new Location(province, city, description);

                            int recruiterId = recruiter.getInt("id");
                            String recruiterName = recruiter.getString("name");
                            String recruiterEmail = recruiter.getString("email");
                            String recruiterPhoneNumber = recruiter.getString("phoneNumber");

                            Recruiter newRecruiter = new Recruiter(recruiterId, recruiterName, recruiterEmail, recruiterPhoneNumber, location1);
                            if (listRecruiter.size() > 0) {
                                boolean success = true;
                                for (Recruiter rec : listRecruiter)
                                    if (rec.getId() == newRecruiter.getId())
                                        success = false;
                                if (success) {
                                    listRecruiter.add(newRecruiter);
                                }
                            } else {
                                listRecruiter.add(newRecruiter);
                            }

                            int jobId = job.getInt("id");
                            int jobFee = job.getInt("fee");
                            String jobName = job.getString("name");
                            String jobCategory = job.getString("category");

                            Job newJob = new Job(jobId, jobName, newRecruiter, jobFee, jobCategory);
                            jobIdList.add(newJob);

                            for (Recruiter sel : listRecruiter) {
                                ArrayList<Job> temp = new ArrayList<>();
                                for (Job jobs : jobIdList) {
                                    if (jobs.getRecruiter().getName().equals(sel.getName()) || jobs.getRecruiter().getEmail().equals(sel.getEmail()) || jobs.getRecruiter().getPhoneNumber().equals(sel.getPhoneNumber())) {
                                        temp.add(jobs);
                                    }
                                }
                                childMapping.put(sel, temp);
                            }
                        }
                        listAdapter = new MainListAdapter(MainActivity.this, listRecruiter, childMapping);
                        expListView.setAdapter(listAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);

    }

    private void fetchJob() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray != null) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject invoice = jsonArray.getJSONObject(i);
                            currentInvoiceId = invoice.getInt("id");
                        }
                        Intent intent = new Intent(MainActivity.this, SelesaiJobActivity.class);
                        intent.putExtra("jobseekerId", jobseekerID);
                        intent.putExtra("currentInvoiceId", currentInvoiceId);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Invoice is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        };

        JobFetchRequest request = new JobFetchRequest(Integer.toString(jobseekerID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}