package co.hyperverge.hyperdocscombinedapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import co.hyperverge.hyperdocscombinedapp.Activities.ImagesActivity;
import co.hyperverge.hyperdocscombinedapp.Activities.LandingActivity;
import co.hyperverge.hyperdocscombinedapp.R;

/**
 * Created by sanchit on 13/05/17.
 */

public class CompletedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    Context mContext;
    List<String> mValues = new ArrayList<>();


    public CompletedAdapter(Context context) {
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompletedHolder(inflater.inflate(R.layout.item_completed, parent, false));
    }

    public void attachTextToView(TextView textView, JSONObject result, String key)
    {
        if(result.has(key)) {
            try {
                textView.setText(result.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            textView.setText("");
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CompletedHolder completedHolder = (CompletedHolder) holder;
        try {
            JSONObject objParent = new JSONObject(mValues.get(position));

            final LandingActivity.DocumentType documentType;
            if(objParent.has("type") && objParent.getString("type").equals(LandingActivity.DocumentType.PAN.name().toLowerCase())){
                documentType = LandingActivity.DocumentType.PAN;
                completedHolder.tvDocumentType.setText("PAN");
                completedHolder.llPanDetected.setVisibility(View.VISIBLE);
                completedHolder.llPanFailed.setVisibility(View.GONE);
            }
            else if(objParent.has("type") && objParent.getString("type").equals(LandingActivity.DocumentType.PASSPORT.name().toLowerCase())){
                documentType = LandingActivity.DocumentType.PASSPORT;
                completedHolder.tvDocumentType.setText("PASSPORT");
                completedHolder.llPanDetected.setVisibility(View.VISIBLE);
                completedHolder.llPanFailed.setVisibility(View.GONE);
            }
            else if(objParent.has("type") && objParent.getString("type").equals(LandingActivity.DocumentType.AADHAAR.name().toLowerCase())){
                documentType = LandingActivity.DocumentType.AADHAAR;
                completedHolder.tvDocumentType.setText("AADHAAR");
                completedHolder.llPanDetected.setVisibility(View.VISIBLE);
                completedHolder.llPanFailed.setVisibility(View.GONE);
            }
            else {
                //failed
                completedHolder.llPanDetected.setVisibility(View.GONE);
                completedHolder.llPanFailed.setVisibility(View.VISIBLE);

//                JSONObject obj = objParent.getJSONObject("details");
                JSONObject obj = objParent;
                if(obj.has("front_url"))
                    Glide.with(mContext).load(new File(obj.getString("front_url"))).into(completedHolder.ivThumb);
                else
                    completedHolder.ivThumb.setImageBitmap(null);
                return;
            }

            completedHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagesActivity.start(mContext, mValues.get(position), documentType);
                }
            });

//            JSONObject obj = objParent.getJSONObject("details");
            JSONObject obj = objParent;

//            int keysRecognized = 0;
//
//            Iterator<?> keys = obj.keys();
//
//            while( keys.hasNext() ) {
//                keys.next();
//                keysRecognized += 1;
//            }
//
//            if(keysRecognized == 0){
//                completedHolder.llPanDetected.setVisibility(View.GONE);
//                completedHolder.llPanFailed.setVisibility(View.VISIBLE);
//            }
//            else{
//                completedHolder.llPanFailed.setVisibility(View.GONE);
//                completedHolder.llPanDetected.setVisibility(View.VISIBLE);
//            }

            if(documentType == LandingActivity.DocumentType.PASSPORT) {
                attachTextToView(completedHolder.tvName, obj, "given_name");
                attachTextToView(completedHolder.tvDocumentNo, obj, "passport_num");
                if(obj.has("front_url"))
                    Glide.with(mContext).load(new File(obj.getString("front_url"))).into(completedHolder.ivThumb);
                else
                    completedHolder.ivThumb.setImageBitmap(null);
            }
            else if(documentType == LandingActivity.DocumentType.PAN){
                attachTextToView(completedHolder.tvName, obj, "name");
                attachTextToView(completedHolder.tvDocumentNo, obj, "pan_no");
                if(obj.has("front_url"))
                    Glide.with(mContext).load(new File(obj.getString("front_url"))).into(completedHolder.ivThumb);
                else
                    completedHolder.ivThumb.setImageBitmap(null);
            }
            else if(documentType == LandingActivity.DocumentType.AADHAAR){
                attachTextToView(completedHolder.tvName, obj, "name");
                attachTextToView(completedHolder.tvDocumentNo, obj, "aadhaar");
                if(obj.has("front_url"))
                    Glide.with(mContext).load(new File(obj.getString("front_url"))).into(completedHolder.ivThumb);
                else
                    completedHolder.ivThumb.setImageBitmap(null);
            }
            //attachTextToView(completedHolder.tvDate, obj, "dob");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataset(List<String> vals) {
        mValues.clear();
        mValues.addAll(vals);
        Collections.reverse(mValues);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private class CompletedHolder extends RecyclerView.ViewHolder {
        public final ImageView ivThumb;
        public final TextView tvName;
        public final TextView tvDocumentNo;
        //public final TextView tvDate;
        public final FrameLayout llPanDetected;
        public final LinearLayout llPanFailed;
        public final TextView tvDocumentType;

        public CompletedHolder(View itemView) {
            super(itemView);
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvDocumentNo = (TextView) itemView.findViewById(R.id.passport_no);
            //tvDate = (TextView) itemView.findViewById(R.id.date);
            llPanDetected = (FrameLayout) itemView.findViewById(R.id.ll_pan_detected);
            llPanFailed = (LinearLayout) itemView.findViewById(R.id.ll_pan_failed);
            tvDocumentType = (TextView) itemView.findViewById(R.id.tv_doc_type);
        }
    }
}