package com.example.demoanalytic;


        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.util.SparseArray;
        import android.view.View;

/**
 * @author caibou
 */
public abstract class ApBaseAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private SparseArray<OnViewClickListener> listenerArray = new SparseArray<>();

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (onItemClickListener != null)
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                onItemLongClickListener.onItemLongClick(v, position);
                return true;
            });
        }
        setViewListener(holder);
        onBindHolder(holder, position);
    }

    protected void setViewListener(VH holder) {
        for (int index = 0, size = listenerArray.size(); index < size; index++) {
            int resId = listenerArray.keyAt(index);
            OnViewClickListener listener = listenerArray.valueAt(index);
            View view = holder.itemView.findViewById(resId);
            if (view != null) view.setOnClickListener(v -> {
                listener.onViewClick(view, holder.getAdapterPosition());
            });
        }
    }

    public boolean isEmpty () {
        return getItemCount() == 0;
    }

    public abstract void onBindHolder(@NonNull VH holder, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void bindViewListener(int resId, OnViewClickListener onViewClickListener) {
        listenerArray.put(resId, onViewClickListener);
    }

    public interface OnItemClickListener {
        void onItemClick(View item, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View item, int position);
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }
}
