package io.appgain.demo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.appgain.demo.R;

public class PersonalizationAdapter extends GenericAdapter<PersonalizationModel> {

    public PersonalizationAdapter( List<PersonalizationModel> items) {
        super(items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = inflate(R.layout.personalization_item ,  parent) ;
        return new PersonalizationViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        PersonalizationViewHodler holder = (PersonalizationViewHodler) viewHolder;
        PersonalizationModel item = getItem(position) ;
        Context context = viewHolder.itemView.getContext();
        holder.value.setText(item.value);
        holder.name.setText(item.name);
    }

    public  static  class PersonalizationViewHodler extends RecyclerView.ViewHolder{



        @BindView(R.id.delete)
        View delete ;

        @BindView(R.id.value)
        public EditText value;

        @BindView(R.id.name)
        public EditText name;

        public PersonalizationViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView) ;
        }
    }
}
