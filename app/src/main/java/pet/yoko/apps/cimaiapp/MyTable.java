package pet.yoko.apps.cimaiapp;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MyTable {

    private JSONObject obj;
    private ArrayList<ProducaoItem> items;
    private RecyclerView.Adapter adapter;

    public MyTable(JSONObject obj,ArrayList<ProducaoItem> items,RecyclerView.Adapter adapter) {
        this.obj = obj;
        this.items = items;
        this.adapter = adapter;
    }


    public void makeTable() throws JSONException {
        items.clear();
        for(Iterator<String> keys = obj.keys(); keys.hasNext();) {
            String chave = keys.next();
            int valor = obj.getInt(chave);
            items.add(new ProducaoItem(chave,valor));
        }
        this.calcularPercentuais(this.getTotalItems());
        adapter.notifyDataSetChanged();
    }

    public void makeTable(String tipoItem) throws JSONException {
        items.clear();
        for(Iterator<String> keys = obj.keys(); keys.hasNext();) {
            if (tipoItem.equals("GrupoItem")) {
                //TODO: Implementar a tabela de grupo
            }
            else {
                String chave = keys.next();
                int valor = obj.getInt(chave);
                items.add(new ProducaoItem(chave,valor));
            }

        }
        if (tipoItem.equals("GrupoItem")) {

        }
        else {
            this.calcularPercentuais(this.getTotalItems());
        }
        adapter.notifyDataSetChanged();
    }

    private int getTotalItems() {
        int total = 0;
        int valor;
        for (int i=0; i<items.size();i++) {
            valor = items.get(i).getQuantidade();
            total = total + valor;
        }
        return (total);
    }

    private void calcularPercentuais(int total) {
        int quantidade;
        float percentual;
        for (int i=0; i<items.size();i++) {
            quantidade = items.get(i).getQuantidade();
            percentual = (quantidade/(float)total)*100;
            items.get(i).setPercentual(percentual);
        }
        items.add(new ProducaoItem("Total", total,100));
        items.add(new ProducaoItem("", -1,-1));
    }

}
