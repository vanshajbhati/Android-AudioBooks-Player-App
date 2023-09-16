package com.google.android.exoplayer.finalmusicapp.userAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer.finalmusicapp.Main;
import com.google.android.exoplayer.finalmusicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import soup.neumorphism.NeumorphCardView;


public class OtpAuth extends AppCompatActivity {

    TextView showNumber;
    EditText inputnumber1,inputnumber2, inputnumber3, inputnumber4, inputnumber5, inputnumber6;

    TextView confirmButton;

    String getotpbackend;


    TextView resendOTP;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_auth);

        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2= findViewById(R.id.inputotp2);
        inputnumber3=findViewById(R.id.inputotp3);
        inputnumber4=findViewById(R.id.inputotp4);
        inputnumber5=findViewById(R.id.inputotp5);
        inputnumber6=findViewById(R.id.inputotp6);

        resendOTP = findViewById(R.id.textresendotp);

         showNumber = findViewById(R.id.showNumber);
         confirmButton = findViewById(R.id.confirm_button);

        final  ProgressBar  progressBar = findViewById(R.id.progressbar_verify_otp);

        final NeumorphCardView cardView = findViewById(R.id.confirm_cardView);

        getotpbackend = getIntent().getStringExtra(  "backendotp");

         showNumber.setText(String.format(
                "+"+getIntent().getStringExtra("ccp")+"- "+getIntent().getStringExtra("mobile")
         ));


         confirmButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty() &&!inputnumber3.getText().toString().trim().isEmpty() &&!inputnumber4.getText().toString().trim().isEmpty() ){
                     String entercodeotp = inputnumber1.getText().toString()+
                     inputnumber2.getText().toString() + inputnumber3.getText().toString() +
                             inputnumber4.getText().toString() +   inputnumber5.getText().toString()  +inputnumber6.getText().toString();

                     if(getotpbackend!=null) {
                         progressBar.setVisibility(View.VISIBLE);
                         cardView.setVisibility(View.INVISIBLE);

                         PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                 getotpbackend, entercodeotp
                         );

                         FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                      progressBar.setVisibility(View.GONE);
                                      cardView.setVisibility(View.VISIBLE);
                                      FirebaseUser user = task.getResult().getUser();
                                      if(task.isSuccessful()){
                                          if (user != null) {
                                          Intent intent = new Intent(getApplicationContext(), Main.class);
                                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);}else {
                                              Toast.makeText(OtpAuth.this, "user not registered", Toast.LENGTH_SHORT).show();
                                          }

                                      }else {
                                          Toast.makeText(OtpAuth.this, "Enter the correct otp", Toast.LENGTH_SHORT).show();
                                      }
                             }
                         });
                     }else Toast.makeText(OtpAuth.this, "please enter all number", Toast.LENGTH_SHORT).show();
                 }
             }
         });



    moveotp();


        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+" +getIntent().getStringExtra("ccp")+"- "+getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        OtpAuth.this,

                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(  OtpAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onCodeSent(@NonNull String newotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                getotpbackend = newotp;
                                Toast.makeText(  OtpAuth.this,  "Otp Sent",  Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });
    }


    // Example function to resend OTP


    private  void moveotp(){

       inputnumber1.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber2.requestFocus();
                }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber6.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}