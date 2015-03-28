package com.dobi.jiecon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dobi.jiecon.R;
import com.dobi.jiecon.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.dobi.jiecon.service.*;
import java.util.LinkedList;

public class AppDetailActivity extends Activity {
    private final String appKey = "APPNAME";
    // The data to show
    private List<Map<String, String>> HistoryList = new ArrayList<Map<String, String>>();
    private SimpleAdapter AppDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ListView lv = (ListView) findViewById(R.id.listViewDetail);

        Intent intent = getIntent();

//		int position = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
//		initList(position);

        AppDetails = new SimpleAdapter(this, HistoryList, android.R.layout.simple_list_item_1, new String[]{appKey}, new int[]{android.R.id.text1});
        lv.setAdapter(AppDetails);
    }

    private List<Map<String, String>> initList(int position) {

        AllAppManager data = MockService.getMockAppData();

        AppManager app1 = data.get(Integer.toString(position + 1));

        LinkedList<SingleAppRecord> ls = app1.getAllItem();

        for (Iterator iter = ls.iterator(); iter.hasNext(); ) {
            SingleAppRecord rec = (SingleAppRecord) iter.next();
            String str = "Start:    " + rec.getStartTime().toString() + "\n";
            str += "End:      " + rec.getEndDate().toString() + "\n";
            str += "Duration: " + rec.getDayDuration() + " Days " + rec.getHourDuration() + " Hours " + rec.getMinDuration() + " Mintues " + rec.getSecDuration() + " Seconds";
            HistoryList.add(createPlanet(appKey, str));
        }

//		// TODO Auto-generated method stub
//		HistoryList.add(createPlanet(appKey, "Mercury"));
//		HistoryList.add(createPlanet(appKey, "Venus"));
//		HistoryList.add(createPlanet(appKey, "Mars"));
//		HistoryList.add(createPlanet(appKey, "Jupiter"));
//		HistoryList.add(createPlanet(appKey, "Saturn"));
//		HistoryList.add(createPlanet(appKey, "Uranus"));
//		HistoryList.add(createPlanet(appKey, "Neptune"));
//		HistoryList.add(createPlanet(appKey, "Earth & Mercury"));

        return HistoryList;
    }
    private Map<String, String> createPlanet(String key, String name) {
        // TODO Auto-generated method stub
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put(key, name);
        return planet;
    }
}
