package com.ugd9_x_yyyy.Adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ugd9_x_yyyy.Models.DTBuku;
import com.ugd9_x_yyyy.Models.TransaksiBuku;
import com.ugd9_x_yyyy.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterTransaksiBuku extends RecyclerView.Adapter<AdapterTransaksiBuku.adapterItemViewHolder> {

    private List<TransaksiBuku> transaksiBukuList;
    private AdapterDTBuku adapterDTBuku;
    private Context context;
    private View view;
    private Double totalBiaya, subTotal;
    private OnQuantityChangeListener mListener;

    public AdapterTransaksiBuku(Context context, List<TransaksiBuku> transaksiBukuList,
                                OnQuantityChangeListener mListener) {
        this.context            = context;
        this.transaksiBukuList  = transaksiBukuList;
        this.mListener          = mListener;
        this.totalBiaya         = 0.0;
        this.subTotal           = 0.0;
    }

    @NonNull
    @Override
    public adapterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_transaksi_buku, parent, false);
        final adapterItemViewHolder holder =  new AdapterTransaksiBuku.adapterItemViewHolder(view);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final adapterItemViewHolder holder, final int position) {
        final TransaksiBuku transaksiBuku = transaksiBukuList.get(position);

        if(transaksiBuku.isChecked || isCheckedBox(transaksiBukuList))
        {
            holder.panelBayar.setVisibility(View.GONE);
            if(transaksiBuku.isChecked)
                holder.checkBox.setChecked(true);
        }
        else
        {
            holder.checkBox.setChecked(false);
            holder.panelBayar.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    transaksiBukuList.get(position).isChecked = true;

                    if(subTotal == 0.0 || isEmptyChildChecked(transaksiBuku)){
                        for(int i=0; i<transaksiBukuList.get(position).getDtBukuList().size(); i++){
                            transaksiBukuList.get(position).getDtBukuList().get(i).isChecked = true;
                        }
                    }
                    holder.panelBayar.setVisibility(View.GONE);

                } else {
                    transaksiBukuList.get(position).isChecked = false;
                    for(int i=0; i<transaksiBukuList.get(position).getDtBukuList().size(); i++){
                        transaksiBukuList.get(position).getDtBukuList().get(i).isChecked = false;
                    }

                    holder.panelBayar.setVisibility(View.VISIBLE);
                }

                holder.recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });

        adapterDTBuku = new AdapterDTBuku(view.getContext(),
                transaksiBuku.getDtBukuList(), new AdapterDTBuku.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(Double total, Double subtotal) {
                totalBiaya = total;
                subTotal = subtotal;

                if(subtotal!=0)
                    holder.checkBox.setChecked(true);
                else
                    holder.checkBox.setChecked(false);

                NumberFormat formatter = new DecimalFormat("#,###");
                holder.tvNamaMahasiswa.setText(transaksiBuku.getNamaToko());
                holder.tvSubtotal.setText("Rp "+ formatter.format(total));

                transaksiBukuList.get(position).setTotalBiaya(total);
                mListener.onQuantityChange(
                        hitungSubTotal(transaksiBukuList),
                        total,
                        isFullChecked(),
                        isEmptyChecked());
            }
        });
        //Modifikasi code disini

        int orientation = view.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));
        } else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            holder.recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        }
        //

        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(adapterDTBuku);
    }

    @Override
    public int getItemCount() {
        return (transaksiBukuList != null) ? transaksiBukuList.size() : 0;
    }

    public class adapterItemViewHolder  extends RecyclerView.ViewHolder{

        RecyclerView recyclerView;
        CheckBox checkBox;
        TextView tvNamaMahasiswa, tvSubtotal;
        Button btnBayar;
        CardView panelBayar, panelTransaksi;

        public adapterItemViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox        = itemView.findViewById(R.id.checkBox);
            recyclerView    = itemView.findViewById(R.id.recycler_view);
            tvNamaMahasiswa = itemView.findViewById(R.id.tvNamaMahasiswa);
            tvSubtotal      = itemView.findViewById(R.id.subtotal);
            btnBayar        = itemView.findViewById(R.id.btnBayar);
            panelBayar      = itemView.findViewById(R.id.panelBayar);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange( Double totalBiaya, Double subTotal, Boolean full, Boolean empty);
    }

    public Double hitungSubTotal(List<TransaksiBuku> transaksiBukus)
    {
        Double total = 0.0;
        for(TransaksiBuku tb : transaksiBukus){
            for(DTBuku dtb : tb.getDtBukuList()){
                if(dtb.isChecked)
                    total += dtb.getJumlah()*dtb.getHarga();
            }
        }
        return total;
    }

    public Boolean isCheckedBox(List<TransaksiBuku> transaksiBukuList)
    {
        for (TransaksiBuku tb : transaksiBukuList) {
            if(tb.isChecked)
                return true;
        }
        return false;
    }

    public Boolean isFullChecked(){
        for (TransaksiBuku tb : transaksiBukuList) {
            if(!tb.isChecked)
                return false;
            else
            {
                for (DTBuku dtb : tb.getDtBukuList()) {
                    if(!dtb.isChecked)
                        return false;
                }
            }
        }
        return true;
    }

    public Boolean isEmptyChecked(){
        for (TransaksiBuku tb : transaksiBukuList) {
            if(tb.isChecked)
                return false;
            else
            {
                for (DTBuku dtb : tb.getDtBukuList()) {
                    if(dtb.isChecked)
                        return false;
                }
            }
        }
        return true;
    }

    public Boolean isEmptyChildChecked(TransaksiBuku tb){
        for (DTBuku dtb : tb.getDtBukuList()) {
            if(dtb.isChecked)
                return false;
        }
        return true;
    }
}