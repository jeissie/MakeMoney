package com.jesse.mxapk;

import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import android.graphics.Canvas;  
import android.graphics.Color;   
import android.graphics.PorterDuff;  
import android.util.AttributeSet;  
import android.view.SurfaceHolder;  
import android.view.SurfaceView;  
import android.view.SurfaceHolder.Callback;  
  
public class MySurfaceView2 extends SurfaceView implements Callback, Runnable {  
    private Context mContext;  
    private SurfaceHolder surfaceHolder;  
    private static boolean  flag = false;// 线程标识  
    private Bitmap bitmap_bg;// 背景图  
  
    private float mSurfaceWindth, mSurfaceHeight;// 屏幕宽高  
  
    private int mBitposX;// 图片的横向位置  
    private int mBitposY;// 图片的纵向位置
    private Canvas mCanvas;  
  
    private Thread thread;  
  
    // 背景移动状态  
    private enum State {  
    	LEFT, RINGHT  
    }  
  
    // 默认为向左  
    private State state = State.LEFT;  
  
    private final int BITMAP_STEP = 1;// 背景画布移动步伐.  
  
    public MySurfaceView2(Context context, AttributeSet attrs) {  
        super(context, attrs);  
          
        this.mContext = context;  
        onStart();
    }  
  
    public void onStart(){
    	setFlag(true);
    	setFocusable(true);  
        setFocusableInTouchMode(true);  
        surfaceHolder = getHolder();  
        surfaceHolder.addCallback(this);  
    }
    /*** 
     * 进行绘制. 
     */  
    protected void onDraw() {  
        drawBG();  
        updateBG();  
    }  
  
    /*** 
     * 更新背景. 
     */  
    public void updateBG() {  
        /** 图片滚动效果 **/  
        switch (state) {  
        case LEFT:  
            mBitposX -= BITMAP_STEP;// 画布左移  
            break;  
        case RINGHT:  
            mBitposX += BITMAP_STEP;// 画布右移  
            break;  
  
        default:  
            break;  
        }  
        if (mBitposX <= -mSurfaceWindth / 2) {  
            state = State.RINGHT;  
        }  
        if (mBitposX >= 0) {  
            state = State.LEFT;  
        }  

    }  
  
    /*** 
     * 绘制背景 
     */  
    public void drawBG() {  
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清屏幕.  
        mCanvas.drawBitmap(bitmap_bg, mBitposX, 0, null);// 绘制当前屏幕背景  
    }  
  
    @Override  
    public void run() {  
        while (isFlag()) {  
            synchronized (surfaceHolder) {  
                mCanvas = surfaceHolder.lockCanvas();  
                onDraw();  
                surfaceHolder.unlockCanvasAndPost(mCanvas);  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
  
    }  
  
    @Override  
    public void surfaceCreated(SurfaceHolder holder) {  
  
        mSurfaceWindth = getWidth();  
        mSurfaceHeight = getHeight();  
        int mWindth = (int) (mSurfaceWindth * 3/2);  
        /*** 
         * 将图片缩放到屏幕的3/2倍. 
         */  
        bitmap_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.surface_view_2);  
  
        thread = new Thread(this);  
        thread.start();  
  
    }  
  
    @Override  
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
  
    }  
  
    @Override  
    public void surfaceDestroyed(SurfaceHolder holder) {  
        setFlag(false);  
    }

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		MySurfaceView2.flag = flag;
	}
  
}  

