package space.tatun.mapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Button km_3, km_10, km_21_42,scamper;
    Intent newIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        km_3 = (Button)findViewById(R.id.km_3);
        km_10 = (Button)findViewById(R.id.km_10);
        km_21_42 = (Button)findViewById(R.id.km_21_42);
        scamper = (Button)findViewById(R.id.scamper);


        //получение разрешения на использования геолокации
        if (ContextCompat.checkSelfPermission(StartActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //использование точного местоположения

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
                //использование преблезительного местоположения
            }
        }

            scamper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newIntent = new Intent(StartActivity.this, MapsActivity2.class);
                    startActivity(newIntent);
                }
            });

            km_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newIntent = new Intent(StartActivity.this, MapsActivity.class);
                    startActivity(newIntent);
                }
            });

            km_10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   newIntent = new Intent(StartActivity.this, MapsActivity.class);
                    startActivity(newIntent);
                }
            });

            km_21_42.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     newIntent = new Intent(StartActivity.this, MapsActivity.class);
                    startActivity(newIntent);
                }
            });

    }

    @Override
    public void finish() {
        super.finish();
    }
}
