package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.Terminal;
import com.avantir.blowfish.repository.MerchantRepository;
import com.avantir.blowfish.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;


    @Transactional(readOnly=true)
    public Terminal findById(Long id) {

        try
        {
            return terminalRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Terminal findByCode(String code) {

        try
        {
            return terminalRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Terminal findByDeviceSerialNo(String deviceSerialNo) {

        try
        {
            return terminalRepository.findByDeviceSerialNoAllIgnoringCase(deviceSerialNo);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public List<Terminal> findAllActive() {

        try
        {
            List<Terminal> list = terminalRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
