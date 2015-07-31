package david.htc_remote;

import java.util.Arrays;
import java.util.UUID;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;


public class IrHandler extends Handler {
    private static final String TAG = "IrHandler";

    public IrHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        UUID resultId;
        String status = null;
        switch (msg.what) {
            case CIRControl.MSG_RET_LEARN_IR:
                resultId = (UUID)msg.getData().getSerializable(CIRControl.KEY_RESULT_ID);
                Log.i(TAG, "Receive IR Returned UUID: " + resultId);

                HtcIrData learntKey = (HtcIrData)msg.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (learntKey != null) {
                    status = String.format("Repeat: %d, Freq: %d, Frame length: %d, Frame = %s",
                                learntKey.getRepeatCount(), learntKey.getFrequency(),
                                learntKey.getFrame().length, Arrays.toString(learntKey.getFrame()));
                }
                else {
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
                super.handleMessage(msg);
        }
        if (status != null) {
            Log.i(TAG, status);
        }
    }
}
