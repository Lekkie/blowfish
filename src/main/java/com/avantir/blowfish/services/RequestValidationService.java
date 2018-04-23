package com.avantir.blowfish.services;

import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */

@Component
public class RequestValidationService {

    @Autowired
    protected AcquirerMerchantTranTypeBinService acquirerMerchantTranTypeBinService;
    @Autowired
    protected MerchantTerminalTranTypeBinService merchantTerminalTranTypeBinService;
    @Autowired
    protected AcquirerService acquirerService;
    @Autowired
    protected MerchantService merchantService;
    @Autowired
    protected TerminalService terminalService;
    @Autowired
    protected MerchantTerminalService merchantTerminalService;
    @Autowired
    protected AcquirerMerchantService acquirerMerchantService;
    @Autowired
    protected TranTypeService tranTypeService;
    @Autowired
    protected BinService binService;
    @Autowired
    protected SecurityService securityService;


    // identify which service shld throw caught & uncaught exception
    public void validate(String mid, String tid, String tranTypeCode, String pan, String expDate){

        if(!securityService.validatePan(pan))
            throw new InvalidPanException("Pan " + pan + " is invalid");

        // chk if System wants Exp date to be validated
        if(!securityService.validateExpDate(expDate))
            throw new InvalidExpiryDateException("Expiry date " + expDate + " is invalid");

        Terminal terminal = terminalService.findByCode(tid).orElseThrow(() -> new TerminalNotSupportedException("Terminal " + tid + " is unknown, not configured yet on the system"));
        Merchant transactingMerchant = merchantService.findByCode(mid).orElseThrow(() -> new MerchantNotSupportedException("Merchant " + mid + " is unknown, not configured yet on the system"));
        MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminal.getTerminalId()).orElseThrow(() -> new MerchantTerminalNotLinkedException("Terminal " + tid + " does not belong to any registered merchant"));
        Merchant terminalMerchant = merchantService.findByMerchantId(merchantTerminal.getMerchantId()).orElseThrow(() -> new MerchantNotSupportedException("Merchant " + merchantTerminal.getMerchantId() + " is not supported"));

        if(transactingMerchant.getId() != terminalMerchant.getId())
            throw new TerminalMerchantMismatchException("This Terminal " + tid + " does not belong to merchant " + mid);

        Optional<AcquirerMerchant> optionalAcqMerch = acquirerMerchantService.findByMerchantId(terminalMerchant.getMerchantId());
        AcquirerMerchant acquirerMerchant = optionalAcqMerch.orElseThrow(() -> new AcquirerMerchantNotLinkedException("Merchant " + terminalMerchant.getCode() + " does not belong to any registered Acquirer"));
        Optional<Acquirer> optionalAcq = acquirerService.findByAcquirerId(acquirerMerchant.getAcquirerId());
        Acquirer acquirer = optionalAcq.orElseThrow(() -> new AcquirerNotSupportedException("Acquirer " + acquirerMerchant.getAcquirerId() + " is not supported"));
        TranType tranType = tranTypeService.findByCode(tranTypeCode).orElseThrow(() -> new TranTypeNotSupportedException("Transaction Type " + tranTypeCode + " is not supported"));
        //Get Bin by PAN
        Optional<Bin> optBin = binService.findByPan(pan);
        Bin bin = optBin.orElseThrow(() -> new BinNotSupportedException("This card's (" + pan + ") Bin is not supported"));
        //validate transaction type is supported for this acquirer and merchant
        Optional<AcquirerMerchantTranTypeBin> optionalAcqMerchTranTypeBin = acquirerMerchantTranTypeBinService.findByAcquirerIdMerchantIdTranTypeIdBinId(acquirer.getAcquirerId(), terminalMerchant.getMerchantId(), tranType.getTranTypeId(), bin.getBinId());
        optionalAcqMerchTranTypeBin.orElseThrow(() -> new AcquirerMerchantTranTypeBinNotSupportedException("Acquirer " + acquirer.getName() + " does not support this Merchant " + terminalMerchant.getName() + " for Transaction Type " + tranType.getName() + " on Card with this Bin " + bin.getCode()));
        //validate tran type for for this merchant, for this terminal, for this PAN
        MerchantTerminalTranTypeBin merchantTerminalTranTypeBin = merchantTerminalTranTypeBinService.findByMerchantIdTerminalIdTranTypeIdBinId(terminalMerchant.getMerchantId(), terminal.getTerminalId(), tranType.getTranTypeId(), bin.getBinId()).orElseThrow(() -> new MerchantTerminalTranTypeBinNotSupportedException("Merchant " + terminalMerchant.getName() + " does not support this Terminal " + terminal.getCode() + " for Transaction Type " + tranType.getName() + " on Card with this Bin " + bin.getCode()));

        //Validate currency
        // Validate amount limit- limit by acquirer, merchant, terminal
        // validate card expiration?
        // POS Entry Mode
        // Translate PIN
        // Set surcharge? (may be in transaction type)
    }

}
