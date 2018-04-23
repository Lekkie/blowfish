package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.MerchantTerminal;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.MerchantTerminalService;
import com.avantir.blowfish.services.IsoMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/merchants/terminals", produces = "application/hal+json")
public class MerchantTerminalController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantTerminalController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTerminalService merchantTerminalService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantTerminal merchantTerminal)
    {
        merchantTerminal = Optional.ofNullable(merchantTerminal).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTerminal"));
        Optional<MerchantTerminal> optional = merchantTerminalService.create(merchantTerminal);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("MerchantTerminal "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody MerchantTerminal merchantTerminal)
    {
        merchantTerminal = Optional.ofNullable(merchantTerminal).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTerminal"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTerminal Id"));
        merchantTerminal.setMerchantTerminalId(id);
        final long finalId = id;
        Optional<MerchantTerminal> optional = merchantTerminalService.update(merchantTerminal);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("MerchantTerminal " + finalId));
        return responseEntity;
    }



    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTerminal Id " + finalId));
        merchantTerminalService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchant_id", required = false) Long merchantId, @RequestHeader(value="terminal_id", required = false) Long terminalId)
    {
        String fxnParams = "id=" + id + ", merchant_id=" + merchantId + ", terminal_id=" + terminalId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);
        if(Optional.ofNullable(merchantId).isPresent())
            return getByMerchantId(merchantId);
        if(Optional.ofNullable(terminalId).isPresent())
            return getByTerminalId(terminalId);
        List<MerchantTerminal> list = merchantTerminalService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminals"));
        List<MerchantTerminal> newList = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(newList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTerminal Id " + finalId));
        Optional<MerchantTerminal> optional = merchantTerminalService.findByMerchantTerminalId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminal " + finalId));
        return responseEntity;
    }


    public Object getByMerchantId(Long merchantId)
    {
        String fxnParams = "merchantId=" + merchantId;
        final Long finalCode = merchantId;
        merchantId = Optional.ofNullable(merchantId).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantId " + finalCode));
        List<MerchantTerminal> list = merchantTerminalService.findByMerchantId(merchantId).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminals"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }

    public Object getByTerminalId(Long terminalId)
    {
        String fxnParams = "terminalId=" + terminalId;
        final Long finalCode = terminalId;
        terminalId = Optional.ofNullable(terminalId).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalId " + finalCode));
        Optional<MerchantTerminal> optional = merchantTerminalService.findByTerminalId(terminalId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminal " + finalCode));
        return responseEntity;
    }


    public MerchantTerminal getLinks(MerchantTerminal merchantTerminal){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantTerminalController.class).slash(merchantTerminal.getMerchantTerminalId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(merchantTerminal.getMerchantId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("merchant");
        merchantTerminal.add(link1);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TerminalController.class).getById(merchantTerminal.getTerminalId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("terminal");
        merchantTerminal.add(link2);

        return merchantTerminal;
    }


    @ExceptionHandler(BlowfishRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Errors handleRuntimeException(HttpServletRequest httpServletRequest, BlowfishRuntimeException brex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(brex.getMessage(), brex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }



}
