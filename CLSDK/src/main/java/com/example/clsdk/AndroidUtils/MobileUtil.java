package com.example.clsdk.AndroidUtils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �������ܣ��ֻ���Ϣ�ɼ�����
 */
public class MobileUtil {

    /**
     * Print telephone info.
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String printMobileInfo(Context context) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("ϵͳʱ�䣺").append(time).append("\n");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        //IMSIǰ����λ460�ǹ��Һ��룬��ε���λ����Ӫ�̴��ţ�00��02���й��ƶ���01����ͨ��03�ǵ��š�
        String providerName = null;
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providerName = "�й��ƶ�";
            } else if (IMSI.startsWith("46001")) {
                providerName = "�й���ͨ";
            } else if (IMSI.startsWith("46003")) {
                providerName = "�й�����";
            }
        }
        sb.append(providerName).append("\n").append(getNativePhoneNumber(context)).append("\n����ģʽ��").append(getNetType(context)).append("\nIMSI�ǣ�").append(IMSI);
        sb.append("\nDeviceID(IMEI)       :").append(tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion:").append(tm.getDeviceSoftwareVersion());
        sb.append("\ngetLine1Number       :").append(tm.getLine1Number());
        sb.append("\nNetworkCountryIso    :").append(tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator      :").append(tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName  :").append(tm.getNetworkOperatorName());
        sb.append("\nNetworkType          :").append(tm.getNetworkType());
        sb.append("\nPhoneType            :").append(tm.getPhoneType());
        sb.append("\nSimCountryIso        :").append(tm.getSimCountryIso());
        sb.append("\nSimOperator          :").append(tm.getSimOperator());
        sb.append("\nSimOperatorName      :").append(tm.getSimOperatorName());
        sb.append("\nSimSerialNumber      :").append(tm.getSimSerialNumber());
        sb.append("\ngetSimState          :").append(tm.getSimState());
        sb.append("\nSubscriberId         :").append(tm.getSubscriberId());
        sb.append("\nVoiceMailNumber      :").append(tm.getVoiceMailNumber());

        return sb.toString();
    }


    /**
     * ��ӡϵͳ��Ϣ
     * @return
     */
    public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  ϵͳ��Ϣ  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append("\nSERIAL             :").append(Build.SERIAL);
        }
        return sb.toString();
    }

    /****
     * ��ȡ��������
     *
     * @param context
     * @return
     */
    public static String getNetType(Context context) {
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info == null) {
                return "";
            }
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
                    return "CDMA";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                    return "EDGE";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                    return "EVDO0";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                    return "EVDOA";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS) {
                    return "GPRS";
                }
                /*
                 * else if(info.getSubtype() ==
                 * TelephonyManager.NETWORK_TYPE_HSDPA){ return "HSDPA"; }else
                 * if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA){
                 * return "HSPA"; }else if(info.getSubtype() ==
                 * TelephonyManager.NETWORK_TYPE_HSUPA){ return "HSUPA"; }
                 */
                else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    return "UMTS";
                } else {
                    return "3G";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * ��ȡ��ǰ���õĵ绰����
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE})
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
        return String.format("�ֻ���: %s", NativePhoneNumber);
    }
    /**
     * IMSI�ǹ����ƶ��û�ʶ����ļ��(International Mobile Subscriber Identity)
     * IMSI����15λ����ṹ���£�
     * MCC+MNC+MIN
     * MCC��Mobile Country Code���ƶ������룬��3λ���й�Ϊ460;
     * MNC:Mobile NetworkCode���ƶ������룬��2λ
     * ���й����ƶ��Ĵ���Ϊ��00��02����ͨ�Ĵ���Ϊ01�����ŵĴ���Ϊ03
     * ���������ǣ�Ҳ��Android�ֻ���APN�����ļ��еĴ��룩��
     * �й��ƶ���46000 46002
     * �й���ͨ��46001
     * �й����ţ�46003
     * ������һ�����͵�IMSI����Ϊ460030912121001
     */
    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        return IMSI;
    }

    /**
     * IMEI��International Mobile Equipment Identity �������ƶ��豸��ʶ���ļ��
     * IMEI��15λ������ɵġ����Ӵ��š�������ÿ̨�ֻ�һһ��Ӧ�����Ҹ�����ȫ����Ψһ��
     * �����Ϊ��
     * 1. ǰ6λ��(TAC)�ǡ��ͺź�׼���롱��һ��������
     * 2. ���ŵ�2λ��(FAC)�ǡ����װ��š���һ��������
     * 3. ֮���6λ��(SNR)�ǡ����š���һ���������˳���
     * 4. ���1λ��(SP)ͨ���ǡ�0�壬Ϊ�����룬Ŀǰ�ݱ���
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }



    /////_________________ ˫��˫��ϵͳIMEI��IMSI������see more on http://benson37.iteye.com/blog/1923946��

    /**
     * ˫��˫�����IMSI��IMSI��PhoneType��Ϣ
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static class TeleInfo {
        public String imsi_1;
        public String imsi_2;
        public String imei_1;
        public String imei_2;
        public int phoneType_1;
        public int phoneType_2;

        @Override
        public String toString() {
            return "TeleInfo{" +
                    "imsi_1='" + imsi_1 + '\'' +
                    ", imsi_2='" + imsi_2 + '\'' +
                    ", imei_1='" + imei_1 + '\'' +
                    ", imei_2='" + imei_2 + '\'' +
                    ", phoneType_1=" + phoneType_1 +
                    ", phoneType_2=" + phoneType_2 +
                    '}';
        }
    }

    /**
     * MTK Phone.
     *
     * ��ȡ MTK �����˫�� IMSI��IMSI ��Ϣ
     */
    public static TeleInfo getMtkTeleInfo(Context context) {
        TeleInfo teleInfo = new TeleInfo();
        try {
            Class<?> phone = Class.forName("com.android.internal.telephony.Phone");

            Field fields1 = phone.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int simId_1 = (Integer) fields1.get(null);

            Field fields2 = phone.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int simId_2 = (Integer) fields2.get(null);

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getSubscriberIdGemini = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", int.class);
            String imsi_1 = (String) getSubscriberIdGemini.invoke(tm, simId_1);
            String imsi_2 = (String) getSubscriberIdGemini.invoke(tm, simId_2);
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;

            Method getDeviceIdGemini = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", int.class);
            String imei_1 = (String) getDeviceIdGemini.invoke(tm, simId_1);
            String imei_2 = (String) getDeviceIdGemini.invoke(tm, simId_2);

            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;

            Method getPhoneTypeGemini = TelephonyManager.class.getDeclaredMethod("getPhoneTypeGemini", int.class);
            int phoneType_1 = (Integer) getPhoneTypeGemini.invoke(tm, simId_1);
            int phoneType_2 = (Integer) getPhoneTypeGemini.invoke(tm, simId_2);
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teleInfo;
    }

    /**
     * MTK Phone.
     *
     * ��ȡ MTK �����˫�� IMSI��IMSI ��Ϣ
     */
    public static TeleInfo getMtkTeleInfo2(Context context) {
        TeleInfo teleInfo = new TeleInfo();
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> phone = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = phone.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int simId_1 = (Integer) fields1.get(null);
            Field fields2 = phone.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int simId_2 = (Integer) fields2.get(null);

            Method getDefault = TelephonyManager.class.getMethod("getDefault", int.class);
            TelephonyManager tm1 = (TelephonyManager) getDefault.invoke(tm, simId_1);
            TelephonyManager tm2 = (TelephonyManager) getDefault.invoke(tm, simId_2);

            String imsi_1 = tm1.getSubscriberId();
            String imsi_2 = tm2.getSubscriberId();
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;

            String imei_1 = tm1.getDeviceId();
            String imei_2 = tm2.getDeviceId();
            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;

            int phoneType_1 = tm1.getPhoneType();
            int phoneType_2 = tm2.getPhoneType();
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teleInfo;
    }

    /**
     * Qualcomm Phone.
     * ��ȡ ��ͨ �����˫�� IMSI��IMSI ��Ϣ
     */
    public static TeleInfo getQualcommTeleInfo(Context context) {
        TeleInfo teleInfo = new TeleInfo();
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> simTMclass = Class.forName("android.telephony.MSimTelephonyManager");
            // Object sim = context.getSystemService("phone_msim");
            Object sim = context.getSystemService(Context.TELEPHONY_SERVICE);
            int simId_1 = 0;
            int simId_2 = 1;

            Method getSubscriberId = simTMclass.getMethod("getSubscriberId", int.class);
            String imsi_1 = (String) getSubscriberId.invoke(sim, simId_1);
            String imsi_2 = (String) getSubscriberId.invoke(sim, simId_2);
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;

            Method getDeviceId = simTMclass.getMethod("getDeviceId", int.class);
            String imei_1 = (String) getDeviceId.invoke(sim, simId_1);
            String imei_2 = (String) getDeviceId.invoke(sim, simId_2);
            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;

            Method getDataState = simTMclass.getMethod("getDataState");
            int phoneType_1 = tm.getDataState();
            int phoneType_2 = (Integer) getDataState.invoke(sim);
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teleInfo;
    }

    /**
     * Spreadtrum Phone.
     *
     * ��ȡ չѶ �����˫�� IMSI��IMSI ��Ϣ
     */
    public static TeleInfo getSpreadtrumTeleInfo(Context context) {
        TeleInfo teleInfo = new TeleInfo();
        try {

            TelephonyManager tm1 = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi_1 = tm1.getSubscriberId();
            String imei_1 = tm1.getDeviceId();
            int phoneType_1 = tm1.getPhoneType();
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imei_1 = imei_1;
            teleInfo.phoneType_1 = phoneType_1;

            Class<?> phoneFactory = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method getServiceName = phoneFactory.getMethod("getServiceName", String.class, int.class);
            getServiceName.setAccessible(true);
            TelephonyManager tm2 = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi_2 = tm2.getSubscriberId();
            String imei_2 = tm2.getDeviceId();
            int phoneType_2 = tm2.getPhoneType();
            teleInfo.imsi_2 = imsi_2;
            teleInfo.imei_2 = imei_2;
            teleInfo.phoneType_2 = phoneType_2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return teleInfo;
    }

    /**
     * ��ȡ MAC ��ַ
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac��ַ
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();

        return mac;
    }

    /**
     * ��ȡ ����ʱ��
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);

        return h + ":" + m;
    }


}