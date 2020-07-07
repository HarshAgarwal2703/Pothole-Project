package com.example.potholedetectiondemo.TAB;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.potholedetectiondemo.LoginFragmentDirections;
import com.example.potholedetectiondemo.MainActivity;
import com.example.potholedetectiondemo.R;

public class ProfileFragment extends Fragment {

    private TextView mName;
    private TextView mNoOfIssues;
    private TextView mContactNo;
    private Button mbtnMyIssues;
    private com.google.android.material.floatingactionbutton.FloatingActionButton mbtnExitToLogin;
    private static final String TAG = "Profile Fragment";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        mName= v.findViewById(R.id.uname);
        mNoOfIssues= v.findViewById(R.id.uissues);
        mContactNo= v.findViewById(R.id.ucontact);
        mbtnExitToLogin= v.findViewById(R.id.mExitToLogin);
        mbtnMyIssues= v.findViewById(R.id.mMyIssues);

        mbtnExitToLogin.setOnClickListener(view -> exitToLogin());
    }

    private void exitToLogin() {
        Navigation.findNavController((MainActivity)getContext(),R.id.nav_host_fragment)
                .navigate(R.id.action_tabFragment_to_loginFragment);
    }


}
