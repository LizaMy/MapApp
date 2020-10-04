package space.tatun.mapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Intent newIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread th = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                }
              catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    Intent newIntent = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(newIntent);

                }
            }
        };
        th.start();  // запускаем поток
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
