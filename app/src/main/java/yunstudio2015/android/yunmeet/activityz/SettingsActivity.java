package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvLogOut;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();

        queue = Volley.newRequestQueue(SettingsActivity.this.getApplicationContext());

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> map = new HashMap<String, String>();
                map.put("token",UtilsFunctions.getToken(SettingsActivity.this));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL + "User/logout", new JSONObject(map), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equals("0")){

                                UtilsFunctions.setID(SettingsActivity.this,null);
                                UtilsFunctions.setToken(SettingsActivity.this,null);

                                Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(i);
                            } else {
                                Toast.makeText(SettingsActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request);


            }
        });
    }

    private void initViews() {
        tvLogOut = (TextView) findViewById(R.id.settings_log_out);
    }
}
