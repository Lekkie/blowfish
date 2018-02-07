package com.avantir.blowfish.processor;

import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.consumers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.model.TranType;
import com.avantir.blowfish.model.Transaction;
import com.avantir.blowfish.processor.trans.FinancialProcessor;
import com.avantir.blowfish.services.TransactionService;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.MessageUtil;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 13/01/2018.
 */

@Component
public class IsoPreprocessor extends APreprocessor {


    @Autowired
    FinancialProcessor financialProcessor;


    // forward to transaction processor request handler
    // if exception is thrown, u r responsbile for sending response
    public void processIso8583(Exchange requestExchange){

        IsoMessage isoMessage = requestExchange.getIsoMessage();
        //Node srcNode = requestExchange.getFromNode();
        ISO8583SrcNode iso8583SrcNode = (ISO8583SrcNode) requestExchange.getIso8583Node();
        ChannelHandlerContext ctx = requestExchange.getCtx();
        String isoPackagerName = requestExchange.getIsoPackagerName();
        boolean binaryBitmap = requestExchange.isBinaryBitmap();

        String pan = IsoUtil.getFieldValue(isoMessage, IsoUtil.f2);
        String tranTypeCode = IsoUtil.getTranType(isoMessage);
        String expDate = IsoUtil.getFieldValue(isoMessage, IsoUtil.f14);
        String tid = IsoUtil.getFieldValue(isoMessage, IsoUtil.f41);
        String mid = IsoUtil.getFieldValue(isoMessage, IsoUtil.f42);

        try{
            validateBase(mid, tid, tranTypeCode, pan, expDate);
        }
        catch(InvalidPanException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_14);
        }
        catch(InvalidExpiryDateException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_54);
        }
        catch(TerminalNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_58);
        }
        catch(MerchantNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_03);
        }
        catch(MerchantTerminalNotLinkedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_58);
        }
        catch(TerminalMerchantMismatchException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_58);
        }
        catch(AcquirerMerchantNotLinkedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_03);
        }
        catch(AcquirerNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_60);
        }
        catch(TranTypeNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_40);
        }
        catch(BinNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_01);
        }
        catch(AcquirerMerchantTranTypeBinNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_57);
        }
        catch(MerchantTerminalTranTypeBinNotSupportedException ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_58);
        }
        catch(Exception ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_06);
        }

        try{
            Transaction transaction = MessageUtil.copyIsoMessageRequest(isoMessage, 1, "");
            requestExchange.setTransaction(transaction);
            TranType tranType = tranTypeService.findByCode(tranTypeCode);

            // ALl 0200 except Bill Payment & Recharge
            financialProcessor.process(requestExchange);
        }
        catch(Exception ex){
            IsoUtil.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoUtil.RESP_06);
        }
    }


    public Object processRest(Exchange requestExchange)throws Exception{
        return null;
    }


}
