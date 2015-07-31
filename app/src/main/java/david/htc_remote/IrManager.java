package david.htc_remote;

import java.util.UUID;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;


public class IrManager {
    private CIRControl control;
    private Handler handler;

    public IrManager(Context context) {
        this.handler = new IrHandler(Looper.getMainLooper());
        this.control = new CIRControl(context, this.handler);
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

    public void start() {
        this.control.start();
    }

    public void stop() {
        this.control.stop();
    }

    public boolean isStarted() {
        return this.control.isStarted();
    }

    public UUID cancelCommand() {
        return this.control.cancelCommand();
    }

    public UUID discardCommand(UUID uuid) {
        return this.control.discardCommand(uuid);
    }
}
