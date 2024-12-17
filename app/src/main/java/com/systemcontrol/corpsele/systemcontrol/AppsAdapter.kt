package com.systemcontrol.corpsele.systemcontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AppsAdapter(private val items: List<AppDatas>, private val clickListener: (String) -> Unit, private val longClickListener: (String) -> Boolean) : RecyclerView.Adapter<AppsAdapter.AppsAdapterHolder>() {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvPackageName: TextView = view.findViewById(R.id.tvPackageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.apps_recyclerview, parent, false)
        return AppsAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: AppsAdapterHolder, position: Int) {
        val item = items[position]
        holder.tvAppName.text = item.appName
        holder.tvPackageName.text = item.packageName
        holder.itemView.setOnClickListener { clickListener(item.packageName) }
        holder.itemView.setOnLongClickListener { longClickListener(item.packageName) }

                // 设置 item view 的高度
//        val height = holder.tvAppName.height + holder.tvPackageName.height
//        holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    }

    override fun getItemCount() = items.size

    class AppsAdapterHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // 初始化视图
        // ...
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvPackageName: TextView = view.findViewById(R.id.tvPackageName)

        // 设置点击监听器
        fun setOnClickListener(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        // 设置长按监听器
        fun setOnLongClickListener(listener: View.OnLongClickListener) {
            itemView.setOnLongClickListener(listener)
        }
    }
}
