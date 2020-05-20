package pet.yoko.apps.cimaiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter <GrupoAdapter.ViewHolder> {

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

    public GrupoAdapter(List<GrupoItem> items) {
        this.items = items;
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
}
