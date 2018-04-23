package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.repository.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class AcquirerMerchantTranTypeBinService {

    @Autowired
    private AcquirerMerchantTranTypeBinRepository acquirerMerchantTranTypeBinRepository;
    @Autowired
    private AcquirerRepository acquirerRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TranTypeRepository tranTypeRepository;
    @Autowired
    private BinRepository binRepository;
    @Autowired
    BinService binService;


    @Transactional(readOnly=true)
    public Optional<AcquirerMerchantTranTypeBin> findByAcquirerMerchantTranTypeBinId(Long id) {
        return acquirerMerchantTranTypeBinRepository.findByAcquirerMerchantTranTypeBinId(id);
    }

    @Transactional(readOnly=true)
    public Optional<AcquirerMerchantTranTypeBin> findByAcquirerIdMerchantIdTranTypeIdBinId(Long acqurerId, Long mid, Long tranTypeId, Long binId) {
        return acquirerMerchantTranTypeBinRepository.findByAcquirerIdMerchantIdTranTypeIdBinId(acqurerId, mid, tranTypeId, binId);
    }

    // ensure that this has to be uncaught exception...it may be caught exception
    @Transactional(readOnly=true)
    public Optional<AcquirerMerchantTranTypeBin> findByAcquirerMerchantTranTypePan(String acquirerCode, String merchantCode, String tranTypeCode, String pan) {

        Acquirer acquirer = acquirerRepository.findByCodeAllIgnoringCase(acquirerCode).orElseThrow(() -> new AcquirerNotSupportedException("Acquirer " + acquirerCode + " is not supported"));
        Merchant merchant = merchantRepository.findByCodeAllIgnoringCase(merchantCode).orElseThrow(() -> new MerchantNotSupportedException("Merchant " + merchantCode + " is not supported"));
        TranType tranType = tranTypeRepository.findByCodeAllIgnoringCase(tranTypeCode).orElseThrow(() -> new TranTypeNotSupportedException("TranType " + tranTypeCode + " is not supported"));
        Bin bin = binService.findByPan(pan).orElseThrow(() -> new BinNotSupportedException("Bin " + pan.substring(0, 6)));

        return acquirerMerchantTranTypeBinRepository.findByAcquirerIdMerchantIdTranTypeIdBinId(acquirer.getAcquirerId(), merchant.getMerchantId(), tranType.getTranTypeId(), bin.getBinId());
    }





}
