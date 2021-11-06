package com.example.tp_android.ui.fichaClinica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.data.model.FichaClinica;
import com.example.tp_android.data.model.Lista;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FichaClinicaAdapter extends BaseAdapter implements Filterable {

    private List<FichaClinica> values;
    private List<FichaClinica> filteredValues;
    private Context context;
    ValueFilter valueFilter;

    public FichaClinicaAdapter( Context context, FichaClinica[] values ) {
        this.context = context;
        this.values = Arrays.asList(values);
        this.filteredValues = Arrays.asList(values);
    }

    @Override
    public int getCount ( ) {
        return values.size();
    }

    @Override
    public FichaClinica getItem(int position) {
        return values.get(position);
    }

    public FichaClinica getItemAtPosition ( int position) {
        return values.get(position);
    }

    @Override
    public long getItemId ( int position ) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));

        View rowView = inflater.inflate(R.layout.fragment_ficha_clinica_item, parent, false);
        TextView idView = rowView.findViewById(R.id.fichaId);
        TextView empleadoView = rowView.findViewById(R.id.empleado);
        idView.setText(values.get(position).getIdFichaClinica().toString());
        empleadoView.setText(values.get(position).getIdEmpleado().getNombre() + " " + values.get(position).getIdEmpleado().getApellido());
        TextView clienteView = rowView.findViewById(R.id.cliente);
        clienteView.setText(values.get(position).getIdCliente().getNombre() + " " + values.get(position).getIdCliente().getApellido());
        TextView obserView = rowView.findViewById(R.id.observacion);
        obserView.setText(values.get(position).getObservacion());

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if ( valueFilter == null ) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltered ( CharSequence constraint ) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<FichaClinica> filterList = new ArrayList<>();

                for ( int i = 0; i < filteredValues.size(); i++) {
                    if ((filteredValues.get(i).getIdCliente().getNombreCompleto().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(filteredValues.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filteredValues;
            } else {
                results.count = filteredValues.size();
                results.values = filteredValues;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            values = (List<FichaClinica> results.values);
            notifyDataSetChanged();
        }
    }
}
