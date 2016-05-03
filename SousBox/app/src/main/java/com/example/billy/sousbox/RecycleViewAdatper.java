package com.example.billy.sousbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.sousbox.api.SpoonacularObjects;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Billy on 4/29/16.
 */
public class RecycleViewAdatper extends RecyclerView.Adapter<RecycleViewAdatper.RecyclerViewHolder> {


    ArrayList<SpoonacularObjects> data;
    //SpoonacularObjects[] data = new SpoonacularObjects[0];
    //LinkedList<SpoonacularObjects> listData;
    Context context;
    private static OnItemClickListener listener;


    public RecycleViewAdatper(ArrayList<SpoonacularObjects> data) {
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
        ImageView recipeImageView;
        TextView recipeTitleText;

        public RecyclerViewHolder (final View itemView) {
            super(itemView);

            recipeImageView = (ImageView) itemView.findViewById(R.id.saved_recipe_imageOne_id);
            recipeTitleText = (TextView) itemView.findViewById(R.id.saved_recipe_imageOne_title_id);

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
        holder.recipeTitleText.setText(data.get(position).getTitle());
//
        String imageURI = data.get(position).getImage();
        if (imageURI.isEmpty()) {
            imageURI = "R.drawable.ic_menu_gallery";
        }

        Picasso.with(context)
                .load("https://webknox.com/recipeImages/"+ imageURI)
                .placeholder(R.drawable.ic_menu_gallery)
                .resize(100, 100)
                .centerCrop()
                .into(holder.recipeImageView);

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
        return data.size();
    }

    public void changeDataSet(SpoonacularObjects[] data){
        //this.data = data;
        notifyDataSetChanged();
    }

}
