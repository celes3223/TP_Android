package com.example.tp_android.ui.paciente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.data.model.Persona;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacienteAdapter  extends BaseAdapter implements Filterable {

    private List<Persona> values;
    private List<Persona> filteredValues;
    private Context context;
    ValueFilter valueFilter;

    public PacienteAdapter(Context context, Persona[] values){
        this.context = context;
        this.values = Arrays.asList(values);
        this.filteredValues = Arrays.asList(values);
    }
    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Persona getItem(int position) {
        return values.get(position);
    }

    public Persona getItemAtPosition(int position) {return values.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.fragment_paciente_item, parent, false);
        TextView idView = rowView.findViewById(R.id.fichaId);
        TextView nombreView = rowView.findViewById(R.id.fichaNombre);
        idView.setText(values.get(position).getIdPersona().toString());
        nombreView.setText(values.get(position).getNombre() + " " + values.get(position).getApellido());
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Persona> filterList = new ArrayList<>();
                for (int i = 0; i < filteredValues.size(); i++) {
                    if ((filteredValues.get(i).getNombreCompleto().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(filteredValues.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filteredValues.size();
                results.values = filteredValues;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            values = (List<Persona>) results.values;
            notifyDataSetChanged();
        }

    }

}
