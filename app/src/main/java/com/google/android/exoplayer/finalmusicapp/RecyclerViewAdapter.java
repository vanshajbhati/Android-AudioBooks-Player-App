package com.google.android.exoplayer.finalmusicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

import static com.google.android.exoplayer.finalmusicapp.MainRecyclerviewActivity.motionLayout;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.simpleExoPlayer;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    private Context mContext ;
    private ArrayList<TrackFiles> mData ;
    ArrayList<TrackFiles> trackFilesArrayListFull;



    public RecyclerViewAdapter(Context mContext, ArrayList<TrackFiles> mData) {
        this.mContext = mContext;
        this.trackFilesArrayListFull =  mData;
        this.mData = new ArrayList<>(trackFilesArrayListFull);

    }


    @Override
    public MyViewHolder onCreateViewHolder(@Nullable  ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardveiw_item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @Nullable MyViewHolder holder, int position   ){
     holder.book_title.setText(mData.get(position).getTitle() );
     holder.book_arthur.setText(mData.get(position).getSinger());

       Glide.with(mContext).load(mData.get(position).getThumbnailUrl()).into(holder.img_book_thumbnail);


       Boolean musicBarBoolean=false;
       String image_url = mData.get(position).getThumbnailUrl();
       String title = mData.get(position).getTitle();
       String author = mData.get(position).getSinger();
       int duration = mData.get(position).getduration();
       String audioUrl = mData.get(position).getUrlLink();



       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               motionLayout.transitionToEnd();

               Intent intent = new Intent(mContext, MediaPlayer_Activity.class);
               intent.putExtra("musicBar",musicBarBoolean);
               intent.putExtra("position",position);
               intent.putExtra("imageUrl", image_url);
               intent.putExtra("title", title);
               intent.putExtra("author", author);
               intent.putExtra("duration", duration );
               intent.putExtra("audioUrl", audioUrl);

               if(simpleExoPlayer !=null){
               if(simpleExoPlayer.isPlaying()){
                   simpleExoPlayer.release();

               }else{
                   simpleExoPlayer.release();
               }

               }
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       mContext.startActivity(intent);
                   }
               }, 360);
           }
       });

        }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    //TODO Filter for search

    @Override
    public Filter getFilter() {
        return trackFileFilter;
    }

    private final Filter trackFileFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<TrackFiles> filteredTrackFiles = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredTrackFiles.addAll(trackFilesArrayListFull);
            }else

        {
            String filterPattern = charSequence.toString().toLowerCase().trim();

            for(TrackFiles trackFiles: trackFilesArrayListFull){


//TODO ---------- filter --------filter --------------------- filter -------------------------------------***********

                if(trackFiles.getTitle().toLowerCase().contains(filterPattern) || trackFiles.getSinger().toLowerCase().contains(filterPattern)){
                    filteredTrackFiles.add(trackFiles); }
            }
        }

            FilterResults results = new FilterResults();
            results.values = filteredTrackFiles;
            results.count = filteredTrackFiles.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            mData.clear();
            mData.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView book_title, book_arthur;
        ImageView img_book_thumbnail;
        NeumorphCardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);


            book_title= (TextView) itemView.findViewById(R.id.book_title_id);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
           cardView =(NeumorphCardView) itemView.findViewById(R.id.cardView_id);
            book_arthur = (TextView) itemView.findViewById(R.id.book_author_id);

        }
    }
}
