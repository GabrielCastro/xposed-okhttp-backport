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

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class HttpHandler extends URLStreamHandler {
    @Override protected URLConnection openConnection(URL url) throws IOException {
        return newOkHttpClient(null /* proxy */).open(url);
    }

    @Override protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if (url == null || proxy == null) {
            throw new IllegalArgumentException("url == null || proxy == null");
        }
        return newOkHttpClient(proxy).open(url);
    }

    @Override protected int getDefaultPort() {
        return 80;
    }

    protected OkHttpClient newOkHttpClient(Proxy proxy) {
        OkHttpClient client = new OkHttpClient();
        client.setFollowProtocolRedirects(false);
        if (proxy != null) {
            client.setProxy(proxy);
        }

        return client;
    }
}
