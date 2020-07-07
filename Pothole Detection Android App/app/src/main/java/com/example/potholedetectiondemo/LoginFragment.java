package com.example.potholedetectiondemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.potholedetectiondemo.Network.APIInterface;
import com.example.potholedetectiondemo.Network.RetrofitService;



import com.example.potholedetectiondemo.R;
import com.example.potholedetectiondemo.model.Request.LoginRequestModel;
import com.google.android.material.textfield.TextInputLayout;


public class LoginFragment extends Fragment {

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtLogin;
    private TextView mTvRegister;
    private APIInterface apiInterface;
    private static final String TAG = "loginFragment";


    private SharedPreferences mSharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_login, container, false);
        apiInterface = RetrofitService.getRetrofitInstance().create(APIInterface.class);
        initViews(view);
        initSharedPreferences();
        return view;
    }

    private void initViews(View v) {
        mEtEmail= v.findViewById(R.id.etEmailID);
        mEtPassword= v.findViewById(R.id.etPassword);
        mBtLogin= v.findViewById(R.id.btnLogin);
        mTvRegister = v.findViewById(R.id.btnRegister);

        mBtLogin.setOnClickListener(view -> login());
        mTvRegister.setOnClickListener(view -> goToRegister());

    }

    private void goToRegister() {

        Navigation.findNavController((MainActivity)getContext(),R.id.nav_host_fragment).
                navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment() );

    }

    private void login() {

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        LoginRequestModel lrm = new LoginRequestModel();
        lrm.setEmailID(email);
        lrm.setPassword(password);

        LoginRequestModel[] loginRequestModel= new LoginRequestModel[1];
        loginRequestModel[0]=lrm;



        apiInterface.comLoginData(loginRequestModel).enqueue(new Callback<LoginRequestModel>() {
            @Override
            public void onResponse(Call<LoginRequestModel> call, Response<LoginRequestModel> response) {
                if (response.isSuccessful()){
                    Log.v(TAG,"\nsuccessful\n\n"+ response.message() );
                }
            }

            @Override
            public void onFailure(Call<LoginRequestModel> call, Throwable t) {
                Log.v(TAG,"\nfailure\n\n"+ t.getMessage() );
            }
        });



    }


    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

}
