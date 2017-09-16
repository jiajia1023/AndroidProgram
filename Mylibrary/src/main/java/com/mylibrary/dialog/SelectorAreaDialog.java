package com.mylibrary.dialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mylibrary.R;
import com.mylibrary.utils.DBUtils;
import com.mylibrary.utils.ScreenUtil;
import com.mylibrary.widgets.RollView;

import java.util.ArrayList;


/**
 * 地区选择.
 * The class is a select province and city and district and county and town
 */
public class SelectorAreaDialog {

    public interface OnResultHandler {

        void handle(String[] location);
    }

    /**
     *
     */
    public enum SCROLLTYPE {

        COUNTY(1),
        TOWN(2);

        SCROLLTYPE(int value) {

            this.value = value;
        }

        public int value;

    }

    public enum MODE {

        PCD(1),
        PCDCT(2);

        MODE(int value) {

            this.value = value;
        }

        public int value;

    }

    private String[] selectorArea = new String[5];
    private MODE mode;
    private int scrollUnits = SCROLLTYPE.COUNTY.value + SCROLLTYPE.TOWN.value;
    private OnResultHandler handler;
    private Context context;
    private final String FORMAT_STR = "yyyy-MM-dd HH:mm";

    private Dialog selectorDialog;
    //省
    private RollView provinceView;
    //市
    private RollView cityView;
    //区
    private RollView districtView;
    //县
    private RollView countyView;
    //镇
    private RollView townView;

    private ArrayList<String> province, city, district, county, town;
    private int provincePosition, cityPosition, districtPosition, countyPosition, townPosition;
    private final long ANIMATORDELAY = 200L;
    private final long CHANGEDELAY = 90L;
    private TextView tv_cancle;
    private TextView tv_select, tv_title;
    private TextView countyText;
    private TextView townText;

    /**
     * 是否显示动画
     */
    private boolean isShowAnimator = true;

    public SelectorAreaDialog(Context context, OnResultHandler resultHandler) {

        this(context, resultHandler, null);
    }

    public SelectorAreaDialog(Context context, OnResultHandler resultHandler, String defaultCity) {

        this.context = context;
        this.handler = resultHandler;
        initDialog();
        initView();
        selectorArea[0] = defaultCity;
    }

    public void show() {

        initTimer();
        addListener();
        selectorDialog.show();
    }

