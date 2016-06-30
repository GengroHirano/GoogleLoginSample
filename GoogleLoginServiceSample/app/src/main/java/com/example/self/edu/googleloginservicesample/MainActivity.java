package com.example.self.edu.googleloginservicesample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

  private static final String TAG = "MainActivity";
  private static final int RC_SIGN_IN = 9001;
  private GoogleApiClient mGoogleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Google Sign-Inの設定
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    findViewById(R.id.log_out).setOnClickListener(this);

    // Google Sign-Inボタンの設定
    SignInButton signInButton = (SignInButton)findViewById(R.id.button_sign_in);
    signInButton.setOnClickListener(this);
    signInButton.setSize(SignInButton.SIZE_STANDARD);
    signInButton.setScopes(gso.getScopeArray());
  }

  // Sign-Inした結果を受け取る処理
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  // Sign-Inした結果を扱う処理
  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount acct = result.getSignInAccount();
      Toast.makeText(getApplicationContext(), ""+acct.getEmail(), Toast.LENGTH_SHORT).show();
    } else {
      // Signed out, show unauthenticated UI.
    }
  }

  // Sign-In処理
  private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_sign_in:
        signIn();
        break;
      case R.id.log_out:
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
          @Override
          public void onResult(Status status) {
            if (status.isSuccess()) {
              Toast.makeText(getApplicationContext(), "LogOut", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(getApplicationContext(), "LogOut_FILED", Toast.LENGTH_SHORT).show();
            }
          }
        });
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.v("Connection", "FILED");
  }
}