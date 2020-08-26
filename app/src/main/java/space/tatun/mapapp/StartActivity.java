package space.tatun.mapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button km_3, km_10, km_21_42,customization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        km_3 = (Button)findViewById(R.id.km_3);
        km_10 = (Button)findViewById(R.id.km_10);
        km_21_42 = (Button)findViewById(R.id.km_21_42);
        customization = (Button)findViewById(R.id.customization);

        customization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(StartActivity.this,CustomizationActivity.class);
                startActivity(newIntent);
            }
        });

        km_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(StartActivity.this,MapsActivity.class);
                startActivity(newIntent);
            }
            });

        km_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(StartActivity.this,MapsActivity.class);
                startActivity(newIntent);
            }
        });

        km_21_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(StartActivity.this,MapsActivity.class);
                startActivity(newIntent);
            }
        });
    }
}
