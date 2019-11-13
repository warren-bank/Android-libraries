package com.github.twitwi.androiddnssddemo;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import javax.jmdns.ServiceInfo;

public class DiscoveryActivity extends Activity {
    private static final String NL = "\n  ";

    private String LABEL_EVENT_RESOLVED;
    private String LABEL_EVENT_REMOVED;
    private String LABEL_NAME;
    private String LABEL_PORT;
    private String LABEL_HOST;
    private String BONJOUR_SERVICE_NAME;
    private String BONJOUR_SERVICE_TYPE;
    private int    BONJOUR_SERVICE_PORT;

    private WifiManager.MulticastLock lock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LABEL_EVENT_RESOLVED = getString(R.string.discovery_output_label_event_resolved);
        LABEL_EVENT_REMOVED  = getString(R.string.discovery_output_label_event_removed);
        LABEL_NAME           = getString(R.string.discovery_output_label_name);
        LABEL_PORT           = getString(R.string.discovery_output_label_port);
        LABEL_HOST           = getString(R.string.discovery_output_label_host);
        BONJOUR_SERVICE_NAME = getString(R.string.constant_bonjour_service_name);
        BONJOUR_SERVICE_TYPE = getString(R.string.constant_bonjour_service_type);
        BONJOUR_SERVICE_PORT = Integer.parseInt(getString(R.string.constant_bonjour_service_port), 10);
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
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("MulticastLock");
        lock.setReferenceCounted(true);
        lock.acquire();
        try {
            jmdns = JmDNS.create();
            jmdns.addServiceListener(BONJOUR_SERVICE_TYPE, listener = new ServiceListener() {

                @Override
                public void serviceResolved(ServiceEvent ev) {
                    String additions = "";
                    if (ev.getInfo().getInetAddresses() != null && ev.getInfo().getInetAddresses().length > 0) {
                        additions = NL + LABEL_HOST + " = " + ev.getInfo().getInetAddresses()[0].getHostAddress();
                    }
                    notifyUser(LABEL_EVENT_RESOLVED + ":" + NL + LABEL_NAME + " = " + ev.getInfo().getQualifiedName() + NL + LABEL_PORT + " = " + ev.getInfo().getPort() + additions);
                }

                @Override
                public void serviceRemoved(ServiceEvent ev) {
                    notifyUser(LABEL_EVENT_REMOVED + ":" + NL + LABEL_NAME + " = " + ev.getName());
                }

                @Override
                public void serviceAdded(ServiceEvent event) {
                    // request that service be resolved
                    jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                }
            });
            serviceInfo = ServiceInfo.create(BONJOUR_SERVICE_TYPE, BONJOUR_SERVICE_NAME, BONJOUR_SERVICE_PORT, "description");
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
