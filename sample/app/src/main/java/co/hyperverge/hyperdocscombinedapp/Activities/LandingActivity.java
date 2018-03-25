package co.hyperverge.hyperdocscombinedapp.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.hyperverge.hyperdocscombinedapp.Adapters.CompletedAdapter;
import co.hyperverge.hyperdocscombinedapp.R;
import co.hyperverge.hyperdocscombinedapp.Utils.Configs;
import co.hyperverge.hyperdocscombinedapp.Utils.SpUtils;
import co.hyperverge.hyperdocssdk.workflows.ocr.activities.CameraActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MY_PERMISSIONS_REQUEST_CAMERA_ACTIVITY = 101;

    private File f = null;

    public static enum DocumentType {
        PAN,
        PASSPORT,
        AADHAAR
    };

    private final ArrayList<String> requiredPermissions = new ArrayList<>(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE));

    DocumentType documentType;

//    private FloatingActionButton fab;
    private FloatingActionMenu fabMenu;

    private FrameLayout flEmptyState;
    private RecyclerView rvCompleted;
    private CompletedAdapter mAdapter;

    List<String> clicked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        setupViews();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupViews() {

        clicked.addAll(SpUtils.getLists());
        flEmptyState = (FrameLayout) findViewById(R.id.fl_empty_state);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

        rvCompleted = (RecyclerView) findViewById(R.id.rv_completed);
        rvCompleted.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CompletedAdapter(this);
        rvCompleted.setAdapter(mAdapter);
        mAdapter.setDataset(clicked);

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundDimming(!fabMenu.isOpened());
                if(fabMenu.isOpened())
                    fabMenu.close(true);
                else
                    fabMenu.open(true);
            }
        });

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });

        findViewById(R.id.subFabPan).setOnClickListener(this);
        findViewById(R.id.subFabPassport).setOnClickListener(this);
        findViewById(R.id.subFabAadhaar).setOnClickListener(this);

        setFabMenuIconChangeAnimation();

    }

    public void startAppropriateCameraActivity(){

        long time = System.currentTimeMillis();

        File dirPath = new File(Configs.FILE_DIR);
        if(!dirPath.exists())
            dirPath.mkdirs();

        String frontPath = Configs.FILE_DIR + time + "_front.jpg";
        String backPath = Configs.FILE_DIR + time + "_back.jpg";

        CameraActivity.toggleDataLogging(true);

        if(documentType == DocumentType.PAN) {
            CameraActivity.startForPan(LandingActivity.this, frontPath, new CameraActivity.ImageListener() {
                @Override
                public void onOCRComplete(JSONObject result) {
                    if (result == null) {
                        Log.e("LandingActivity", "Result: null");
                        return;
                    }
                    Log.i("LandingActivity", result.toString());
                    clicked.add(result.toString());
                    SpUtils.saveLists(clicked);
                    mAdapter.setDataset(clicked);

                    // Start ImagesActivity if result is present
                    try {
                        if(result.has("type") && result.getString("type").equals(DocumentType.PAN.name().toLowerCase())){
                            documentType = DocumentType.PAN;
                            ImagesActivity.start(LandingActivity.this, result.toString(), documentType);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(documentType == DocumentType.PASSPORT){
            CameraActivity.startForPassport(LandingActivity.this, frontPath, backPath, CameraActivity.DocumentSide.FRONT_BACK, new CameraActivity.ImageListener() {
                @Override
                public void onOCRComplete(JSONObject result) {
                    if (result == null) {
                        Log.e("LandingActivity", "Result: null");
                        return;
                    }
                    Log.i("LandingActivity", result.toString());
                    clicked.add(result.toString());
                    SpUtils.saveLists(clicked);
                    mAdapter.setDataset(clicked);

                    // Start ImagesActivity if result is present
                    try {
                        if(result.has("type") && result.getString("type").equals(DocumentType.PASSPORT.name().toLowerCase())){
                            documentType = DocumentType.PASSPORT;
                            ImagesActivity.start(LandingActivity.this, result.toString(), documentType);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(documentType == DocumentType.AADHAAR){
            CameraActivity.startForAadhaar(LandingActivity.this, frontPath, backPath, CameraActivity.DocumentSide.FRONT_BACK, new CameraActivity.ImageListener() {
                @Override
                public void onOCRComplete(JSONObject result) {
                    if (result == null) {
                        Log.e("LandingActivity", "Result: null");
                        return;
                    }
                    Log.i("LandingActivity", result.toString());
                    clicked.add(result.toString());
                    SpUtils.saveLists(clicked);
                    mAdapter.setDataset(clicked);

                    // Start ImagesActivity if result is present
                    try {
                        if(result.has("type") && result.getString("type").equals(DocumentType.AADHAAR.name().toLowerCase())){
                            documentType = DocumentType.AADHAAR;
                            ImagesActivity.start(LandingActivity.this, result.toString(), documentType);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void checkAndGetCameraPermission(){
        // Assume thisActivity is the current activity
        ArrayList<String> missingPermissions = checkForMissingPermissions();
        // Should we show an explanation?

        if(missingPermissions.size() == 0){
            startAppropriateCameraActivity();
            return;
        }

        ArrayList<String> toBeRequestedPermissions = new ArrayList<>();
        ArrayList<String> rationalePermissions = new ArrayList<>();
        for(String missingPermission: missingPermissions){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                    missingPermission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                rationalePermissions.add(missingPermission);

            } else {

                // No explanation needed, we can request the permission.
                toBeRequestedPermissions.add(missingPermission);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if(toBeRequestedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(LandingActivity.this,
                    toBeRequestedPermissions.toArray(new String[0]),
                    MY_PERMISSIONS_REQUEST_CAMERA_ACTIVITY);
        }
        if(rationalePermissions.size() > 0){
            String permissionsTxt = "";
            for(String perm: rationalePermissions){
                String[] permSplit = perm.split("\\.");
                permissionsTxt += permSplit[permSplit.length - 1] + ", ";
            }

            permissionsTxt = permissionsTxt.substring(0, permissionsTxt.length() - 2);
            Toast.makeText(LandingActivity.this, "Please give " + permissionsTxt + " permissions by going to Settings", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.subFabPan){
            //Toast.makeText(LandingActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            documentType = DocumentType.PAN;
            checkAndGetCameraPermission();
            fabMenu.close(false);
            setBackgroundDimming(false);
        }
        else if(v.getId() == R.id.subFabPassport){
            documentType = DocumentType.PASSPORT;
            checkAndGetCameraPermission();
            fabMenu.close(false);
            setBackgroundDimming(false);
        }
        else if(v.getId() == R.id.subFabAadhaar){
            documentType = DocumentType.AADHAAR;
            checkAndGetCameraPermission();
            fabMenu.close(false);
            setBackgroundDimming(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(clicked.size() > 0)
            flEmptyState.setVisibility(View.GONE);
        else
            flEmptyState.setVisibility(View.VISIBLE);
    }

    public ArrayList<String> checkForMissingPermissions(){
        ArrayList<String> missingPermissions = new ArrayList<>();
        for(String permission : requiredPermissions){
            if(ContextCompat.checkSelfPermission(LandingActivity.this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permission);
            }
        }
        return missingPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA_ACTIVITY:

                for(int i = 0; i < grantResults.length; i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(LandingActivity.this, "Cannot start as all the permissions were not granted", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                startAppropriateCameraActivity();
                return;
        }
    }

    private void setBackgroundDimming(boolean dimmed) {
        final float targetAlpha = dimmed ? 1f : 0;
        final int endVisibility = dimmed ? View.VISIBLE : View.GONE;
        final View mDimmerView = findViewById(R.id.dimmer_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDimmerView.setVisibility(View.VISIBLE);
            mDimmerView.animate()
                    .alpha(targetAlpha)
                    .setDuration(200)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mDimmerView.setVisibility(endVisibility);
                        }
                    })
                    .start();
        }
        else{
            mDimmerView.setVisibility(endVisibility);
        }
    }

    private void setFabMenuIconChangeAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabMenu.getMenuIconView().setImageResource(fabMenu.isOpened()
                        ? R.drawable.group_5 : R.drawable.ic_fab_close);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        fabMenu.setIconToggleAnimatorSet(set);
    }

    public void dimmedBackgroundClicked(View view) {
        fabMenu.close(true);
        setBackgroundDimming(false);
    }
}
