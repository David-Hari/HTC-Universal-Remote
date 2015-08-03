package david.htc_remote;

import android.util.SparseArray;
import com.htc.circontrol.CIRControl;


public class Errors {
    public static final SparseArray<String> map = new SparseArray<>();

    static {
        map.put(CIRControl.ERR_NONE, "None");
        map.put(CIRControl.ERR_UNKNOWN, "Unknown");
        map.put(CIRControl.ERR_CMD_FAILED, "Command failed");
        map.put(CIRControl.ERR_HW_BUSY, "Hardware busy");
        map.put(CIRControl.ERR_IO_ERROR, "IO error");
        map.put(CIRControl.ERR_CMD_DROPPED, "Command dropped");
        map.put(CIRControl.ERR_TTY_SETUP_ERROR, "TTY setup error");
        map.put(CIRControl.ERR_TTY_WRITE_ERROR, "TTY write error");
        map.put(CIRControl.ERR_TTY_READ_ERROR, "TTY read error");
        map.put(CIRControl.ERR_INVALID_VALUE, "Invalid value");
        map.put(CIRControl.ERR_LEARNING_TIMEOUT, "Learning timeout");
        map.put(CIRControl.ERR_CANCEL_FAIL, "Cancel fail");
        map.put(CIRControl.ERR_CHECKSUM_ERROR, "Checksum error");
        map.put(CIRControl.ERR_OUT_OF_FREQ, "Out of frequency");
        map.put(CIRControl.ERR_CANCEL, "Cancel");
        map.put(CIRControl.ERR_PULSE_ERROR, "Pulse error");
        map.put(CIRControl.ERR_PIPE_ERROR, "Pipe error");
        map.put(CIRControl.ERR_OBJ_ERROR, "Obj error");
    }

    static String stringFor(int code) {
        return map.get(code, "Unknown error");
    }
}
