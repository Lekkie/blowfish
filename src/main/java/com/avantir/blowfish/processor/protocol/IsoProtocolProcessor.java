package com.avantir.blowfish.processor.protocol;

import com.avantir.blowfish.clients.iso8583.ISO8583SinkNode;
import com.avantir.blowfish.entity.Transaction;
import com.avantir.blowfish.messaging.endpoint.EndpointStarter;
import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.model.IsoResponse;
import com.avantir.blowfish.servers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.BlowfishResponse;
import com.avantir.blowfish.model.IsoRequest;
import com.avantir.blowfish.processor.factory.AbstractFactory;
import com.avantir.blowfish.processor.factory.FactoryProducer;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.servers.iso8583.SrcNodeInfo;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.RequestValidationService;
import com.avantir.blowfish.services.TransactionService;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */

@Component
public class IsoProtocolProcessor implements ProtocolProcessor {

    private static final Logger logger = LoggerFactory.getLogger(IsoProtocolProcessor.class);

    @Autowired
    TransactionService transactionService;
    @Autowired
    IsoMessageService isoMessageService;
    @Autowired
    RequestValidationService requestValidationService;



    public Optional<BlowfishResponse> processRequest(BlowfishRequest blowfishRequest){
        processIso8583Request(blowfishRequest);
        return Optional.empty();
    }

    public void processResponse(BlowfishResponse blowfishResponse){
        processIso8583Response(blowfishResponse);
    }




    public void processIso8583Request(BlowfishRequest blowfishRequest){
        IsoRequest isoMsg = (IsoRequest) blowfishRequest;
        ISO8583Node iso8583Node = (ISO8583Node) isoMsg.getIso8583Node();
        if(iso8583Node instanceof ISO8583SrcNode)
            sendToSinkNode(blowfishRequest, isoMsg, iso8583Node);
        else
            throw new BlowfishIllegalArgumentException("Expecting a Src Node");
    }


    public void processIso8583Response(BlowfishResponse blowfishResponse){
        IsoResponse isoResponse = (IsoResponse) blowfishResponse;
        ISO8583Node iso8583Node = (ISO8583Node) isoResponse.getIso8583Node();

        if(iso8583Node instanceof ISO8583SinkNode)
            returnToSrcNode(isoResponse.getIsoMessage());
        else
            throw new BlowfishIllegalArgumentException("Expecting a Sink Node");
    }


    private void sendToSinkNode(BlowfishRequest blowfishRequest, IsoRequest isoRequest, ISO8583Node iso8583Node){
        IsoMessage isoMessage = isoRequest.getIsoMessage();
        ISO8583SrcNode iso8583SrcNode = (ISO8583SrcNode) iso8583Node;
        ChannelHandlerContext ctx = isoRequest.getChannelHandlerContext();
        String isoPackagerName = isoRequest.getIsoPackagerName();
        boolean binaryBitmap = isoRequest.isBinaryBitmap();

        try{
            // get MTI, get tran type and use it to validate fields
            String pan = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f2);
            String tranTypeCode = isoMessageService.getTranType(isoMessage);
            String expDate = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f14);
            String tid = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f41);
            String mid = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f42);
            requestValidationService.validate(mid, tid, tranTypeCode, pan, expDate);

            Optional<AbstractFactory> optionalAbstractFactory = FactoryProducer.getFactory("TRANSACTION_PROCESSOR");
            AbstractFactory abstractFactory = optionalAbstractFactory.orElseThrow(() -> new BlowfishProcessingErrorException("TransactionProcessorFactory"));
            Optional<RequestProcessor> optionalTransactionProcessor = abstractFactory.getTransactionProcessor(blowfishRequest);
            RequestProcessor transactionProcessor = optionalTransactionProcessor.orElseThrow(() -> new TranTypeNotSupportedException("Transaction not supported"));
            transactionProcessor.process(blowfishRequest);

        }
        catch(InvalidPanException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_14);
        }
        catch(InvalidExpiryDateException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_54);
        }
        catch(TerminalNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_58);
        }
        catch(MerchantNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_03);
        }
        catch(MerchantTerminalNotLinkedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_58);
        }
        catch(TerminalMerchantMismatchException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_58);
        }
        catch(AcquirerMerchantNotLinkedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_03);
        }
        catch(AcquirerNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_60);
        }
        catch(TranTypeNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_40);
        }
        catch(BinNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_01);
        }
        catch(AcquirerMerchantTranTypeBinNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_57);
        }
        catch(MerchantTerminalTranTypeBinNotSupportedException ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_58);
        }
        catch(Exception ex){
            isoMessageService.sendResponse(iso8583SrcNode, ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_06);
        }
    }


    private void returnToSrcNode(IsoMessage isoMessage){

        String msgType = String.format("%04X", isoMessage.getType());
        String stan = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f11);
        String transmissionDateTime = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f7);
        String acqInstId = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f32);
        String forwardingInstId = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f33);
        String termId = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f41);
        String merchantId = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f42);
        Optional<Transaction> optionalTransaction = transactionService.findByOriginalDataElements(msgType, stan, transmissionDateTime, acqInstId, forwardingInstId, termId, merchantId);
        Transaction transaction = optionalTransaction.orElseThrow(() -> new BlowfishEntityNotFoundException("Transaction"));

        if(transaction == null){
            logger.error("returnResponseToSrcNode: Transaction request cannot be found in the database");
            return;
        }

        transaction = transactionService.copyIsoMessageResponse(isoMessage, transaction);
        transactionService.create(transaction);

        String tmsKey = transaction.getTmsKey();
        SrcNodeInfo srcNodeInfo = EndpointStarter.getSrcNodeInfoTreeMap().get(tmsKey);
        if(srcNodeInfo == null){
            logger.error("returnResponseToSrcNode: Src Node Info for this message (TMS Key: " + tmsKey + " cannot be found");
            return;
        }
        //find src node, forward response
        ISO8583SrcNode iso8583SrcNode = EndpointStarter.getISO8583SrcNode(srcNodeInfo.getSrcNodeId());
        if(srcNodeInfo == null){
            logger.error("returnResponseToSrcNode: Src Node Connection for this message (TMS Key: " + tmsKey + " cannot be found");
            return;
        }

        iso8583SrcNode.sendResponseToRemote(srcNodeInfo.getCtx(), isoMessage);
    }

}
