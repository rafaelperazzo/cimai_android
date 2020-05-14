package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    public static final String ANO = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.cmbAno);

    }

    public void pesquisaClick(View view) {
        Intent intent = new Intent(this, PesquisaActivity.class);
        Spinner ano = (Spinner)findViewById(R.id.cmbAno);
        String sAno = ano.getSelectedItem().toString();
        intent.putExtra(ANO,sAno);
        startActivity(intent);
    }

    public void ensinoClick(View view) {

    }
}
