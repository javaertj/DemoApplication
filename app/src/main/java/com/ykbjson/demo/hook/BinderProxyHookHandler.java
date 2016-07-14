package com.ykbjson.demo.hook;

import android.os.IBinder;
import android.os.IInterface;

import com.ykbjson.demo.tools.SLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 包名：com.drivingassisstantHouse.library.tools.test
 * 描述：
 * 创建者：yankebin
 * 日期：2016/3/9
 */
public class BinderProxyHookHandler implements InvocationHandler {
    private static final String TAG = "BinderProxyHookHandler";

    // 绝大部分情况下,这是一个BinderProxy对象
    // 只有当Service和我们在同一个进程的时候才是Binder本地对象
    // 这个基本不可能
    private Object base;
    private Class<?> iInterface;
    private String stubName;
    private String serviceName;


    public BinderProxyHookHandler(Object dstBase, String stubName, String iInterfaceName, String serviceName) {
        this.base = dstBase;
        this.serviceName = serviceName;
        try {
            this.stubName = stubName;
            this.iInterface = Class.forName(iInterfaceName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("queryLocalInterface".equals(method.getName())) {

            SLog.d(TAG + " hook queryLocalInterface");

            // 这里直接返回真正被Hook掉的Service接口
            // 这里的 queryLocalInterface 就不是原本的意思了
            // 我们肯定不会真的返回一个本地接口, 因为我们接管了 asInterface方法的作用
            // 因此必须是一个完整的 asInterface 过的 IInterface对象, 既要处理本地对象,也要处理代理对象
            // 这只是一个Hook点而已, 它原始的含义已经被我们重定义了; 因为我们会永远确保这个方法不返回null
            // 让 IClipboard.Stub.asInterface 永远走到if语句的else分支里面
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),

                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的
                    new Class[]{IBinder.class, IInterface.class, this.iInterface},
                    new BinderHookHandler(base, stubName, serviceName));
        } else if ("startActivity".equals(method.getName())) {
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),

                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的
                    new Class[]{IBinder.class, IInterface.class, this.iInterface},
                    new BinderHookHandler(base, stubName, serviceName));
        }

        SLog.d(TAG + " method:" + method.getName());
        return method.invoke(base, args);
    }
}
