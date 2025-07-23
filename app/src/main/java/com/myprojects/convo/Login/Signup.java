package com.myprojects.convo.Login;

import android.os.Bundle;
import android.view.View;
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
import com.myprojects.convo.R;
import com.myprojects.convo.databinding.ActivitySignupBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Signup extends AppCompatActivity {

    private ActivitySignupBinding binding;

    private TextInputEditText enterUsername,enterEmail,enterPassword,enterConfirmPassword;

    private MaterialButton login_btn,cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        enterUsername=binding.UsernameEditText;
        enterEmail = binding.emailEditText;
        enterPassword= binding.passwordEditText;
        enterConfirmPassword= binding.confirmPasswordEditText;

        login_btn = binding.loginBtn;
        cancel_btn = binding.cancelBtn;




            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (enterPassword.getText().toString().equals(enterConfirmPassword.getText().toString()))
                    {
                        Api_Client apiClient = Retrofit_init.getRetrofit().create(Api_Client.class);

                        Call<Response_Modal> call = apiClient.addUserInfo(enterUsername.getText().toString()
                                ,enterEmail.getText().toString()
                                ,enterPassword.getText().toString());


                        call.enqueue(new Callback<Response_Modal>() {
                            @Override
                            public void onResponse(Call<Response_Modal> call, Response<Response_Modal> response) {
                                if (response.isSuccessful())
                                {

                                    onBackPressed();
                                    Toast.makeText(Signup.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response_Modal> call, Throwable t) {
                                Toast.makeText(Signup.this, "Signup Not Successful", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    else
                    {
                        Toast.makeText(Signup.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                    }



                }
            });






        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}