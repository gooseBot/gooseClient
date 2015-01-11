package time2go.goosegun.gooseController;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class UdpClient extends Activity implements OnClickListener {
	
    /** Called when the activity is first created. */
	public Button btnData, btnKid, btnGun, btnManual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnData = (Button) findViewById(R.id.data);
        btnData.setOnClickListener(this);
        btnKid = (Button) findViewById(R.id.kid);
        btnKid.setOnClickListener(this);
        btnGun = (Button) findViewById(R.id.gun);
        btnGun.setOnClickListener(this);
        btnManual = (Button) findViewById(R.id.manual);
        btnManual.setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        ((ToggleButton) findViewById(R.id.kid)).setChecked(false);
        ((ToggleButton) findViewById(R.id.data)).setChecked(false);
        ((ToggleButton) findViewById(R.id.gun)).setChecked(false);
        ((ToggleButton) findViewById(R.id.manual)).setChecked(false);
        LongOperation LongOperationTask = new LongOperation();
        LongOperationTask.execute("sts",this);
    }
    @Override
	public void onClick(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        String command = "";
        // detect the view that was "clicked"
        switch (view.getId()) {
        case R.id.data:
            if (on) {
                command="don";
            } else {
                command="dof";
            }
            break;
        case R.id.kid:
            if (on) {
                command="kon";
            } else {
                command="kof";
            }
            break;
        case R.id.gun:
            if (on) {
                command="gon";
            } else {
                command="gof";
            }
            break;
        case R.id.manual:
            if (on) {
                command="mon";
            } else {
                command="mof";
            }
            break;
        }
        LongOperation LongOperationTask = new LongOperation();
        LongOperationTask.execute(command,this);
    }
}

