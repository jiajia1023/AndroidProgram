package com.mylibrary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.mylibrary.helper.SqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据库管理工具
 * This tool is a database manage utils
 * Created by ChrisZou on 2016/4/9.
 */
public class DBUtils {

    private static Context mContext;
    private static DBUtils instance;
    private SqlHelper DBHepler;
    private ContentValues values;
    private SQLiteDatabase db;
    private List<CityData> cityBeen = new ArrayList<>();
    /**
     * 全国地区表
     */
    public static final String nationwideArea = "lscity";
    public static final int FINISH = 12;
    public static final int ERROR = 13;

    public boolean isSearch = true;

    private Executors mExecutors = null;
    private ExecutorService mExecutorService = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINISH:
                    if (isSearch) {
                        ((DBUtils.OnSearchListener) msg.obj).onFinish(cityBeen);
                        break;
                    }
                    mExecutorService.shutdownNow();
                    break;
                case ERROR:

                    break;
            }
            db.close();
        }
    };


    public static DBUtils newInstance(Context context) {
        if (instance == null || mContext == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }

    private DBUtils(Context context) {
        mContext = context;
        init(context);
    }

    public void init(Context context) {
        DBHepler = new SqlHelper ( context, SqlHelper.DBBeatName);
        values = new ContentValues();
        db = DBHepler.getWritableDatabase();
        DBHepler = new SqlHelper(context, SqlHelper.DBName);
        db = DBHepler.getWritableDatabase();
        db.close();
        mExecutorService = Executors.newCachedThreadPool();
//        mExecutorService=Executors.newFixedThreadPool(5);

    }

    public SQLiteDatabase openCityDB(Context context) {
        if (db.isOpen()) {
            db.close();
        }
        SqlHelper DBHepler = new SqlHelper(context, SqlHelper.DBName);
        SQLiteDatabase db = DBHepler.getWritableDatabase();
        return db;
    }

    public SQLiteDatabase openDB(Context context) {
        SqlHelper DBHepler = new SqlHelper(context, SqlHelper.DBBeatName);
        SQLiteDatabase db = DBHepler.getWritableDatabase();
        return db;
    }


    /**
     * 查找城市
     *
     * @return
     */
    public void readCity(final OnSearchListener onSearchListener) {
        db = openCityDB(mContext);
        this.isSearch = true;
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                cityBeen = new ArrayList<>();
                try {
                    cursor = db.query(nationwideArea, null, "target_level=?", new String[]{"1"}, null, null, null);//查询一级城市
                    if (cursor.moveToFirst()) {
                        do {
                            CityData bean = new CityData();
                            bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                            bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                            bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                            bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                            bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                            bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                            bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                            bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                            bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                            bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                            bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                            bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                            bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                            bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                            bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                            bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                            cityBeen.add(readOneBelow(bean));
                        } while (cursor.moveToNext());
                    }
                    sendMessage(FINISH, onSearchListener);
                } catch (Exception ex) {
                    Log.e("TEST", "readCity: 数据库查询错误");
                    sendMessage(ERROR, onSearchListener);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (db != null && db.isOpen()) {
                        db.close();
                    }
                }
            }

        });
    }

    public List<CityData> searchCity(String searchKey, final List<CityData> dataList) {
        final List<CityData> parentBeen = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            final CityData parentBean = dataList.get(i);
            searchKey = searchKey.toLowerCase();
            if (parentBean.targetPinying.contains(searchKey) || parentBean.targetPy.contains(searchKey.toUpperCase()) || parentBean.targetPy.contains(searchKey) || parentBean.targetName.contains(searchKey)) {
                parentBeen.add(parentBean);
                continue;
            }
            List<CityData> childBeen = new ArrayList<>();
            final List<CityData> childParentList = parentBean.cityChildList;
            for (CityData childBean : childParentList) {
                if (childBean.targetPinying.contains(searchKey) || childBean.targetPy.contains(searchKey) || childBean.targetPy.contains(searchKey.toUpperCase()) || parentBean.targetName.contains(searchKey)) {
                    childBeen.add(childBean);
                }
            }
            if (childBeen.size() > 0) {
                parentBeen.addAll(childBeen);
            }
        }
        return parentBeen;
    }


    final public static String TableNameAllCitys_field[] = {"city_name",
            "province_name", "province_code", "target_code", "target_name",
            "target_level", "province_py", "target_py", "target_lat",
            "target_lng", "target_pinyin", "city_code", "station_code"};

