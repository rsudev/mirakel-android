/*******************************************************************************
 * Mirakel is an Android App for managing your ToDo-Lists
 *
 * Copyright (c) 2013 Anatolij Zelenin, Georg Semmler.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.azapps.tools;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.azapps.mirakel.helper.MirakelCommonPreferences;

public class Log {
    private static final String TAG = "de.azapps.tools.Log";
    private static FileWriter fileWriter = null;
    private static boolean writeToFile = false;

    public static void enableLoggingToFile() {
        writeToFile = true;
    }

    public static void disableLoggingToFile() {
        writeToFile = false;
        fileWriter = null;
    }

    public static void d(final @Nullable String tag, final @Nullable  String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(final @Nullable  String tag, final @Nullable  String msg, final Throwable e) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.d(tag, msg, e);
        }
    }

    public static void e(final @Nullable  String tag, final @Nullable String msg) {
        if (tag == null || msg == null) {
            return;
        }
        android.util.Log.e(tag, msg);
        write("e", tag, msg);
    }

    public static void e(final @Nullable String tag, final @Nullable String msg, final Throwable tr) {
        if (tag == null || msg == null) {
            return;
        }
        android.util.Log.e(tag, msg, tr);
        write("e", tag, msg, tr);
    }


    private static String getStackTraceString(final Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }


    public static void i(final @Nullable String tag, final @Nullable String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(final @Nullable String tag, final @Nullable String msg, final Throwable e) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.i(tag, msg, e);
        }
        write("i", tag, msg, e);
    }

    public static void v(final @Nullable String tag, final @Nullable String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(final @Nullable String tag, final @Nullable String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.w(tag, msg);
        }
        write("w", tag, msg);
    }

    public static void w(final @Nullable String tag, final @Nullable String msg, final Throwable e) {
        if (tag == null || msg == null) {
            return;
        }
        if (MirakelCommonPreferences.isDebug()) {
            android.util.Log.w(tag, msg, e);
        }
        write("w", tag, msg, e);
    }

    public static void wtf(final @Nullable String tag, final @Nullable String msg) {
        if (tag == null || msg == null) {
            return;
        }
        android.util.Log.wtf(tag, msg);
        write("wtf", tag, msg);
    }

    public static void wtf(final @Nullable String tag, final @Nullable String msg, final Throwable e) {
        if (tag == null || msg == null) {
            return;
        }
        android.util.Log.wtf(tag, e);
        write("wtf", tag, msg, e);
    }

    private static String getTime() {
        return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
    }

    private static void init() {
        if (fileWriter != null) {
            return;
        }
        try {
            fileWriter = new FileWriter(new File(FileUtils.getLogDir(),
                                                 getTime() + ".log"));
        } catch (final Exception e) {
            writeToFile = false;
            fileWriter = null;
            Log.e(TAG, "Could not open file for logging");
        }
    }

    public static void write(final String critic, final String tag,
                             final String msg) {
        write(critic, tag, msg, null);
    }

    public static void write(final String critic, final String tag,
                             final String msg, final Throwable throwable) {
        if (!writeToFile) {
            return;
        }
        init();
        if (fileWriter != null) {
            String stacktrace = "";
            if (throwable != null) {
                stacktrace = "\nStackTrace:" + getStackTraceString(throwable);
            }
            try {
                fileWriter.write(getTime() + "::" + critic + "::" + tag + "::"
                                 + msg + stacktrace + "\n");
                fileWriter.flush();
            } catch (final Exception e) {
                fileWriter = null;
                writeToFile = false;// Prevent stackoverflow from recursive
                // calling
                Log.e(TAG, "Could not write to file for logging");
            }
        }
    }

    public static void longInfo(final @Nullable String str) {
        if (str == null) {
            return;
        }
        if (str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else {
            Log.i(TAG, str);
        }
    }
}
