package anuj.wifidirect.wifi;

import android.app.ListFragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import anuj.wifidirect.*;
/**
 * Created by ok on 2017/12/6.
 */

public class GroupListFragment extends ListFragment  {

    private ArrayAdapter<String> adapter;
    //private ArrayList list;

     String[] presidents = {
            "Dwight D. Eisenhower",
            "John F. Kennedy"
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(),
                "createview" ,
                Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.group_list, container, false);

    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(),
                "oncreate" ,
                Toast.LENGTH_SHORT).show();
        setListAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, presidents));

    }

//    public void onListItemClick(ListView parent, View v,
//                                int position, long id)
//    {
//
//
//
//        Toast.makeText(getActivity(),
//                "You have selected " + presidents[position],
//                Toast.LENGTH_SHORT).show();
//    }


 //   @Override
//    public void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//    }
    public void ShowGroupInfromation(String[] array)
    {
        //int size = list.size();
        //presidents[2]=String.valueOf(size);
        //String[] array = (String[])list.toArray(new String[size]);
        setListAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, presidents));
        //setListAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, presidents));
//        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, presidents);
//               //绑定适配器时，必须通过ListFragment.setListAdapter()接口，而不是ListView.setAdapter()或其它方法
//                 this.setListAdapter(adapter);
    }
//
//     @Override
//     public View onCreateView(LayoutInflater inflater, ViewGroup container,
//             Bundle savedInstanceState) {
//                // TODO Auto-generated method stub
//                 return super.onCreateView(inflater, container, savedInstanceState);
//            }

             @Override
     public void onPause() {
                 // TODO Auto-generated method stub
                super.onPause();
             }

//    public void showDetails() {
//        this.getView().setVisibility(View.VISIBLE);
//        TextView view = (TextView) mContentView.findViewById(R.id.device_address);
//        view.setText(device.deviceAddress);
//        view = (TextView) mContentView.findViewById(R.id.device_info);
//        view.setText(device.toString());
//    }

}
