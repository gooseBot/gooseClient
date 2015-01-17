package time2go.goosegun.gooseController;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPcommunication extends AsyncTask<Object, Boolean, String> {
    private static final int UDP_SERVER_PORT = 8888;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    private String lText="err";
    Activity callerActivity;

    @Override
    protected String doInBackground(Object... params) {
        String udpMsg = (String) params[0];
        callerActivity = (Activity) params[1];
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
            //not waiting for a response for trg commands
            if ((udpMsg.length()>=3) && udpMsg.substring(0,3).equals("trg")){
                lText = udpMsg.substring(0,3);    //used in onpostexecute needs to be 3 char command only
            } else {
                ds.setSoTimeout(4000);
                ds.receive(dpr);
                lText = new String(lMsg, 0, dpr.getLength());
            }
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
        // show the UDP response in the textView
        TextView txt = (TextView) callerActivity.findViewById(R.id.udpResponse);
        txt.setText(result);
        // if err then no need to examine the response, or for trg commands since not waiting for a response
        if (result=="err" || result=="trg") {return;}
        // if result > 3 then it is status message, so clear buttons first
        if (result.length()>3){
            ((ToggleButton) callerActivity.findViewById(R.id.kid)).setChecked(false);
            ((ToggleButton) callerActivity.findViewById(R.id.data)).setChecked(false);
            ((ToggleButton) callerActivity.findViewById(R.id.gun)).setChecked(false);
            ((ToggleButton) callerActivity.findViewById(R.id.manual)).setChecked(false);
        }
        // otherwise examine the response and update the UI
        for (int i=0;i<result.length();i=i+3){
            String cmd = result.substring(i,i+3);
            if (cmd.equals("kon")) {((ToggleButton) callerActivity.findViewById(R.id.kid)).setChecked(true);}
            if (cmd.equals("don")) {((ToggleButton) callerActivity.findViewById(R.id.data)).setChecked(true);}
            if (cmd.equals("gon")) {((ToggleButton) callerActivity.findViewById(R.id.gun)).setChecked(true);}
            if (cmd.equals("mon")) {((ToggleButton) callerActivity.findViewById(R.id.manual)).setChecked(true);}
        }
    }
}
