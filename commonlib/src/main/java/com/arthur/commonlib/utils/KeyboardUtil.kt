package com.arthur.commonlib.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 输入法工具类
 */
object KeyboardUtil {

    /**
     * 显示输入法
     *
     * @param view 控件view
     */
    fun showKeyboard(view: View) {
        view.requestFocus()

        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    /**
     * 隐藏输入法
     *
     * @param view 控件view
     */
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    fun hideKeyboard(event: MotionEvent, view: View?) {
        view?.apply {
            try {
                if (view is EditText) {
                    val location = intArrayOf(0, 0)
                    view.getLocationInWindow(location)
                    val left = location[0]
                    val top = location[1]
                    val right = left + view.getWidth()
                    val bottom = top + view.getHeight()
                    // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                    if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bottom) {
                        // 隐藏键盘
                        val token = view.getWindowToken()
                        val inputMethodManager =
                            view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(token, 0)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}