package david.htc_remote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class PulseSignatureView extends View {

    public PulseSignatureView(Context context) {
        super(context);
    }

    public PulseSignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PulseSignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 250, 120, 56);

        RectF area = new RectF(0, 0, this.getWidth(), this.getHeight());
        Paint brush = new Paint();
        brush.setARGB(255, 20, 60, 200);
        canvas.drawArc(area, 135, 270, true, brush);
    }
}
