package com.arthur.commonlib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils


/**
 * @author  Zeng Hailiang
 * @date  2022/1/4
 */

object ClipBoardUtil {

    /**
     * 复制纯文本
     */
    fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val replyClip = ClipData.newPlainText("TEXT", text)
        clipboard?.setPrimaryClip(replyClip)
    }

    /**
     * 复制htmlText
     */
    fun copyHtml(context: Context, text: String, htmlText: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val replyClip = ClipData.newHtmlText("HTML", text, htmlText)
        clipboard?.setPrimaryClip(replyClip)
    }

    /**
     * 复制intent
     */
    fun copyHtml(context: Context, intent: Intent) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val replyClip = ClipData.newIntent("INTENT", intent)
        clipboard?.setPrimaryClip(replyClip)
    }

    /**
     * 复制URI
     */
    fun copyURI(context: Context, uri: Uri) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val replyClip = ClipData.newUri(context.contentResolver, "URI", uri)
        clipboard?.setPrimaryClip(replyClip)
    }

    /**
     * 获取剪切板上的内容
     */
    fun paste(): String {
        return paste(com.arthur.commonlib.ability.AppKit.context)
    }

    /**
     * 获取剪切板上的内容
     */
    fun paste(context: Context): String {
        try {
            val manager = com.arthur.commonlib.ability.AppKit.context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (manager != null) {
                if (manager.hasPrimaryClip() && manager.primaryClip != null
                    && manager.primaryClip!!.itemCount > 0
                ) {
                    val addedText = manager.primaryClip!!.getItemAt(0).text
                    val addedTextString = addedText.toString()
                    if (!TextUtils.isEmpty(addedTextString)) {
                        return addedTextString
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}