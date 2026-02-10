package com.systemcontrol.corpsele.systemcontrol.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.systemcontrol.corpsele.systemcontrol.R;
import com.systemcontrol.corpsele.systemcontrol.generated.callback.OnClickListener;
import com.systemcontrol.corpsele.systemcontrol.mvvm.listener.OnAnyListener;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;


public class ApiModelAdapter extends ListAdapter<ApiModel, ApiModelAdapter.ApiModelHolder> {
    private OnAnyListener onAnyListener;
    public ApiModelAdapter(OnAnyListener onAnyListener) {
        super(DIFF_CALLBACK);
        this.onAnyListener = onAnyListener;
    }
    private static final DiffUtil.ItemCallback<ApiModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ApiModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ApiModel oldItem, @NonNull ApiModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull ApiModel oldItem, @NonNull ApiModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };
    @NonNull
    @Override
    public ApiModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apikey, parent, false);
        return new ApiModelHolder(itemView, onAnyListener);
    }
    @Override
    public void onBindViewHolder(@NonNull ApiModelHolder holder, int position) {
        ApiModel currentApi = getItem(position);
        holder.textViewTitle.setText(currentApi.getTitle());
        holder.textViewDescription.setText(currentApi.getDescription());
        holder.textViewPriority.setText("Priority: " + currentApi.getPriority());

        // 简单的视觉反馈
        if (currentApi.getPriority() == 1) {
            holder.textViewPriority.setTextColor(0xFFFF0000); // Red
        } else {
            holder.textViewPriority.setTextColor(0xFF000000); // Black
        }
        holder.textViewApiKey.setText(currentApi.getApiKey());
        holder.textViewUrl.setText(currentApi.getUrl());
        holder.bind(currentApi);
    }
    public ApiModel getApiModelAt(int position) {
        return getItem(position);
    }
    static class ApiModelHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private TextView textViewUrl;
        private TextView textViewApiKey;
        private ApiModel currentApi;
        public ApiModelHolder(View itemView, OnAnyListener onAnyListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_apiModelTitle);
            textViewDescription = itemView.findViewById(R.id.tv_apiKeyDescription);
            textViewPriority = itemView.findViewById(R.id.tv_apiModelPriiority);
            textViewUrl = itemView.findViewById(R.id.tv_apiModelUrl);
            textViewApiKey = itemView.findViewById(R.id.tv_apiModelApiKey);
            itemView.setOnClickListener(v -> {
                if (currentApi != null && onAnyListener != null) {
                    onAnyListener.onItemClick(currentApi);
                }
            });
        }

        public void bind(ApiModel apiModel) {
            currentApi = apiModel;
//            textViewTitle.setText(apiModel.getTitle());
        }
    }
}