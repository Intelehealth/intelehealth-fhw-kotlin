package org.intelehealth.app.ui.prescription.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.intelehealth.app.R;
import org.intelehealth.common.ui.adapter.BaseRecyclerViewAdapter;
import org.intelehealth.data.offline.entity.Patient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import org.intelehealth.app.databinding.PrescriptionListItemBinding
import org.intelehealth.app.databinding.RowItemHelpFaqBinding
import org.intelehealth.app.ui.help.viewholder.FAQViewHolder
import org.intelehealth.app.ui.prescription.viewholder.PrescriptionViewHolder
import org.intelehealth.data.offline.entity.Prescription

/**
 * Created by Tanvir Hasan on 24/04/25.
 * Email: mhasann@intelihealth.org
 */
class PrescriptionRecyclerViewAdapter(
    context: Context,
    private var prescriptions: MutableList<Prescription>
) : BaseRecyclerViewAdapter<Prescription>(context, prescriptions) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PrescriptionListItemBinding.inflate(inflater, parent, false)
        return PrescriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewHolderClickListener?.let { (holder as PrescriptionViewHolder).setViewClickListener(it) }
        (holder as PrescriptionViewHolder).bind(getItem(position))
    }

    fun updateList(newList: MutableList<Prescription>) {
        val olderListSize = prescriptions.size
        prescriptions.addAll(newList)
        notifyItemRangeChanged(olderListSize-1, newList.size)
    }

}
