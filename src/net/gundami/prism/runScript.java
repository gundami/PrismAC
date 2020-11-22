package net.gundami.prism;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class runScript {
    File path=new File("./config/haproxy/config");
    public Runnable Haproxy() throws IOException {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    String[] configFilesArray=path.list();
                    String configFiles="";
                    for(int i=0;i<configFilesArray.length;i++){
                        if(i == configFilesArray.length - 1){
                            configFiles = configFiles + " ./config/haproxy/config/"+configFilesArray[i];

                        }else {
                            configFiles = configFiles + " ./config/haproxy/config/"+configFilesArray[i] + " -f ";

                        }
                    }
                    BufferedWriter out = new BufferedWriter(new FileWriter("./scripts/haproxy.sh"));
                    out.write("#!/bin/bash\n" +
                            "export LD_LIBRARY_PATH=\"/home/container/lib\"\n"+
                            "./bin/haproxy -f ./config/haproxy/haproxy.cfg -f" + configFiles + " -st");
                    out.close();

                    new ProcessExecutor().command("/bin/bash","-c", "./scripts/haproxy.sh")
                            .redirectOutput(Slf4jStream.of("haproxy").asInfo()).execute();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidExitValueException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }
    public  Runnable runFrps() throws IOException {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    new ProcessExecutor().command("./bin/frps","-c", "./config/frps.ini")
                            .redirectOutput(Slf4jStream.of("frps").asInfo()).execute();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidExitValueException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }
    public static Runnable runFrpc() throws IOException {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    new ProcessExecutor().command("./bin/frpc","-c", "./config/frpc.ini")
                            .redirectOutput(Slf4jStream.of("nginx").asInfo()).execute();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidExitValueException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }
    public static Runnable runNginx() throws IOException {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    new ProcessExecutor().command("/bin/bash","-c", "./scripts/nginx.sh")
                            .redirectOutput(Slf4jStream.of("nginx").asInfo()).execute();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidExitValueException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }

}
