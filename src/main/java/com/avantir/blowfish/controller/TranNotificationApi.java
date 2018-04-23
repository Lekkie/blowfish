package com.avantir.blowfish.controller;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Transaction;
import com.avantir.blowfish.model.BlowfishResponse;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.model.TranNotification;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.processor.*;
import com.avantir.blowfish.processor.protocol.RestProtocolProcessor;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 13/10/2017.
 */
@RestController
@RequestMapping(value = "api/v1/trans/notifications", produces = "application/json")
public class TranNotificationApi {

    private static final Logger logger = LoggerFactory.getLogger(TranNotificationApi.class);

    @Autowired
    @Qualifier("restProtocolProcessor")
    RestProtocolProcessor processor;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ErrorService errorService;

    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object authenticate(@RequestBody TranNotification tranNotification)
    {
        Optional<TranNotification> optionalAcq = Optional.ofNullable(tranNotification);
        tranNotification = optionalAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("TranNotification"));
        tranNotification.validate();

        Optional<BlowfishResponse> optionalBlowfishResponse = processor.processRequest(tranNotification);

        ResponseEntity responseEntity = optionalBlowfishResponse.map(blowfishResponse ->{
                    return new ResponseEntity<Object>(blowfishResponse, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishProcessingErrorException("TranNotification "));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get()
    {
        Optional<List<Transaction>> optional = transactionService.findAll();
        List<Transaction> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("TranNotifications"));
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    @ExceptionHandler(BlowfishProcessingErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Errors handleBlowfishProcessingErrorException(HttpServletRequest httpServletRequest, BlowfishRuntimeException brex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(brex.getMessage(), brex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }



    @ExceptionHandler(BlowfishRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Errors handleBlowfishRuntimeException(HttpServletRequest httpServletRequest, BlowfishRuntimeException brex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(brex.getMessage(), brex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }


}
