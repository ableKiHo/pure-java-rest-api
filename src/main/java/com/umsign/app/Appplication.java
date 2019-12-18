package com.umsign.app;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Appplication {
    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/hello", httpExchange -> {
            if("GET".equals(httpExchange.getRequestMethod())) {
                String respText = "Hello!";
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
        server.setExecutor(null);
        server.start();
    }
}