//    public String[] searchCityId(Context context, String provinceTemp, String cityTemp, String address) {
//        String cityCode =.locCityId;
//        String cityName = MainApplication.locCityName;
//        if (!db.isOpen()) {
//            openDB(context);
//        }
//        Cursor cursor = null;
//        try {
//            String[] selectionArgs = new String[]{provinceTemp, cityTemp};
//            cursor = db.rawQuery("SELECT * FROM " + nationwideArea + " WHERE "
//                    + TableNameAllCitys_field[1] + " LIKE '" + provinceTemp
//                    + "%' " + " OR " + TableNameAllCitys_field[0] + " LIKE '"
//                    + cityTemp + "%' ORDER BY " + TableNameAllCitys_field[4]
//                    + " DESC ", new String[]{});
//            if (cursor.moveToFirst()) {
//                do {
//                    String address2 = cursor.getString(cursor.getColumnIndex("target_name"));
//                    if (address.contains(address2)) {
//                        cityName = cursor.getString(cursor.getColumnIndex("target_name"));
//                        cityCode = cursor.getString(cursor.getColumnIndex("target_code"));
//                        return new String[]{cityCode, cityName};
//                    }
//
//                } while (cursor.moveToNext());
//                do {
//                    String address2 = cursor.getString(cursor.getColumnIndex("target_name"));
//                    if (cityTemp.contains(address2)) {
//                        cityName = cursor.getString(cursor.getColumnIndex("target_name"));
//                        cityCode = cursor.getString(cursor.getColumnIndex("target_code"));
//                        return new String[]{cityCode, cityName};
//                    }
//
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception ex) {
//            Log.e("TEST", "readCity: 数据库查询错误");
//            ex.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//        return new String[]{cityCode, cityName};
//    }

    /**
     * 查询一级子城市
     *
     * @param cityBean
     * @return
     */
    private CityData readOneBelow(CityData cityBean) {
//        db=openCityDB(mContext);
        Cursor cursor = db.query(nationwideArea, null, "province_py=?", new String[]{cityBean.provincePy}, null, null, null);
        List<CityData> cityChildBeen = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    CityData bean = new CityData();
                    bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                    bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                    bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                    bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                    bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                    bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                    bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                    bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                    bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                    bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                    bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                    bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                    bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                    bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                    bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                    cityChildBeen.add(readTwoBelow(bean));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return readTwoBelow(cityChildBeen.get(0));
    }

    /**
     * 查询二级子城市
     *
     * @param cityBean
     * @return
     */
    private CityData readTwoBelow(CityData cityBean) {
//        db=openCityDB(mContext);
        Cursor cursor = db.query(nationwideArea, null, "target_py=?", new String[]{cityBean.targetPy}, null, null, null);
        List<CityData> cityChildBeen = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    CityData bean = new CityData();
                    bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                    bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                    bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                    bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                    bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                    bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                    bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                    bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                    bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                    bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                    bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                    bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                    bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                    bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                    bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                    cityChildBeen.add(bean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cityBean.cityChildList = cityChildBeen;
        return cityBean;
    }

    /**
     * 查找省
     *
     * @return
     */
    public List<CityData> readProvince() {
        db = openCityDB(mContext);
        Cursor cursor = null;
        cityBeen = new ArrayList<>();
        try {
            cursor = db.query(nationwideArea, null, "target_level=?", new String[]{"1"}, null, null, null);//查询一级城市
            if (cursor.moveToFirst()) {
                do {
                    CityData bean = new CityData();
                    bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                    bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                    bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                    bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                    bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                    bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                    bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                    bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                    bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                    bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                    bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                    bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                    bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                    bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                    bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                    cityBeen.add(bean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("TEST", "readCity: 数据库查询错误");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return cityBeen;
    }

    /**
     * 查询市
     *
     * @param provinceName
     * @return
     */
    public List<CityData> readCity(String provinceName) {
        if(TextUtils.isEmpty(provinceName))
            provinceName="北京";
        db=openCityDB(mContext);
        Cursor cursor = db.query(nationwideArea, null, "province_name=? and target_level=?", new String[]{provinceName,"2"}, null, null, null);
        List<CityData> cityChildBeen = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    CityData bean = new CityData();
                    bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                    bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                    bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                    bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                    bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                    bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                    bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                    bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                    bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                    bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                    bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                    bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                    bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                    bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                    bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                    cityChildBeen.add(bean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(!(cityChildBeen.size()>0)){
            CityData bean = new CityData();
            bean.targetName=provinceName;
            bean.cityName=provinceName;
            bean.provinceName=provinceName;
            cityChildBeen.add(bean);
        }
        return cityChildBeen;
    }

    /**
     * 查询区
     *
     * @param cityName
     * @return
     */
    public List<CityData> readDistrict(String cityName) {
        if(TextUtils.isEmpty(cityName))
            cityName="海淀";
        db=openCityDB(mContext);
        Cursor cursor = db.query(nationwideArea, null, "city_name=? and target_level=?", new String[]{cityName,"3"}, null, null, null);
        List<CityData> cityChildBeen = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    CityData bean = new CityData();
                    bean.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                    bean.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                    bean.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
                    bean.targetCode = cursor.getString(cursor.getColumnIndex("target_code"));
                    bean.targetName = cursor.getString(cursor.getColumnIndex("target_name"));
                    bean.targetLevel = cursor.getString(cursor.getColumnIndex("target_level"));
                    bean.provincePy = cursor.getString(cursor.getColumnIndex("province_py"));
                    bean.targetPy = cursor.getString(cursor.getColumnIndex("target_py"));
                    bean.targetLat = cursor.getFloat(cursor.getColumnIndex("target_lat"));
                    bean.targetLng = cursor.getFloat(cursor.getColumnIndex("target_lng"));
                    bean.targetPinying = cursor.getString(cursor.getColumnIndex("target_pinyin"));
                    bean.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                    bean.stationCode = cursor.getString(cursor.getColumnIndex("station_code"));
                    bean.nationalCode = cursor.getString(cursor.getColumnIndex("national_code"));
                    bean.postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                    bean.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
                    cityChildBeen.add(bean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(!(cityChildBeen.size()>0)){
            CityData cityData=new CityData();
            cityData.targetName=cityName;
            cityChildBeen.add(cityData);
        }
        return cityChildBeen;
    }

    private void sendMessage(int what, OnSearchListener onSearchListener) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = onSearchListener;
        handler.sendMessage(msg);
    }

    public void destroy() {
        this.isSearch = false;
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

public interface OnSearchListener {
    void onFinish(List<CityData> cityDatas);
}

public static class CityData {
    public String cityName;
    public String provinceName;
    public String provinceCode;
    public String targetCode;
    public String targetName;
    public String targetLevel;
    public String provincePy;
    public String targetPy;
    public float targetLat;
    public float targetLng;
    public String targetPinying;
    public String cityCode;
    public String stationCode;
    public String nationalCode;
    public String postCode;
    public String areaCode;
    public List<CityData> cityChildList = new ArrayList<>();
}
}
