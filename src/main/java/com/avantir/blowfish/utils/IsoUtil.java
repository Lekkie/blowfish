package com.avantir.blowfish.utils;

import com.avantir.blowfish.consumers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.model.TMSKeyOrigDataElem;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.codecs.CompositeField;
import com.solab.iso8583.parse.ConfigParser;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.FileReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class IsoUtil {

    private static final Logger logger = LoggerFactory.getLogger(IsoUtil.class);

    public static IsoMessage createResponse(IsoMessage request, String isoPackagerName, boolean binaryBitmap){

        MessageFactory<IsoMessage> messageFactory = getMessageFactory(isoPackagerName, binaryBitmap);
        if(messageFactory != null) {
            IsoMessage resp =  messageFactory.createResponse(request);
            resp.setField(f52, null);
            return resp;
        }
        return null;
    }

    public static MessageFactory getMessageFactory(String isoPackagerName, boolean binaryBitmap){

        //URL url = new URL("");
        //MessageFactory<IsoMessage> messageFactory = ConfigParser.createFromUrl(url);
        //MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();
        //String template = EndpointStarter.readFile(isoPackager.getTemplate());
        //StringReader reader = new StringReader(isoPackager.getTemplate());
        //StringReader reader = new StringReader(template);

        try{
            ClassLoader classLoader = MessageFactory.class.getClassLoader();
            URL url = classLoader.getResource("packagers/" + isoPackagerName);
            if(url != null){
                String templateFilename = url.getFile();
                FileReader reader = new FileReader(templateFilename);
                MessageFactory<IsoMessage> messageFactory = ConfigParser.createFromReader(reader);
                messageFactory.setUseBinaryBitmap(binaryBitmap);
                //messageFactory.setUseBinaryMessages(false);
                //messageFactory.setCharacterEncoding(StandardCharsets.US_ASCII.name());
                messageFactory.setCharacterEncoding(StandardCharsets.UTF_8.name());
                return messageFactory;
            }
        }
        catch(Exception ex){
            logger.error("getMessageFactory: Error getting messageFactory", ex);
            ex.printStackTrace();;
        }
        return null;
    }

    public static IsoMessage setCompositeChildValue(IsoMessage isoMessage, String isoPackagerName, boolean binaryBitmap, int parentFieldNo, int childFieldNo, String compositeChildVal){

        IsoValue isoValue = isoMessage.getField(parentFieldNo);
        CompositeField composite =isoValue == null ? null : ((CompositeField) isoValue.getValue());
        if(composite == null)
        {
            composite = new CompositeField();
        }

        MessageFactory<IsoMessage> messageFactory = getMessageFactory(isoPackagerName, binaryBitmap);
        if(messageFactory != null) {
            IsoMessage templ = messageFactory.getMessageTemplate(isoMessage.getType());
            IsoValue compositeTempl = templ.getField(parentFieldNo);
            CompositeField compositeTemplVal = (CompositeField) compositeTempl.getValue();
            IsoValue compositeTemplChildField = compositeTemplVal.getField(childFieldNo);
            IsoType compositeChildType = compositeTemplChildField.getType();

            int totalLen = getCompositeFieldTotalLen(composite, compositeTemplVal, compositeTemplChildField);
            if(!compositeChildType.needsLength()){
                int compositeChildValLen = compositeChildVal.length();
                totalLen += compositeChildValLen;
                IsoValue childIsoValue = new IsoValue<String>(compositeChildType, compositeChildVal, compositeChildValLen);
                //composite.addValue(childFieldNo, childIsoValue);
            }
            else{
                int compositeTemplChildLen = compositeTemplChildField.getLength();
                totalLen += compositeTemplChildLen;
                IsoValue childIsoValue =new IsoValue<String>(compositeChildType, compositeChildVal, compositeTemplChildLen);
                //composite.addValue(childFieldNo, childIsoValue);
            }

            isoMessage.setValue(parentFieldNo, composite, composite, compositeTempl.getType(), totalLen);
            return isoMessage;
        }
        return null;
    }


    private static int getCompositeFieldTotalLen(CompositeField composite, CompositeField compositeTemplVal, IsoValue compositeTemplChildField){
        int totalLen = 0;
        int j = 0;
        /*
        IsoValue[] values = compositeTemplVal.getValues();
        for(IsoValue index:values){
            if(composite.getField(j)!= null){
                String compositeChildVal1 = composite.getObjectValue(j);
                IsoValue compositeTemplChildField1 = compositeTemplVal.getField(j);
                IsoType compositeChildType1 = compositeTemplChildField1.getType();
                if(!compositeChildType1.needsLength())
                    totalLen += compositeChildVal1.length();
                else
                    totalLen += compositeTemplChildField.getLength();
            }
            j++;
        }
        */
        return totalLen;
    }


    public static IsoMessage setValue(IsoMessage isoMessage, String isoPackagerName, boolean binaryBitmap, int fieldNo, String val) {

        MessageFactory<IsoMessage> messageFactory = getMessageFactory(isoPackagerName, binaryBitmap);
        if (messageFactory != null) {
            IsoMessage templ = messageFactory.getMessageTemplate(isoMessage.getType());
            IsoValue fieldTempl = templ.getField(fieldNo);
            IsoType templFieldType = templ.getField(fieldNo).getType();
            if(!templFieldType.needsLength()){
                isoMessage.setValue(fieldNo,  val, templFieldType, val.length());

            }else{
                IsoValue field = templ.getField(fieldNo);
                isoMessage.setValue(fieldNo, val, templFieldType, field.getLength());
            }
            return isoMessage;
        }

        return null;
    }


    public static void sendResponse(ISO8583SrcNode iso8583SrcNode, ChannelHandlerContext ctx, IsoMessage isoReq, String packagerName, boolean binaryBitmap, String responseCode){
        IsoMessage response = returnResponse(isoReq, packagerName, binaryBitmap, responseCode);
        if(response != null)
            iso8583SrcNode.sendResponseToRemote(ctx, response);
    }


    public static IsoMessage returnResponse(IsoMessage isoMessage, String packagerName, boolean binaryBitmap, String respCode){
        IsoMessage response = createResponse(isoMessage, packagerName, binaryBitmap);
        if(response != null)
            response.setField(f39, IsoType.ALPHA.value(respCode, 2));
        return response;
    }

    public static String getCompositeFieldValue(IsoMessage isoMessage, int parentFieldNo, int childFieldNo){
        try{
            CompositeField composite = (CompositeField) isoMessage.getField(parentFieldNo).getValue();
            return composite.getObjectValue(childFieldNo).toString();
        }
        catch(Exception ex){
            logger.error("getCompositeFieldValue: Error getting from field value", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getFieldValue(IsoMessage isoMessage, int fieldNo){
        try{
            IsoValue isoValue = isoMessage.getField(fieldNo);
            Object val = isoValue == null ? null : isoValue.getValue();
            return val == null ? null : val.toString();
        }
        catch(Exception ex){
            logger.error("getFieldValue: Error getting from field value", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getTranType(IsoMessage isoMessage){
        try{
            String proc = getFieldValue(isoMessage, f3);
            return proc == null ? null : proc.substring(0,2);
        }
        catch(Exception ex){
            logger.error("getTranType: Error getting from transaction type", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getFromAccountType(IsoMessage isoMessage){
        try{
            String proc = getFieldValue(isoMessage, f3);
            return proc == null ? null : proc.substring(2,4);
        }
        catch(Exception ex){
            logger.error("getFromAccountType: Error getting from account type", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getToAccountType(IsoMessage isoMessage){
        try{
            String proc = getFieldValue(isoMessage, f3);
            return proc == null ? null : proc.substring(4,6);
        }
        catch(Exception ex){
            logger.error("getToAccountType: Error getting to account type", ex);
            ex.printStackTrace();
        }
        return null;
    }


    public static String getOriginalDataElement(IsoMessage isoMessage){
        //Get switch key from original data element
        // type (4)
        // + stan (6)
        // + transmission date & time (10)
        // + acquiring inst id (11) - (right justified with leading zeroes)
        // + forwarding inst id (11) - (right justified with leading zeroes)
        String type = String.format("%04X", isoMessage.getType());
        if(isResponse(isoMessage))
            type = getRequestType(type);
        String stan = getFieldValue(isoMessage, f11);
        String transmissionDateTime = getFieldValue(isoMessage, f7);
        String acquiringInstId = getFieldValue(isoMessage, f32);
        acquiringInstId = acquiringInstId == null ? "" : acquiringInstId;
        acquiringInstId = StringUtil.leftPad(acquiringInstId, 11, '0');
        String forwardingInstId = getFieldValue(isoMessage, f33);
        forwardingInstId = forwardingInstId == null ? "" : forwardingInstId;
        forwardingInstId = StringUtil.leftPad(forwardingInstId, 11, '0');
        String terminalId = getFieldValue(isoMessage, f41);
        String merchantId = getFieldValue(isoMessage, f42);
        String originalDataElement = type + stan + transmissionDateTime + acquiringInstId + forwardingInstId + terminalId + merchantId;
        return originalDataElement;
    }

    /*
    public static String getRoutingInfo(Node srcNode, Node sinkNode, IsoMessage isoMessage, TotalsGroup totalsGroup){
        String srcNodeName = StringUtil.rightPad(srcNode.getName(), 12, ' '); // right pad
        String snkNodeName = StringUtil.rightPad(sinkNode.getName(), 12, ' '); // right pad
        String stan = getFieldValue(isoMessage, f11);
        String totalsGroupName = StringUtil.rightPad(totalsGroup.getName(), 12, ' '); // right pad
        String routingInfo = srcNodeName + snkNodeName + stan + stan + totalsGroupName;
        return routingInfo;
    }
    */


    public static Long generateTMSKey(IsoMessage isoMessage, TMSKeyOrigDataElemService tmsKeyOrigDataElemService){
        String originalDataElement = getOriginalDataElement(isoMessage);
        //Save tmskey & original Data element
        TMSKeyOrigDataElem tmsKeyOrigDataElem = new TMSKeyOrigDataElem();
        tmsKeyOrigDataElem.setOriginalDataElement(originalDataElement);
        try {
            tmsKeyOrigDataElemService.create(tmsKeyOrigDataElem);
        }
        catch(DataIntegrityViolationException divex){
            logger.error("receiveRequestFromRemote: Duplicate transaction type, stan, transmission datetime, acquiringId, forwardingId, terminal Id, merchant Id already exists", divex);
            return -1L;
        }
        catch(Exception ex){
            logger.error("receiveRequestFromRemote: Unable to create TMS key", ex);
            return 0L;
        }

        tmsKeyOrigDataElem = tmsKeyOrigDataElemService.findByOriginalDataElement(originalDataElement);
        if(tmsKeyOrigDataElem == null){
            logger.error("receiveRequestFromRemote: TMS key not found for this message");
            return 0L;
        }
        return  tmsKeyOrigDataElem.getTmsKeyOrigDataElemId();
    }

    public static Long getTMSKey(IsoMessage isoMessage, TMSKeyOrigDataElemService tmsKeyOrigDataElemService){
        String originalDataElement = getOriginalDataElement(isoMessage);
        TMSKeyOrigDataElem tmsKeyOrigDataElem = tmsKeyOrigDataElemService.findByOriginalDataElement(originalDataElement);
        return tmsKeyOrigDataElem == null ? 0L : tmsKeyOrigDataElem.getTmsKeyOrigDataElemId();
    }




    // https://en.wikipedia.org/wiki/ISO_8583
    public static boolean isRequest(IsoMessage isoMessage){
        String type = String.format("%04X", isoMessage.getType());
        int len = type.length();
        if(type.length() > 2){
            String last2Xters = type.substring(len - 2, len);
            return last2Xters.equalsIgnoreCase("00") //Request
                    || last2Xters.equalsIgnoreCase("20") //Advice
                    || last2Xters.equalsIgnoreCase("40") // Notification
                    || last2Xters.equalsIgnoreCase("60"); //Instruction (ISO 8583:2003 only)
        }
        return false;
    }

    // https://en.wikipedia.org/wiki/ISO_8583
    public static boolean isResponse(IsoMessage isoMessage){
        String type = String.format("%04X", isoMessage.getType());
        int len = type.length();
        if(type.length() > 2){
            String last2Xters = type.substring(len - 2, len);
            return last2Xters.equalsIgnoreCase("10") //Request response
                    || last2Xters.equalsIgnoreCase("30") //Advice Response
                    || last2Xters.equalsIgnoreCase("50") //Notification Acknowledgement
                    || last2Xters.equalsIgnoreCase("70"); //Instruction Acknowledgement (ISO 8583:2003 only)
        }
        return false;
    }

    public static boolean isEchoRequest(IsoMessage isoMessage){
        String type = String.format("%04X", isoMessage.getType());
        return "0800".equals(type) || "0801".equals(type);
    }

    public static boolean isEchoResponse(IsoMessage isoMessage){
        String type = String.format("%04X", isoMessage.getType());
        return "0810".equals(type);
    }

    public static String getResponseType(String requestType){
        int reqTypeInt = Integer.parseInt(requestType);
        int reqTypeMod = reqTypeInt % 10;
        if(reqTypeMod == 1)
            reqTypeInt = reqTypeInt - reqTypeMod + 10 ;
        if(reqTypeMod == 3)
            reqTypeInt = reqTypeInt - 1 + 10 ;
        String respType = String.valueOf(reqTypeInt);
        if(respType.length() < 4)
            respType = "0" + respType;
        return respType;
    }

    public static String getRequestType(String responseType){
        int respTypeInt = Integer.parseInt(responseType);
        int respTypeMod = respTypeInt % 10;
        if(respTypeMod == 1)
            respTypeInt = respTypeInt - respTypeMod - 10 ;
        if(respTypeMod == 3)
            respTypeInt = respTypeInt - 1 + 10 ;
        String reqType = String.valueOf(respTypeInt);
        if(reqType.length() < 4)
            reqType = "0" + reqType;
        return reqType;
    }



    /*
    //This is a temporary code
    private void process(ChannelHandlerContext ctx,IsoMessage toTranMgr){

        // just return response
        if(isRequest(toTranMgr)){
            logger.debug("Incoming request: " + toTranMgr.debugString());
            //final IsoMessage response = iso8583Connector.getIsoMessageFactory().createResponse(toTranMgr);
            response.setField(39, IsoType.ALPHA.value("00", 2));
            System.out.println("Outgoing response: " +response.debugString());
            sendReply(ctx, response);
        }
        else{
            System.out.println("Incoming response to Client Bank B: " + toTranMgr.debugString());
        }
    }
    */



    public static void  main(String[] args){
        try{
            String reqType = "0200";
            String respType = "0210";
            System.out.println(reqType + ": " + getResponseType(reqType));
            System.out.println(respType + ": " + getRequestType(respType));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }



    public static final int SRC_NODE = 0;
    public static final int SINK_NODE = 1;
    public static final int LMK_MODE = 0;
    public static final int ZMK_MODE = 1;
    public static final int ZPK_MODE = 2;
    public static final int PVK_MODE = 3;


    public final static int f2 = 2;
    public final static int f3 = 3;
    public final static int f4 = 4;
    public final static int f5 = 5;
    public final static int f6 = 6;
    public final static int f7 = 7;
    public final static int f8 = 8;
    public final static int f9 = 9;
    public final static int f10 = 10;
    public final static int f11 = 11;
    public final static int f12 = 12;
    public final static int f13 = 13;
    public final static int f14 = 14;
    public final static int f15 = 15;
    public final static int f16 = 16;
    public final static int f17 = 17;
    public final static int f18 = 18;
    public final static int f19 = 19;
    public final static int f20 = 20;
    public final static int f21 = 21;
    public final static int f22 = 22;
    public final static int f23 = 23;
    public final static int f24 = 24;
    public final static int f25 = 25;
    public final static int f26 = 26;
    public final static int f27 = 27;
    public final static int f28 = 28;
    public final static int f29 = 29;
    public final static int f30 = 30;
    public final static int f31 = 31;
    public final static int f32 = 32;
    public final static int f33 = 33;
    public final static int f34 = 34;
    public final static int f35 = 35;
    public final static int f36 = 36;
    public final static int f37 = 37;
    public final static int f38 = 38;
    public final static int f39 = 39;
    public final static int f40 = 10;
    public final static int f41 = 41;
    public final static int f42 = 42;
    public final static int f43 = 43;
    public final static int f44 = 44;
    public final static int f45 = 45;
    public final static int f46 = 46;
    public final static int f47 = 47;
    public final static int f48 = 48;
    public final static int f49 = 49;
    public final static int f50 = 50;
    public final static int f51 = 51;
    public final static int f52 = 52;
    public final static int f53 = 53;
    public final static int f54 = 54;
    public final static int f55 = 55;
    public final static int f56 = 56;
    public final static int f57 = 57;
    public final static int f58 = 58;
    public final static int f59 = 59;
    public final static int f60 = 60;
    public final static int f61 = 61;
    public final static int f62 = 62;
    public final static int f63 = 63;
    public final static int f64 = 64;
    public final static int f65 = 65;
    public final static int f66 = 66;
    public final static int f67 = 67;
    public final static int f68 = 68;
    public final static int f69 = 69;
    public final static int f70 = 70;
    public final static int f71 = 71;
    public final static int f72 = 72;
    public final static int f73 = 73;
    public final static int f74 = 74;
    public final static int f75 = 75;
    public final static int f76 = 76;
    public final static int f77 = 77;
    public final static int f78 = 78;
    public final static int f79 = 79;
    public final static int f80 = 80;
    public final static int f81 = 81;
    public final static int f82 = 82;
    public final static int f83 = 83;
    public final static int f84 = 84;
    public final static int f85 = 85;
    public final static int f86 = 86;
    public final static int f87 = 87;
    public final static int f88 = 88;
    public final static int f89 = 89;
    public final static int f90 = 90;
    public final static int f91 = 91;
    public final static int f92 = 92;
    public final static int f93 = 93;
    public final static int f94 = 94;
    public final static int f95 = 95;
    public final static int f96 = 96;
    public final static int f97 = 97;
    public final static int f98 = 98;
    public final static int f99 = 99;
    public final static int f100 = 100;
    public final static int f101 = 101;
    public final static int f102 = 102;
    public final static int f103 = 103;
    public final static int f104 = 104;
    public final static int f105 = 105;
    public final static int f106 = 106;
    public final static int f107 = 107;
    public final static int f108 = 108;
    public final static int f109 = 109;
    public final static int f110 = 110;
    public final static int f111 = 111;
    public final static int f112 = 112;
    public final static int f113 = 113;
    public final static int f114 = 114;
    public final static int f115 = 115;
    public final static int f116 = 116;
    public final static int f117 = 117;
    public final static int f118 = 118;
    public final static int f119 = 119;
    public final static int f120 = 120;
    public final static int f121 = 121;
    public final static int f122 = 122;
    public final static int f123 = 123;
    public final static int f124 = 124;
    public final static int f125 = 125;
    public final static int f126 = 126;
    public final static int f127 = 127;


    public final static String RESP_00 = "00";
    public final static String RESP_01 = "01"; // Refer to card issuer
    public final static String RESP_03 = "03"; // Invalid Merchant
    public final static String RESP_04 = "04"; // Pick-up card
    public final static String RESP_06 = "06"; // Error
    public final static String RESP_12 = "12"; // Invalid transaction
    public final static String RESP_13 = "13"; // Invalid amount
    public final static String RESP_14 = "14"; // Invalid card number
    public final static String RESP_30 = "30"; // Format error
    public final static String RESP_33 = "33"; // Expired card, pick up
    public final static String RESP_34 = "34"; // Suspected fraud
    public final static String RESP_40 = "40"; // Function not supported
    public final static String RESP_54 = "54"; // Expired card
    public final static String RESP_57 = "57"; // Transaction not permitted to cardholder
    public final static String RESP_58 = "58"; // Transaction not permitted on terminal
    public final static String RESP_60 = "60"; // Contact acquirer
    public final static String RESP_68 = "68"; // Response received too late
    public final static String RESP_91 = "91";
    public final static String RESP_92 = "92";
    public final static String RESP_94 = "94"; // duplicate transaction
    public final static String RESP_96 = "96";


}
