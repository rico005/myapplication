package com.example.fangsheng.myapplication.baidupic;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.fangsheng.myapplication.R;
import com.example.fangsheng.myapplication.remote.RemoteBusiness;
import com.squareup.picasso.Picasso;

public class BaiduPicListAdapter extends RecyclerView.Adapter<BaiduPicListAdapter.ViewHolder>{

    public List<String> picUrls;
    private Context context;

    public BaiduPicListAdapter(Context context){
        this.context = context;
    }

    @Override
    public BaiduPicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_display_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaiduPicListAdapter.ViewHolder holder, int position) {
        String url = picUrls.get(position);

        Picasso.with(context).load(url).resize(800, 400).into(holder.picImg);

        //holder.picImg.setTag(url);
        //new BitmapGetTask(holder.picImg).execute(url);
    }

    @Override
    public int getItemCount() {
        if (picUrls != null){
            return picUrls.size();
        }
        return 0;
    }

    public void refresh(List<String> urls){
        picUrls = urls;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picImg;

        public ViewHolder(View itemView) {
            super(itemView);

            picImg = (ImageView)itemView.findViewById(R.id.pic_img);
        }
    }

    public static class BitmapGetTask extends AsyncTask<String, Void, Bitmap>{

        private ImageView img;
        private String url;

        public BitmapGetTask(ImageView img){
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            this.url = url[0];
            return RemoteBusiness.getBitmapFromUrl(this.url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (img.getTag() != null){
                String revUrl = (String)img.getTag();
                if (this.url.equals(revUrl)){
                    img.setImageBitmap(bitmap);
                }
            }
        }
    }
}
