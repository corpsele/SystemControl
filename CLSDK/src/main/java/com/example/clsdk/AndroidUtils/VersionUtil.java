package com.example.clsdk.AndroidUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * �������ܣ�app�汾����
 */
public class VersionUtil {
    /**
     * ��ȡ�汾��
     *
     * @return ��ǰӦ�õİ汾��
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * �汾�Ƚ�
     *
     * @param nowVersion    app�汾
     * @param serverVersion �������汾
     * @return
     */
    public static boolean compareVersion(String nowVersion, String serverVersion) {

        if (nowVersion != null && serverVersion != null) {
            String[] nowVersions = nowVersion.split("\\.");
            String[] serverVersions = serverVersion.split("\\.");
            if (nowVersions != null && serverVersions != null && nowVersions.length > 1 && serverVersions.length > 1) {
                int nowVersionsFirst = Integer.parseInt(nowVersions[0]);
                int serverVersionFirst = Integer.parseInt(serverVersions[0]);
                int nowVersionsSecond = Integer.parseInt(nowVersions[1]);
                int serverVersionSecond = Integer.parseInt(serverVersions[1]);
                if (nowVersionsFirst < serverVersionFirst) {
                    return true;
                } else if (nowVersionsFirst == serverVersionFirst && nowVersionsSecond < serverVersionSecond) {
                    return true;
                }
            }
        }
        return false;
    }
}