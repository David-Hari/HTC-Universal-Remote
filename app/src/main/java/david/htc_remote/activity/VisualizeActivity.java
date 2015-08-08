package david.htc_remote.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
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


/**
 * Activity to display graphical representations of the IR commands.
 */
public class VisualizeActivity extends Activity implements Handler.Callback {
    private static final int PULSE_VIEW_HEIGHT = 120;
    private static final int PULSE_VIEW_MARGIN = 15;
    private CIRControl control;

    @Bind(R.id.mainLayout) LinearLayout mainLayout;
    @Bind(R.id.learnButton) Button learnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        ButterKnife.bind(this);

        Handler handler = new Handler(Looper.getMainLooper(), this);
        this.control = new CIRControl(getApplicationContext(), handler);
        this.control.start();
    }

    /**
     * Starts learning command.
     */
    @OnClick(R.id.learnButton)
    public void learnCommand() {
        this.control.learnIRCmd(10);
        this.learnButton.setEnabled(false);
    }

    /**
     * Handle messages from IR controller.
     * @param message
     * @return - True if the message was handled, false otherwise
     */
    @Override
    public boolean handleMessage(Message message) {
        this.learnButton.setEnabled(true);
        switch (message.what) {
            case CIRControl.MSG_RET_LEARN_IR:
                HtcIrData commandData = (HtcIrData)message.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (commandData != null) {
                    this.addPulseSignatureView(new IrCommand(commandData));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Creates a new pulse signature view with the given command, and adds it to the main view.
     * @param command
     */
    private void addPulseSignatureView(IrCommand command) {
        PulseSignatureView view = new PulseSignatureView(this);
        view.setModel(command);
        LinearLayout.LayoutParams viewLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PULSE_VIEW_HEIGHT);
        viewLayout.setMargins(0, PULSE_VIEW_MARGIN, 0, PULSE_VIEW_MARGIN);
        view.setLayoutParams(viewLayout);
        this.mainLayout.addView(view);
    }
}
