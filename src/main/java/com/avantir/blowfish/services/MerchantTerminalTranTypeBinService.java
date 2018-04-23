package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
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
    private BinService binService;
    @Autowired
    StringService stringService;

    @Transactional(readOnly=false)
    public Optional<MerchantTerminalTranTypeBin> create(MerchantTerminalTranTypeBin merchantTerminalTranTypeBin) {
        return Optional.ofNullable(merchantTerminalTranTypeBinRepository.save(merchantTerminalTranTypeBin));
    }

    @Transactional(readOnly=false)
    public Optional<MerchantTerminalTranTypeBin> update(MerchantTerminalTranTypeBin newMerchantTerminalTranTypeBin) {

        MerchantTerminalTranTypeBin oldMerchantTerminalTranTypeBin = merchantTerminalTranTypeBinRepository.findByMerchantTerminalTranTypeBinId(newMerchantTerminalTranTypeBin.getMerchantTerminalTranTypeBinId()).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminalTranTypeBin"));

        if(newMerchantTerminalTranTypeBin.getMerchantId() > 0)
            oldMerchantTerminalTranTypeBin.setMerchantId(newMerchantTerminalTranTypeBin.getMerchantId());
        if(newMerchantTerminalTranTypeBin.getTerminalId() > 0)
            oldMerchantTerminalTranTypeBin.setTerminalId(newMerchantTerminalTranTypeBin.getTerminalId());
        if(newMerchantTerminalTranTypeBin.getTranTypeId() > 0)
            oldMerchantTerminalTranTypeBin.setTranTypeId(newMerchantTerminalTranTypeBin.getTranTypeId());
        if(newMerchantTerminalTranTypeBin.getBinId() > 0)
            oldMerchantTerminalTranTypeBin.setBinId(newMerchantTerminalTranTypeBin.getBinId());
        return Optional.ofNullable(merchantTerminalTranTypeBinRepository.save(oldMerchantTerminalTranTypeBin));
    }


    @Transactional(readOnly=true)
    public Optional<MerchantTerminalTranTypeBin> findByMerchantTerminalTranTypeBinId(Long id) {
        return merchantTerminalTranTypeBinRepository.findByMerchantTerminalTranTypeBinId(id);
    }

    @Transactional(readOnly=true)
    public Optional<MerchantTerminalTranTypeBin> findByMerchantIdTerminalIdTranTypeIdBinId(Long mid, Long tid, Long tranTypeId, Long binId) {
        return merchantTerminalTranTypeBinRepository.findByMerchantIdTerminalIdTranTypeIdBinId(mid, tid, tranTypeId, binId);
    }


    // Throw caught exception when you cant find, so that user can handle
    @Transactional(readOnly=true)
    public Optional<MerchantTerminalTranTypeBin> findByMerchantTerminalTranTypePan(String merchantCode, String terminalCode, String tranTypeCode, String pan) throws Exception {

        Merchant merchant = merchantRepository.findByCodeAllIgnoringCase(merchantCode).orElseThrow(() -> new BlowfishEntityNotFoundException("Merchant " + merchantCode));
        Terminal terminal = terminalRepository.findByCodeAllIgnoringCase(terminalCode).orElseThrow(() -> new BlowfishEntityNotFoundException("Terminal " + terminalCode));
        TranType tranType = tranTypeRepository.findByCodeAllIgnoringCase(tranTypeCode).orElseThrow(() -> new BlowfishEntityNotFoundException("TranType " + tranTypeCode));
        Bin bin = binService.findByPan(pan).orElseThrow(() -> new BlowfishEntityNotFoundException("Bin " + pan));

        return merchantTerminalTranTypeBinRepository.findByMerchantIdTerminalIdTranTypeIdBinId(merchant.getMerchantId(), terminal.getTerminalId(), tranType.getTranTypeId(), bin.getBinId());
    }





}
