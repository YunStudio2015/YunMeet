package yunstudio2015.android.yunmeet.activityz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.ImageLoadOptions;

public class RestingActivity extends AppCompatActivity {


    @Bind(R.id.ivv)
    ImageView ivv;

    private String ulr = "http://attach.bbs.miui.com/forum/201308/28/195800loquaugdcud5dhwa.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resting);
        ButterKnife.bind(this);
        ImageLoader.getInstance().displayImage(ulr, ivv, ImageLoadOptions.getDisplayImageOptions(this));
        // add a button in the bottom that will get a date.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
