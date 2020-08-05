package pet.yoko.apps.cimaiapp.tasks;

import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import pet.yoko.apps.cimaiapp.db.AppDatabase;
import pet.yoko.apps.cimaiapp.db.ItemPrograd;

public class TaskDownloadPrograd extends TaskDownload {

    public TaskDownloadPrograd(AppDatabase db, String url) {
        super(db, url);
    }

    @Override
    void processarJSON(String conteudo) {
        try {
            JSONArray arr = new JSONArray(conteudo);
            db.itemProgradDao().delete_all();
            for (int i=0; i<arr.length(); i++) {
                int ano = arr.getJSONObject(i).getInt("ano");
                int nCursos = arr.getJSONObject(i).getInt("ncursos");
                int sisu = arr.getJSONObject(i).getInt("sisu");
                int alunos_matriculados = arr.getJSONObject(i).getInt("alunos_matriculados");
                int alunos_ingressantes = arr.getJSONObject(i).getInt("alunos_ingressantes");
                int alunos_concluintes = arr.getJSONObject(i).getInt("alunos_concluintes");
                ItemPrograd item = new ItemPrograd(ano,nCursos,sisu,alunos_matriculados,alunos_ingressantes,alunos_concluintes);
                db.itemProgradDao().insert(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
