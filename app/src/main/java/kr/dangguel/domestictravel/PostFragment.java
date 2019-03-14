package kr.dangguel.domestictravel;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {
    AdView adView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post,container,false);
        adView = view.findViewById(R.id.adView);
        TextView setting = view.findViewById(R.id.go_setting);
        TextView post = view.findViewById(R.id.go_post);

        setting.setOnClickListener(onClickListener);
        post.setOnClickListener(onClickListener);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.go_setting:
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+"kr.dangguel.domestictravel"));
                    startActivity(intent);
                    break;

                case R.id.go_post:
                    Intent intent1 = new Intent(getActivity(),PostActivity.class);
                    startActivityForResult(intent1,40);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 40:
                    Toast.makeText(getActivity(), "전송 완료", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
