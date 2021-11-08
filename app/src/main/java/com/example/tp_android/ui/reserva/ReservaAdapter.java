package com.example.tp_android.ui.reserva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.data.model.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReservaAdapter extends BaseAdapter implements Filterable {

    private List<Reserva> values;
    private List<Reserva> filteredValues;
    private Context context;
    ValueFilter valueFilter;

    public ReservaAdapter(Context context, Reserva[] values){
        this.context = context;
        this.values = Arrays.asList(values);
        this.filteredValues = Arrays.asList(values);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Reserva getItem(int position) {
        return values.get(position);
    }

    public Reserva getItemAtPosition(int position) {return values.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.fragment_reserva_item, parent, false);

        TextView idView = (TextView) rowView.findViewById(R.id.text_id2);
        TextView empleadoView = (TextView) rowView.findViewById(R.id.text_nombre2);
        TextView nombreView = (TextView) rowView.findViewById(R.id.text_apellido2);
        TextView fechaView = (TextView) rowView.findViewById(R.id.fechaReserva);

        idView.setText(values.get(position).getIdReserva().toString());
        empleadoView.setText(values.get(position).getIdEmpleado().getApellido().toUpperCase());
        nombreView.setText(values.get(position).getIdCliente().getNombreCompleto());
        SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = parseador.parse(values.get(position).getFecha());
            fechaView.setText(formateador.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                List<Reserva> filterList = new ArrayList<>();
                for (int i = 0; i < filteredValues.size(); i++) {
                    if ((filteredValues.get(i).getIdCliente().getNombreCompleto().toUpperCase()).contains(constraint.toString().toUpperCase())
                            ||(filteredValues.get(i).getIdEmpleado().getNombreCompleto().toUpperCase()).contains(constraint.toString().toUpperCase())) {
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
            values = (List<Reserva>) results.values;
            notifyDataSetChanged();
        }

    }
}
