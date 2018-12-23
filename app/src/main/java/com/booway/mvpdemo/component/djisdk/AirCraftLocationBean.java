package com.booway.mvpdemo.component.djisdk;

import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.LocationCoordinate3D;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：
 */

public class AirCraftLocationBean {

    public AirCraftLocationBean(LocationCoordinate3D locationCoordinate3D,
                                GPSSignalLevel signalLevel) {
        this.mLocationCoordinate3D = locationCoordinate3D;
        this.mGPSSignalLevel = signalLevel;

    }

    private LocationCoordinate3D mLocationCoordinate3D;

    public LocationCoordinate3D getLocationCoordinate3D() {
        return mLocationCoordinate3D;
    }

//    public void setLocationCoordinate3D(LocationCoordinate3D locationCoordinate3D) {
//        mLocationCoordinate3D = locationCoordinate3D;
//    }

    private GPSSignalLevel mGPSSignalLevel;


    public GPSSignalLevel getGPSSignalLevel() {
        return mGPSSignalLevel;
    }

//    public void setGPSSignalLevel(GPSSignalLevel GPSSignalLevel) {
//        mGPSSignalLevel = GPSSignalLevel;
//    }
}
