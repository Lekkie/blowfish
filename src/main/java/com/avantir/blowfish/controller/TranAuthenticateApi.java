package com.avantir.blowfish.controller;

import com.avantir.blowfish.model.BlowfishResponse;
import com.avantir.blowfish.model.TranAuthenticate;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.processor.Processor;
import com.avantir.blowfish.processor.protocol.RestProtocolProcessor;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.IsoMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by lekanomotayo on 13/10/2017.
 */
@RestController
@RequestMapping("api/v1/transactions/auths")
public class TranAuthenticateApi {

    private static final Logger logger = LoggerFactory.getLogger(TranAuthenticateApi.class);

    @Autowired
    @Qualifier("restProtocolProcessor")
    RestProtocolProcessor processor;
    @Autowired
    ErrorService errorService;

    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object authenticate(@RequestBody TranAuthenticate tranAuthenticate)
    {
        Optional<TranAuthenticate> optionalAcq = Optional.ofNullable(tranAuthenticate);
        tranAuthenticate = optionalAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("TranAuthenticate"));
        tranAuthenticate.validate();

        Optional<BlowfishResponse> optionalBlowfishResponse = processor.processRequest(tranAuthenticate);

        ResponseEntity responseEntity = optionalBlowfishResponse.map(blowfishResponse ->{
            return new ResponseEntity<Object>(blowfishResponse, HttpStatus.OK);
        })
                .orElseThrow(() -> new BlowfishProcessingErrorException("TranAuthenticate "));
        return responseEntity;

        /*
        try{
            Exchange requestExchange = new Exchange();
            requestExchange.setMessage(tranAuthenticate);
            return new ResponseEntity<Object>(processor.process(requestExchange), HttpStatus.NO_CONTENT);
        }
        catch(InvalidPanException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_14, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(InvalidExpiryDateException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_54, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(TerminalNotSupportedException | MerchantTerminalNotLinkedException |
                TerminalMerchantMismatchException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_58, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(MerchantNotSupportedException | AcquirerMerchantNotLinkedException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_03, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(AcquirerNotSupportedException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_60, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(TranTypeNotSupportedException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_40, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(BinNotSupportedException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_01, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(AcquirerMerchantTranTypeBinNotSupportedException ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_57, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(errorService.getError(IsoMessageService.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        */
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
