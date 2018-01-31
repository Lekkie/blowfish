package com.avantir.blowfish.processor;

import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.ResponseExchange;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by lekanomotayo on 12/01/2018.
 */
public abstract class APreprocessor implements IProcessor{

    @Autowired
    protected static AcquirerMerchantTranTypeBinService acquirerMerchantTranTypeBinService;
    @Autowired
    protected static MerchantTerminalTranTypeBinService merchantTerminalTranTypeBinService;
    @Autowired
    protected static AcquirerService acquirerService;
    @Autowired
    protected static MerchantService merchantService;
    @Autowired
    protected static TerminalService terminalService;
    @Autowired
    protected static MerchantTerminalService merchantTerminalService;
    @Autowired
    protected static AcquirerMerchantService acquirerMerchantService;
    @Autowired
    protected static TranTypeService tranTypeService;
    @Autowired
    protected static BinService binService;

    public static void validateBase(String mid, String tid, String tranTypeCode, String pan, String expDate) throws Exception{

        if(!BlowfishUtil.validatePan(pan))
            throw new InvalidPanException("Pan " + pan + " is invalid");

        // chk if System wants Exp date to be validated
        if(!BlowfishUtil.validateExpDate(expDate))
            throw new InvalidExpiryDateException("Expiry date " + expDate + " is invalid");

        Terminal terminal = terminalService.findByCode(tid);
        if(terminal == null)
            throw new TerminalNotSupportedException("Terminal " + tid + " is unknown, not configured yet on the system");

        Merchant transactingMerchant = merchantService.findByCode(mid);
        if(transactingMerchant == null)
            throw new MerchantNotSupportedException("Merchant " + mid + " is unknown, not configured yet on the system");

        MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminal.getId());
        if(merchantTerminal == null)
            throw new MerchantTerminalNotLinkedException("Terminal " + tid + " does not belong to any registered merchant");

        Merchant terminalMerchant = merchantService.findById(merchantTerminal.getMerchantId());
        if(terminalMerchant == null)// shld really never happen, but stranger things have happened
            throw new MerchantNotSupportedException("Merchant " + merchantTerminal.getMerchantId() + " is not supported");

        if(transactingMerchant.getId() != terminalMerchant.getId())
            throw new TerminalMerchantMismatchException("This Terminal " + tid + " does not belong to merchant " + mid);

        AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(terminalMerchant.getId());
        if(acquirerMerchant == null)
            throw new AcquirerMerchantNotLinkedException("Merchant " + terminalMerchant.getCode() + " does not belong to any registered Acquirer");

        Acquirer acquirer = acquirerService.findById(acquirerMerchant.getAcquirerId());
        if(acquirer == null)// shld really never happen, but stranger things have happened
            throw new AcquirerNotSupportedException("Acquirer " + acquirerMerchant.getAcquirerId() + " is not supported");

        TranType tranType = tranTypeService.findByCode(tranTypeCode);
        if(tranType == null)
            throw new TranTypeNotSupportedException("Transaction Type " + tranTypeCode + " is not supported");

        List<Bin> binList = binService.findAllActive();
        Bin bin = BlowfishUtil.getBinFromPan(binList, pan);
        if(bin == null)
            throw new BinNotSupportedException("This card's (" + pan + ") is not supported");

        //validate transaction type is supported for this acquirer and merchant
        AcquirerMerchantTranTypeBin acquirerMerchantTranTypeBin = acquirerMerchantTranTypeBinService.findByAcquirerIdMerchantIdTranTypeIdBinId(acquirer.getId(), terminalMerchant.getId(), tranType.getId(), bin.getId());
        if(acquirerMerchantTranTypeBin == null)
            throw new AcquirerMerchantTranTypeBinNotSupportedException("Acquirer " + acquirer.getName() + " does not support this Merchant " + terminalMerchant.getName() + " for Transaction Type " + tranType.getName() + " on Card with this Bin " + bin.getCode());

        //validate tran type for for this merchant, for this terminal, for this PAN
        MerchantTerminalTranTypeBin merchantTerminalTranTypeBin = merchantTerminalTranTypeBinService.findByMerchantIdTerminalIdTranTypeIdBinId(terminalMerchant.getId(), terminal.getId(), tranType.getId(), bin.getId());
        if(merchantTerminalTranTypeBin == null)
            throw new MerchantTerminalTranTypeBinNotSupportedException("Merchant " + terminalMerchant.getName() + " does not support this Terminal " + terminal.getCode() + " for Transaction Type " + tranType.getName() + " on Card with this Bin " + bin.getCode());


        //Validate currency
        // Validate amount limit- limit by acquirer, merchant, terminal
        // validate card expiration?
        // POS Entry Mode
        // Translate PIN
        // Set surcharge? (may be in transaction type)
    }



}
