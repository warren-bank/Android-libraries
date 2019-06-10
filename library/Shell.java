package br.com.criativasoft.eduaccess.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import br.com.criativasoft.eduaccess.MainApplication;

import static android.os.Build.VERSION_CODES.M;


public class Shell {

    private static final String TAG = "Shell";

    public static void exec(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void startADB(int port) throws  IOException{
        String cmds[] = {
                "setprop service.adb.tcp.port " + port,
                "stop adbd",
                "start adbd"
        };

        String result = execForResult("getprop service.adb.tcp.port");

        Log.i(TAG, "Starting ADB, current port = " + result);

        // TCP not enabled (frist time)
        if(result == null || !result.contains(Integer.toString(port))){
            exec(cmds);
            return;
        }

        // ADB.D not running
        result = execForResult("getprop init.svc.adbd");
        if(result == null || !result.contains("running")){
            exec(cmds);
        }
    }

    public static void execScript(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<String> lines = new LinkedList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            //lines.add(line);
            exec(line);
        }
        //exec(lines.toArray(new String[]{}));
    }

    public static String execForResult(String...strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try{
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            closeSilently(outputStream, response);
        }
        return res;
    }

    private static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }

    public static void closeSilently(Object... xs) {
        // Note: on Android API levels prior to 19 Socket does not implement Closeable
        for (Object x : xs) {
            if (x != null) {
                try {
                    if (x instanceof Closeable) {
                        ((Closeable) x).close();
                    } else if (x instanceof Socket) {
                        ((Socket) x).close();
                    } else if (x instanceof DatagramSocket) {
                        ((DatagramSocket) x).close();
                    } else {
                        throw new RuntimeException("cannot close " + x);
                    }
                } catch (Throwable e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }
}
