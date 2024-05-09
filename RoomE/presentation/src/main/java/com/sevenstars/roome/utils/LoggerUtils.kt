package com.sevenstars.roome.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/* -------------------------------------------------------------------------------------------
    val logger = LoggerUtils().getInstance()

    logBtn.setOnClickListener {
        logger.debug("디버그 로그").info("인포 로그").warning("워닝 로그").error("에러 로그")
    }
------------------------------------------------------------------------------------------- */

class LoggerUtils {
    fun getInstance(): LoggerUtils {
        Logger.addLogAdapter(AndroidLogAdapter())
        return this
    }

    fun debug(s: String): LoggerUtils {
        Logger.t(TAG).d(s)
        return this
    }

    fun info(s: String): LoggerUtils {
        Logger.t(TAG).i(s)
        return this
    }

    fun warning(s: String): LoggerUtils {
        Logger.t(TAG).w(s)
        return this
    }

    fun error(s: String): LoggerUtils {
        Logger.t(TAG).e(s)
        return this
    }

    companion object {
        const val TAG = "LOGGER"
    }
}