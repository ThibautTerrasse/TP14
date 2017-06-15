package com.example.admin.tp14;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ProgressBar bar;
    Handler handler;
    AtomicBoolean isRunning = new AtomicBoolean();
    AtomicBoolean isPausing = new AtomicBoolean();
    Bundle messageBundle = new Bundle();
    Message message;

    public static final String PROGRESS = "progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setProgress(0);
        handler = new Handler(){

            public void handleMessage(Message msg) {

                int p= msg.getData().getInt(PROGRESS);

                bar.incrementProgressBy(p);

            }

        };
    }

Thread thread = new Thread(new Runnable() {

    @Override
    public void run() {
        try{
            for(int i=0;i<=100 && isRunning.get();i++){
                while(isPausing.get() && (isRunning.get())){
                    Thread.sleep(1000);
                }

                Thread.sleep(300);
                message = handler.obtainMessage();

                messageBundle.putInt(PROGRESS, i);

                message.setData(messageBundle);

                handler.sendMessage(message);
            }
        }catch (Throwable t) {

        }
    }
});

    @Override
    protected void onStart() {
        super.onStart();

        bar.setProgress(0);
        isRunning.set(true);
        isPausing.set(false);
        thread.start();
    }
     @Override
    protected  void onPause(){
         super.onPause();
         isRunning.set(false);
         isPausing.set(true);
     }


}
