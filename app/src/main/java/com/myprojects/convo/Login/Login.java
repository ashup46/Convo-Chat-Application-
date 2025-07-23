package com.myprojects.convo.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.myprojects.convo.Database_Setup.Api_Client;
import com.myprojects.convo.Database_Setup.Response_Modal;
import com.myprojects.convo.Database_Setup.Retrofit_init;
import com.myprojects.convo.MainActivity;
import com.myprojects.convo.R;
import com.myprojects.convo.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private TextInputEditText enterEmail,enterPassword;

    private MaterialButton loginbtn;

    private TextView noAccounttxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        enterEmail= binding.emailEditText;
        enterPassword = binding.passwordEditText;
        loginbtn = binding.loginBtn;
        noAccounttxt = binding.sigupAccount;



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api_Client apiClient = Retrofit_init.getRetrofit().create(Api_Client.class);
                Call<Response_Modal> call = apiClient.verifyLogin(enterEmail.getText().toString(),enterPassword.getText().toString());
//                Call<Response_Modal> call = apiClient.verifyLogin("vanya@gmail.com","vanya");

                call.enqueue(new Callback<Response_Modal>() {
                    @Override
                    public void onResponse(Call<Response_Modal> call, Response<Response_Modal> response) {
                        if (response.isSuccessful())
                        {
                            Response_Modal responseModal = response.body();

                            String status = responseModal.getStatus();

                         /*   Log.d("LoginError", "status : " + status);
                            Log.d("LoginError", "message : " + responseModal.getMessage());*/

                            if (responseModal.getStatus().equals("success"))
                            {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("useremail",enterEmail.getText().toString());
//                                intent.putExtra("useremail","vanya@gmail.com");
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Invalid Response", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response_Modal> call, Throwable t) {

                        Log.d("LoginError", t.getMessage());

                        Toast.makeText(Login.this, "Response Failure", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        noAccounttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });


       ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}