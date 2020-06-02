package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pet.yoko.apps.cimaiapp.prae.PraeActivity;

public class MainActivity extends AppCompatActivity {
    public static final String ANO = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void pesquisaClick(View view) {
        Intent intent = new Intent(this, PesquisaActivity.class);
        startActivity(intent);
    }

    public void PRAEclick(View view) {
        Intent intent = new Intent(this, PraeActivity.class);
        startActivity(intent);
    }

    public void ensinoClick(View view) {

    }
}
