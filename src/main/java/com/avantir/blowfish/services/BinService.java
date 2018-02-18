package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.repository.AcquirerRepository;
import com.avantir.blowfish.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class BinService {

    @Autowired
    private BinRepository binRepository;


    @Transactional(readOnly=true)
    public Bin findById(Long id) {

        try
        {
            return binRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Bin findByCode(String code) {

        try
        {
            return binRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public Bin findByPan(String pan) {

        try
        {
            List<Bin> binList = binRepository.findAll();
            List<Bin> matchedBinList = new ArrayList<Bin>();
            for(Bin bin: binList){
                String binCode = bin.getCode();
                if(pan.startsWith(binCode))
                    matchedBinList.add(bin);
            }
            if(matchedBinList.size() > 1){
                Bin maxLenBin = matchedBinList.get(0);
                int maxLenBinLen = maxLenBin.getCode().length();
                for(Bin bin : matchedBinList){
                    if(maxLenBinLen < bin.getCode().length())
                        maxLenBin = bin;
                }
                return maxLenBin;
            }
            else if(matchedBinList.size() == 1){
                return matchedBinList.get(0);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public List<Bin> findAllActive() {

        try
        {
            List<Bin> list = binRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
