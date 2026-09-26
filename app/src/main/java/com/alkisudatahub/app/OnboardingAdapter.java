package com.alkisudatahub.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.SlideHolder> {

    private final List<OnboardingSlide> slides;

    OnboardingAdapter(List<OnboardingSlide> slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public SlideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_onboarding, parent, false);
        return new SlideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideHolder holder, int position) {
        OnboardingSlide slide = slides.get(position);
        holder.icon.setImageResource(slide.iconRes);
        holder.title.setText(slide.titleRes);
        holder.body.setText(slide.bodyRes);
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    static class SlideHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView body;

        SlideHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.slide_icon);
            title = itemView.findViewById(R.id.slide_title);
            body = itemView.findViewById(R.id.slide_body);
        }
    }
}
