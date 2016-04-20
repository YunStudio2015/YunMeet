package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyft.android.scissors.CropView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import yunstudio2015.android.yunmeet.R;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class CroppingActivity extends AppCompatActivity {

    @Bind(R.id.crop_view)
    CropView crop_view;


    @Bind({ R.id.crop_fab})
    List<View> buttons;

    CompositeSubscription subscriptions = new CompositeSubscription();


    private static final int CODE_CROP_REQUEST = 2;
    private float pivotX;
    private float pivotY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropping);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);
        // get the image uri and load it into the view.
        String imagePath = getIntent().getStringExtra("data");
        Uri uri = getImageContentUri(this, new File(imagePath));
        crop_view.setImageURI(null);
        crop_view.extensions()
                .load(uri);
        updateButtons();
        pivotX = 100;
        pivotY = 100;
    }

    @OnClick(R.id.rotate_left)
    public void rotate_left () {
        Matrix matrix = new Matrix();
        crop_view.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) 90, pivotX, pivotY);
        crop_view.setImageMatrix(matrix);
        Toast.makeText(this, "90", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rotate_right)
    public void rotate_right () {
        Matrix matrix = new Matrix();
        crop_view.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) -90, pivotX, pivotY);
        crop_view.setImageMatrix(matrix);
        Toast.makeText(this, "-90", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.crop_fab)
    public void onCropClicked() {
        String filename = ""+ SystemClock.currentThreadTimeMillis();
        final File croppedFile = new File(getCacheDir(), filename+".jpg");

        Observable<Void> onSave = Observable.from(crop_view.extensions()
                .crop()
                .quality(100)
                .format(JPEG)
                .into(croppedFile))
                .subscribeOn(io())
                .observeOn(mainThread());

        subscriptions.add(onSave
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void nothing) {
                        Log.d("croppingact", "at --- "+croppedFile.toString());
                        // finish activity setting result and sending data back to the user.
                        // jump to an activity where to show the image.
                        Intent intent = new Intent();
                        intent.putExtra("path", croppedFile.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }));
    }

    private void updateButtons() {
        ButterKnife.apply(buttons, VISIBILITY, View.VISIBLE);
    }

    static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override
        public void set(final View view, final Integer visibility, int index) {
            view.setVisibility(visibility);
        }
    };

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT  != Build.VERSION_CODES.KITKAT)
            return;
        setTranslucentStatus(activity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
