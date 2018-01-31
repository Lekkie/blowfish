package com.avantir.blowfish.consumers.rest.api;

import com.avantir.blowfish.consumers.rest.model.Error;
import com.avantir.blowfish.consumers.rest.model.TranAuthenticate;
import com.avantir.blowfish.consumers.rest.model.TranNotification;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.processor.RestPreprocessor;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lekanomotayo on 13/10/2017.
 */
@RestController
@RequestMapping("/transactions/notifications")
public class TranNotificationApi {

    private static final Logger logger = LoggerFactory.getLogger(TranNotificationApi.class);

    @Autowired
    RestPreprocessor restPreprocessor;

    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object authenticate(@RequestBody TranNotification tranNotification, HttpServletResponse response)
    {
        try{
            Exchange requestExchange = new Exchange();
            requestExchange.setMessage(tranNotification);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return restPreprocessor.processRest(requestExchange);
        }
        catch(InvalidPanException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_14, ex.getMessage());
        }
        catch(InvalidExpiryDateException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_54, ex.getMessage());
        }
        catch(TerminalNotSupportedException | MerchantTerminalNotLinkedException |
                TerminalMerchantMismatchException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_58, ex.getMessage());
        }
        catch(MerchantNotSupportedException | AcquirerMerchantNotLinkedException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_03, ex.getMessage());
        }
        catch(AcquirerNotSupportedException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_60, ex.getMessage());
        }
        catch(TranTypeNotSupportedException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_40, ex.getMessage());
        }
        catch(BinNotSupportedException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_01, ex.getMessage());
        }
        catch(AcquirerMerchantTranTypeBinNotSupportedException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_57, ex.getMessage());
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    /*
    @RequestMapping(method= RequestMethod.GET,
            value = "/{cbnCode}",
            headers = "Accept=application/json")
    @ResponseBody
    public String  getDirectCredit(@PathVariable("cbnCode") String cbnCode)
    {
        try
        {
            return null;
        }
        catch(Exception ex)
        {

            logger.debug("welcome() is executed, value {}", "mkyong");
            logger.error("This is Error message", ex);
            return "<Exception>";
        }
    }
    */



}
