package pet.yoko.apps.cimaiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.cimaiapp.prae.PraeActivity;

public class MainActivity extends AppCompatActivity {

    public static final String ANO = "";
    public int VERSAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VERSAO = getVersionCode();
        try {
            run("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp",0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionCode);
    }

    public void atualizarClick(View v) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pet.yoko.apps.cimaiapp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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

    void run(String url, final int tipo) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tipo==0) {
                            verificarAtualizacao(myResponse);
                        }
                        else {

                        }

                    }
                });

            }
        });
    }

    public void verificarAtualizacao(String myResponse) {
        int versaoNova = Ferramenta.getAppPlayStoreVersion(myResponse);
        if (VERSAO<versaoNova) {
            popupAtualizar();
        }
    }

    public void popupAtualizar() {
        DialogFragment newFragment = new AtualizarDialog();
        newFragment.show(getSupportFragmentManager(),"Atualizar");
    }

}
