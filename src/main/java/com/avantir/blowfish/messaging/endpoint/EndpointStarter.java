package com.avantir.blowfish.messaging.endpoint;

import com.avantir.blowfish.consumers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.consumers.iso8583.SrcNodeInfo;
import com.avantir.blowfish.messaging.model.ISOBridge;
import com.avantir.blowfish.messaging.model.SAPEndpoint;
import com.avantir.blowfish.messaging.model.TCPEndpoint;
import com.avantir.blowfish.messaging.services.ISOBridgeService;
import com.avantir.blowfish.messaging.services.NodeService;
import com.avantir.blowfish.messaging.services.SAPEndpointService;
import com.avantir.blowfish.messaging.services.TCPEndpointService;
import com.avantir.blowfish.processor.IsoPostprocessor;
import com.avantir.blowfish.processor.IsoPreprocessor;
import com.avantir.blowfish.providers.iso8583.ISO8583SinkNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Component
public class EndpointStarter {


    //@Autowired
    //static ;

    public EndpointStarter(){

    }

    private static TreeMap<Long, SAPInterchange> sapInterchangeTreeMap = new TreeMap<Long, SAPInterchange>();
    private static TreeMap<Long, ISO8583SinkNode> iso8583SinkNodeTreeMap = new TreeMap<Long, ISO8583SinkNode>();
    private static TreeMap<String, SrcNodeInfo> srcNodeInfoTreeMap = new TreeMap<String, SrcNodeInfo>();
    private static TreeMap<Long, ISO8583SrcNode> iso8583SrcNodeTreeMap = new TreeMap<Long, ISO8583SrcNode>();


