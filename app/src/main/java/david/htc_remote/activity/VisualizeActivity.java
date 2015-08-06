package david.htc_remote.activity;

import android.app.Activity;
import android.os.Bundle;

import butterknife.OnClick;

import david.htc_remote.R;


public class VisualizeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
    }

    @OnClick(R.id.learnButton)
    public void learnCommand() {
    }
}
