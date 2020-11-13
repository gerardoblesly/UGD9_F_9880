package com.ugd9_x_yyyy.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ugd9_x_yyyy.API.BukuAPI;
import com.ugd9_x_yyyy.Models.DTBuku;
import com.ugd9_x_yyyy.R;
import com.ugd9_x_yyyy.Views.ViewsCart;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class AdapterDTBuku extends RecyclerView.Adapter<AdapterDTBuku.adapterSubItemViewHolder> {

    private List<DTBuku> listDTBuku;
    private Context context;
    private View view;
    private Double totalBiaya, subTotal;
    private AdapterDTBuku.OnQuantityChangeListener mListener;

    public AdapterDTBuku(Context context, List<DTBuku> listDTBuku,
                         AdapterDTBuku.OnQuantityChangeListener mListener) {
        this.context        = context;
        this.listDTBuku     = listDTBuku;
        this.mListener      = mListener;
        this.totalBiaya     = 0.0;
        this.subTotal       = 0.0;
    }

    @NonNull
    @Override
    public AdapterDTBuku.adapterSubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_dtbuku, parent, false);
        adapterSubItemViewHolder holder = new AdapterDTBuku.adapterSubItemViewHolder(view);

        holder.jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalBiaya = subTotal = 0.0;
                for(int i=0; i<listDTBuku.size(); i++) {
                    totalBiaya += (listDTBuku.get(i).getJumlah()*listDTBuku.get(i).getHarga());
                    if(listDTBuku.get(i).isChecked)
                        subTotal += (listDTBuku.get(i).getJumlah()*listDTBuku.get(i).getHarga());
                }

                mListener.onQuantityChange(totalBiaya, subTotal);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterDTBuku.adapterSubItemViewHolder holder, final int position) {
        final DTBuku dtb = listDTBuku.get(position);

        if(dtb.isChecked) {
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.nama.setText(dtb.getNamaBuku());
        holder.harga.setText("Rp "+ formatter.format(dtb.getHarga()));
        holder.jumlah.setText(String.valueOf(dtb.getJumlah()));
        Glide.with(context)
                .load(BukuAPI.URL_IMAGE +dtb.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.gambar);

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.jumlah.getText().toString());

                jumlah -= 1;

                if(jumlah < 1)
                    jumlah = 1;

                listDTBuku.get(position).setJumlah(jumlah);
                holder.jumlah.setText(String.valueOf(jumlah));
                notifyDataSetChanged();
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.jumlah.getText().toString());

                jumlah += 1;

                listDTBuku.get(position).setJumlah(jumlah);
                holder.jumlah.setText(String.valueOf(jumlah));
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    listDTBuku.get(position).isChecked = true;
                }
                else {
                    listDTBuku.get(position).isChecked = false;
                }
                mListener.onQuantityChange(totalBiaya, hitungSubTotal(listDTBuku));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listDTBuku != null) ? listDTBuku.size() : 0;
    }

    public class adapterSubItemViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView nama, harga;
        private EditText jumlah;
        private Button btnPlus, btnMinus;
        private ImageView btnHapus, gambar;

        public adapterSubItemViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            nama     = itemView.findViewById(R.id.namaBuku);
            harga    = itemView.findViewById(R.id.harga);
            jumlah   = itemView.findViewById(R.id.jumlah);
            btnMinus = itemView.findViewById(R.id.btnMin);
            btnPlus  = itemView.findViewById(R.id.btnPlus);
            gambar   = itemView.findViewById(R.id.gambar);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange( Double totalBiaya, Double subtotal);
    }

    public Double hitungSubTotal(List<DTBuku> dtBukuList)
    {
        Double subTotal = 0.0;
        for(DTBuku dt : dtBukuList){
            if(dt.isChecked)
                subTotal += (dt.getJumlah()*dt.getHarga());
        }
        return subTotal;
    }
}