package pet.yoko.apps.cimaiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView descricao;
        public TextView quantidade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = (TextView)itemView.findViewById(R.id.colunaDescricao);
            quantidade = (TextView)itemView.findViewById(R.id.colunaQuantidade);
        }
    }

    private List<ProducaoItem> items;

    public CustomAdapter(List<ProducaoItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.rowlayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProducaoItem item = items.get(position);
        TextView descricao = holder.descricao;
        TextView quantidade = holder.quantidade;
        descricao.setText(item.descricao);
        quantidade.setText(String.valueOf(item.quantidade));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
