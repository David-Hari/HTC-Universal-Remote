package david.htc_remote;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;


public class MainActivity extends Activity implements Handler.Callback {
    private static final String TAG = "IrManager";
    private static final int LEARN_TIMEOUT = 10; // seconds
    private CIRControl control;
    private Handler handler;
    private SparseArray<HtcIrData> commands = new SparseArray<>();
    private Map<UUID, Button> waitingCommands = new HashMap<>();

    @Bind(R.id.statusLabel) TextView statusLabel;

    /**
     * TODO:
     *  - Store IR codes (perhaps in a database)
     *  - Find better way of updating UI from manager/handler
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.handler = new Handler(Looper.getMainLooper(), this);
        this.control = new CIRControl(getApplicationContext(), this.handler);
        this.control.start();
    }

    public void onButtonClick(View view) {
        Button button = (Button)view;
        HtcIrData commandData = commands.get(button.getId(), null);

        if (commandData == null) {
            this.learnCommand(button, LEARN_TIMEOUT);
        }
        else {
            this.transmit(commandData);
        }
    }

    public void transmit(final HtcIrData command) {
        this.statusLabel.setText("Transmitting IR command");
        this.handler.post(new Runnable() {
            public void run() {
                control.transmitIRCmd(command, true);
            }
        });
    }

    public void learnCommand(Button button, int timeout) {
        this.statusLabel.setText("");
        UUID queueId = this.control.learnIRCmd(timeout);
        if (queueId != null) {
            this.statusLabel.setText("Learning IR command");
            this.waitingCommands.put(queueId, button);
        }
    }

    public void storeNewCommand(Button button, HtcIrData command) {
        this.commands.put(button.getId(), command);
        button.setBackgroundColor(0xFFBADAF4);
    }

    @Override
    public boolean handleMessage(Message msg) {
        UUID resultId;
        String status = "";
        switch (msg.what) {
            case CIRControl.MSG_RET_LEARN_IR:
                resultId = (UUID)msg.getData().getSerializable(CIRControl.KEY_RESULT_ID);
                Log.i(TAG, "Receive IR Returned UUID: " + resultId);

                HtcIrData learntCommand = (HtcIrData)msg.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (learntCommand != null) {
                    Button button = this.waitingCommands.get(resultId);
                    if (button != null) {
                        this.storeNewCommand(button, learntCommand);
                    }
                    else {
                        status = "Learn IR Error: No button found for " + resultId;
                    }
                }
                else {
                    status = "Learn IR Error: " + Errors.stringFor(msg.arg1);
                }
                break;
            case CIRControl.MSG_RET_TRANSMIT_IR:
                resultId = (UUID)msg.getData().getSerializable(CIRControl.KEY_RESULT_ID);
                Log.i(TAG, "Send IR Returned UUID: "+resultId);
                status = "Send IR Error: " + Errors.stringFor(msg.arg1);
                break;
            case CIRControl.MSG_RET_CANCEL:
                status = "Cancel Error: " + Errors.stringFor(msg.arg1);
                break;
            default:
                return false;
        }
        this.statusLabel.setText(status);
        return true;
    }
}
