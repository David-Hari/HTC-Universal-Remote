package david.htc_remote.ir;

import com.htc.htcircontrol.HtcIrData;


/**
 * Wrapper around the HtcIrData class to provide extra information.
 */
public class IrCommand {
    private HtcIrData data;
    private int pulseLength;

    public IrCommand(HtcIrData data) {
        this.data = data;
        this.pulseLength = -1;
    }

    public HtcIrData getData() {
        return this.data;
    }

    public int[] getFrame() {
        return this.data.getFrame();
    }

    public int getFrequency() {
        return this.data.getFrequency();
    }

    public int getPeriod() {
        return this.data.getPeriod();
    }

    public int getPeriodTolerance() {
        return this.data.getPeriodTolerance();
    }

    public int getRepeatCount() {
        return this.data.getRepeatCount();
    }

    public int getPulseLength() {
        if (this.pulseLength == -1 && this.data != null) {
            this.pulseLength = 0;
            int[] pulses = this.data.getFrame();
            for (int pulse : pulses) {
                this.pulseLength += pulse;
            }
        }
        return this.pulseLength;
    }
}
