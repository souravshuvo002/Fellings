package com.sourav.feelings.Profile;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sourav.feelings.Accounts.Get_User_Info_A;
import com.sourav.feelings.CodeClasses.Variables;
import com.sourav.feelings.Main_Menu.MainMenuActivity;
import com.sourav.feelings.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.sourav.feelings.Profile.EditProfile.EditProfile_F;
import com.sourav.feelings.Profile.Profile_Details.Profile_Details_F;
import com.sourav.feelings.R;
import com.sourav.feelings.Settings.Setting_F;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_F extends RootFragment {

    View view;
    Context context;

    public static ImageView profile_image;
    TextView user_name, user_details;
    public static TextView age;


    LinearLayout setting_layout, edit_profile_layout;


    public Profile_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_two, container, false);
        context = getContext();


        edit_profile_layout = view.findViewById(R.id.edit_profile_layout);
        edit_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editprofile();
            }
        });


        setting_layout = view.findViewById(R.id.setting_layout);
        setting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting_profile();
            }
        });


        profile_image = view.findViewById(R.id.profile_image);
        user_name = view.findViewById(R.id.user_name);
        user_details = view.findViewById(R.id.user_details);
        age = view.findViewById(R.id.age);


        // show the picture age and username of the user
        Picasso.with(context).load(MainMenuActivity.user_pic)
                .resize(200, 200)
                .centerCrop()
                .into(profile_image);

        user_name.setText(MainMenuActivity.user_name);
        age.setText(" " + MainMenuActivity.birthday);


        Get_User_Info();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile_detail();
            }
        });



        return view;
    }

    private void Get_User_Info() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", MainMenuActivity.user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Variables.getUserInfo, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d("responce",respo);
                        Parse_user_info(respo);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("respo",error.toString());
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);
    }


    // open the view of Edit profile where 6 pic is visible
    public void Profile_detail() {

        Profile_Details_F profile_details_f = new Profile_Details_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, profile_details_f).commit();

    }

    // open the view of Edit profile where 6 pic is visible
    public void Editprofile() {
        EditProfile_F editProfile_f = new EditProfile_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, editProfile_f).commit();
    }

    // open the view of Edit profile where 6 pic is visible
    public void Setting_profile() {
        Setting_F setting_f = new Setting_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, setting_f).commit();
    }

    public void Parse_user_info(String loginData) {
        try {
            JSONObject jsonObject = new JSONObject(loginData);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msg = jsonObject.getJSONArray("msg");
                JSONObject userdata = msg.getJSONObject(0);

                user_details.setText(userdata.optString("about_me"));
                /*job_title_edit.setText(userdata.optString("job_title"));
                company_edit.setText(userdata.optString("company"));
                school_edit.setText(userdata.optString("school"));
                dateofbrith_edit.setText(userdata.optString("birthday"));*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
