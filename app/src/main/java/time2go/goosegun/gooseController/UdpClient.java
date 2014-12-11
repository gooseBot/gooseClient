package time2go.goosegun.gooseController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.os.AsyncTask;

public class UdpClient extends Activity implements OnClickListener {
	
    /** Called when the activity is first created. */
	public Button btn2, btn4, btn6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(this);
        btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(this);
    }
    @Override
	public void onClick(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        String command = "";
        // detect the view that was "clicked"
        switch (view.getId()) {
        case R.id.button2:
            if (on) {
                command="don";
            } else {
                command="dof";
            }
            break;
        case R.id.button4:
            if (on) {
                command="kon";
            } else {
                command="kof";
            }
            break;
        case R.id.button6:
            if (on) {
                command="gon";
            } else {
                command="gof";
            }
            break;
        }
        new LongOperation().execute(command);
    }
    
    private class LongOperation extends AsyncTask<String, Void, String>  {
        private static final int UDP_SERVER_PORT = 8888;
        private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    	String lText;
    	
    	@Override
    	protected String doInBackground(String... params) {
	    	String udpMsg = params[0];
	    	DatagramSocket ds = null;
	    	byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
	    	
	    	try {
				ds = new DatagramSocket();
				//InetAddress serverAddr = InetAddress.getByName("crazycats.Linksysnet.com");
                //192, 168, 1, 178  address of goose gun on my local network at home
                byte [] b = new byte[] {(byte)192,(byte)168,(byte)1,(byte)178};
                InetAddress serverAddr = InetAddress.getByAddress(b);
				DatagramPacket dp = new DatagramPacket(udpMsg.getBytes(), udpMsg.length(), serverAddr, UDP_SERVER_PORT);
				DatagramPacket dpr = new DatagramPacket(lMsg, lMsg.length);
				ds.send(dp);
				//disable timeout for testing
				ds.setSoTimeout(4000);			
				ds.receive(dpr);
				lText = new String(lMsg, 0, dpr.getLength());
				Log.i("UDP packet received", lText);
			} catch (SocketException e) {
				e.printStackTrace();
			}catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ds != null) {
					ds.close();
				}
			}
	    	return lText;
    	}
    	@Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.text1);
            txt.setText(result);               
        }

        @Override
        protected void onProgressUpdate(Void... values) {}    	
    }
}