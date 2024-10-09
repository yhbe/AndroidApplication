package com.example.d308vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    // This ExcursionAdapter class extending RecyclerView.Adapter is a great example of inheritence.
    // By extending RecyclerView.Adapter, ExcursionAdapter gains the properties and methods of the Adapter class.
    // Inheritence is a great way to give classes predefined methods and properties from parent classes.
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private final TextView excursionItemView3;
        private final TextView excursionItemView4;


        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView2);
            excursionItemView2 = itemView.findViewById(R.id.textView3);
            excursionItemView3 = itemView.findViewById(R.id.textView4);
            excursionItemView4 = itemView.findViewById(R.id.textView5);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Excursion current = mExcursions.get(position);
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("excursionID", current.getExcursionID());
                intent.putExtra("excursionTitle", current.getExcursionTitle());
                intent.putExtra("excursionDate", current.getExcursionDate());
                intent.putExtra("vacationID", current.getVacationID());
                context.startActivity(intent);
            });
        }
    }

    public ExcursionAdapter(Context context){
        mInflater=LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }
    // By overriding this predefined inherited method, the code here shows polymorphism.
    // Polymorphism refers to the ability of a subclass to redefine inherited properties or methods.

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions!=null){
            Excursion current=mExcursions.get(position);
            String title =current.getExcursionTitle();

            String vacationIDTxtString = "Vacation ID: " + (current.getVacationID());
            String excursionIDTxtString = "Excursion ID: " + (current.getExcursionID());

            holder.excursionItemView.setText(title);
            holder.excursionItemView2.setText(current.getExcursionDate());
            holder.excursionItemView3.setText(vacationIDTxtString);
            holder.excursionItemView4.setText(excursionIDTxtString);
        }
        else{
            holder.excursionItemView.setText("No excursion title");
            holder.excursionItemView2.setText("No excursion id");
        }
    }

    public void setExcursions(List<Excursion> excursions){
        mExcursions=excursions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mExcursions!=null) return mExcursions.size();
        else return 0;
    }


}
