package com.example.demoanalytic


import android.net.wifi.ScanResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_net_device.view.*

/**
 * 文件：IrBeanAdapter
 * 时间：2018/8/17.
 * 备注：
 */
class IrBrandAdapterAp(private var devices: List<ScanResult>)
    : ApBaseAdapter<IrBrandAdapterAp.ViewHolder>() {

    private var searchTxt = ""
    fun setSearchText(searchTxt: String) {
        this.searchTxt = searchTxt
    }

    override fun onBindHolder(holder: ViewHolder, position: Int) {
        val irDevice = devices[position]
        holder.tvName.text = irDevice.SSID

    }


    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_net_device, parent, false))
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvName = item.tv_name!!

    }

}