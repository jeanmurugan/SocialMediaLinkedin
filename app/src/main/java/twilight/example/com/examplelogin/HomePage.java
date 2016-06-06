package twilight.example.com.examplelogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class HomePage extends Activity {

    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host+ "/v1/people/~:(id,email-address,formatted-name,phone-numbers,picture-urls::(original))";

    //private static final String url="http://api.linkedin.com/v1/people/~/connections:(id,first-name,last-name,public-profile-url,picture-url)";

    private ProgressDialog progress;
    private TextView user_name, user_email,user_id;
    private ImageView profile_picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize the progressbar
        progress= new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        user_id=(TextView) findViewById(R.id.user_id);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        linkededinApiHelper();

    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(HomePage.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Home page Exception :"+e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public  void  showResult(JSONObject response){

        System.out.println("Response :"+response);

        try {
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());
            user_id.setText(response.getString("id"));
            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);
            //System.out.println(response.getString("id"));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Home page Exception :"+e.getMessage());
        }
    }

}
