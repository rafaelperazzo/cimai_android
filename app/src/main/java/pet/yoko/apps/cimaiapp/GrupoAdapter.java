package pet.yoko.apps.cimaiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter <GrupoAdapter.ViewHolder> implements Filterable {



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nome;
        public TextView lider;
        public TextView area;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView)itemView.findViewById(R.id.colunaGrupoNome);
            lider = (TextView)itemView.findViewById(R.id.colunaGrupoLider);
            area = (TextView)itemView.findViewById(R.id.colunaGrupoArea);
        }
    }

    private List<GrupoItem> items;
    private List<GrupoItem> items_filtrados;

    public GrupoAdapter(List<GrupoItem> items) {

        this.items = items;
        this.items_filtrados = items;
    }
    @NonNull
    @Override
    public GrupoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.grupo_row_layout,parent,false);
        GrupoAdapter.ViewHolder viewHolder = new GrupoAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.ViewHolder holder, int position) {
        GrupoItem item = items.get(position);
        TextView nome = holder.nome;
        TextView lider = holder.lider;
        TextView area = holder.area;
        nome.setText(item.nome);
        lider.setText(item.lider);
        area.setText(item.area);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    items_filtrados = items;
                }
                else {
                    ArrayList<GrupoItem> filteredList = new ArrayList<>();
                    for (GrupoItem grupoItem : items) {
                        if (grupoItem.nome.contains(charString)||grupoItem.lider.contains(charString)||grupoItem.area.contains(charString)) {
                            filteredList.add(grupoItem);
                        }
                    }
                    items_filtrados = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = items_filtrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items_filtrados = (ArrayList<GrupoItem>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
