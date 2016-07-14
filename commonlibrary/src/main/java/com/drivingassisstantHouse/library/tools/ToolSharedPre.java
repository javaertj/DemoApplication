package com.drivingassisstantHouse.library.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * 关于sharedPreference的操作方法
 *
 * @author sunji
 */
public class ToolSharedPre {


    private Context mContext = null;

    public ToolSharedPre(Context context) {
        this.mContext = context;
    }


    /**
     * 获取一个sharedPreference
     *
     * @param fileName 文件名称
     **/
    private SharedPreferences getSp(String fileName) {
        //File creation mode: the default mode is 0
        SharedPreferences preferences = mContext.getSharedPreferences(
                fileName, Context.MODE_MULTI_PROCESS);//允许多进程，否则remoteService无法操作
        return preferences;
    }


    /**
     * 判断字符串是否为empty
     *
     * @param str 要判断的字符串
     * @return true为空或长度为0 ，其他则false
     **/

    private boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /*****************************清理sp中的内容*********************************/

    /**
     * 清理指定sp文件中的所有的信息
     *
     * @param fileName 文件名称，不能为空
     * @return true 清理sp信息成功,false其他任何情况
     **/
    public boolean clearAllPreferences(String fileName) {

        if (isEmpty(fileName))
            return false;
        Editor editor = getSp(fileName).edit();
        editor.clear();
        return editor.commit();
    }

    /**
     * 移除sp中指定的某个key及其value
     *
     * @param fileName 文件名
     * @param key      要移除的key
     * @return true 移除指定key成功,false其他任何情况
     **/
    public boolean clearSpByKey(String fileName, String key) {
        if (isEmpty(fileName) || isEmpty(key)) {
            return false;
        }
        Editor editor = getSp(fileName).edit();
        editor.remove(key);
        return editor.commit();
    }

    /***********************************保存数据到sp************************************/
    /**
     * 保存int类型数据
     *
     * @param fileName sp文件名称
     * @param key      键
     * @param data     值
     * @return true 成功，其他则 false
     */
    public boolean saveIntToSp(String fileName, String key, int data) {
        if (isEmpty(fileName) || isEmpty(key)) {
            return false;
        }
        Editor editor = getSp(fileName).edit();
        editor.putInt(key, data);
        return editor.commit();
    }

    /**
     * 保存指定的字符串键值对到sp中
     *
     * @param fileName sp文件名称
     * @param key      要保存的key
     * @param value    要保存的key对应的value
     * @return true保存键值对成功, false其他任何情况
     **/
    public boolean saveStringToSp(String fileName, String key, String value) {
        if (isEmpty(fileName) || isEmpty(key)) {
            return false;
        }
        Editor editor = getSp(fileName).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 保存指定的布尔键值对到sp中
     *
     * @param fileName sp文件名称
     * @param key      要保存的key
     * @param value    要保存的key对应的value
     * @return true保存键值对成功, false其他任何情况
     **/
    public boolean saveBooleanToSp(String fileName, String key, boolean value) {
        if (isEmpty(fileName) || isEmpty(key)) {
            return false;
        }
        Editor editor = getSp(fileName).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 保存指定的long值对到sp中
     *
     * @param fileName sp文件名称
     * @param key      要保存的key
     * @param value    要保存的key对应的value
     * @return true保存键值对成功, false其他任何情况
     **/
    public boolean saveLongToSp(String fileName, String key, long value) {
        if (isEmpty(fileName) || isEmpty(key))
            return false;
        Editor editor = getSp(fileName).edit();
        editor.putLong(key, value);
        return editor.commit();
    }


    /**
     * 保存浮点数到指定的sp文件中
     *
     * @param filename sp文件名称
     * @param key      key名称
     * @param value    key对应的值
     * @return 保存是否成功，true成功，其他false
     */
    public boolean saveFloatToSp(String filename, String key, float value) {
        if (isEmpty(filename) || isEmpty(key))
            return false;
        Editor editor = getSp(filename).edit();
        editor.putFloat(key, value);
        return editor.commit();
    }


    /*****************************从sp中获取数据********************************/


    /**
     * 获取int类型数据
     *
     * @param fileName 文件名称
     * @param key      键
     * @return 若对应的key存在则返回相应的值，若不存在则返回-1;
     * @throws ClassCastException 若指定的key对应的不是int类型，则抛出异常
     */
    public int getIntFromSp(String fileName, String key) throws ClassCastException {
        if (isEmpty(fileName) || isEmpty(key))
            return -1;
        return getSp(fileName).getInt(key, -1);
    }

    /**
     * 从指定的Sp中取出指定的浮点数值
     *
     * @param fileName sp文件名称
     * @param key      关键字
     * @return 将返回相应的值，如果不存在则返回-1.0
     * @throws ClassCastException 若指定的key对应的不是float类型，则抛出异常
     */
    public float getFloatFromSp(String fileName, String key) throws ClassCastException {
        float result = -1.0F;
        if (isEmpty(fileName) || isEmpty(key)) {
            return result;
        }
        return getSp(fileName).getFloat(key, result);


    }

    /**
     * 从指定的sp文件中根据指定的key获取相应字符串的数据，默认返回空串
     *
     * @param fileName sp文件名称
     * @param key      指定的key
     * @return 如果存在则返回相应的字符串值，如果不存在key则返回默认值
     * @throws ClassCastException 若指定的key对应的不是String类型，则抛出异常
     */
    public String getStringFromSp(String fileName, String key) throws ClassCastException {
        String result = "";
        if (isEmpty(fileName) || isEmpty(key))
            return result;
        return getSp(fileName).getString(key, result);
    }

    /**
     * 从指定的sp文件中根据指定的key获取相应的布尔类型数据
     *
     * @param fileName sp文件名称
     * @param key      指定的key
     * @return 如果存在则返回相应的布尔值，如果不存在key，则返回默认值
     * @throws ClassCastException 若指定的key对应的不是boolean类型，则抛出异常
     */
    public boolean getBooleanFromSp(String fileName, String key) throws ClassCastException {
        boolean result = false;
        if (isEmpty(fileName) || isEmpty(key))
            return result;
        return getSp(fileName).getBoolean(key, result);
    }


    /**
     * 从指定的sp文件中根据指定的key获取相应的long类型数据
     *
     * @param fileName sp文件名称
     * @param key      指定的key
     * @return 如果存在则返回相应的布尔值，如果不存在key，则返回默认值
     * @throws ClassCastException 若指定的key对应的不是long类型，则抛出异常
     */
    public long getLongFromSp(String fileName, String key) throws ClassCastException {
        long result = -1;
        if (isEmpty(fileName) || isEmpty(key))
            return result;
        return getSp(fileName).getLong(key, result);

    }

    /**
     * 从指定的sp文件中获取所有数据
     *
     * @param fileName sp文件名称
     * @return 如果成功则返回相应的key-value键值对，其他则返回null
     * @throws ClassCastException 若指定的key对应的不是long类型，则抛出异常
     */
    public Map<String, ?> getAllFromSp(String fileName) throws NullPointerException {
        if (isEmpty(fileName)) {
            return null;
        }
        return getSp(fileName).getAll();
    }


}
