package david.htc_remote.ir;

import com.htc.htcircontrol.HtcIrData;


public class IrCommand {
    private String name;
    private HtcIrData data;

    public IrCommand(HtcIrData data) {
        this.data = data;
    }

    public IrCommand(String name, HtcIrData data) {
        this.name = name;
        this.data = data;
    }

    public HtcIrData getData() {
        return this.data;
    }
}
