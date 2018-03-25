package co.hyperverge.hyperdocscombinedapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.hyperverge.hyperdocscombinedapp.R;
import co.hyperverge.hyperdocscombinedapp.Utils.Configs;
import co.hyperverge.hyperdocssdk.workflows.ocr.activities.BaseActivity;

/**
 * Created by sanchit on 13/05/17.
 */

public class ImagesActivity extends BaseActivity {

    private String mResult;
    private LandingActivity.DocumentType documentType;
    private LinearLayout llFront;
    private LinearLayout llResultFront;
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvDob;
    private TextView tvPassportNo;
    private TextView tvGender;
    private TextView tvDoi;
    private TextView tvDoe;
    private TextView tvMother;
    private TextView tvFather;
    private TextView tvAddress;
    private TextView tvSpouse;

    private ImageView ivFront;
    private ImageView ivBack;

    private LayoutInflater inflater;

    public static void start(Context context, String result, LandingActivity.DocumentType documentType) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("result", result);
        intent.putExtra("documentType", documentType.name());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        if(!getIntent().hasExtra("result") || !getIntent().hasExtra("documentType"))
            return;
        mResult = getIntent().getStringExtra("result");
        documentType = LandingActivity.DocumentType.valueOf(getIntent().getStringExtra("documentType"));

        inflater = LayoutInflater.from(ImagesActivity.this);
        setupViews();
    }

    private void setTextFromJSON(JSONObject json, TextView tv, String tag)
    {
        if(json.has(tag)) {
            try {
                tv.setText(json.getString(tag));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            tv.setText("-");
        }
    }

    private void setupViews() {
        findViewById(R.id.iv_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        JSONObject resultJSON = new JSONObject();
        try {
            resultJSON = new JSONObject(mResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.headingTV)).setText(documentType.name());

        String frontKeyList[] = new String[]{};
        String backKeyList[] = new String[]{};
        HashMap<String, String> keyToNameMap = new HashMap<>();

        if(documentType == LandingActivity.DocumentType.PAN){
            frontKeyList = Configs.PAN_ORDERED_KEYS;
            keyToNameMap = Configs.PAN_JSON_FIELD_KEY_TO_NAME_MAP();
        }
        else if(documentType == LandingActivity.DocumentType.PASSPORT){
            frontKeyList = Configs.PASSPORT_ORDERED_FRONT_KEYS;
            backKeyList = Configs.PASSPORT_ORDERED_BACK_KEYS;
            keyToNameMap = Configs.PASSPORT_JSON_FIELD_KEY_TO_NAME_MAP();
        }
        else if(documentType == LandingActivity.DocumentType.AADHAAR){
            frontKeyList = Configs.AADHAAR_ORDERED_FRONT_KEYS;
            backKeyList = Configs.AADHAAR_ORDERED_BACK_KEYS;
            keyToNameMap = Configs.AADHAAR_JSON_FIELD_KEY_TO_NAME_MAP();
        }

        int fieldsRead = 0;

        for(String key: frontKeyList){
            FieldValueTableRowViewHolder holder = new FieldValueTableRowViewHolder((TableRow) inflater.inflate(R.layout.field_value_table_row, null,false));
            holder.fieldTV.setText(keyToNameMap.get(key));
            try {
                holder.valueTV.setText(resultJSON.getString(key));
                ((TableLayout)findViewById(R.id.tl_front)).addView(holder.tableRow);
                fieldsRead++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ivFront = (ImageView) findViewById(R.id.iv_front);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ImageView ivOneSide = (ImageView) findViewById(R.id.iv_one_side);
        try {
            if(resultJSON.has("front_url") && resultJSON.has("back_url")) {
                findViewById(R.id.ll_col_view).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_one_side).setVisibility(View.GONE);
                Glide.with(this).load(resultJSON.getString("front_url")).placeholder(R.color.grey).into(ivFront);
                Glide.with(this).load(resultJSON.getString("back_url")).placeholder(R.color.grey).into(ivBack);
                final JSONObject finalResultJSON = resultJSON;
                ivFront.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showImageInFullScreenView(finalResultJSON.getString("front_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showImageInFullScreenView(finalResultJSON.getString("back_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(resultJSON.has("front_url")){
                findViewById(R.id.ll_col_view).setVisibility(View.GONE);
                findViewById(R.id.ll_one_side).setVisibility(View.VISIBLE);
                Glide.with(this).load(resultJSON.getString("front_url")).placeholder(R.color.grey).into(ivOneSide);
                ((TextView)findViewById(R.id.tv_one_side_image_heading)).setText("FRONT PAGE");

                final JSONObject finalResultJSON1 = resultJSON;
                ivOneSide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showImageInFullScreenView(finalResultJSON1.getString("front_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if(resultJSON.has("back_url")){
                findViewById(R.id.ll_col_view).setVisibility(View.GONE);
                findViewById(R.id.ll_one_side).setVisibility(View.VISIBLE);
                Glide.with(this).load(resultJSON.getString("back_url")).placeholder(R.color.grey).into(ivOneSide);
                ((TextView)findViewById(R.id.tv_one_side_image_heading)).setText("BACK PAGE");

                final JSONObject finalResultJSON2 = resultJSON;
                ivOneSide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showImageInFullScreenView(finalResultJSON2.getString("front_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(String key: backKeyList){
            FieldValueTableRowViewHolder holder = new FieldValueTableRowViewHolder(inflater.inflate(R.layout.field_value_table_row, null,false));
            holder.fieldTV.setText(keyToNameMap.get(key));
            try {
                holder.valueTV.setText(resultJSON.getString(key));
                ((TableLayout)findViewById(R.id.tl_front)).addView(holder.tableRow);
                fieldsRead++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(fieldsRead == 0){
            findViewById(R.id.emptyPlaceholderTV).setVisibility(View.VISIBLE);
        }

//        setTextFromJSON(resultJSON, tvMother, "mother");
//        setTextFromJSON(resultJSON, tvFather, "father");
//        setTextFromJSON(resultJSON, tvAddress, "address");
//        setTextFromJSON(resultJSON, tvSpouse, "spouse");

    }

    public void dismissFullScreenImageView(View view) {
        findViewById(R.id.fl_full_screen_view).setVisibility(View.GONE);
    }

    public void showImageInFullScreenView(String filePath){
        ImageView fullScreenPreviewIV = (ImageView) findViewById(R.id.iv_preview);
        Glide.with(this).load(filePath).into(fullScreenPreviewIV);
        findViewById(R.id.fl_full_screen_view).setVisibility(View.VISIBLE);
    }

    private class FieldValueTableRowViewHolder {
        public TextView fieldTV;
        public TextView valueTV;
        public TableRow tableRow;

        public FieldValueTableRowViewHolder(View itemView) {
            tableRow = (TableRow) itemView;
            fieldTV = (TextView) itemView.findViewById(R.id.tv_field);
            valueTV = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }

}