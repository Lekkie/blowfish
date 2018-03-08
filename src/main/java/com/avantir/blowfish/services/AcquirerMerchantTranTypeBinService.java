package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.*;
import com.avantir.blowfish.repository.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
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


    @Transactional(readOnly=true)
    public AcquirerMerchantTranTypeBin findByAcquirerMerchantTranTypeBinId(Long id) {

        try
        {
            return acquirerMerchantTranTypeBinRepository.findByAcquirerMerchantTranTypeBinId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public AcquirerMerchantTranTypeBin findByAcquirerIdMerchantIdTranTypeIdBinId(Long acqurerId, Long mid, Long tranTypeId, Long binId) {

        try
        {
            return acquirerMerchantTranTypeBinRepository.findByAcquirerIdMerchantIdTranTypeIdBinId(acqurerId, mid, tranTypeId, binId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public AcquirerMerchantTranTypeBin findByAcquirerMerchantTranTypePan(String acquirerCode, String merchantCode, String tranTypeCode, String pan) throws Exception {

        try
        {
            Acquirer acquirer = acquirerRepository.findByCodeAllIgnoringCase(acquirerCode);
            if(acquirer == null)
                return null; // throw acquirer not configured exception

            Merchant merchant = merchantRepository.findByCodeAllIgnoringCase(merchantCode);
            if(merchant == null)
                return null; // throw acquirer not configured exception

            TranType tranType = tranTypeRepository.findByCodeAllIgnoringCase(tranTypeCode);
            if(tranType == null)
                return null; // throw acquirer not configured exception

            List<Bin> binList = binRepository.findByStatus(1);
            Bin bin = BlowfishUtil.getBinFromPan(binList, pan);
            if(bin == null)
                return null; // throw acquirer not configured exception

            return acquirerMerchantTranTypeBinRepository.findByAcquirerIdMerchantIdTranTypeIdBinId(acquirer.getAcquirerId(), merchant.getMerchantId(), tranType.getTranTypeId(), bin.getBinId());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }





}
