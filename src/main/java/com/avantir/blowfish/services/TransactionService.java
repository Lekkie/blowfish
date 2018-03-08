package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.model.Transaction;
import com.avantir.blowfish.repository.BinRepository;
import com.avantir.blowfish.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional(readOnly=false)
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly=true)
    public Transaction findByTransactionId(Long transactionId) {

        try
        {
            return transactionRepository.findByTransactionId(transactionId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Transaction findByOriginalDataElements(String msgType, String stan, String transmissionDateTime,
                                          String acquirerInstId, String forwardingInstId,
                                          String terminalId, String merchantId) {

        try
        {
            return transactionRepository.findByMsgTypeStanTransmissionDateTimeAcquirerInstIdForwardingInstIdTerminalIdMerchantIdAllIgnoringCase(msgType, stan, transmissionDateTime, acquirerInstId, forwardingInstId, terminalId, merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}
