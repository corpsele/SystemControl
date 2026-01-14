package com.example.clsdk.AndroidUtils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * �������ܣ�Toast��Ϣ����
 */
public class ToastUtil {

    /**
     * ���ʹ�õ���ʾ�������������������ʹ�á�
     * ��Ļ����λ�ö�ʱ����ʾToast��
     *
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        ToastShortCenter(context,message);
    }

    /**
     * ��Ļ�ײ��м�λ����ʾ��ʱ��Toast
     *
     * @param context
     * @param message
     */
    public static void ToastShortBottomCenter(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ��Ļ�ײ����λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortBottomLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�ײ��ұ�λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortBottomRight(Context context, String message) {

        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ����λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortCenter(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�������λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortCenterLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�����ұ�λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortCenterRight(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ��������λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortTopCenter(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�������λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortTopLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�����ұ�λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastShortTopRight(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�ײ��м�λ����ʾ��ʱ��Toast
     *
     * @param context
     * @param message
     */
    public static void ToastLongBottomCenter(Context context, String message) {
        if (context != null) {

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ��Ļ�ײ����λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongBottomLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�ײ��ұ�λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongBottomRight(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ����λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongCenter(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�������λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongCenterLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�����ұ�λ�ö�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongCenterRight(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ��������λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongTopCenter(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�������λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongTopLeft(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }

    /**
     * ��Ļ�����ұ�λ�ó�ʱ����ʾToast
     *
     * @param context
     * @param message
     */
    public static void ToastLongTopRight(Context context, String message) {
        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
            toast.show();
        }
    }

}