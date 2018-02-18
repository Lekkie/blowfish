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
public class MerchantTerminalService {

    @Autowired
    private MerchantTerminalRepository merchantTerminalRepository;


    @Transactional(readOnly=true)
    public MerchantTerminal findById(Long id) {

        try
        {
            //Optional<MerchantTerminal> optional = merchantTerminalRepository.findById(id);
            //return optional.orElse(null);
            return merchantTerminalRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public MerchantTerminal findByTerminalId(Long terminalId) {

        try
        {
            return merchantTerminalRepository.findByTerminalId(terminalId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}
