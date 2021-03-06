package com.example.mediraj.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mediraj.BuildConfig;
import com.example.mediraj.R;
import com.example.mediraj.helper.Constant;
import com.example.mediraj.helper.DataManager;
import com.example.mediraj.helper.LocaleHelper;
import com.example.mediraj.helper.SessionManager;
import com.example.mediraj.model.UserData;
import com.example.mediraj.webapi.APiClient;
import com.example.mediraj.webapi.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    TextView userName, userPhone, userEmail, appVersion, emergency;
    LinearLayout profileLay, offerLay, promoLay, logoutLay, devTeam, emergencyLay, aboutLay, contactUsLay;
    String lan_pref = null;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView userImg;
    private ApiInterface apiInterface;
    private SwitchCompat toggle, lanChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        initView();
        setUserData();

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationOffOn(DataManager.getInstance().getUserData(MoreActivity.this).data.id);
            }
        });


        lanChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LocaleHelper.writeString(MoreActivity.this, Constant.LANG_INFO, "bn");
                    LocaleHelper.setLocale(MoreActivity.this, "bn");
                    recreate();
                } else {
                    LocaleHelper.writeString(MoreActivity.this, Constant.LANG_INFO, "en");
                    LocaleHelper.setLocale(MoreActivity.this, "en");
                    recreate();
                }
                Toast.makeText(MoreActivity.this, getText(R.string.lan_change), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void initView() {

        //bottom navigation view and related listener
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.history) {
                startActivity(new Intent(this, OrderActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.more) {
                return true;
            }
            return false;
        });

        //other views
        lanChange = findViewById(R.id.lanChange);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        userImg = findViewById(R.id.userImg);
        profileLay = findViewById(R.id.profileLay);
        offerLay = findViewById(R.id.offerLay);
        promoLay = findViewById(R.id.promoLay);
        aboutLay = findViewById(R.id.aboutLay);
        emergencyLay = findViewById(R.id.emergencyLay);
        devTeam = findViewById(R.id.devTeam);
        logoutLay = findViewById(R.id.logoutLay);
        toggle = findViewById(R.id.toggle);
        appVersion = findViewById(R.id.appVersion);
        emergency = findViewById(R.id.emer_call);
        contactUsLay = findViewById(R.id.contactUsLay);
        lanChange = findViewById(R.id.lanChange);

        //for language switch
        lan_pref = LocaleHelper.readString(MoreActivity.this, Constant.LANG_INFO);
        if (lan_pref == null) {
            lanChange.setChecked(false);
        } else if (lan_pref.equals("bn")) {
            lanChange.setChecked(true);
        } else {
            lanChange.setChecked(false);
        }


        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        appVersion.setText(version);
        //lister part
        profileLay.setOnClickListener(this);
        offerLay.setOnClickListener(this);
        promoLay.setOnClickListener(this);
        logoutLay.setOnClickListener(this);
        devTeam.setOnClickListener(this);
        emergencyLay.setOnClickListener(this);
        aboutLay.setOnClickListener(this);
        contactUsLay.setOnClickListener(this);


        //api interface initialization
        apiInterface = APiClient.getClient().create(ApiInterface.class);
    }

    private void setUserData() {
        if (DataManager.getInstance().getUserData(getApplicationContext()) != null
                &&
                DataManager.getInstance().getUserData(getApplicationContext()).data != null
                &&
                DataManager.getInstance().getUserData(getApplicationContext()).data.id != null) {

            if (DataManager.getInstance().getUserData(this).data.name != null) {
                userName.setText(DataManager.getInstance().getUserData(this).data.name);
            }

            if (DataManager.getInstance().getUserData(this).data.mobile != null) {
                userPhone.setText(DataManager.getInstance().getUserData(this).data.mobile);
            }

            if (DataManager.getInstance().getUserData(this).data.email != null) {
                userEmail.setText(DataManager.getInstance().getUserData(this).data.email);
            }

            if (DataManager.getInstance().getUserData(getApplicationContext()).data.avatar != null) {
                Glide.with(this)
                        .load(Constant.USER_AVATAR_URL + DataManager.getInstance().getUserData(this).data.avatar)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                        .centerCrop()
                        .into(userImg);
            }

            toggle.setChecked(DataManager.getInstance().getUserData(getApplicationContext()).data.notification.equalsIgnoreCase("on"));

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.profileLay) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.offerLay) {

        } else if (id == R.id.promoLay) {

        } else if (id == R.id.aboutLay) {
            startActivity(new Intent(this, AboutUsActivity.class));
        } else if (id == R.id.emergencyLay) {
            startActivity(new Intent(this, EmergencyNumberActivity.class));
        } else if (id == R.id.devTeam) {
            startActivity(new Intent(this, DeveloperTeam.class));
        } else if (id == R.id.logoutLay) {
            alertLogout();
        } else if (id == R.id.contactUsLay) {
            contactUsSelection();
        }
    }


    public void alertLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MoreActivity.this);
        alertDialog.setTitle(R.string.alert_logout);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SessionManager.logout(MoreActivity.this, apiInterface);
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    private void notificationOffOn(String id) {
        DataManager.getInstance().showProgressMessage(MoreActivity.this, getString(R.string.please_wait));
        Call<UserData> notiStatusCall = apiInterface.notificationStatus(Constant.AUTH, id);
        notiStatusCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    UserData data = response.body();
                    assert data != null;
                    if (data.response == 200) {
                        Toast.makeText(getApplicationContext(), data.message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MoreActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


    @Override
    public void onBackPressed() {
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home != selectedItemId) {
            bottomNavigationView.setSelectedItemId(R.id.home);
            finish();
        }
    }

    // Contact us alert dialog
    public void contactUsSelection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.contact_us_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallery = dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:support@mediraj.com")); // only email apps should handle this
                    startActivity(intent);
                    dialog.dismiss();
                    dialog.cancel();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MoreActivity.this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01730-336655"));
                startActivity(intent);
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}