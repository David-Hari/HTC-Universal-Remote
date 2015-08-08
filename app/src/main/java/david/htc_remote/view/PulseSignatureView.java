package david.htc_remote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import david.htc_remote.ir.IrCommand;


/**
 * View that displays the on/off pulses of an IR command as vertical bars.
 */
public class PulseSignatureView extends View {
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final int FOREGROUND_COLOR = Color.RED;
    private IrCommand model;
    private Paint brush;

    public PulseSignatureView(Context context) {
        super(context);
        init();
    }

    public PulseSignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulseSignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.brush = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.brush.setColor(FOREGROUND_COLOR);
    }

    public void setModel(IrCommand model) {
        this.model = model;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);

        float widthScale = (float)this.getWidth() / (float)this.model.getPulseLength();
        float left = 0, right;
        int[] frame = this.model.getFrame();
        for (int i = 0; i < frame.length; i++) {
            right = left + (frame[i] * widthScale);
            if (i % 2 == 0) {
                // Only draw even pulses, the others are "off" and will just show the background colour
                canvas.drawRect(left, 0.0f, right, (float)this.getHeight(), this.brush);
            }
            left = right;
        }
    }
}
