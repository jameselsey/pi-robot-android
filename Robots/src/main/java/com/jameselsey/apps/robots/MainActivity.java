package com.jameselsey.apps.robots;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {


    private static final String TAG = "OUTPUT";

    private static final String serverIp = "192.168.0.11";
    private static final int serverPort = 3033;

    private final String leftForwardCommand = "L-FORWARD";
    private final String leftBackCommand = "L-BACK";
    private final String rightForwardCommand = "R-FORWARD";
    private final String rightBackCommand = "R-BACK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button leftForward = (Button) findViewById(R.id.leftForward);
        Button leftBack = (Button) findViewById(R.id.leftBack);
        Button rightForward = (Button) findViewById(R.id.rightForward);
        Button rightBack = (Button) findViewById(R.id.rightBack);

        TextView serverIpTextView = (TextView) findViewById(R.id.serverIp);
        serverIpTextView.setText(serverIp + ":" + serverPort);

        leftForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sendCommand(leftForwardCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sendCommand(leftForwardCommand + "-Stop");
                        break;
                    }
                }
                return false;
            }
        });

        leftBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sendCommand(leftBackCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sendCommand(leftBackCommand + "-Stop");
                        break;
                    }
                }
                return false;
            }
        });

        rightForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sendCommand(rightForwardCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sendCommand(rightForwardCommand + "-Stop");
                        break;
                    }
                }
                return false;
            }
        });

        rightBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sendCommand(rightBackCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sendCommand(rightBackCommand + "-Stop");
                        break;
                    }
                }
                return false;
            }
        });
    }


    private void sendCommand(String command) {
        new SendCommandTask().execute(command);
    }

    class SendCommandTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... commands) {
            String command = commands[0];
            try {
                //TODO: make this configurable inside the app
                Socket socket = new Socket(serverIp, serverPort);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.write(command);
                out.flush();

                Log.d(TAG, "Successfully sent " + command);
            } catch (IOException ioe) {
                Log.d(TAG, "Unable to send command", ioe);
            }
            return null;
        }
    }
}
