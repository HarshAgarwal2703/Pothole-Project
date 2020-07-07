package com.example.potholedetectiondemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.potholedetectiondemo.model.Request.RegisterRequestModel;


public class RegisterFragment extends Fragment {

    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtEmailID;
    private EditText mEtContactNumber;
    private EditText mEtPassword;
    private static final String TAG = "registerFragment";
    private Button mBtRegister;

    private SharedPreferences mSharedPreferences;

    private APIInterface apiInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        initSharedPreferences();
        return view;
    }

    private void initViews(View v ) {
        mBtRegister= v.findViewById(R.id.btnRegister); //btnRegister
        mEtFirstName=v.findViewById(R.id.etFirstName);
        mEtLastName= v.findViewById(R.id.etLastName);
        mEtContactNumber= v.findViewById(R.id.etContactNumber);
        mEtEmailID= v.findViewById(R.id.etEmailID);
        mEtPassword= v.findViewById(R.id.etPassword);

        mBtRegister.setOnClickListener(view -> register());

    }

    private void register() {
        String fname= mEtFirstName.getText().toString();
        String lname= mEtLastName.getText().toString();
        String contactNo= mEtContactNumber.getText().toString();
        String email = mEtEmailID.getText().toString();
        String password = mEtPassword.getText().toString();

        RegisterRequestModel rrm = new RegisterRequestModel();
        rrm.setFirst_name(fname);
        rrm.setLast_name(lname);
        rrm.setPhone_number(contactNo);
        rrm.setEmail_id(email);
        rrm.setPassword(password);

        RegisterRequestModel[] registerRequestModel= new RegisterRequestModel[1];
        registerRequestModel[0]=rrm;

        Log.e(TAG, "register: " + registerRequestModel[0].getPassword());
        apiInterface.comRegisterData(registerRequestModel).enqueue(new Callback<RegisterRequestModel>() {
            @Override
            public void onResponse(Call<RegisterRequestModel> call, Response<RegisterRequestModel> response) {
                if (response.isSuccessful()){
                    Log.v(TAG,"\nsuccessful\n\n"+ response.message() );
                }
            }

            @Override
            public void onFailure(Call<RegisterRequestModel> call, Throwable t) {
                Log.v(TAG,"\nfailure\n\n"+ t.getMessage() );
            }
        });

    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}
