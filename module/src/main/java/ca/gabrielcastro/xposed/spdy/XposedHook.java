/*
 * This file is part of OkHttpBackPort.
 *
 * Copyright 2013-2014 Gabriel Castro (c)
 *
 *     OkHttpBackPort is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     OkHttpBackPort is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with OkHttpBackPort.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.gabrielcastro.xposed.spdy;

import com.squareup.okhttp.HttpHandler;
import com.squareup.okhttp.HttpsHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Dictionary;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHook implements IXposedHookZygoteInit {

    private static URLStreamHandlerFactory other = null;

    private static final URLStreamHandlerFactory sFactory = new URLStreamHandlerFactory() {
        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            switch (protocol) {
                case "http":
                    return new HttpHandler();
                case "https":
                    return new HttpsHandler();
            }
            if (other != null) {
                return other.createURLStreamHandler(protocol);
            }
            return null;
        }
    };

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        URL.setURLStreamHandlerFactory(sFactory);
        XposedHelpers.findAndHookMethod(URL.class, "setURLStreamHandlerFactory", URLStreamHandlerFactory.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                if (other != null) {
                    methodHookParam.setThrowable(new Error("Factory already set"));
                    return null;
                }
                other = (URLStreamHandlerFactory) methodHookParam.args[0];
                return null;
            }
        });
    }

}
