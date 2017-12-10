/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package anuj.wifidirect.wifi;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;
import java.lang.String;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import anuj.wifidirect.R;
import anuj.wifidirect.utils.PermissionsAndroid;

/**
 * An activity that uses WiFi Direct APIs to discover and connect with available
 * devices. WiFi Direct APIs are asynchronous and rely on callback mechanism
 * using interfaces to notify the application of operation success or failure.
 * The application should also register a BroadcastReceiver for notification of
 * WiFi state related events.
 */
public class WiFiDirectActivity extends AppCompatActivity implements ChannelListener, DeviceListFragment.DeviceActionListener, DeviceListFragment.MyListener {


    private Toolbar mToolbar;
    public static final String TAG = "wifidirectdemo";
    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;
    private FragmentManager fragmanager;
    private FragmentTransaction transaction;
    private ArrayList GroupArray;
    private ArrayList device;
    private ArrayList wholeDevice;
    private String[] array;
    private ArrayList groupList;
    WifiP2pDeviceList peerList;
    private String[] dialogName;
    private String groupName;
    private int groupListNum;
    private ArrayList<ArrayList> groupDevice;

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        groupList = new ArrayList();//初始化存放groupList的列表（用来放name？)
        groupListNum=0;
        groupDevice = new ArrayList();
        initViews();
        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        checkStoragePermission();
    }

    /*
      Ask permissions for Filestorage if device api > 23
       */
    //  @TargetApi(Build.VERSION_CODES.M)
    private void checkStoragePermission() {
        boolean isExternalStorage = PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(this);
        if (!isExternalStorage) {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(this);
        }
    }

    private void initViews() {
        // add necessary intent values to be matched.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WifiDirect");
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //获取 WifiP2pManager对象并在下一步初始化
        channel = manager.initialize(this, getMainLooper(), this);
    }


    /**
     * register the BroadcastReceiver with the intent values to be matched
     */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        if (fragmentList != null) {
            fragmentList.clearPeers();
        }
        if (fragmentDetails != null) {
            fragmentDetails.resetViews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atn_direct_enable:
                if (manager != null && channel != null) {

                    // Since this is the system wireless settings activity, it's
                    // not going to send us a result. We will be notified by
                    // WiFiDeviceBroadcastReceiver instead.

                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                } else {
                    Log.e(TAG, "channel or manager is null");
                }
                return true;

            case R.id.atn_direct_discover:
                if (!isWifiP2pEnabled) {
                    Toast.makeText(WiFiDirectActivity.this, R.string.p2p_off_warning,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                        .findFragmentById(R.id.frag_list);
                fragment.onInitiateDiscovery();
                manager.discoverPeers(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Discovery Initiated",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this, "Discovery Failed : " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            case R.id.group:
                //显示组名、组员
                //生成组名，组的列表
                final ArrayList List = new ArrayList();//不知道为什么要加final
                manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup group) {
                        // String groupPassword = group.getPassphrase(); //只有owner才是有password的


                        String groupMember = group.getClientList().toString();//可以获得连接到group的member列表
                        String name = group.getNetworkName();//可以获得group的wifi热点名称（SSID）

                        List.add(name);
                        //Toast.makeText(WiFiDirectActivity.this, "group information "+name,
                              //  Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "group information" + name);
                        List.add(group.getOwner().deviceName);
                        //for (int i = 0; i < group.getClientList().size(); i++) {
                        for (WifiP2pDevice groupdevice:group.getClientList())
                        {
                            List.add(groupdevice.deviceName);//懵逼不知道怎么传东西
                            //Toast.makeText(WiFiDirectActivity.this, "group information "+ group.getClientList().toArray()[i],
                                  //  Toast.LENGTH_SHORT).show();
                        }
                        int size=List.size();
                        String[] array = (String[])List.toArray(new String[size]);
                        Toast.makeText(WiFiDirectActivity.this, array[1]+" and "+array[0] + "and"+size,
                                Toast.LENGTH_SHORT).show();

                       final GroupListFragment fragmentgroup = (GroupListFragment) getFragmentManager().findFragmentById(R.id.frag_group);
                       fragmentgroup.ShowGroupInfromation(array);
//
//                        //创建一个list，第一个存放owner的name，后面放成员的device
//                        //发送消息给成员，并且使他们相连
//                        //groupname存放在自己的group列表中，点击可以查看list
//
//                        //Toast.makeText(WiFiDirectActivity.this, name+' '+groupMember,
//                        //      Toast.LENGTH_SHORT).show();
//
//
//                           /* HashMap<String, String> map = new HashMap<String, String>();
//                            map.put("name", name);
//                            map.put("address", groupPassword);
//                            peersshow.add(map);
//
//                            mAdapter = new MyAdapter(peersshow);
//                            mRecyclerView.setAdapter(mAdapter);
//                            mRecyclerView.setLayoutManager(new LinearLayoutManager
//                                    (MainActivity.this));*/
                   }
                });

//
//
                //fragmentgroup.ShowGroupInfromation(List);

                //GroupListFragment frag1 = new GroupListFragment();
                //ListFragmentSelf frgSelf = new ListFragmentSelf();

                //transaction.add(R.id.fragment2, frgSelf, "frgSelf");

                return true;

            case R.id.formGroup:
                wholeDevice=GroupArray;
//                wholeDevice = new ArrayList();
//                wholeDevice.add("phone1");
//                wholeDevice.add("phone2");
//                wholeDevice.add("phone3");
                //单击formGroup生成dailogs，来进行选择
                //int len=//Device的数目
                //wholedevice含当前所有的device的数组
                int len=wholeDevice.size();
                dialogName = new String[len]; //orz姐姐记得初始化啊！
                device = new ArrayList();//存放选择的device名字
                boolean isChecked[] = new boolean[len];
                //对数组初始化
                Iterator itname=wholeDevice.iterator();
                for (int i=0;i<len;i++) {
                    isChecked[i] = false;
                    dialogName[i] = itname.next().toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(WiFiDirectActivity.this);
                AlertDialog dialog =builder.setTitle("Group member挑选")//标题栏
                      .setMultiChoiceItems(
                            dialogName,
                              //new String[] {"phone1","phone2","phone3"},//device名字列表，
                    isChecked,
                    new DialogInterface.OnMultiChoiceClickListener(){
                        @Override
                        public void onClick(
                                DialogInterface dialog,int which,boolean isChecked){
                            if (isChecked){
                                device.add(wholeDevice.get(which));
                                Iterator iter = device.iterator();
                                while (iter.hasNext()){
                                Toast.makeText(WiFiDirectActivity.this, iter.next().toString(),
                                        Toast.LENGTH_SHORT).show();}
                            }
//                            else
//                            {
//                                  //可以再加一点鲁棒性，现在一个按钮按来按去会加多次
//                            }
                        }
                    })
                        .setPositiveButton("确定",//positiveButton即确定按钮，negativeButton为取消按钮
                                           new DialogInterface.OnClickListener() {//对确定按钮的点击事件进行设置于处理
                            @Override
                            public void onClick(DialogInterface dialog,
                            int which){
                                groupListNum += 1;
                                groupName="Group"+String.valueOf(groupListNum);
                                groupList.add(groupName);//在列表中加入GroupList
                                groupDevice.add(device);
                                dialog.dismiss();
                                Toast.makeText(WiFiDirectActivity.this, "确定",
                                        Toast.LENGTH_SHORT).show();
                                for (ArrayList array:groupDevice)
                                    for (Object devicename:array)//神之Object
                                Toast.makeText(WiFiDirectActivity.this, devicename.toString(),
                                        Toast.LENGTH_SHORT).show();
//                                Iterator itergroup = groupDevice.iterator();
//                                while (itergroup.hasNext())
//                                {
//                                    Iterator iterdevice = itergroup.next().iterator()
//                                }
                            }
                            }
                        ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }
                ).create();
                dialog.show();


                //新部分结束






                //创建组
                //跳出选项菜单

//                final GroupFragment fragmentgroupdetail = (GroupFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.frag_groupdetail);

                Bundle bundle = new Bundle();//传递数据
                //int i=0;
                //GroupArray = new String[10];
               // GroupArray[0]="and";
               // for (WifiP2pDevice groupdevice:peerList.getDeviceList())
                 //   GroupArray[i++]=groupdevice.deviceName;
                bundle.putStringArrayList("DATA",GroupArray);//这里的values就是我们要传的member的数据
                //bundle.putString("DATA",GroupArray[0]);
                //Toast.makeText(WiFiDirectActivity.this, "GroupArray[0]" + GroupArray[0],
                  //      Toast.LENGTH_SHORT).show();
                GroupFragment fragmentgroupdetail = new GroupFragment();
                fragmentgroupdetail.setArguments(bundle);
                fragmanager = getFragmentManager();
                transaction = fragmanager.beginTransaction();
                transaction.add(R.id.frag_groupdetail, fragmentgroupdetail, "frag1");
                transaction.commit();
                //fragmentgroupdetail.test();
                //fragmentgroup.GetGroupPeers(peerlist);

                //选peerlist的列表

                //生成group的信息
                //private List<WifiP2pDevice> Grouppeers = new ArrayList<WifiP2pDevice>();   //创建一个列表
                //传入一个组名 string——groupname
               // GroupName=groupname;
               // Grouppeers.addAll(peerList.getDeviceList());




                //连接
                //发送信息

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String putString()
    {
        return "hello";
//        return GroupArray;
    }

    public void sendContent(ArrayList array) {//接受从DeviceListFragment中传出的内容(目前传的是device名字列表），将列表存在了GroupArray这个ArrayList中
        GroupArray = array;
        Toast.makeText(WiFiDirectActivity.this, "sendcontect"+GroupArray.get(0), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void showDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);

    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WiFiDirectActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceDetailFragment fragment = (DeviceDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        manager.removeGroup(channel, new ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancelDisconnect() {

        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> listOfFragments = getSupportFragmentManager().getFragments();

        if(listOfFragments.size()>=1){
            for (Fragment fragment : listOfFragments) {
                if(fragment instanceof DeviceDetailFragment){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
//        (getSupportFragmentManager().findFragmentById(R.id.device_detail_container)).
//                onActivityResult(requestCode,resultCode,data);
    }
}
