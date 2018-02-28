package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.consumers.rest.model.Error;
import com.avantir.blowfish.consumers.rest.model.Parameter;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.KeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping("api/v1/merchants")
public class MerchantsApi {


    private static final Logger logger = LoggerFactory.getLogger(MerchantsApi.class);
    @Autowired
    MerchantService merchantService;


    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object create(@RequestBody Merchant merchant, HttpServletResponse response)
    {
        try{
            merchantService.create(merchant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH,
            consumes = "application/json",
            value = "/{id}",
            produces = "application/json")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Merchant newMerchant, HttpServletResponse response)
    {
        try{
            if(newMerchant == null)
                throw new Exception();

            newMerchant.setId(id);
            newMerchant = merchantService.update(newMerchant);
            response.setStatus(HttpServletResponse.SC_OK);
            return newMerchant;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.DELETE,
            consumes = "application/json",
            value = "/{id}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            merchantService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET,
            headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(code != null && !code.isEmpty())
                return getByCode(code, response);

            List<Merchant> merchantList = merchantService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            return merchantList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getById(long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            Merchant merchant = merchantService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return merchant;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByCode(String code, HttpServletResponse response)
    {
        String fxnParams = "code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            Merchant merchant = merchantService.findByCode(code);
            response.setStatus(HttpServletResponse.SC_OK);
            return merchant;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
