package david.htc_remote;

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

import butterknife.ButterKnife;
import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;


public class MainActivity extends Activity implements Handler.Callback {
    private static final String TAG = "IrManager";
    private static final int LEARN_TIMEOUT = 10; // seconds
    private CIRControl control;
    private Handler handler;
    private SparseArray<HtcIrData> commands = new SparseArray<>();

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

        if (commands.indexOfKey(button.getId()) < 0) {
            // Learn command and set it to this button
            this.learnCommand(LEARN_TIMEOUT);
            //commands.put(button.getId(), new HtcIrData());
            //button.setBackgroundColor(0xFFBADAF4);
        }
        else {
            // Send button's command
        }
    }

    public void transmit(final HtcIrData command) {
        this.handler.post(new Runnable() {
            public void run() {
                control.transmitIRCmd(command, true);
            }
        });
    }

    public UUID learnCommand(int timeout) {
        return this.control.learnIRCmd(timeout);
    }

    @Override
    public boolean handleMessage(Message msg) {
        UUID resultId;
        String status = null;
        switch (msg.what) {
            case CIRControl.MSG_RET_LEARN_IR:
                resultId = (UUID)msg.getData().getSerializable(CIRControl.KEY_RESULT_ID);
                Log.i(TAG, "Receive IR Returned UUID: " + resultId);

                HtcIrData learntKey = (HtcIrData)msg.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (learntKey == null) {
                    switch (msg.arg1) {
                        case CIRControl.ERR_LEARNING_TIMEOUT:
                            status = "Learn IR Error: ERR_LEARNING_TIMEOUT";
                            break;
                        case CIRControl.ERR_PULSE_ERROR:
                            //CIR receives IR data but data is unusable.
                            //The common error is caused by user he/she does not align the phone's CIR receiver
                            // with CIR transmitter of plastic remote.
                            status = "Learn IR Error: ERR_PULSE_ERROR";
                            break;
                        case CIRControl.ERR_OUT_OF_FREQ:
                            //This error is to warn user that the device is not supported or
                            // the phone's CIR receiver does not align with CIR transmitter of the device.
                            status = "Learn IR Error: ERR_OUT_OF_FREQ";
                            break;
                        case CIRControl.ERR_IO_ERROR:
                            //CIR hardware component is busy in doing early CIR activity.
                            status = "Learn IR Error: ERR_IO_ERROR";
                            break;
                        default:
                            status = "";
                            break;
                    }
                }
                break;
            case CIRControl.MSG_RET_TRANSMIT_IR:
                resultId = (UUID)msg.getData().getSerializable(CIRControl.KEY_RESULT_ID);
                Log.i(TAG, "Send IR Returned UUID: "+resultId);
                switch (msg.arg1) {
                    case CIRControl.ERR_IO_ERROR:
                        //CIR hardware component is busy in doing early CIR command.
                        status = "Send IR Error: ERR_IO_ERROR";
                        break;
                    case CIRControl.ERR_INVALID_VALUE:
                        status = "Send IR Error: ERR_INVALID_VALUE";
                        break;
                    case CIRControl.ERR_CMD_DROPPED:
                        //SDK might be too busy to send IR key, developer can try later, or send IR key with non-droppable setting
                        status = "Send IR Error: ERR_CMD_DROPPED";
                        break;
                    default:
                        status = "";
                        break;
                }
                break;
            case CIRControl.MSG_RET_CANCEL:
                switch (msg.arg1) {
                    case CIRControl.ERR_IO_ERROR:
                        //CIR hardware component is busy in doing early CIR command.
                        status = "Cancel Error: ERR_IO_ERROR";
                        break;
                    case CIRControl.ERR_CANCEL_FAIL:
                        //CIR hardware component is busy in doing early CIR command.
                        status = "Cancel Error: ERR_CANCEL_FAIL";
                        break;
                    default:
                        status = "";
                        break;
                }
                break;
            default:
                return false;
        }
        if (status != null) {
            Log.i(TAG, status);
        }
        return true;
    }
}
