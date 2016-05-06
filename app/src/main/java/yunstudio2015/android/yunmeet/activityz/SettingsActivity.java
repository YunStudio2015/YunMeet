package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsFunctions.setID(SettingsActivity.this,null);
                UtilsFunctions.setToken(SettingsActivity.this,"-1");

                // @TODO 向服务器发送请求，注销token
                Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void initViews() {
        tvLogOut = (TextView) findViewById(R.id.settings_log_out);
    }
}
