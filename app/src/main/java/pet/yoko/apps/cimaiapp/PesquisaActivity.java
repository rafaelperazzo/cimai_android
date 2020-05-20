package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class PesquisaActivity extends AppCompatActivity {
    String url = "https://apps.yoko.pet//api/cimaiapi.php?tabela=producoes&ano=";
    ProgressBar progressoMainPesquisa;
    TextView periodicos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        setTitle("PESQUISA");
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        progressoMainPesquisa = (ProgressBar)findViewById(R.id.progressoPesquisaMain);
        progressoMainPesquisa.setVisibility(View.VISIBLE);
        periodicos = (TextView)findViewById(R.id.txtPeriodicos);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.ANO);
        url = url + message;
        TextView ano = (TextView) (findViewById(R.id.txtAno));
        ano.setText(message);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Fragment fragment = new PesquisaFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PesquisaFragment();
                        break;
                    case 1:
                        fragment = new PesquisaGruposFragment();
                        break;
                    case 2:
                        fragment = new PesquisaProjetosFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public int getAno() {
        int ano = 2019;
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.ANO);
        try {
            ano = Integer.parseInt(message);
        }
        catch (NumberFormatException e) {
            ano = 2020;
        }
        return (ano);
    }

}
