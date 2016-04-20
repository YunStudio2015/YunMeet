package yunstudio2015.android.yunmeet.customviewz;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ulrich on 2/23/2016.
 */
public class HorizontalSwipeRefreshLayout extends SwipeRefreshLayout {
    public HorizontalSwipeRefreshLayout(Context context) {
        super(context);
    }

    public HorizontalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
