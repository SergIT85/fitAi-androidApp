package com.by_korchagin.core.common.logger

import com.by_korchagin.core.common.base.Logger

object MyLogger : Logger {
    override fun d(tag: String, message: String) { android.util.Log.d(tag, message) }
    override fun e(tag: String, message: String, throwable: Throwable?) { android.util.Log.e(tag, message, throwable) }
    override fun w(tag: String, message: String) { android.util.Log.w(tag, message) }
    override fun i(tag: String, message: String) { android.util.Log.i(tag, message) }
}
