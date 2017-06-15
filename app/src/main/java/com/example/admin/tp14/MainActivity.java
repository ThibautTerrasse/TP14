package com.example.admin.tp14;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ProgressBar bar;
    ProgressBar bar2;
    Handler handler;
    AtomicBoolean isRunning = new AtomicBoolean();
    AtomicBoolean isPausing = new AtomicBoolean();

    Bundle messageBundle = new Bundle();
    Message message;

    public static final String PROGRESS = "progress";

    class Traitement extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(),"Démarrage", Toast.LENGTH_LONG).show();
        }

        @Override

        protected void onProgressUpdate(Integer...values){
            bar2.setProgress(values[0]);
        }
        @Override
        protected String doInBackground(Void... arg0){
            int progress;
            for(progress = 0; progress <= 100; progress++){
                if(isCancelled())break;
                try{
                    Thread.sleep(1000);

                }catch (InterruptedException e){

                }
                publishProgress(progress);
            }
            return " ,dong";
        }
        @Override
        protected void onPostExecute(String s){
            Toast.makeText(getApplicationContext(),"Fini", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar2 = (ProgressBar) findViewById(R.id.progressBar2);
        bar2.setProgress(0);
        Traitement traitement = new Traitement();

        handler = new Handler(){

            public void handleMessage(Message msg) {

                int p= msg.getData().getInt(PROGRESS);

                bar.incrementProgressBy(p);

            }

        };
        bar.setProgress(0);
        isRunning.set(true);
        isPausing.set(false);
        thread.start();

        traitement.execute();
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
    protected  void onPause(){
         super.onPause();
         isPausing.set(true);
     }

    @Override
    protected void onResume() {
        super.onResume();
        isPausing.set(false);

    }
}

