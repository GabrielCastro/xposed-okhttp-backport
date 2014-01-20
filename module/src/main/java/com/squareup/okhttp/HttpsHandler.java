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

package com.squareup.okhttp;

import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public final class HttpsHandler extends HttpHandler {
    private static final List<String> ENABLED_TRANSPORTS = Arrays.asList("http/1.1");

    @Override
    protected int getDefaultPort() {
        return 443;
    }

    @Override
    protected OkHttpClient newOkHttpClient(Proxy proxy) {
        OkHttpClient client = super.newOkHttpClient(proxy);
        client.setTransports(ENABLED_TRANSPORTS);

        HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
        // Assume that the internal verifier is better than the
        // default verifier.
        try {
            if (!Class.forName("javax.net.ssl.DefaultHostnameVerifier").isInstance(verifier)) {
                client.setHostnameVerifier(verifier);
            }
        } catch (ClassNotFoundException e) {
            // if we cannot find the class than let's NOT taper with it
        }

        return client;
    }
}
