package com.systemcontrol.corpsele.systemcontrol;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.systemcontrol.corpsele.systemcontrol.R;

import java.util.List;

public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
    private Context context;
    private List<String> list;
    private View inflater;
    private ClickInterface clickInterface;

    //构造方法，传入数据
    public RecycleAdapterDome(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClick(ClickInterface clickInterface){
       this.clickInterface = clickInterface;
    }

    public interface ClickInterface{
        public void onItemClick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.item_container, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //将数据和控件绑定
//        myViewHolder.textView.setText(list.get(i));
        myViewHolder.textView.setText(list.get(myViewHolder.getAdapterPosition()));
        myViewHolder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (clickInterface != null){
//                    clickInterface.onItemClick(v, i);
                    clickInterface.onItemClick(v, myViewHolder.getAdapterPosition());
                }
            }
        });
    }
/*
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        //将数据和控件绑定
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (clickInterface != null){
                    clickInterface.onItemClick(v, position);
                }
            }
        });
    }
*/
    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
