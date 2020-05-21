package pet.yoko.apps.cimaiapp;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabelaListaGrupos {

    private JSONObject obj;
    private ArrayList<GrupoItem> items;
    private RecyclerView.Adapter adapter;

    public TabelaListaGrupos(JSONObject obj,ArrayList<GrupoItem> items,RecyclerView.Adapter adapter) {
        this.obj = obj;
        this.items = items;
        this.adapter = adapter;
    }

    public void makeTable() throws JSONException {

        items.clear();
        List<String> linha = new ArrayList<String>();

        for(Iterator<String> keys = obj.keys(); keys.hasNext();) {

            String chave = keys.next();
            String valor = obj.getString(chave);
            JSONObject jsonDados = new JSONObject(valor);

            linha.clear();
            for(Iterator<String> subchaves = jsonDados.keys(); subchaves.hasNext();) {
                String subchave = subchaves.next();
                String valorsubchave = jsonDados.getString(subchave);
                linha.add(valorsubchave);
            }
            items.add(new GrupoItem(linha.get(0),linha.get(1),linha.get(2)));
        }
        items.add(new GrupoItem("Total",String.valueOf(items.size()),""));
        items.add(new GrupoItem("","",""));
        adapter.notifyDataSetChanged();
    }

}
