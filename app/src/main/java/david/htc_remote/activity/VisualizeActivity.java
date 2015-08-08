package david.htc_remote.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;

import david.htc_remote.R;
import david.htc_remote.ir.IrCommand;
import david.htc_remote.view.PulseSignatureView;


public class VisualizeActivity extends Activity implements Handler.Callback {
    private static final int PULSE_VIEW_HEIGHT = 120;
    private static final int PULSE_VIEW_MARGIN = 15;
    private CIRControl control;

    @Bind(R.id.mainLayout) LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        ButterKnife.bind(this);

        Handler handler = new Handler(Looper.getMainLooper(), this);
        this.control = new CIRControl(getApplicationContext(), handler);
        this.control.start();
    }

    @OnClick(R.id.learnButton)
    public void learnCommand() {
        this.control.learnIRCmd(10);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CIRControl.MSG_RET_LEARN_IR:
                HtcIrData commandData = (HtcIrData)msg.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (commandData != null) {
                    this.addPulseSignatureView(new IrCommand(commandData));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void addPulseSignatureView(IrCommand command) {
        PulseSignatureView view = new PulseSignatureView(this);
        view.setModel(command);
        LinearLayout.LayoutParams viewLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PULSE_VIEW_HEIGHT);
        viewLayout.setMargins(0, PULSE_VIEW_MARGIN, 0, PULSE_VIEW_MARGIN);
        view.setLayoutParams(viewLayout);
        mainLayout.addView(view);
    }
}
