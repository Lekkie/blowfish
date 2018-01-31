package com.avantir.blowfish.api.iso8583;

import com.avantir.blowfish.config.IsoEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Configuration
public class EndpointStarter {


    static SAPInterchange sapInterchange;


    public EndpointStarter(){

    }

    @Autowired
    public EndpointStarter(IsoEndpointConfig isoEndpointConfig){
        try{
            start(isoEndpointConfig);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public static void start(IsoEndpointConfig isoEndpointConfig)throws Exception{
        SAPInterchange sapInterchange = new SAPInterchange(isoEndpointConfig);
        sapInterchange.start();
    }

    @PreDestroy
    public void stop() {
        sapInterchange.stop();
    }




    public static void main(String[] args){
        try{
            //ISOPackager isoPackager = new ISOPackager();
            String template = EndpointStarter.readFile("/Users/lekanomotayo/projects/phoenix/j8583.xml");

            EndpointStarter endpointStarter = new EndpointStarter();
            //endpointStarter.start(sapEndpoint1, tcpEndpoint1, isoPackager);
            //endpointStarter.start(sapEndpoint2, tcpEndpoint2, isoPackager);

            System.out.println("Test:");



        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public static String readFile(String filename)
    {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }catch(Exception ex){}
        }
        return content;
    }


}
