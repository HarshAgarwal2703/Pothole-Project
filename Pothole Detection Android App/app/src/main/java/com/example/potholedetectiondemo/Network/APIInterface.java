package com.example.potholedetectiondemo.Network;

import com.example.potholedetectiondemo.model.Request.LoginRequestModel;
import com.example.potholedetectiondemo.model.Request.PostRequestModel;
import com.example.potholedetectiondemo.model.Request.RegisterRequestModel;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * User: Aman
 * Date: 29-12-2019
 * Time: 01:32 PM
 */
public interface APIInterface {

//    @POST(value = "mobile")
//    Call<LoginResponseModel> mobileLogin(
//            @Header("@") String header,
//            @Body LoginRequestModel body
//    );
//
//    @GET(value = "userdashboard")
//    Call<UserDashboardResponseModel> getUserCategory();
//
//    @GET("getsubcategories/{id}")
//    Call<SubcategoryModel> getSubCategory(@Path("id") int id);
//
//    @POST(value = "storeuserlocation")
//    Call<StoreLocationResponseModel> storeLocation(@Body StoreLocationRequestModel storeLocationRequestModel);

    @Multipart
    @POST(value = "uploadimagepost/")
    Call<PostRequestModel> uploadImage(@Part MultipartBody.Part image,
                                       @Part("title") RequestBody title,
                                       @Part("Description") RequestBody Description,
                                       @Part("app_user_field") RequestBody app_user_field,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude,
                                       @Part("created_date") RequestBody created_date,
                                       @Part("published_date") RequestBody published_date,
                                       @Part("status") RequestBody status,
                                       @Part("vote_count") RequestBody vote_count,
                                       @Part("address") RequestBody address);



    @POST(value = "uploadaccpost")
    Call<PostRequestModel> uploadAccData(@Body PostRequestModel postRequestModel);


//    @Multipart
//    @POST(value="userimage")
//    Call<UserImageResponseModel> userImage(@Part("user_details_id") String id,
//                                           @Part MultipartBody.Part file);
//    @GET(value = "gettimeslots")
//    Call<GetTimeSlotResponseModel> getTimeSlots();
//
//    @Multipart
//    @POST(value = "workerregister")
//    Call<VendorRegisterResponseModel> vendorRegister(@Part("worker_firstname") RequestBody first_name,
//                                                     @Part("worker_lastname") RequestBody last_name,
//                                                     @Part("worker_dob") RequestBody email,
//                                                     @Part("worker_mobile") RequestBody mobile,
//                                                     @Part MultipartBody.Part file1,
//                                                     @Part MultipartBody.Part file2,
//                                                     @Part MultipartBody.Part file3);
//}

    @POST(value= "appuser/login/")
    Call<LoginRequestModel> comLoginData(@Body LoginRequestModel[] loginRequestModel);

    @POST(value= "appuser/register/")
    Call<RegisterRequestModel> comRegisterData(@Body RegisterRequestModel[] registerRequestModel);
}