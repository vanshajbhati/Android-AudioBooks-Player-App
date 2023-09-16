package com.google.android.exoplayer.finalmusicapp.userAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer.finalmusicapp.R;
import com.hbb20.CountryCodePicker;

import soup.neumorphism.NeumorphCardView;

public class LoginActivity extends AppCompatActivity {

    TextView confirm;
    EditText enternumber;
    CountryCodePicker countryCodePicker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

       confirm = findViewById(R.id.confirm_button);
       enternumber = findViewById(R.id.phoneNumber);
         countryCodePicker = findViewById(R.id.ccp);

     final   ProgressBar  progressBar = findViewById(R.id.progressbar_sending_otp);

        final NeumorphCardView  cardView = findViewById(R.id.confirm_cardView);

        confirm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(LoginActivity.this, OtpAuth.class);

                 startActivity(intent);

//                 if (!enternumber.getText().toString().trim().isEmpty()) {
//                     if ((enternumber.getText().toString().trim()).length() == 10) {
//
//                         progressBar.setVisibility(View.VISIBLE);
//                         cardView.setVisibility(View.INVISIBLE);
//
//                         PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                                 "+" + countryCodePicker.getSelectedCountryCode() + enternumber.getText().toString(),
//                                 60, TimeUnit.SECONDS, LoginActivity.this,
//
//                                 new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                     @Override
//                                     public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                         progressBar.setVisibility(View.GONE);
//                                         cardView.setVisibility(View.VISIBLE);
//                                     }
//
//                                     @Override
//                                     public void onVerificationFailed(@NonNull FirebaseException e) {
//                                         progressBar.setVisibility(View.GONE);
//                                         cardView.setVisibility(View.VISIBLE);
//                                         Toast.makeText(  LoginActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
//                                     }
//
//                                     @Override
//                                     public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
//                                         progressBar.setVisibility(View.GONE);
//                                         cardView.setVisibility(View.VISIBLE);
//                                         Log.e("time out", "+" + countryCodePicker.getSelectedCountryCode() + enternumber.getText().toString() );
//                                       // Toast.makeText(LoginActivity.this, "+" + countryCodePicker.getSelectedCountryCode() + enternumber.getText().toString(), Toast.LENGTH_SHORT).show();
//                                     }
//
//                                     @Override
//                                     public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//                                         progressBar.setVisibility(View.GONE);
//                                         cardView.setVisibility(View.VISIBLE);
//                                         Intent intent = new Intent(LoginActivity.this, OtpAuth.class);
//                                         intent.putExtra("mobile", enternumber.getText().toString());
//                                         intent.putExtra("ccp", countryCodePicker.getSelectedCountryCode());
//                                         intent.putExtra( "backendotp",s);
//                                         startActivity(intent);
//
//                                     }
//                                 }
//                         );
//
//
//                     } else {
//                         Toast.makeText(LoginActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
//                     }
//                 }else {
//                     Toast.makeText(  LoginActivity.this, "Enter Mobile number" ,Toast.LENGTH_SHORT).show();
//                 }



             }
         });
    }
}