    private void initDialog() {

        if (selectorDialog == null) {
            selectorDialog = new Dialog(context, R.style.AppTheme_TimeDialog);
            selectorDialog.setCancelable(false);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setContentView(R.layout.dialog_selector_area);
            Window window = selectorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getScreenWidth(context);
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {

        provinceView = (RollView) selectorDialog.findViewById(R.id.provinceView);
        cityView = (RollView) selectorDialog.findViewById(R.id.cityView);
        districtView = (RollView) selectorDialog.findViewById(R.id.districtView);
        countyView = (RollView) selectorDialog.findViewById(R.id.countyView);
        townView = (RollView) selectorDialog.findViewById(R.id.townView);
        tv_cancle = (TextView) selectorDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) selectorDialog.findViewById(R.id.tv_select);
        tv_title = (TextView) selectorDialog.findViewById(R.id.tv_title);
        countyText = (TextView) selectorDialog.findViewById(R.id.countyText);
        townText = (TextView) selectorDialog.findViewById(R.id.townText);

        tv_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                selectorDialog.dismiss();
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mode != null) {
                    switch (mode.value) {
                        case 1:
                            selectorArea = new String[]{selectorArea[0], selectorArea[1],
                                    selectorArea[2]};
                            break;
                        case 2:
                            break;
                    }
                }
                handler.handle(selectorArea);
                selectorDialog.dismiss();
            }
        });
        setMode(MODE.PCD);
    }

    private void initTimer() {

        initArrayList();
        initData();
        loadComponent();
    }

    private void initData() {

        //
        if (TextUtils.isEmpty(selectorArea[0])) {
            selectorArea[0] = "北京";
        }
        //
        for (DBUtils.CityData provinceData : DBUtils.newInstance(context).readProvince()) {
            this.province.add(provinceData.provinceName);
        }
        for (DBUtils.CityData cityData : DBUtils.newInstance(context)
                .readCity(selectorArea[0])) {
            this.city.add(cityData.targetName);
        }
        if (TextUtils.isEmpty(selectorArea[1])) {
            selectorArea[1] = city.get(0);
        }
        for (DBUtils.CityData districtData : DBUtils.newInstance(context)
                .readDistrict(selectorArea[1])) {
            this.district.add(districtData.targetName);
            this.county.add(districtData.targetName);
            this.town.add(districtData.targetName);
        }
        if (TextUtils.isEmpty(selectorArea[2])) {
            selectorArea[2] = "";
            if (district.size() > 0) {
                selectorArea[2] = district.get(0);
            }
        }
        if ((scrollUnits & SCROLLTYPE.COUNTY.value) == SCROLLTYPE.COUNTY.value &&
                TextUtils.isEmpty(selectorArea[3])) {
            selectorArea[3] = "";
            if (county.size() > 0) {
                selectorArea[3] = county.get(0);
            }
        }
        if ((scrollUnits & SCROLLTYPE.TOWN.value) == SCROLLTYPE.TOWN.value &&
                TextUtils.isEmpty(selectorArea[4])) {
            selectorArea[4] = "";
            if (town.size() > 0) {
                selectorArea[4] = town.get(0);
            }
        }
    }

    private void initArrayList() {

        if (province == null) province = new ArrayList<>();
        if (city == null) city = new ArrayList<>();
        if (district == null) district = new ArrayList<>();
        if (county == null) county = new ArrayList<>();
        if (town == null) town = new ArrayList<>();
        province.clear();
        city.clear();
        district.clear();
        county.clear();
        town.clear();
    }


    private void addListener() {

        provinceView.setOnSelectListener(new RollView.onSelectListener() {

            @Override
            public void onSelect(String text) {

                setCurrentValue(0, text);
                cityChange();
            }
        });
        cityView.setOnSelectListener(new RollView.onSelectListener() {

            @Override
            public void onSelect(String text) {

                setCurrentValue(1, text);
                districtChange();
            }
        });
        districtView.setOnSelectListener(new RollView.onSelectListener() {

            @Override
            public void onSelect(String text) {

                setCurrentValue(2, text);
                countyChange();
            }
        });
        countyView.setOnSelectListener(new RollView.onSelectListener() {

            @Override
            public void onSelect(String text) {

                setCurrentValue(3, text);
                townChange();
            }
        });
        townView.setOnSelectListener(new RollView.onSelectListener() {

            @Override
            public void onSelect(String text) {

                setCurrentValue(4, text);
            }
        });

    }

    private void setCurrentValue(int i, String text) {

        selectorArea[i] = text;
    }

    private void loadComponent() {

        provinceView.setData(province);
        cityView.setData(city);
        districtView.setData(district);
        countyView.setData(county);
        townView.setData(town);

        provinceView.setSelected(selectorArea[0]);
        cityView.setSelected(selectorArea[1]);
        districtView.setSelected(selectorArea[2]);
        if ((scrollUnits & SCROLLTYPE.COUNTY.value) == SCROLLTYPE.COUNTY.value) {
            countyView.setSelected(selectorArea[3]);
        }
        if ((scrollUnits & SCROLLTYPE.TOWN.value) == SCROLLTYPE.TOWN.value) {
            townView.setSelected(selectorArea[4]);
        }
        excuteScroll();
    }

    private void excuteScroll() {

        provinceView.setCanScroll(province.size() > 1);
        cityView.setCanScroll(city.size() > 1);
        districtView.setCanScroll(district.size() > 1);
        countyView.setCanScroll(county.size() > 1 && (scrollUnits & SCROLLTYPE.COUNTY.value) ==
                SCROLLTYPE.COUNTY.value);
        townView.setCanScroll(town.size() > 1 && (scrollUnits & SCROLLTYPE.TOWN.value) ==
                SCROLLTYPE.TOWN.value);
    }

    private void cityChange() {

        city.clear();
        for (DBUtils.CityData cityData : DBUtils.newInstance(context).readCity(selectorArea[0])) {
            city.add(cityData.targetName);
        }
        selectorArea[1] = city.get(0);
        cityView.setData(city);
        cityView.setSelected(0);
        excuteAnimator(ANIMATORDELAY, cityView);
        cityView.postDelayed(new Runnable() {

            @Override
            public void run() {

                districtChange();
            }
        }, CHANGEDELAY);
    }

    private void districtChange() {

        district.clear();
        for (DBUtils.CityData cityData : DBUtils.newInstance(context)
                .readDistrict(selectorArea[1])) {
            district.add(cityData.targetName);
        }
        if (district.size() > 0) {
            selectorArea[2] = district.get(0);
        } else {
            selectorArea[2] = selectorArea[1];
        }
        districtView.setData(district);
        districtView.setSelected(0);
        excuteAnimator(ANIMATORDELAY, districtView);
        districtView.postDelayed(new Runnable() {

            @Override
            public void run() {

                countyChange();
            }
        }, CHANGEDELAY);
    }

    private void countyChange() {

        if ((scrollUnits & SCROLLTYPE.COUNTY.value) == SCROLLTYPE.COUNTY.value) {
            county.clear();
            for (DBUtils.CityData cityData : DBUtils.newInstance(context)
                    .readDistrict(selectorArea[2])) {
                county.add(cityData.targetName);
            }
            selectorArea[3] = county.get(0);
            countyView.setData(county);
            countyView.setSelected(0);
            excuteAnimator(ANIMATORDELAY, countyView);
        }
        countyView.postDelayed(new Runnable() {

            @Override
            public void run() {

                townChange();
            }
        }, CHANGEDELAY);
    }

    private void townChange() {

        if ((scrollUnits & SCROLLTYPE.COUNTY.value) == SCROLLTYPE.COUNTY.value) {
            town.clear();
            for (DBUtils.CityData cityData : DBUtils.newInstance(context)
                    .readDistrict(selectorArea[3])) {
                town.add(cityData.targetName);
            }
            selectorArea[4] = town.get(0);
            townView.setData(town);
            townView.setSelected(0);
            excuteAnimator(ANIMATORDELAY, townView);
        }
        excuteScroll();
    }

    private void excuteAnimator(long ANIMATORDELAY, View view) {

        if (isShowAnimator) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                    0f, 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                    1.3f, 1f);
            PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                    1.3f, 1f);
            ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ)
                    .setDuration(ANIMATORDELAY).start();
        }
    }

    public void setNextBtTip(String str) {

        tv_select.setText(str);
    }

    public void setTitle(String str) {

        tv_title.setText(str);
    }

    public int disScrollUnit(SCROLLTYPE... scrolltypes) {

        if (scrolltypes == null || scrolltypes.length == 0) {
            scrollUnits = SCROLLTYPE.COUNTY.value + SCROLLTYPE.COUNTY.value;
        }
        for (SCROLLTYPE scrolltype : scrolltypes) {
            scrollUnits ^= scrolltype.value;
        }
        return scrollUnits;
    }

    /**
     * @param mode The mode is a display mode for control hour and minute
     * @see #mode
     */
    public void setMode(MODE mode) {

        this.mode = mode;
        switch (mode.value) {
            case 1:
                disScrollUnit(SCROLLTYPE.COUNTY, SCROLLTYPE.TOWN);
                countyView.setVisibility(View.GONE);
                townView.setVisibility(View.GONE);
                countyText.setVisibility(View.GONE);
                townText.setVisibility(View.GONE);
                break;
            case 2:
                disScrollUnit();
                countyView.setVisibility(View.VISIBLE);
                townView.setVisibility(View.VISIBLE);
                countyText.setVisibility(View.VISIBLE);
                townText.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setIsLoop(boolean isLoop) {

        this.provinceView.setIsLoop(isLoop);
        this.cityView.setIsLoop(isLoop);
        this.districtView.setIsLoop(isLoop);
        this.countyView.setIsLoop(isLoop);
        this.townView.setIsLoop(isLoop);
    }

    public void setSelectArea(String[] selectArea) {

        this.selectorArea = selectArea;
    }

    public void setShowAnimator(boolean isShowAnimator) {

        this.isShowAnimator = isShowAnimator;
    }

    public static class Builder {

        private static Builder instance;
        private static SelectorAreaDialog sMSelectorAreaDialog;

        public static Builder getInstance(Context context, OnResultHandler onResultHandler) {

            if (instance == null) {
                instance = new Builder();
            }
            if (sMSelectorAreaDialog == null) {
                sMSelectorAreaDialog = new SelectorAreaDialog(context, onResultHandler);
            }
            return instance;
        }

        public Builder setDefaultCity(String defaultCity) {

            sMSelectorAreaDialog.selectorArea[0] = defaultCity;
            return this;
        }

        public Builder show() {

            sMSelectorAreaDialog.show();
            return this;
        }

        public Builder setIsLoop(boolean isLoop) {
            sMSelectorAreaDialog.setIsLoop(isLoop);
            return this;
        }

        public SelectorAreaDialog getSelector() {
            return sMSelectorAreaDialog;
        }
    }
}
