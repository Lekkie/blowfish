package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.MerchantTerminal;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.MerchantTerminalService;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
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
@RequestMapping("api/v1/merchants/terminals")
public class MerchantTerminalsApi {


    private static final Logger logger = LoggerFactory.getLogger(MerchantTerminalsApi.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTerminalService merchantTerminalService;


    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantTerminal merchantTerminal, HttpServletResponse response)
    {
        try{
            merchantTerminalService.create(merchantTerminal);
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
    public Object update(@PathVariable("id") long id, @RequestBody MerchantTerminal newMerchantTerminal, HttpServletResponse response)
    {
        try{
            if(newMerchantTerminal == null)
                throw new Exception();

            newMerchantTerminal.setId(id);
            newMerchantTerminal = merchantTerminalService.update(newMerchantTerminal);
            response.setStatus(HttpServletResponse.SC_OK);
            return newMerchantTerminal;
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
            merchantTerminalService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchant_id", required = false) Long merchantId, @RequestHeader(value="terminal_id", required = false) Long terminalId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", merchant_id=" + merchantId + ", terminal_id=" + terminalId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(merchantId != null && merchantId > 0)
                return getByMerchantId(merchantId, response);

            if(terminalId != null && terminalId > 0)
                return getByTerminalId(terminalId, response);

            List<MerchantTerminal> merchantTerminalList = merchantTerminalService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            return merchantTerminalList;
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
            MerchantTerminal merchantTerminal = merchantTerminalService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return merchantTerminal;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByMerchantId(long merchantId, HttpServletResponse response)
    {
        String fxnParams = "merchantId=" + merchantId + ",HttpServletResponse=" + response.toString();
        try
        {
            List<MerchantTerminal> merchantTerminalList = merchantTerminalService.findByMerchantId(merchantId);
            response.setStatus(HttpServletResponse.SC_OK);
            return merchantTerminalList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    public Object getByTerminalId(long terminalId, HttpServletResponse response)
    {
        String fxnParams = "terminalId=" + terminalId + ",HttpServletResponse=" + response.toString();
        try
        {
            MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminalId);
            response.setStatus(HttpServletResponse.SC_OK);
            return merchantTerminal;
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
