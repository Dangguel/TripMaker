package kr.dangguel.domestictravel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment {
    ArrayList<SchduleList> schduleLists;
    ScheduleAdapter adapter;
    ListView listView;
    ArrayList<TimeSchedule> timeSchedules = new ArrayList<>();
    ArrayList<TimeSchedule>[] totalSchedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        final NaviActivity naviActivity = (NaviActivity) getActivity();
        this.schduleLists = naviActivity.schduleLists;

        listView = view.findViewById(R.id.schedule_listview);
        adapter = new ScheduleAdapter(schduleLists, inflater);
        listView.setAdapter(adapter);

        totalSchedule = new ArrayList[schduleLists.size()];

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(naviActivity, AddSchedule.class);
                if (totalSchedule != null && totalSchedule[position] != null) {
                    intent.putExtra("schedule", totalSchedule[position]);
                }
                intent.putExtra("position", position);
                startActivityForResult(intent, 10);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    timeSchedules = (ArrayList<TimeSchedule>) data.getSerializableExtra("schedule");
                    int day = data.getIntExtra("day", -1);

                    totalSchedule[day] = timeSchedules;
                    break;
            }
        }
    }
}
