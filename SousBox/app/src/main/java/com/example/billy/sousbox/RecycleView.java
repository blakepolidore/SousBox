package com.example.billy.sousbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.sousbox.api.SpoonacularObjects;

import java.util.LinkedList;

/**
 * Created by Billy on 4/29/16.
 */
public class RecycleView extends RecyclerView.Adapter<RecycleView.RecyclerViewHolder> {

    SpoonacularObjects[] data = new SpoonacularObjects[0];
    //LinkedList<SpoonacularObjects> listData;
    Context context;
    private static OnItemClickListener listener;


    public RecycleView(SpoonacularObjects[] data) {
        this.data = data;
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // this is where we setup TextView and all

        public RecyclerViewHolder (final View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });

        }

    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//        holder.headline.setText(data.get(position).getTitle());
//        holder.articleAbstract.setText(data.get(position).getAbstractResult());
//        holder.ago.setText(agoText);
//
//        String imageURI = data.get(position).getThumbnail_standard();
//        if (imageURI.isEmpty()) {
//            imageURI = "R.drawable.nyt_icon";
//        }
//
//        Picasso.with(context)
//                .load(imageURI)
//                .placeholder(R.drawable.nyt_icon)
//                .resize(100, 100)
//                .centerCrop()
//                .into(holder.imageIcon);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycleview_custom_layout, parent, false);

        RecyclerViewHolder vh = new RecyclerViewHolder(view);

        return vh;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public void changeDataSet(SpoonacularObjects[] data){
        this.data = data;
        notifyDataSetChanged();
    }

}
