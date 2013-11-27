package com.github.rholder.jvm.loopunswitching;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public class JvmBug {

    /**
     * This appears to exhibit the same behavior as the bug reported here:
     * {@code https://code.google.com/p/crawler4j/issues/detail?id=136}. Adding
     * the {@code -XX:-LoopUnswitching} JVM argument fixes this issue, as
     * referenced in a similar bug reported in
     * {@code https://issues.apache.org/jira/browse/HTTPCLIENT-1173}.
     *
     * @throws InterruptedException
     */
    public static void bug() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 50000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<Cookie> someCookies = new ArrayList<Cookie>();
                    for (int j = 0; j < 5; j++) {
                        BasicClientCookie c = new BasicClientCookie("20", randomAlphanumeric(300));
                        someCookies.add(c);
                    }
                    formatCookies(someCookies);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }

    public static List<Header> formatCookies(final List<Cookie> cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("List of cookies may not be null");
        }
        int version = Integer.MAX_VALUE;
        boolean isSetCookie2 = true;
        for (Cookie cookie: cookies) {
            if (!(cookie instanceof SetCookie2)) {
                isSetCookie2 = false;
            }
            if (cookie.getVersion() < version) {
                version = cookie.getVersion();
            }
        }
        if (isSetCookie2) {
            return new ArrayList<Header>();
        } else {
            return new ArrayList<Header>();
        }
    }

    public static void main(String... args) throws InterruptedException {
        bug();
    }
}
