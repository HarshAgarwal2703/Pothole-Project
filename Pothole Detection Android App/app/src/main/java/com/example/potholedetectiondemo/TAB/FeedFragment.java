package com.example.potholedetectiondemo.TAB;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.potholedetectiondemo.R;
import com.example.potholedetectiondemo.model.IssueModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private static final String TAG = "TAB_1";
    int radius = 50;
    private double latitude;
    private double longitude;
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private List<IssueModel> IssueList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btnFilter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvNoInternet;
    private ProgressBar progressBar;
    private TextView textViewFilter;

    public FeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        IssueList = new ArrayList<IssueModel>();
        IssueModel a = new IssueModel(1, "100", "100", 1, "12/01/20", false, "HI", "BYE", "Finished");
        IssueList.add(a);
        recyclerView = view.findViewById(R.id.RecyclerTab1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearVertical);

        feedAdapter = new FeedAdapter(getContext(), IssueList);
        recyclerView.setAdapter(feedAdapter);
        getObjectArray();

        swipeRefreshLayout = view.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getSampleData();
                getObjectArray();
//                swipeRefreshLayout.setRefreshing(false);

            }
        });
        return view;
    }

    public void getObjectArray() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        FeedFragment.this.latitude = location.getLatitude();
                        FeedFragment.this.longitude = location.getLongitude();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("latitude", latitude);
                            jsonObject.put("longitude", longitude);
                            jsonObject.put("radius", radius);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        getData(jsonArray);


                    }
                });

    }

    private void getData(JSONArray jsonArray) {
        swipeRefreshLayout.setRefreshing(true);

        IssueList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, "http://bhavyaahir1729.pythonanywhere.com/api/filterimagepost/", jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        swipeRefreshLayout.setRefreshing(false);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Log.e(TAG, "onResponse: " + response.getJSONObject(i));
                                JSONObject jsonObject = response.getJSONObject(i);
                                jsonObject.put("image", "http://bhavyaahir1729.pythonanywhere.com/" + jsonObject.get("image"));
                                IssueModel issueModelClass = new Gson().fromJson(jsonObject.toString(), IssueModel.class);
                                Log.e(TAG, "onResponse: " + issueModelClass);

                                IssueList.add(issueModelClass);


                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                        }
                        feedAdapter.notifyDataSetChanged();

//
//                    String Url = String.format(AppConstants.UPVOTE_LIST + "2" + "/", "2");
//                    final ArrayList<Integer> postArrayList = new ArrayList<>();
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            @Override
//                            public void onResponse (String response){
//                                Log.d(TAG, "onResponse: " + response);
//                                try {
//                                    JSONArray t = new JSONArray(response);
//                                    for (int i = 0; i < t.length(); i++) {
//                                        JSONObject object = t.getJSONObject(i);
//                                        postArrayList.add(object.getInt("post_id"));
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                for (IssueModel IssueObject : IssueList) {
//                                    for (int i : postArrayList) {
//                                        if (IssueObject.getId() == i) {
//                                            IssueObject.setCheckLiked(true);
//                                        }
//                                    }
//                                }
//                            }
//
//                            feedAdapter.notifyDataSetChanged();
//
//                        }
//                    }, new com.android.volley.Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, "onErrorResponse: " + error.getMessage() );
//                        }});
//
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                    stringRequest.setShouldCache(false);
//                    requestQueue.add(stringRequest);
//                    feedAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley ", error.toString());

            }
        });
        feedAdapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }
}
