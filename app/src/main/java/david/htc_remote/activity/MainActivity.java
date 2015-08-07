package david.htc_remote.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.htc.circontrol.CIRControl;
import com.htc.htcircontrol.HtcIrData;

import david.htc_remote.ir.Errors;
import david.htc_remote.R;
import david.htc_remote.ir.IrCommand;


public class MainActivity extends Activity implements Handler.Callback {
    private CIRControl control;
    private Handler handler;
    private SparseArray<IrCommand> commands = new SparseArray<>();
    private Map<UUID, Button> waitingCommands = new HashMap<>();

    @Bind(R.id.statusLabel) TextView statusLabel;

    /**
     * TODO:
     *  - Store IR codes (perhaps in a database)
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

    @OnClick(R.id.showVisualizationButton)
    public void showVisualization() {
        Intent intent = new Intent(this, VisualizeActivity.class);
        startActivity(intent);
    }

    public void onButtonClick(View view) {
        Button button = (Button)view;
        IrCommand command = commands.get(button.getId(), null);

        if (command == null) {
            this.learnCommand(button);
        }
        else {
            this.transmit(command.getData());
        }
    }

    public void transmit(final HtcIrData commandData) {
        this.statusLabel.setText("Transmitting IR command");
        this.handler.post(new Runnable() {
            public void run() {
                control.transmitIRCmd(commandData, true);
            }
        });
    }

    public void learnCommand(Button button) {
        this.statusLabel.setText("");
        UUID queueId = this.control.learnIRCmd(10);
        if (queueId != null) {
            this.statusLabel.setText("Learning IR command");
            this.waitingCommands.put(queueId, button);
        }
    }

    public void storeNewCommand(Button button, IrCommand command) {
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

                HtcIrData commandData = (HtcIrData)msg.getData().getSerializable(CIRControl.KEY_CMD_RESULT);

                if (commandData != null) {
                    Button button = this.waitingCommands.get(resultId);
                    if (button != null) {
                        this.storeNewCommand(button, new IrCommand(commandData));
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
                if (msg.arg1 != CIRControl.ERR_NONE) {
                    status = "Send IR Error: " + Errors.stringFor(msg.arg1);
                }
                break;
            case CIRControl.MSG_RET_CANCEL:
                if (msg.arg1 != CIRControl.ERR_NONE) {
                    status = "Cancel Error: " + Errors.stringFor(msg.arg1);
                }
                break;
            default:
                return false;
        }
        this.statusLabel.setText(status);
        return true;
    }
}
