package roman10.tutorial.udpcommclient;

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
import android.widget.TextView;
import android.os.AsyncTask;

public class UdpClient extends Activity implements OnClickListener {
	
    /** Called when the activity is first created. */
	public Button btn2, btn3, btn4, btn5, btn6, btn7;
	//public Button btn3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(this);           
        btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(this);           
        btn4 = (Button)findViewById(R.id.button4);
        btn4.setOnClickListener(this);           
        btn5 = (Button)findViewById(R.id.button5);
        btn5.setOnClickListener(this);           
        btn6 = (Button)findViewById(R.id.button6);
        btn6.setOnClickListener(this);           
        btn7 = (Button)findViewById(R.id.button7);
        btn7.setOnClickListener(this);           
    }
    @Override
	public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
        case R.id.button2:
            new LongOperation().execute("dof");
            break;
        case R.id.button3:
            new LongOperation().execute("don");
            break;
        case R.id.button4:
            new LongOperation().execute("kon");
            break;
        case R.id.button5:
            new LongOperation().execute("kof");
            break;
        case R.id.button6:
            new LongOperation().execute("gof");
            break;
        case R.id.button7:
            new LongOperation().execute("gon");
            break;
        }
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
				InetAddress serverAddr = InetAddress.getByName("crazycats.Linksysnet.com");
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
            //TextView txt = (TextView) findViewById(R.id.text1);
            //byte[] resultBytes = result.getBytes();
            //String responseCode = new String(resultBytes);
            //txt.setText(Arrays.toString(resultBytes));
            //loadPlot(resultBytes);
            TextView txt = (TextView) findViewById(R.id.text1);
            txt.setText(result);               
        }

        @Override
        protected void onPreExecute() {
            //TextView txt = (TextView) findViewById(R.id.text1);
            //txt.setText("");        	
        }

        @Override
        protected void onProgressUpdate(Void... values) {}    	
    }
}