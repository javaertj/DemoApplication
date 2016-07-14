
package com.drivingassisstantHouse.library.tools;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.drivingassisstantHouse.library.MApplication;
import com.drivingassisstantHouse.library.data.message.NotificationMessage;
import com.drivingassisstantHouse.library.widget.ProgressDialog;


/**
 * 对话框工具类
 * ---只能使用23以下的sdk进行编译，由于6.0废弃了notification的某些api，以至于某些方法不存在从而导致无法编译通过
 * @author sunji
 * @version 1.0
 */
public class ToolAlert {

	private static ProgressDialog mProgressDialog;
	
	/**
	 * 显示ProgressDialog
	 * @param context 上下文
	 * @param message 消息
	 */
	public static void loading(Context context, String message){
		
		loading(context,message,true);
	}
	
	/**
	 * 显示ProgressDialog
	 * @param context 上下文
	 * @param message 消息
	 */
	public static void loading(Context context, String message,final ILoadingOnKeyListener listener){
		
		loading(context, message, true, listener);
	}
	
	/**
	 * 显示ProgressDialog
	 * @param context 上下文
	 * @param message 消息
	 * @param cancelable 是否可以取消
	 */
	public static void loading(Context context, String message,boolean cancelable){
		
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context,message);
			mProgressDialog.setCancelable(cancelable);
		}
		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
		mProgressDialog.show();
	}
	
	/**
	 * 显示ProgressDialog
	 * @param context 上下文
	 * @param message 消息
	 */
	public static void loading(Context context, String message,boolean cancelable ,final ILoadingOnKeyListener listener){
		
		if(mProgressDialog == null){
			mProgressDialog = new ProgressDialog(context,message);
			mProgressDialog.setCancelable(cancelable);
		}
		
		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
		
		if(null != listener)
		{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	            	listener.onKey(dialog, keyCode, event);
	                return false;
	            }
	        });
		}else{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                	mProgressDialog.dismiss();
	                }
	                return false;
	            }
	        });
		}
		
		mProgressDialog.show();
	}
	
	/**
	 * 判断加载对话框是否正在加载
	 * @return 是否
	 */
	public static boolean isLoading(){
		
		if(null != mProgressDialog){
			return mProgressDialog.isShowing();
		}else{
			return false;
		}
	}
	
	/**
	 * 关闭ProgressDialog
	 */
	public static void closeLoading(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	/**
	 * 更新ProgressDialog进度消息
	 * @param message 消息
	 */
	public static void updateProgressText(String message){
		if(mProgressDialog == null ) return ;
		
		if(mProgressDialog.isShowing()){
			mProgressDialog.setMessage(message);
		}
	}
	
    /**
     * 弹出对话框
     * @param context 上下文
     * @param msg  对话框内容

     */
    public static AlertDialog dialog(Context context,String msg) {
    	return dialog(context,"",msg);
    }
    
    /**
     * 弹出对话框
	 * @param  context 上下文
     * @param title 对话框标题
     * @param msg  对话框内容

     */
    public static AlertDialog dialog(Context context,String title,String msg) {
    	return dialog(context,title,msg,null);
    }
    
    /**
     * 弹出对话框
	 * @param context 上下文
     * @param title 对话框标题
     * @param msg  对话框内容
     * @param okBtnListenner 确定按钮点击事件

     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context,title,msg,okBtnListenner,null);
    }
    
    /**
     * 弹出对话框
	 * @param context 上下文
     * @param title 对话框标题
     * @param msg  对话框内容
     * @param okBtnListenner 确定按钮点击事件
     * @param cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
    	return dialog(context,null,title,msg,okBtnListenner,cancelBtnListenner);
    }
    
    /**
     * 弹出对话框
	 * @param  context 上下文

     * @param icon  对话框图标
	 * @param title 对话框标题
     * @param msg 对话框显示的内容
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg) {
    	return dialog(context,icon,title,msg,null);
    }
    
    /**
     * 弹出对话框
	 * @param context 上下文
	 * @param  icon 对话框图标
     * @param title 对话框标题
     * @param msg  对话框内容
     * @param okBtnListenner 确定按钮点击事件

     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context, icon, title, msg, okBtnListenner, null);
    }
	
    /**
     * 弹出对话框
	 * @param context 上下文
     * @param icon  标题图标
     * @param title 对话框标题
     * @param msg  对话框内容
     * @param okBtnListenner 确定按钮点击事件
     * @param cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg, OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
        Builder dialogBuilder = new Builder(context);
        if(null != icon){
        	dialogBuilder.setIcon(icon);
        }
        if(ToolString.isNoBlankAndNoNull(title)){
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setMessage(msg);
        if(null != okBtnListenner){
        	dialogBuilder.setPositiveButton(android.R.string.ok, okBtnListenner);
        }
        if(null != cancelBtnListenner){
        	dialogBuilder.setNegativeButton(android.R.string.cancel, cancelBtnListenner);
        }
        dialogBuilder.create();
        return dialogBuilder.show();
    }
    
    /**
     * 弹出一个自定义布局对话框
     * @param context 上下文
     * @param view 自定义布局View
     */
	public static AlertDialog dialog(Context context,View view) {
		final Builder builder = new Builder(context);
		builder.setView(view);
		return builder.show();
	}
	
    /**
     * 弹出一个自定义布局对话框
     * @param context 上下文
     * @param resId 自定义布局View对应的layout id
     */
	public static AlertDialog dialog(Context context,int resId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(resId, null);
		Builder builder = new Builder(context);
		builder.setView(view);
		return builder.show();
	}
    
    /**
     * 弹出较短的Toast消息
     * @param msg 消息内容
     */
    public static void toastShort(String msg) {
        Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 弹出较短的Toast消息
     * @param msg 消息内容
     */
    public static void toastShort(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出较长的Toast消息
     * @param msg 消息内容
     */
    public static void toastLong(String msg) {
        Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_LONG).show();
    }
    
    /**
     * 弹出较长的Toast消息
     * @param msg 消息内容
     */
    public static void toastLong(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    
    /**
     * 弹出自定义时长的Toast消息
     * @param msg 消息内容
     */
    public static void toast(String msg,int duration) {
        Toast.makeText(MApplication.gainContext(), msg, duration).show();
    }
    
    /**
     * 弹出自定义时长的Toast消息
     * @param msg 消息内容
     */
    public static void toast(Context context,String msg,int duration) {
        Toast.makeText(context, msg, duration).show();
    }
    
    /**
     * 弹出Pop窗口
     * @param context 依赖界面上下文
     * @param anchor 触发pop界面的控件
     * @param viewId pop窗口界面layout
     * @param xoff 窗口X偏移量
     * @param yoff 窗口Y偏移量
     */
    public static PopupWindow popwindow(Context context,View anchor,int viewId,int xoff,int yoff){
        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
        PopupWindow pw = new PopupWindow(menuView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setTouchable(true);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
//        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * 弹出Pop窗口
     * @param anchor 触发pop界面的控件
     * @param popView pop窗口界面
     * @param xoff 窗口X偏移量
     * @param yoff 窗口Y偏移量
     */
    public static PopupWindow popwindow(View anchor,View popView,int xoff,int yoff){
        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
     * @param context 依赖界面上下文
     * @param anchor 触发pop界面的控件
     * @param viewId pop窗口界面layout
     * @param xoff 窗口X偏移量
     * @param yoff 窗口Y偏移量
     * @param outSideTouchable 点击其他地方是否关闭窗口
     */
    public static PopupWindow popwindow(Context context,View anchor,int viewId,int xoff,int yoff,boolean outSideTouchable){
        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setTouchable(outSideTouchable);
        pw.setFocusable(outSideTouchable);
        pw.setOutsideTouchable(outSideTouchable);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
     * @param anchor 触发pop界面的控件
     * @param popView pop窗口界面
     * @param xoff 窗口X偏移量
     * @param yoff 窗口Y偏移量
     * @param outSideTouchable 点击其他地方是否关闭窗口
     */
    public static PopupWindow popwindow(View anchor,View popView,int xoff,int yoff,boolean outSideTouchable){
        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(outSideTouchable);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        
        return pw;
    }
    
    /**
     * 指定坐标弹出Pop窗口
     * @param pw pop窗口对象
     * @param anchor 触发pop界面的控件
     * @param popView pop窗口界面
     * @param x 窗口X
     * @param y 窗口Y
     *
     */
    public static PopupWindow popwindowLoction(PopupWindow pw,View anchor,View popView,int x,int y){
    	if(pw == null){
    		pw = new PopupWindow(popView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
    		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    		pw.setOutsideTouchable(false);
    	}
    	
    	if (pw.isShowing()) {
    		pw.update(x,y,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			pw.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
		}	
        
        return pw;
    }
    

    /**
     * 方向枚举
     *
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
    
	/**
	 * 在控件的上方打开popwindow
	 * 
	 * @param pw PopupWindow
	 * @param anchor 打开popw的目标控件
	 * @param popView popview布局view
	 * @param direction 在目标控件的方向：上(Direction.UP)、右(Direction.RIGHT)、下(Direction.DOWN)、左(Direction.LEFT)
	 * @return
	 */
    public static PopupWindow popwindowLoction(PopupWindow pw, View anchor,View popView,Direction direction) {

		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		if (pw == null) {
			pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
			pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pw.setOutsideTouchable(false);
		}
		
		switch (direction) {
		case UP:
			if (pw.isShowing()) {
				pw.update(location[0], location[1] - pw.getHeight(),
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0],
						location[1] - pw.getHeight());
			}
			break;
		case DOWN:
			if (pw.isShowing()) {
				pw.update(location[0], location[1] + pw.getHeight(),
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0],
						location[1] + pw.getHeight());
			}
			break;
		case LEFT:
			if (pw.isShowing()) {
				pw.update(location[0]-pw.getWidth(), location[1],
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				pw.showAtLocation(anchor, Gravity.NO_GRAVITY,location[0]-pw.getWidth(), location[1]);
			}
			break;
		case RIGHT:
			if (pw.isShowing()) {
				pw.update(location[0]+anchor.getWidth(), location[1],LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0]+anchor.getWidth(), location[1]);
			}
			break;

		default:
			if (pw.isShowing()) {
				pw.update(location[0], location[1] - pw.getHeight(),
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0],
						location[1] - pw.getHeight());
			}
			break;
		}

		return pw;
	} 

	private static  NotificationManager getNoticeManager(Context context){
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		return  manager;
	}

	/**
	 * 往状态栏发送一条通知消息(sdk level 11及以上使用)
	 * @param context 上下文
	 * @param message 消息Bean
	 */
	public static  void notificationAfterSdk11(Context context,NotificationMessage message){

		//设置通知显示的参数
		Intent mIntent = new Intent(context, message.getForwardComponent());
		mIntent.setAction(ToolString.gainUUID());
		mIntent.putExtras(message.getExtras());
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		//自动更新PendingIntent的Extra数据
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 通过Notification.Builder来创建通知，注意API Level
		// API11之后才支持
		Notification.Builder builder=new Notification.Builder(context);
		builder	.setSmallIcon(message.getIconResId()); // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
						// icon)
		builder.setTicker(message.getStatusBarText());// 设置在statusbar上显示的提示文字
		builder.setWhen(System.currentTimeMillis());//触发时间
		builder.setDefaults(Notification.DEFAULT_SOUND);//	通知时发出的默认声音
		builder.setContentTitle(message.getMsgTitle());//  显示的标题
		builder	.setContentText(message.getMsgContent());// TextView中显示的详细内容
		builder.setContentIntent(pIntent); // 关联PendingIntent
		builder	.setNumber(1) ;// 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
		Notification notify2 =null;
		if(Build.VERSION.SDK_INT>=16){
			notify2=builder.build();// 16及之后增加的
		}else {
			notify2=builder.getNotification(); // 11及以后增加的，需要注意build()是在API level
		}
		notify2.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击清除
		getNoticeManager(context).notify(message.getId(), notify2);

	}

    /**
     * 往状态栏发送一条通知消息(sdk level 23以下使用)
     * @param mContext 上下文
     * @param message 消息Bean
     */

    public static void notificationBefore23(Context mContext,NotificationMessage message){

        //构造Notification
        Notification notice = new Notification();
        notice.icon = message.getIconResId();
        notice.tickerText = message.getStatusBarText();
        notice.when = System.currentTimeMillis();
        /**
         *  notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
			notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动 
			notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动 
			notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
         */
        notice.defaults = Notification.DEFAULT_SOUND;//通知时发出的默认声音
        /**
         *  notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失  
			notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行  
			notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失  
			notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应
         */
        notice.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击清除
        
        //设置通知显示的参数 
        Intent mIntent = new Intent(mContext, message.getForwardComponent());
        mIntent.setAction(ToolString.gainUUID());
        mIntent.putExtras(message.getExtras());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        //自动更新PendingIntent的Extra数据
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//6.0（23）删掉了该方法
		if(Build.VERSION.SDK_INT<23) {
//			notice.setLatestEventInfo(mContext, message.getMsgTitle(), message.getMsgContent(), pIntent);
		}
        //发送通知
		getNoticeManager(mContext).notify(message.getId(), notice);
    }



    
    
    /**
     * 发送自定义布局通知消息
     * @param mContext 上下文
     * @param message  消息Bean
     */
    public static void notificationCustomView(Context mContext,NotificationMessage message){

        //构造Notification
    	Notification mNotify = new Notification();  
        mNotify.icon = message.getIconResId();  
        mNotify.tickerText = message.getStatusBarText();  
        mNotify.when = System.currentTimeMillis();  
        mNotify.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击清除
        mNotify.contentView = message.getmRemoteViews();
        
        //设置通知显示的参数 
        Intent mIntent = new Intent(mContext, message.getForwardComponent());
        mIntent.setAction(ToolString.gainUUID());
        mIntent.putExtras(message.getExtras());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mNotify.contentIntent = contentIntent;
        
        //发送通知
		getNoticeManager(mContext).notify(message.getId(), mNotify);
    }
    
    /**
     * Loading监听对话框
     */
    public interface ILoadingOnKeyListener{
    	 public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);
    }
}
