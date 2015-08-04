package david.htc_remote;

import com.htc.htcircontrol.HtcIrData;


public class IrCommand {
    private String name;
    private HtcIrData data;

    public IrCommand(String name, HtcIrData data) {
        this.name = name;
        this.data = data;
    }
}
