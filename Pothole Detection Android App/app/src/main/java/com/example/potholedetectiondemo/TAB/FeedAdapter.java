package com.example.potholedetectiondemo.TAB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.potholedetectiondemo.R;
import com.example.potholedetectiondemo.model.IssueModel;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PageViewHolder> {

    private static final String TAG = "FeedRecyclerAdapter";
    private Context context;
    private List<IssueModel> issueList;

    public FeedAdapter(Context context, List<IssueModel> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.issues_card_view, parent, false);
        PageViewHolder pageViewHolder = new PageViewHolder(view);
        return pageViewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final PageViewHolder holder, int position) {
        final IssueModel issueModelClass = issueList.get(position);
        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
        holder.tvStatus.setText(issueModelClass.getStatus());
        holder.descriptionTexView.setText(issueModelClass.getDescription());
        holder.TitleTextView.setText(issueModelClass.getTitle());
        Glide.with(context).load(issueModelClass.getImageUrl()).into(holder.ImageView);

        if (issueModelClass.isCheckLiked()) {
            holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
        } else {
            holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
//        holder.ImageView.setImageDrawable(Glide.get(context).);
        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (!issueModelClass.isCheckLiked()) {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                        issueModelClass.setCheckLiked(true);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));

                    } else {
                        holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        issueModelClass.setCheckLiked(false);
                        issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                        holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
                    }
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    Log.d("TEST", "onSingleTap");
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                return true;
            }

        });

        holder.UpVoteLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!issueModelClass.isCheckLiked()) {
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                    issueModelClass.setCheckLiked(true);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() + 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));
                } else {
                    holder.UpVoteLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    issueModelClass.setCheckLiked(false);
                    issueModelClass.setVotes((int) (issueModelClass.getVotes() - 1));
                    holder.UpvotesTextView.setText(String.valueOf(issueModelClass.getVotes()));

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return issueList.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView;
        TextView descriptionTexView;
        TextView AreaTexView;
        TextView UpvotesTextView;
        android.widget.ImageView ImageView;
        ImageButton UpVoteLikeButton;
        CardView cardView;
        private TextView tvStatus;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.TitleTextView);
            descriptionTexView = itemView.findViewById(R.id.DescriptionTextView);
            UpvotesTextView = itemView.findViewById(R.id.UpvotesTextView);
            ImageView = itemView.findViewById(R.id.PostImageView);
            UpVoteLikeButton = itemView.findViewById(R.id.likeIcon);
            cardView = itemView.findViewById(R.id.CardView);
            AreaTexView = itemView.findViewById(R.id.AreaTextView);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }
}
