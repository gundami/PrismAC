package net.gundami.prism;

import httpServer.frame.Server;
import httpServer.frame.ClassLoader;
import httpServer.frame.JarLoader;
import httpServer.frame.http.HttpServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class main {
    public static void main(String[] args){
        ExecutorService trd = Executors.newFixedThreadPool(5);
        try {
            trd.execute(new runScript().runFrps());
            trd.execute(new runScript().Haproxy());
            trd.execute(new runScript().runFrpc());
            trd.execute(new runScript().runNginx());

        } catch (IOException e) {
            e.printStackTrace();
        }

        startHttp();
    }
    private static void startHttp(){
        try {
            String user_dir = System.getProperty("user.dir");
            JarLoader.load(user_dir + "/libs");
            JarLoader.addusr_paths(user_dir + "/libs");
            ClassLoader.loadClassesFromPath();

            final HttpServer httpserver = new HttpServer(16666);
            httpserver.bootstrap();//启动
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
