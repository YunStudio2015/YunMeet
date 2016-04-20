package yunstudio2015.android.yunmeet.customviewz;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ulrich on 4/6/16.
 */
public class GridLayoutManAger extends GridLayoutManager {


    public GridLayoutManAger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GridLayoutManAger(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridLayoutManAger(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

  /*  @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    // compute the height and width of the ...
final int widthMode = View.MeasureSpec.getMode(widthSpec);
    final int heightMode = View.MeasureSpec.getMode(heightSpec);
    final int widthSize = View.MeasureSpec.getSize(widthSpec);
    final int heightSize = View.MeasureSpec.getSize(heightSpec);
    int width = 0;
    int height = 0;
        int[] mMeasuredDimension = {widthSpec, heightSpec};

    for (int i = 0; i < getItemCount(); i++) {
        measureScrapChild(recycler, i,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                mMeasuredDimension);

        if (getOrientation() == HORIZONTAL) {
            width = width + mMeasuredDimension[0];
            if (i == 0) {
                height = mMeasuredDimension[1];
            }
        } else {
            height = height + mMeasuredDimension[1];
            if (i == 0) {
                width = mMeasuredDimension[0];
            }
        }
    }
    switch (widthMode) {
        case View.MeasureSpec.EXACTLY:
            width = widthSize;
        case View.MeasureSpec.AT_MOST:
        case View.MeasureSpec.UNSPECIFIED:
    }

    switch (heightMode) {
        case View.MeasureSpec.EXACTLY:
            height = heightSize;
        case View.MeasureSpec.AT_MOST:
        case View.MeasureSpec.UNSPECIFIED:
    }

    setMeasuredDimension(width, height);
}
*/

}
