package com.example.superluckylotus;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: Adapt
 * @packageName:com.example.superluckylotus
 * @description: 视频item适配器
 * @data: 2020.07.16 22:36
 **/

public class Adapter extends RecyclerView.Adapter <Adapter.LinearViewHolder>{

    private Context mContext;
    //视频上的信息,后期连接数据库，改写为数据库内的信息
    String[] username = {"username1","username2","username3"};
    String[] tab = {"#A#B","#B#C","#C#D"};
    String[] detail = {"abcde","efgh","cba"};
    String[] bgm = {"music1","music2","music3"};


    public Adapter(Context context){
        this.mContext=context;
    }//构造方法
    @Override
    //返回一个ViewHolder
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    public Adapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_video,parent,false));
    }

    @Override
    //绑定ViewHolder
    //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    public void onBindViewHolder(Adapter.LinearViewHolder holder, final int position) {
        holder.Username.setText(username[position]);
        holder.Tab.setText(tab[position]);
        holder.Detail.setText(detail[position]);
        holder.Bgm.setText(bgm[position]);
    }

    @Override
    //获取列表长度
    public int getItemCount() {
        return 3;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        //找到xml中的组件
        private TextView Username,Tab,Detail,Bgm;

        public LinearViewHolder(View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.textView_username);
            Tab = itemView.findViewById(R.id.textView_tab);
            Detail = itemView.findViewById(R.id.textView_detail);
            Bgm=itemView.findViewById(R.id.textView_bgm);
        }
    }
}

