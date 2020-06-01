package pet.yoko.apps.cimaiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class PesquisaActivity extends AppCompatActivity {

    ProgressBar progressoMainPesquisa;
    TextView periodicos;

    @Nullable SimpleIdlingResource mIdlingResource;

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



    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
