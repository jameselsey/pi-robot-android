package com.jameselsey.apps.robots;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.String.format;

public class MainActivity extends Activity {


    private static final String TAG = "OUTPUT";

    private String serverIp = "192.168.0.11";
    private int serverPort = 3033;

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

        EditText serverIpEditText = (EditText) findViewById(R.id.ipAddress);
        serverIpEditText.setText(serverIp);
        serverIpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    serverIp = ((EditText)v).getText().toString();
                    Log.d(TAG,"User has lost focus on serverIpEditText, updating server ip to " + serverIp);
                }
            }
        });

        EditText serverPortEditText = (EditText) findViewById(R.id.port);
        serverPortEditText.setText("" + serverPort);
        serverPortEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    serverPort = Integer.parseInt(((EditText)v).getText().toString());
                    Log.d(TAG,"User has lost focus on portEditText, updating port to " + serverPort);
                }
            }
        });

        leftForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        buttonPress(view);
                        sendCommand(leftForwardCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        buttonRelease(view);
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
                        buttonPress(view);
                        sendCommand(leftBackCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        buttonRelease(view);
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
                        buttonPress(view);
                        sendCommand(rightForwardCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        buttonRelease(view);
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
                        buttonPress(view);
                        sendCommand(rightBackCommand + "-Start");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        buttonRelease(view);
                        sendCommand(rightBackCommand + "-Stop");
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void buttonPress(View v){
        v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    private void buttonRelease(View v){
        v.getBackground().clearColorFilter();
    }

    private void sendCommand(String command) {
        new SendCommandTask().execute(command);
    }

    class SendCommandTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... commands) {
            String command = commands[0];
            Log.d(TAG, format("Sending command %s to %s:%d", command, serverIp, serverPort));
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
