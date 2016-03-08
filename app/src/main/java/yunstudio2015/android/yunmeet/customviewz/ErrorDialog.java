package yunstudio2015.android.yunmeet.customviewz;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import yunstudio2015.android.yunmeet.R;


/**
 * 举措中Dialog
 *
 */


public class ErrorDialog extends Dialog {

    private TextView tips_loading_msg;

    private String message = null;

    public ErrorDialog(Context context) {
        super(context);
        message = getContext().getResources().getString(R.string.data_failure);
    }

    public ErrorDialog(Context context, String message) {
        super(context);
        this.message = message;
        this.setCancelable(false);
    }

    public ErrorDialog(Context context, int theme, String message) {
        super(context, theme);
        this.message = message;
        this.setCancelable(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.view_tips_error);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);
    }

    public void setText(String message) {
        this.message = message;
        tips_loading_msg.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }

}
