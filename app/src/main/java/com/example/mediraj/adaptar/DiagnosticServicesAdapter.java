package com.example.mediraj.adaptar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mediraj.R;
import com.example.mediraj.helper.Constant;
import com.example.mediraj.model.AllDiagnosticModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiagnosticServicesAdapter extends RecyclerView.Adapter<DiagnosticServicesAdapter.MyViewHoldr> {
    Context context;
    List<AllDiagnosticModel.Datum> allDiagnosticModels;
    OnDiagnosticClick onDiagnosticClick;

    public DiagnosticServicesAdapter(Context context, List<AllDiagnosticModel.Datum> allDiagnosticModels, OnDiagnosticClick onDiagnosticClick) {
        this.context = context;
        this.allDiagnosticModels = allDiagnosticModels;
        this.onDiagnosticClick = onDiagnosticClick;
    }


    @NonNull
    @NotNull
    @Override
    public MyViewHoldr onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new DiagnosticServicesAdapter.MyViewHoldr(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_diagnostic_service_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHoldr holder, int position) {

        if (allDiagnosticModels.get(position).isChecked()){
            holder.addToCart_btn.setText(context.getText(R.string.card_added));
            holder.addToCart_btn.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow));
        }else {
            holder.addToCart_btn.setText(context.getString(R.string.add_to_cart));
            holder.addToCart_btn.setBackgroundTintList(context.getResources().getColorStateList(R.color.primaryColor));
        }

        holder.diagnostic_name.setText(allDiagnosticModels.get(position).getTitle());
        holder.diagnostic_price.setText(context.getString(R.string.moneySymbol) + " " + String.valueOf(allDiagnosticModels.get(position).getPrice()));

        Glide.with(context)
                .load(Constant.Diagonestic_AVATAR_URL + allDiagnosticModels.get(position).getLogo())
                .apply(new RequestOptions().placeholder(R.drawable.ic_diagonistic))
                .into(holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return allDiagnosticModels.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchList(List<AllDiagnosticModel.Datum> filteredList) {
        allDiagnosticModels = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHoldr extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView diagnostic_name, diagnostic_price;
        Button addToCart_btn;
        ImageView diagnostic_love;

        public MyViewHoldr(@NonNull @NotNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.cirIM_dia);
            diagnostic_name = itemView.findViewById(R.id.diagonstic_name);
            diagnostic_price = itemView.findViewById(R.id.diagonstic_price);
            addToCart_btn = itemView.findViewById(R.id.addToCart_btn);
            diagnostic_love = itemView.findViewById(R.id.diagonstic_love);


            //on click listener on addToCartBtn
            addToCart_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!allDiagnosticModels.get(getAdapterPosition()).isChecked()) {
                        allDiagnosticModels.get(getAdapterPosition()).setChecked(true);
                        onDiagnosticClick.sendOrDeleteData(getAdapterPosition(), "add");
                    } else {
                        allDiagnosticModels.get(getAdapterPosition()).setChecked(false);
                        onDiagnosticClick.sendOrDeleteData(getAdapterPosition(), "delete");

                    }
                    notifyDataSetChanged();
                }
            });

        }
    }

    //interface to some data manipulation
    public interface OnDiagnosticClick {
        void sendOrDeleteData(int position, String opType);
    }
}
