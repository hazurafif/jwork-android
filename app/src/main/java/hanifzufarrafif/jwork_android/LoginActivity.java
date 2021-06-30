package hanifzufarrafif.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
public class LoginActivity extends AppCompatActivity {

    /**
     * Method onCreate untuk inisiasi dalam pembuatan layout activity_login.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inisiasi view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inisiasi komponen
        EditText etEmail = findViewById(R.id.email);
        EditText etPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.login);
        TextView tvRegister = findViewById(R.id.Register);

        //Method btnLogin saat button Login diklik
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                /**
                                 * melamar pekerjaan (2 a.)
                                 */
                                loginIntent.putExtra("jobseekerID", jsonObject.getInt("id"));
                                loginIntent.putExtra("jobseekerName", jsonObject.getString("name"));


                                startActivity(loginIntent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                System.out.println(loginRequest);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}