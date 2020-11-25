package net.gundami.prism;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class runScript {
    File path=new File("./config/haproxy/tmp/frontend");
    public Runnable Haproxy() throws IOException {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    String[] configFilesArray=path.list();
                    String frontendFiles="";
                    String backendFiles="";
                    frontendFiles=frontendFiles+readFile("./config/haproxy/haproxy.cfg")+"\n";
                    backendFiles=backendFiles+readFile("./config/haproxy/haproxy-backend.cfg")+"\n";
                    for(int i=0;i< configFilesArray.length;i++){
                        frontendFiles=frontendFiles+readFile("./config/haproxy/tmp/frontend/"+configFilesArray[i]);

                        backendFiles=backendFiles+readFile("./config/haproxy/tmp/backend/"+configFilesArray[i]);
                    }

                    BufferedWriter frontend = new BufferedWriter(new FileWriter("./config/haproxy/frontend.cfg"));
                    frontend.write(frontendFiles+"#default to frps\n" +
                            "    default_backend frp_app");
                    frontend.close();
                    BufferedWriter backend = new BufferedWriter(new FileWriter("./config/haproxy/backend.cfg"));
                    backend.write(backendFiles);
                    backend.close();

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
    public String readFile(String strFile){
        try{
            InputStream is = new FileInputStream(strFile);
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            String result = new String(bytes);
            is.close();
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }

    }

}
