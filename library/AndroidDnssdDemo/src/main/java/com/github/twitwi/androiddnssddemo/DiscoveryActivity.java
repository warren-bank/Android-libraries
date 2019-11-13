package com.github.twitwi.androiddnssddemo;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import javax.jmdns.ServiceInfo;

public class DiscoveryActivity extends Activity {
    private static final String BONJOUR_SERVICE_TYPE = "_androiddnssddemo._tcp.local.";
    private static final int    BONJOUR_SERVICE_PORT = 7654;

    private android.net.wifi.WifiManager.MulticastLock lock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(){
            public void run() {
                setUp();
            }
        }.start();
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	if (jmdns != null) {
            if (listener != null) {
                jmdns.removeServiceListener(BONJOUR_SERVICE_TYPE, listener);
                listener = null;
            }
            jmdns.unregisterAllServices();
            try {
                jmdns.close();
            } catch (IOException e) {}
            jmdns = null;
    	}
        lock.release();
    }

    private JmDNS jmdns = null;
    private ServiceListener listener = null;
    private ServiceInfo serviceInfo;

    private void setUp() {
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("mylockthereturn");
        lock.setReferenceCounted(true);
        lock.acquire();
        try {
            jmdns = JmDNS.create();
            jmdns.addServiceListener(BONJOUR_SERVICE_TYPE, listener = new ServiceListener() {

                @Override
                public void serviceResolved(ServiceEvent ev) {
                    String NL = "\n  ";
                    String additions = "";
                    if (ev.getInfo().getInetAddresses() != null && ev.getInfo().getInetAddresses().length > 0) {
                        additions = NL + "host = " + ev.getInfo().getInetAddresses()[0].getHostAddress();
                    }
                    notifyUser("Service resolved:" + NL + "name = " + ev.getInfo().getQualifiedName() + NL + "port = " + ev.getInfo().getPort() + additions);
                }

                @Override
                public void serviceRemoved(ServiceEvent ev) {
                    notifyUser("Service removed: " + ev.getName());
                }

                @Override
                public void serviceAdded(ServiceEvent event) {
                    // request that service be resolved
                    jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                }
            });
            serviceInfo = ServiceInfo.create(BONJOUR_SERVICE_TYPE, "AndroidTest", BONJOUR_SERVICE_PORT, "plain test service from android");
            jmdns.registerService(serviceInfo);
        }
        catch (IOException e) {}
    }

    private void notifyUser(final String msg) {
        DiscoveryActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                TextView t = (TextView)findViewById(R.id.text);
                t.setText(msg + "\n" + t.getText());
            }
        });
    }
}
