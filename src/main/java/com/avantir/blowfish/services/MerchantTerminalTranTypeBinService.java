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
public class MerchantTerminalTranTypeBinService {

    @Autowired
    private MerchantTerminalTranTypeBinRepository merchantTerminalTranTypeBinRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    private TranTypeRepository tranTypeRepository;
    @Autowired
    private BinRepository binRepository;


    @Transactional(readOnly=true)
    public MerchantTerminalTranTypeBin findById(Long id) {

        try
        {
            //Optional<MerchantTerminalTranTypeBin> optional = merchantTerminalTranTypeBinRepository.findById(id);
            //return optional.orElse(null);
            return merchantTerminalTranTypeBinRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public MerchantTerminalTranTypeBin findByMerchantIdTerminalIdTranTypeIdBinId(Long mid, Long tid, Long tranTypeId, Long binId) {

        try
        {
            return merchantTerminalTranTypeBinRepository.findByMerchantIdTerminalIdTranTypeIdBinId(mid, tid, tranTypeId, binId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public MerchantTerminalTranTypeBin findByMerchantTerminalTranTypePan(String merchantCode, String terminalCode, String tranTypeCode, String pan) throws Exception {

        try
        {
            Merchant merchant = merchantRepository.findByCodeAllIgnoringCase(merchantCode);
            if(merchant == null)
                return null; // throw acquirer not configured exception


            Terminal terminal = terminalRepository.findByCodeAllIgnoringCase(terminalCode);
            if(terminal == null)
                return null; // throw acquirer not configured exception


            TranType tranType = tranTypeRepository.findByCodeAllIgnoringCase(tranTypeCode);
            if(tranType == null)
                return null; // throw acquirer not configured exception

            List<Bin> binList = binRepository.findByStatus(1);
            Bin bin = BlowfishUtil.getBinFromPan(binList, pan);
            if(bin == null)
                return null; // throw acquirer not configured exception

            return merchantTerminalTranTypeBinRepository.findByMerchantIdTerminalIdTranTypeIdBinId(merchant.getId(), terminal.getId(), tranType.getId(), bin.getId());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }





}
