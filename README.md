xposed-okhttp-backport
======================

OkHttp (http://square.github.io/okhttp/) is included by default in AOSP > 4.4 
This module brings in a later version and backports it to all Xposed supported versions of android.
All code is either copied from AOSP or part of the OkHttp library

> HTTP is the way modern applications network. It’s how we exchange data & media. Doing HTTP efficiently makes your stuff load faster and saves bandwidth.
> 
> OkHttp is an HTTP client that’s efficient by default:
> 
> SPDY support allows all requests to the same host to share a socket.
> Connection pooling reduces request latency (if SPDY isn’t available).
> Transparent GZIP shrinks download sizes.
> Response caching avoids the network completely for repeat requests.
> OkHttp perseveres when the network is troublesome: it will silently recover from common connection problems. If your service has multiple IP addresses OkHttp will attempt alternate addresses if the first connect fails. This is necessary for IPv4+IPv6 and for services hosted in redundant data centers. OkHttp also recovers from problematic proxy servers and failed SSL handshakes.

This only applies to code that calls ```URL#open()``` that is it imporoves preformance of application data transfers it dose __not__ affect WebViews or any Browser
