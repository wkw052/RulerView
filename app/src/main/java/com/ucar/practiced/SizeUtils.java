package com.ucar.practiced;

import android.content.res.Resources;
import android.util.DisplayMetrics;
/**
 * @author kaiwen.wu
 * @date 2019/11/23
 * dp、sp分别转换为px（像素）
 */
public class SizeUtils {
    private SizeUtils(){
    }
    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }
    public static int spToPixel(float sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (sp * metrics.scaledDensity + 0.5f);
    }
}
