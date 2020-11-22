package net.gundami.prism;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import httpServer.frame.http.HttpCmd;
import org.json.JSONObject;

public class httpApi extends HttpCmd{
    static {
        //注册接口 "/haproxy/api"
        HttpCmd.register("/haproxy/api", httpApi.class);
    }

    @Override
    public void execute() {  //接口业务逻辑处理方法
        JSONObject params = getJSONObject();//读取post请求json参数并创建JSONObject
        String action = params.optString("action","");
        String name = params.optString("name","");
        String domain = params.optString("domain","");
        int port = params.optInt("port",0);
        if(action.equals("add")){
            System.out.println("Adding,,,");
            if(domain.equals("")){
                System.out.println("no domain, set an autodomain");
                String autoDomain = name+".mcdn2.gundami.net";
                String flg = name+"_flg";
                String bkend = name+"_app";
                writeConfig(name,autoDomain,port,flg,bkend);
                System.out.println("new haproxy config: "+name);
                result.put("code","0");
                result.put("msg","ok");
                result.put("autodomain","true");
                response(result);
            }else{
                System.out.println("domain ok");
                String flg = name+"_flg";
                String bkend = name+"_app";
                writeConfig(name,domain,port,flg,bkend);
                System.out.println("new haproxy config: "+name);
                result.put("code","0");
                result.put("msg","ok");
                result.put("autodomain","false");
                response(result);
            }


        }
        if(action.equals("delete")){
            File configFile = new File("./config/haproxy/config/"+name+".cfg");
            configFile.delete();
            System.out.println(name+" delete");
            result.put("code","0");
            result.put("msg","ok");
            response(result);

        }
        try {
            ExecutorService trd = Executors.newFixedThreadPool(2);
            trd.execute(new runScript().Haproxy());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void writeConfig(String name,String domain,int port, String flg,String bkend){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./config/haproxy/config/"+name + ".cfg"));
            out.write("frontend " + name + "\n" +
                    "   mode tcp\n"+
                    "   bind 0.0.0.0:8205\n"+
                    "   tcp-request inspect-delay 3s\n"+
                    "   acl " + flg + " req.payload(5,0) -m sub " + domain + "\n" +
                    "   tcp-request content accept if " + flg + "\n" +
                    "   use_backend " + bkend + " if " + flg + "\n" +
                    "backend " + bkend + "\n" +
                    "   mode tcp\n" +
                    "       server " + name + " " + "127.0.0.1:" + port);
            out.close();
            System.out.println(name+" write finished");
        }catch(IOException e){

        }
    }
}
