package io.appgain.sdk.Service;




import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sotra on 5/10/2017.
 */
public interface UploadFileService {


    @Multipart
    @POST("B2B/admin_panel/api/uploadImages")
    Call<ResponseBody> upload(
            @Part ArrayList<MultipartBody.Part> files
    );


}
