package kr.dangguel.domestictravel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

import java.util.ArrayList;

public class MainPage1Fragment extends Fragment {

    CalAdapter calAdapter;
    MyListView listView;
    MainActivity mainActivity;
    ArrayList<SaveCalVO> cals;
    View view;
    MainPage2Fragment frag2;
    BoomMenuButton bmb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_page1_fragment, container, false);
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cals = mainActivity.cals;
        listView = view.findViewById(R.id.listView);
        calAdapter = new CalAdapter(getLayoutInflater(), cals, mainActivity);
        listView.setAdapter(calAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bmb=view.findViewById(R.id.bmb);
                bmb.setOnBoomListener(new OnBoomListenerAdapter() {
                    @Override
                    public void onClicked(int index, BoomButton boomButton) {
                        super.onClicked(index, boomButton);
                        switch (index) {
                            case 0:
                                Intent intent = new Intent(mainActivity,NaviActivity.class);
                                SaveCalVO saveCalVO = cals.get(position);
                                String title = saveCalVO.title;
                                String range = saveCalVO.range;
                                int untilday = saveCalVO.untilday;
                                String days = saveCalVO.days;
                                intent.putExtra("title",title);
                                intent.putExtra("range",range);
                                intent.putExtra("untilday",untilday);
                                intent.putExtra("days",days);

                                startActivity(intent);
                                break;
                            case 1:
                                mainActivity.frag2.editMode();
                                mainActivity.changeIndex(position);
                                cals.remove(position);
                                mainActivity.startFragment2();
                                mainActivity.frag1.updateAdapter();
                                break;
                            case 2:
                                cals.remove(position);
                                mainActivity.frag1.updateAdapter();
                                break;
                        }
                    }
                });
                bmb.boom();
            }
        });

    }

    void updateAdapter() {
        calAdapter.notifyDataSetInvalidated();
    }

}