    @Autowired
    public EndpointStarter(IsoPreprocessor isoPreprocessor, IsoPostprocessor isoPostprocessor, TCPEndpointService tcpEndpointService, SAPEndpointService sapEndpointService, NodeService nodeService, ISOBridgeService isoBridgeService){
        List<TCPEndpoint> TCPEndpointList = tcpEndpointService.findAllActive();
        if(TCPEndpointList != null && TCPEndpointList.size() > 0){
            for(TCPEndpoint tcpEndpoint : TCPEndpointList){
                SAPEndpoint sapEndpoint = sapEndpointService.findByTcpEndpointId(tcpEndpoint.getId());
                ISOBridge isoBridge = isoBridgeService.findByISOBridgeId(sapEndpoint.getIsoBridgeId());
                try{
                    start(isoPreprocessor, isoPostprocessor, sapEndpoint, nodeService, tcpEndpoint, isoBridge);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }


    public static void start(IsoPreprocessor isoPreprocessor, IsoPostprocessor isoPostprocessor, SAPEndpoint sapEndpoint, NodeService nodeService, TCPEndpoint tcpEndpoint, ISOBridge isoBridge)throws Exception{
        SAPInterchange sapInterchange = new SAPInterchange(isoPreprocessor, isoPostprocessor, sapEndpoint, nodeService, tcpEndpoint, isoBridge);
        sapInterchange.start();
        sapInterchangeTreeMap.put(sapEndpoint.getId(), sapInterchange);
    }

    public static void stop(SAPEndpoint sapEndpoint){
        SAPInterchange sapInterchangeClients = sapInterchangeTreeMap.get(sapEndpoint.getId());
        stopSAPInterchange(sapInterchangeClients);
    }

    @PreDestroy
    public void stop() {
        for(Map.Entry<Long, SAPInterchange> entry : sapInterchangeTreeMap.entrySet()) {
            Long key = entry.getKey();
            SAPInterchange value = entry.getValue();
            stopSAPInterchange(value);
        }
    }

    private static void stopSAPInterchange(SAPInterchange sapInterchange){
        sapInterchange.stop();
    }



    public static TreeMap<Long, SAPInterchange> getSapInterchanges(){
        return sapInterchangeTreeMap;
    }

    public static ISO8583SrcNode getISO8583SrcNode(Long srcNodeId){
        return iso8583SrcNodeTreeMap.get(srcNodeId);
    }

    public static ISO8583SinkNode getISO8583SinkNode(Long sinkNodeId){
        return iso8583SinkNodeTreeMap.get(sinkNodeId);
    }


    public static void addSrcNode(Long id, ISO8583SrcNode iso8583SrcNode){
        iso8583SrcNodeTreeMap.put(id, iso8583SrcNode);
    }

    public static void addSinkNode(Long id, ISO8583SinkNode iso8583SinkNode){
        iso8583SinkNodeTreeMap.put(id, iso8583SinkNode);
    }

    public static void addSrcNodeInfo(String tmsKey, SrcNodeInfo srcNodeInfo){
        srcNodeInfoTreeMap.put(tmsKey, srcNodeInfo);
    }

    public static TreeMap<String, SrcNodeInfo> getSrcNodeInfoTreeMap() {
        return srcNodeInfoTreeMap;
    }

    public static void main(String[] args){
        try{
            //ISOPackager isoPackager = new ISOPackager();
            String template = EndpointStarter.readFile("/Users/lekanomotayo/projects/phoenix/j8583.xml");
            //isoPackager.setId(1L);
            //isoPackager.setName("PostPackager");
            //isoPackager.setTemplate(template);

            SAPEndpoint sapEndpoint1 = new SAPEndpoint();
            sapEndpoint1.setName("TestInterchange1");
            sapEndpoint1.setSnkNodeId(1L);
            sapEndpoint1.setSrcNodeId(2L);
            sapEndpoint1.setTcpEndpointId(1L);

            TCPEndpoint tcpEndpoint1 = new TCPEndpoint();
            tcpEndpoint1.setName("IP Address");
            tcpEndpoint1.setDescription("");
            tcpEndpoint1.setId(1L);
            tcpEndpoint1.setServer(true);
            tcpEndpoint1.setStatus(1);
            //TCPEndpointPortIP tcpEndpointPortIP1 = new TCPEndpointPortIP("0.0.0.0", 9000);
            //tcpEndpoint1.setTcpEndpointPortIP(tcpEndpointPortIP1);
            tcpEndpoint1.setIp("0.0.0.0");
            tcpEndpoint1.setPort(9000);

            SAPEndpoint sapEndpoint2 = new SAPEndpoint();
            sapEndpoint2.setName("TestInterchange2");
            sapEndpoint2.setSnkNodeId(1L);
            sapEndpoint2.setSrcNodeId(2L);
            sapEndpoint2.setTcpEndpointId(1L);

            TCPEndpoint tcpEndpoint2 = new TCPEndpoint();
            tcpEndpoint2.setName("IP Address");
            tcpEndpoint2.setDescription("");
            tcpEndpoint2.setId(1L);
            tcpEndpoint2.setServer(false);
            tcpEndpoint2.setStatus(1);
            //TCPEndpointPortIP tcpEndpointPortIP2 = new TCPEndpointPortIP("127.0.0.1", 9000);
            //tcpEndpoint2.setTcpEndpointPortIP(tcpEndpointPortIP2);
            tcpEndpoint1.setIp("127.0.0.1");
            tcpEndpoint1.setPort(9000);

            EndpointStarter endpointStarter = new EndpointStarter();
            //endpointStarter.start(sapEndpoint1, tcpEndpoint1, isoPackager);
            //endpointStarter.start(sapEndpoint2, tcpEndpoint2, isoPackager);

            System.out.println("Test:");

            /*
            MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();// [1]
            SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9000);
            Iso8583Client<IsoMessage> client = new Iso8583Client<>(socketAddress, messageFactory);// [2]
            client.addMessageListener(new IsoMessageListener<IsoMessage>() { // [3]

                @Override
                public boolean applies(IsoMessage isoMessage) {
                    return isoMessage.getType() ==  0x800;
                }

                @Override
                public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
                    final IsoMessage response = client.getIsoMessageFactory().createResponse(isoMessage);
                    response.setField(39, IsoType.ALPHA.value("00", 2));
                    response.setField(60, IsoType.LLLVAR.value("XXX", 3));
                    ctx.writeAndFlush(response);
                    return false;
                }

            });
            //client.getConfiguration().replyOnError(true);// [4]
            client.init();// [5]
            client.connectAsync();// [6]
            if (client.isConnected()) { // [7]

                IsoMessage message = messageFactory.newMessage(0x800);
                client.send(message);// [8]
            }
            client.shutdown();// [9]
            */

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
