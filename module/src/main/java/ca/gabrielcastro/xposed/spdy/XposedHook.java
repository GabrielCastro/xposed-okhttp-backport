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
