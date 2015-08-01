package david.htc_remote;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import butterknife.ButterKnife;
import com.htc.htcircontrol.HtcIrData;


public class MainActivity extends Activity {
    private static final int LEARN_TIMEOUT = 10; // seconds
    private IrManager ir;
    private SparseArray<HtcIrData> commands = new SparseArray<HtcIrData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.ir = new IrManager(getApplicationContext());
        this.ir.start();
    }

    public void onButtonClick(View view) {
        Button button = (Button)view;

        if (commands.indexOfKey(button.getId()) < 0) {
            // Learn command and set it to this button
            this.ir.learnCommand(LEARN_TIMEOUT);
            //commands.put(button.getId(), new HtcIrData());
            //button.setBackgroundColor(0xFFBADAF4);
        }
        else {
            // Send button's command
        }
    }

    /**
     * TODO:
     *  - Store IR codes (perhaps in a database)
     *  - Find better way of updating UI from manager/handler
     */
}
