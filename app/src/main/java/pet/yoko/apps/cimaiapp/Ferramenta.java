package pet.yoko.apps.cimaiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.SimpleTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.inamik.text.tables.Cell.Functions.TOP_ALIGN;
import static com.inamik.text.tables.Cell.Functions.VERTICAL_CENTER;

public class Ferramenta {

    private Context c;
    private ProgressBar progressoMain;
    private static SharedPreferences sharedPref;

    public Ferramenta(Context c) {
        this.c = c;
    }

    public Ferramenta(Context c, ProgressBar pb) {
        this.c = c;
        this.progressoMain = pb;
    }

    public static SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public static void setSharedPref(SharedPreferences sharedPref) {
        Ferramenta.sharedPref = sharedPref;
    }

    public static void setPref(String config, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(config, value);
        editor.commit();
    }

    public static String getPref(String config, String defaultValue) {
        String atualizacao = sharedPref.getString(config,defaultValue);
        return (atualizacao);
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

    public String getTableText(RecyclerView tabela, String tipo, String titulo) {

        if (tipo.equals("ProducaoItem")) {

            SimpleTable st = SimpleTable.of();
            st.nextRow();
            st.nextCell().addLine("Descrição");
            st.nextCell().addLine("Quantidade");
            st.nextCell().addLine("Percentual");

            CustomAdapter adapter = (CustomAdapter)tabela.getAdapter();

            for (int i=0; i<adapter.getItemCount(); i++) {
                String descricao = adapter.getItems().get(i).descricao;
                int quantidade = adapter.getItems().get(i).quantidade;
                float percentual = adapter.getItems().get(i).percentual;
                st.nextRow();
                st.nextCell().addLine(descricao).applyToCell(VERTICAL_CENTER.withHeight(5));
                st.nextCell().addLine(String.valueOf(quantidade)).applyToCell(VERTICAL_CENTER.withHeight(5));
                st.nextCell().addLine(String.valueOf(percentual)).applyToCell(VERTICAL_CENTER.withHeight(5));
            }

            GridTable gt = st.toGrid();
            gt = Border.of(Border.Chars.of('+', '-', '|')).apply(gt);
            return(Util.asString(gt));
        }
        else if (tipo.equals("GrupoItem")) {

            SimpleTable st = SimpleTable.of();
            st.nextRow();
            st.nextCell().addLine("Pessoa");
            st.nextCell().addLine("Área");
            st.nextCell().addLine("Descrição");

            GrupoAdapter adapter = (GrupoAdapter)tabela.getAdapter();

            for (int i=0; i<adapter.getItemCount(); i++) {
                String descricao = adapter.getItems().get(i).nome;
                String area = adapter.getItems().get(i).area;
                String pessoa = adapter.getItems().get(i).lider;
                st.nextRow();
                st.nextCell().addLine(pessoa);
                st.nextCell().addLine(String.valueOf(area));
                st.nextCell().addLine(String.valueOf(descricao));

            }

            GridTable gt = st.toGrid();
            gt = Border.of(Border.Chars.of('+', '-', '|')).apply(gt);
            return(Util.asString(gt));
        }
        return ("");
    }

    public void shareTextContent(Context c,String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhando dados...");
        c.startActivity(shareIntent);
    }

    public void setImgProjetosShareClick(final Context c, final RecyclerView recyclerView,  ImageView share, final String tipo, final String titulo) {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conteudo = "";
                conteudo = getTableText(recyclerView,tipo,titulo);
                shareTextContent(c,conteudo);
            }
        });
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

    public static int getAppPlayStoreVersion(String html) {
        Document doc = Jsoup.parse(html);
        int versaoPublicada = 0;
        Elements versao = doc.getElementsByClass("htlgb");
        try {
            versaoPublicada = Integer.parseInt(versao.get(7).text());
        }
        catch (NumberFormatException e) {
            versaoPublicada = 0;
        }
        return (versaoPublicada);
    }

}
