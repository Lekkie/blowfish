package com.avantir.blowfish.messaging;

import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.entity.Transaction;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 12/01/2018.
 */
public class Exchange {

    Message message;
    IsoMessage isoMessage;
    boolean iso8583Exchange;
    String isoPackagerName;
    boolean binaryBitmap;
    Node fromNode;
    ISO8583Node iso8583Node;
    ChannelHandlerContext ctx;
    Transaction transaction;

    public Exchange(){

    }

    public Exchange(String isoPackagerName, boolean binaryBitmap, IsoMessage isoMessage, boolean iso8583Exchange, Node fromNode, ISO8583Node iso8583Node, ChannelHandlerContext ctx){
        this.isoPackagerName = isoPackagerName;
        this.binaryBitmap = binaryBitmap;
        this.isoMessage = isoMessage;
        this.fromNode = fromNode;
        this.iso8583Node = iso8583Node;
        this.ctx = ctx;
        this.iso8583Exchange = iso8583Exchange;
    }

    public Exchange(Message message){
        this.message = message;
    }


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    public IsoMessage getIsoMessage() {
        return isoMessage;
    }

    public void setIsoMessage(IsoMessage isoMessage) {
        this.isoMessage = isoMessage;
    }

    public boolean isIso8583Exchange() {
        return iso8583Exchange;
    }

    public void setIso8583Exchange(boolean iso8583Exchange) {
        this.iso8583Exchange = iso8583Exchange;
    }

    public String getIsoPackagerName() {
        return isoPackagerName;
    }

    public void setIsoPackagerName(String isoPackagerName) {
        this.isoPackagerName = isoPackagerName;
    }

    public boolean isBinaryBitmap() {
        return binaryBitmap;
    }

    public void setBinaryBitmap(boolean binaryBitmap) {
        this.binaryBitmap = binaryBitmap;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public ISO8583Node getIso8583Node() {
        return iso8583Node;
    }

    public void setIso8583Node(ISO8583Node iso8583Node) {
        this.iso8583Node = iso8583Node;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
