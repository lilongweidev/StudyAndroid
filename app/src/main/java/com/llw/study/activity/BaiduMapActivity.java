package com.llw.study.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.Manifest;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.llw.study.basic.StudyActivity;
import com.llw.study.databinding.ActivityBaiduMapBinding;

public class BaiduMapActivity extends StudyActivity<ActivityBaiduMapBinding> implements BaiduMap.OnMapClickListener,
        OnGetGeoCoderResultListener {

    private static final String TAG = BaiduMapActivity.class.getSimpleName();
    private ActivityResultLauncher<String> locationIntent;//定位请求

    public LocationClient mLocationClient = null;
    private BaiduMap mBaiduMap;
    private GeoCoder geoCoder;
    private LatLng latLng;

    @Override
    protected void onRegister() {
        super.onRegister();
        locationIntent = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                //开始定位
                showMsg("开始定位");
                mLocationClient.start();
            }
        });
    }

    @Override
    protected void onCreate() {
        back(binding.toolbar);
        initLocation();
        //初始化地图
        initMap();
        //初始化地理编码
        initGeoCoder();
        //检查是否拥有定位权限
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationIntent.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            //开始定位
            mLocationClient.start();
        }

    }

    private void initLocation() {
        try {
            //同意隐私政策
            LocationClient.setAgreePrivacy(true);
            mLocationClient = new LocationClient(this);

            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);//需要地址
            option.setNeedNewVersionRgc(true);//最新版本的地址信息
            option.setIsNeedAltitude(true);//需要高度
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            mLocationClient.setLocOption(option);
            //声明LocationClient类
            MyLocationListener myLocationListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myLocationListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGeoCoder() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    private void initMap() {
        mBaiduMap = binding.mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //地图点击监听
        mBaiduMap.setOnMapClickListener(this);
    }

    @Override
    protected void onResume() {
        binding.mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        binding.mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
    }


    /**
     * 地图点击
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.clear();//清除之前的图层
        changeMapCenter(latLng);
    }

    /**
     * 地图Poi点击
     *
     * @param mapPoi
     */
    @Override
    public void onMapPoiClick(MapPoi mapPoi) {

    }

    /**
     * 地理编码（即地址转坐标）
     * @param geoCodeResult
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    /**
     * 逆地理编码（即坐标转地址）
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.d(TAG, "onGetReverseGeoCodeResult: 没有找到检索结果");
            return;
        }
        //详细地址
        String address = reverseGeoCodeResult.getAddress();
        //行政区号
        int adCode = reverseGeoCodeResult.getCityCode();
        Log.d(TAG, "address: " + address);
    }

    /**
     * 定位监听
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double altitude = location.getAltitude();//获取海拔高度
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            String describe = location.getLocationDescribe();
            Log.i(TAG, "onReceiveLocation: " + describe);
            //设置标题 显示所在区和街道
            binding.toolbar.setTitle(location.getDistrict() + " " + location.getStreet());

            latLng = new LatLng(latitude, longitude);
            //mapView 销毁后不在处理新接收的位置
            changeMapCenter(latLng);
        }
    }

    /**
     * 切换地图中心点
     *
     * @param latLng
     */
    private void changeMapCenter(LatLng latLng) {
        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(location.getDirection())
                .latitude(latLng.latitude)
                .longitude(latLng.longitude).build();
        //设置地图位置
        mBaiduMap.setMyLocationData(locData);

        //根据经纬度进行反编码
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).pageNum(0).pageSize(100));

        MapStatus.Builder builder = new MapStatus.Builder()//创建地图状态构造器
                .target(latLng)//设置地图中心点，传入经纬度对象
                .zoom(18.0f);//设置地图缩放级别 4-21 越大约清晰

        //改变地图状态，使用地图状态更新工厂中的新地图状态方法，传入状态构造器
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
}