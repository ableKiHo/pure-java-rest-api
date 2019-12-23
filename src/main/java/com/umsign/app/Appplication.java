package com.umsign.app;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.umsign.app.api.user.handler.AllUserListHandler;
import com.umsign.app.api.user.handler.FindUserHandler;
import com.umsign.app.api.user.handler.RegisterationHandler;
import com.umsign.app.filter.AccessLogFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.umsign.app.Configuration.*;
import static com.umsign.app.api.ApiUtils.getQueryMap;
import static com.umsign.app.api.ApiUtils.splitQuery;

@Slf4j
public class Appplication {

    public static void main(String[] args) throws IOException {
        log.info("Appplication start!");
        AccessLogFilter accessLogFilter = new AccessLogFilter();
        List<Filter> defaultFilters = Arrays.asList(new Filter[] {accessLogFilter});
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        RegisterationHandler registrationHandler = new RegisterationHandler(getUserService(), getObjectMapper(), getErrorHandler());
        server.createContext("/api/users/register", registrationHandler::handle).getFilters().addAll(defaultFilters);

        AllUserListHandler allUserListHandler = new AllUserListHandler(getUserService(), getObjectMapper(), getErrorHandler());
        server.createContext("/api/users/allUsers", allUserListHandler::handle).getFilters().addAll(defaultFilters);;

        FindUserHandler findUserHandler = new FindUserHandler(getUserService(), getObjectMapper(), getErrorHandler());
        server.createContext("/api/users/findUser", findUserHandler::handle).getFilters().addAll(defaultFilters);;

        HttpContext httpContext = server.createContext("/api/hello", httpExchange -> {
            log.info("call /api/hello");
            if("GET".equals(httpExchange.getRequestMethod())) {
                Map<String, String> params = getQueryMap(httpExchange.getRequestURI().getRawQuery());
                String noNameText = "Anonymous";
                String name = params.getOrDefault("name", noNameText);
                String respText = String.format("Hello %s!", name);
                httpExchange.sendResponseHeaders(200, respText.getBytes().length); //response code and length
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(respText.getBytes());
                outputStream.flush();  // 출력 스트림과 버퍼된 출력 바이트를 강제로 쓰게 한다.
                // 기본적인 출력 스트림은 버퍼에 데이터가 가득 차면 그때 데이터를 출려기키는데 이 메소드를 사용하면 저장된 데이터의 크기에 관계없이 바로 출력
                httpExchange.close();
            }else {
                httpExchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

        });
        httpContext.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                //'admin:admin' -(Base64)-> 'YWRtaW46YWRtaW4=' ==>  -H 'Authorization: Basic YWRtaW46YWRtaW4='
                return user.equals("admin") && pwd.equals("admin");
            }
        });
        httpContext.getFilters().addAll(defaultFilters);
        server.setExecutor(null);
        server.start();

        log.info("HttpServer start! Server is listening on port " + serverPort);
    }


}
