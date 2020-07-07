package com.example.potholedetectiondemo.TAB;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.potholedetectiondemo.Network.APIInterface;
import com.example.potholedetectiondemo.Network.RetrofitService;
import com.example.potholedetectiondemo.R;
import com.example.potholedetectiondemo.model.Request.PostRequestModel;
import com.example.potholedetectiondemo.model.parseBitmap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class uploadFragment extends Fragment {

    private static final String TAG = "uploadFragment";
    private EditText title;
    private ImageView img_view;
    private EditText description;
    private String latitude;
    private String longitude;
    private String timestamp;
    private String address;
    private TextView Area;
    private APIInterface apiInterface;
    private FloatingActionButton uploadFAB;
    private parseBitmap imagePath;

    private String filePath;
    private MultipartBody.Part image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        img_view = view.findViewById(R.id.Display_Image);
        description = view.findViewById(R.id.DescriptionEditText);
        //TextView location = view.findViewById(R.id.loc);
        Area = view.findViewById(R.id.AreaText);
        apiInterface = RetrofitService.getRetrofitInstance().create(APIInterface.class);
        uploadFAB = view.findViewById(R.id.floatingActionButton);
        imagePath = uploadFragmentArgs.fromBundle(getArguments()).getImageBitmap();
        latitude = uploadFragmentArgs.fromBundle(getArguments()).getLatitude();
        longitude = uploadFragmentArgs.fromBundle(getArguments()).getLongitude();
        timestamp =parseTime();

        //Bitmap bitmapimage = BitmapFactory.decodeFile(imagePath);
        address = getCompleteAddressString(Double.parseDouble(latitude), Double.parseDouble(longitude));
        Area.setText(address);
        img_view.setImageBitmap(imagePath.getBitmap());
        title = view.findViewById(R.id.TitleEditText);
        title.setText("Title");
        description.setText("This is the Description");


        uploadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();

            }
        });
        return view;
    }

    private void upload() {



        filePath= tempFileImage(getContext(),imagePath.getBitmap(),"image");
        File file1 = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("image", file1.getName(), requestBody);
        final RequestBody title1 = RequestBody.create(MediaType.parse("multipart/form-data"), title.getText().toString());
        final RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), description.getText().toString());
//        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), latitude);
//        RequestBody longi = RequestBody.create(MediaType.parse("multipart/form-data"), longitude);
        final RequestBody cre = RequestBody.create(MediaType.parse("multipart/form-data"), timestamp);
        final RequestBody pub = RequestBody.create(MediaType.parse("multipart/form-data"), timestamp);

        Log.e(TAG, "upload: " + timestamp);
        Log.e(TAG, "upload: " + cre.toString());

        apiInterface.uploadImage( body,title1,desc,
                RequestBody.create(MediaType.parse("multipart/form-data"),"2"),
                RequestBody.create(MediaType.parse("multipart/form-data"),latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"),longitude),
                RequestBody.create(MediaType.parse("multipart/form-data"),timestamp),
                RequestBody.create(MediaType.parse("multipart/form-data"),timestamp),
                RequestBody.create(MediaType.parse("multipart/form-data"), ""),
                RequestBody.create(MediaType.parse("multipart/form-data"),"0"),
                RequestBody.create(MediaType.parse("multipart/form-data"),address))
                .enqueue(new Callback<PostRequestModel>() {
            @Override
            public void onResponse(Call<PostRequestModel> call, Response<PostRequestModel> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                    Toast.makeText(getContext(),"Successfull",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.tabFragment);
                }
            }

            @Override
            public void onFailure(Call<PostRequestModel> call, Throwable t) {
                Log.e(TAG, "onResponse: " + t.getMessage());

            }
        });
    }
    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Log.e(TAG, "getCompleteAddressString: " + LATITUDE);
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("Current loction address", strReturnedAddress.toString());
            } else {
                Log.e("Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Current loction address", "Cannot get Address!");
        }
        return strAdd;
    }

    public static String parseTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());

    }
}

