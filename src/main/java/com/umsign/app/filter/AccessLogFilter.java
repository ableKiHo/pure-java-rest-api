package com.umsign.app.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class AccessLogFilter extends Filter {
    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        String uuid = UUID.randomUUID().toString();
        log.info(String.format("[%s]> %s %s %s", uuid, httpExchange.getProtocol(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        long startTimeMs = System.currentTimeMillis();
        chain.doFilter(httpExchange);
        long endTimeMs = System.currentTimeMillis();
        log.info(String.format("[%s]< %s %d(ms)", uuid, httpExchange.getResponseCode(), (endTimeMs-startTimeMs)));
    }

    @Override
    public String description() {
        return "Log Access (request & response)";
    }
}
