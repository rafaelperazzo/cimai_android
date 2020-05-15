package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PesquisaChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_chart);
        Intent intent = getIntent();
        String message = intent.getStringExtra(PesquisaFragment.TIPO);
    }
}
