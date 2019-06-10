import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Shell {

    public final class Result {
        public String stdout;
        public String stderr;
        public int    status;

        public Result(String stdout, String stderr, int status) {
            this.stdout = stdout;
            this.stderr = stderr;
            this.status = status;
        }
    }

    private static final String TAG = "Shell";

    public static Result execForResult(String...strings) {
        Process su = null;
        DataOutputStream outputStream = null;
        InputStream stdout = null;
        InputStream stderr = null;
        Result res = null;
        try{
            su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            stdout = su.getInputStream();
            stderr = su.getErrorStream();

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            res = new Result(
                readFully(stdout).trim(),
                readFully(stderr).trim(),
                su.exitValue()
            );
        } catch (IOException e){
            Log.e(TAG, e.getMessage(), e);
        } finally {
            closeSilently(outputStream, stdout, stderr);
        }
        return res;
    }

    public static void exec(String...strings) {
        Process su = null;
        DataOutputStream outputStream = null;
        try{
            su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } catch (IOException e){
            Log.e(TAG, e.getMessage(), e);
        } finally {
            closeSilently(outputStream);
        }
    }

    public static Result execScriptForResult(String inputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        return execScriptForResult(inputFile);
    }

    public static Result execScriptForResult(File inputFile) throws IOException {
        InputStream input = new FileInputStream(inputFile);
        return execScriptForResult(input);
    }

    public static Result execScriptForResult(InputStream input) throws IOException {
        String[] lines = readScript(input);
        return execForResult(lines);
    }

    public static void execScript(String inputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        execScript(inputFile);
    }

    public static void execScript(File inputFile) throws IOException {
        InputStream input = new FileInputStream(inputFile);
        execScript(input);
    }

    public static void execScript(InputStream input) throws IOException {
        String[] lines = readScript(input);
        exec(lines);
    }

    private static String[] readScript(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> lines = new LinkedList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[]{});
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

    private static void closeSilently(Object... xs) {
        // Note: on Android API levels prior to 19, Socket does not implement Closeable
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

    public static void startADB(int port) throws  IOException{
        String cmds[] = {
                "setprop service.adb.tcp.port " + port,
                "stop adbd",
                "start adbd"
        };
        String result;

        // TCP not enabled (first time)
        result = execForResult("getprop service.adb.tcp.port").stdout;
        if(result == null || !result.equals(Integer.toString(port))){
            Log.i(TAG, "Starting ADB, current port = " + result + ", new port = " + Integer.toString(port));
            exec(cmds);
            return;
        }

        // ADB.D not running
        result = execForResult("getprop init.svc.adbd").stdout;
        if(result == null || !result.equals("running")){
            Log.i(TAG, "Starting ADB at port " + Integer.toString(port));
            exec(cmds);
            return;
        }

        Log.i(TAG, "ADB is running at port " + Integer.toString(port));
    }

    public static void stopADB() throws  IOException{
        startADB(-1);
    }
}
