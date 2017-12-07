package anuj.wifidirect.wifi;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

import anuj.wifidirect.R;
import anuj.wifidirect.utils.PermissionsAndroid;
import anuj.wifidirect.utils.SharedPreferencesHandler;
import anuj.wifidirect.utils.Utils;



/**
 * Created by ok on 2017/12/7.
 */

public class GroupFragment extends Fragment {// implements WifiP2pManager.ConnectionInfoListener {

    private View ContentView = null;
    private String[] array;
    private ArrayList Array;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


//    @Override
//    public void onAttach(Activity WifiDirectActivity) {
//        super.onAttach( WifiDirectActivity);
//        titles = ((MainActivity)  WifiDirectActivity).getTitles();
//    }
    @Override
    public void onAttach(Activity activity) {
         super.onAttach(activity);
         String aa = ((WiFiDirectActivity) activity).putString();
        Toast.makeText(getActivity(),
                aa,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey("picker_path")) {
//                pickerPath = savedInstanceState.getString("picker_path");
//            }
//        }
    }

//    @Override
//    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//        this.info = info;
//        this.getView().setVisibility(View.VISIBLE);
//
//        // The owner IP is now known.
//        TextView view = (TextView) mContentView.findViewById(R.id.group_owner);
//        view.setText(getResources().getString(R.string.group_owner_text)
//                + ((info.isGroupOwner == true) ? getResources().getString(R.string.yes)
//                : getResources().getString(R.string.no)));
//
//        // InetAddress from WifiP2pInfo struct.
//        view = (TextView) mContentView.findViewById(R.id.device_info);
//        if (info.groupOwnerAddress.getHostAddress() != null)
//            view.setText("Group Owner IP - " + info.groupOwnerAddress.getHostAddress());
//        else {
//            CommonMethods.DisplayToast(getActivity(), "Host Address not found");
//        }
//
//        // After the group negotiation, we assign the group owner as the file
//        // server. The file server is single threaded, single connection server
//        // socket.
//
//
//
//
//        try {
//            String GroupOwner = info.groupOwnerAddress.getHostAddress();
//            if (GroupOwner != null && !GroupOwner.equals(""))
//                SharedPreferencesHandler.setStringValues(getActivity(),
//                        getString(R.string.pref_GroupOwnerAddress), GroupOwner);
//            mContentView.findViewById(R.id.btn_start_client).setVisibility(View.VISIBLE);
//
//            //first check for file storage permission
//            if (!PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(getActivity())) {
//                Utils.getInstance().showToast("Please enable storage Permission from application storage option");
//                return;
//            }
//
//            if (info.groupFormed && info.isGroupOwner) {
//            /*
//             * set sharedprefrence which remember that device is server.
//        	 */
//                SharedPreferencesHandler.setStringValues(getActivity(),
//                        getString(R.string.pref_ServerBoolean), "true");
//
//                DeviceDetailFragment.FileServerAsyncTask FileServerobj = new DeviceDetailFragment.FileServerAsyncTask(
//                        getActivity(), FileTransferService.PORT);
//                if (FileServerobj != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                        FileServerobj.executeOnExecutor(
//                                AsyncTask.THREAD_POOL_EXECUTOR,
//                                new String[]{null});
//                        // FileServerobj.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Void);
//                    } else
//                        FileServerobj.execute();
//                }
//            } else {
//                // The other device acts as the client. In this case, we enable the
//                // get file button.
//                if (!ClientCheck) {
//                    DeviceDetailFragment.firstConnectionMessage firstObj = new DeviceDetailFragment.firstConnectionMessage(
//                            GroupOwnerAddress);
//                    if (firstObj != null) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                            firstObj.executeOnExecutor(
//                                    AsyncTask.THREAD_POOL_EXECUTOR,
//                                    new String[]{null});
//                        } else
//                            firstObj.execute();
//                    }
//                }
//
//                DeviceDetailFragment.FileServerAsyncTask FileServerobj = new DeviceDetailFragment.FileServerAsyncTask(
//                        getActivity(), FileTransferService.PORT);
//                if (FileServerobj != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                        FileServerobj.executeOnExecutor(
//                                AsyncTask.THREAD_POOL_EXECUTOR,
//                                new String[]{null});
//                    } else
//                        FileServerobj.execute();
//
//                }
//
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void test()
//    {
//        array = getArguments().getStringArray("DATA");
//        Toast.makeText(getActivity(),
//                array[0],
//                Toast.LENGTH_SHORT).show();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ContentView = inflater.inflate(R.layout.group_detail, null);
//



//        ContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                WifiP2pConfig config = new WifiP2pConfig();
//                if (config != null && config.deviceAddress != null && device != null) {
//                    config.deviceAddress = device.deviceAddress;
//                    config.wps.setup = WpsInfo.PBC;
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                    progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
//                            "Connecting to :" + device.deviceAddress, true, true
//                    );
//                    ((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);
//                } else {
//
//                }
//            }
//        });
//
        ContentView.findViewById(R.id.btn_disconnect).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        ((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
                        Bundle bundle = getArguments();
                        Array = bundle.getStringArrayList("DATA");
                        //String b=bundle.getString("DATA");
                        Toast.makeText(getActivity(),
                                Array.get(1).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        ContentView.findViewById(R.id.send).setOnClickListener(


                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WifiP2pConfig config = new WifiP2pConfig();
                        //根据groupmember的地址，分别连接并且发送信息
//                        for (WifiP2pDevice device:xxxxx )
//                            ((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);//??只想连接，不想出现别的orz

                        //挑文件发送orz
                        // pickFilesSingle();



                    }


                });

        return ContentView;
    }

}
