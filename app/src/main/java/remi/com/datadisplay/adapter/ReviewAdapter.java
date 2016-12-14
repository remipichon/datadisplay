package remi.com.datadisplay.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import remi.com.datadisplay.R;
import remi.com.datadisplay.model.Review;

public class ReviewAdapter extends ArrayAdapter<Review> {
    LayoutInflater vi;

    private final ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, R.layout.review_list_item,reviews);

        this.reviews = reviews;

        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = vi.inflate(R.layout.review_list_item, null);

            holder = new ViewHolder();
            holder.rating = (TextView) convertView.findViewById(R.id.review_list_item_rating);
            holder.browser = (TextView) convertView.findViewById(R.id.review_list_item_browser_name);
            holder.platform = (TextView) convertView.findViewById(R.id.review_list_item_platform);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Review review = reviews.get(position);

        holder.rating.setText(review.getRating().toString());
        holder.browser.setText(review.getBrowserName());
        holder.platform.setText(review.getPlatform());

        return convertView;

    }



    private class ViewHolder {
        TextView rating;
        TextView browser;
        TextView platform;
    }


}
