package david.htc_remote;

import android.util.SparseArray;
import com.htc.circontrol.CIRControl;


public class Errors {
    public static final SparseArray<String> map = new SparseArray<>();

    static {
        map.put(CIRControl.ERR_NONE, "ERR_NONE");
        map.put(CIRControl.ERR_UNKNOWN, "ERR_UNKNOWN");
        map.put(CIRControl.ERR_CMD_FAILED, "ERR_CMD_FAILED");
        map.put(CIRControl.ERR_HW_BUSY, "ERR_HW_BUSY");
        map.put(CIRControl.ERR_IO_ERROR, "ERR_IO_ERROR");
        map.put(CIRControl.ERR_CMD_DROPPED, "ERR_CMD_DROPPED");
        map.put(CIRControl.ERR_TTY_SETUP_ERROR, "ERR_TTY_SETUP_ERROR");
        map.put(CIRControl.ERR_TTY_WRITE_ERROR, "ERR_TTY_WRITE_ERROR");
        map.put(CIRControl.ERR_TTY_READ_ERROR, "ERR_TTY_READ_ERROR");
        map.put(CIRControl.ERR_INVALID_VALUE, "ERR_INVALID_VALUE");
        map.put(CIRControl.ERR_LEARNING_TIMEOUT, "ERR_LEARNING_TIMEOUT");
        map.put(CIRControl.ERR_CANCEL_FAIL, "ERR_CANCEL_FAIL");
        map.put(CIRControl.ERR_CHECKSUM_ERROR, "ERR_CHECKSUM_ERROR");
        map.put(CIRControl.ERR_OUT_OF_FREQ, "ERR_OUT_OF_FREQ");
        map.put(CIRControl.ERR_CANCEL, "ERR_CANCEL");
        map.put(CIRControl.ERR_PULSE_ERROR, "ERR_PULSE_ERROR");
        map.put(CIRControl.ERR_PIPE_ERROR, "ERR_PIPE_ERROR");
        map.put(CIRControl.ERR_OBJ_ERROR, "ERR_OBJ_ERROR");
    }
}
