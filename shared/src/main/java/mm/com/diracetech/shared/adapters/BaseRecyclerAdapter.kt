package mm.com.diracetech.shared.adapters

import androidx.recyclerview.widget.RecyclerView
import mm.com.diracetech.shared.viewholders.BaseViewHolder

abstract class BaseRecyclerAdapter<VH : BaseViewHolder<T>, T> : RecyclerView.Adapter<VH>() {

    private var dataList: MutableList<T> = ArrayList()

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.data = dataList[position]
    }

    fun setNewData(newData: MutableList<T>) {
        dataList = newData
        notifyDataSetChanged()
    }
}