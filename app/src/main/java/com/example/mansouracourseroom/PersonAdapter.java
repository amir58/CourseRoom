package com.example.mansouracourseroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder> {

    List<Person> personList;
    PersonAdapterClick adapterClick;

    public PersonAdapter(List<Person> personList, PersonAdapterClick adapterClick) {
        this.personList = personList;
        this.adapterClick = adapterClick;
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Person person = personList.get(position);

        holder.textViewId.setText(String.valueOf(person.getId()));
        holder.textViewName.setText(person.getName());
        holder.textViewSpec.setText(person.getSpecialization());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    class PersonHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewName, textViewSpec;
        public PersonHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.person_item_id_tv);
            textViewName = itemView.findViewById(R.id.person_item_name_tv);
            textViewSpec = itemView.findViewById(R.id.person_item_spec_tv);
        }
    }

}
