package pet.yoko.apps.cimaiapp.tasks;

import android.widget.ProgressBar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;

import pet.yoko.apps.cimaiapp.db.AppDatabase;
import pet.yoko.apps.cimaiapp.db.ItemPrae;

public class TaskDownloadPrae extends TaskDownload{

    public TaskDownloadPrae(AppDatabase db, String url, LocalBroadcastManager broadcast) {
        super(db, url, broadcast);
    }

    @Override
    void processarJSON(String conteudo) {
        try {
            JSONArray arr = new JSONArray(conteudo);
            db.itemPraeDao().delete_all();
            for (int i=0; i<arr.length(); i++) {
                int ano = arr.getJSONObject(i).getInt("ano");
                int beneficiarios = arr.getJSONObject(i).getInt("beneficiarios");
                int alimentacao = arr.getJSONObject(i).getInt("alimentacao");
                int creche = arr.getJSONObject(i).getInt("creche");
                int emergencial = arr.getJSONObject(i).getInt("emergencial");
                int financeiro_eventos = arr.getJSONObject(i).getInt("financeiro_eventos");
                int inclusao = arr.getJSONObject(i).getInt("inclusao");
                int moradia = arr.getJSONObject(i).getInt("moradia");
                int oculos = arr.getJSONObject(i).getInt("oculos");
                int transporte = arr.getJSONObject(i).getInt("transporte");
                int bia = arr.getJSONObject(i).getInt("bia");
                int indiretos = arr.getJSONObject(i).getInt("indiretos");
                ItemPrae item = new ItemPrae(ano,beneficiarios,alimentacao,creche,emergencial,financeiro_eventos,inclusao,moradia,oculos,transporte,bia,indiretos);
                db.itemPraeDao().insert(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
