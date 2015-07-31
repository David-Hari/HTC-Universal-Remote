package david.htc_remote;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends Activity {
    @Bind(R.id.mainLayout) LinearLayout mainLayout;
    PopupWindow popupWindow;
    private IrManager ir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //this.ir = new IrManager(getApplicationContext());
        //this.ir.start();
        this.popupWindow = new PopupWindow(this);

		Button popupButton = new Button(this);
		popupButton.setText("OK");
		LinearLayout popupLayout = new LinearLayout(this);
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        popupLayout.addView(popupButton);
		popupWindow = new PopupWindow(popupLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(popupLayout);
    }

    @OnClick(R.id.addButton)
    public void addButton() {
        Button button = new Button(this);
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        button.setText("Button");

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
                popupWindow.update(50, 50, 300, 80);
            }
        });

        mainLayout.addView(button);
    }

    /**
     * TODO:
     *  - Store IR codes (perhaps in a database)
     *  - Find better way of updating UI from manager/handler
     */
}
