package pet.yoko.apps.cimaiapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Ferramenta {

    private Context c;
    private ProgressBar progressoMain;

    public Ferramenta(Context c) {
        this.c = c;
    }

    public Ferramenta(Context c, ProgressBar pb) {
        this.c = c;
        this.progressoMain = pb;
    }

    public void prepararRecycleView(RecyclerView recyclerView, ArrayList items, RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(c,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    public void idle(PesquisaActivity activity,boolean inativo) {
        if (activity.mIdlingResource!=null) {
            activity.mIdlingResource.setIdleState(inativo);
        }
    }

    public void filtrarTabela(ArrayList<GrupoItem> listaProjetos, GrupoAdapter listaProjetosAdapter, String newText) {
        ArrayList<GrupoItem> filtrada = new ArrayList<>();
        if (!newText.isEmpty()) {
            for (GrupoItem linha: listaProjetos) {
                if (linha.lider.toLowerCase().contains(newText)) {
                    filtrada.add(linha);
                }
            }
            filtrada.add(new GrupoItem("Total",String.valueOf(filtrada.size()),""));
            filtrada.add((new GrupoItem("","","")));
            listaProjetosAdapter.setItems(filtrada);
        }
        else {
            listaProjetosAdapter.setItems(listaProjetos);
        }
        listaProjetosAdapter.notifyDataSetChanged();
    }

    void excec(String url, final Activity activity) throws IOException {
        idle(((PesquisaActivity)activity),false);
        progressoMain.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressoMain.setVisibility(View.GONE);

                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                idle(((PesquisaActivity)activity),true);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        executarTarefa();
                    }
                });

            }
        });
    }

    private void executarTarefa() {

    }

}
