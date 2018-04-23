package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.MerchantTermParam;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.MerchantTermParamService;
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
@RequestMapping(value = "api/v1/merchants/termparams", produces = "application/hal+json")
public class MerchantTermParamController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantTermParamController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTermParamService merchantTermParamService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantTermParam merchantTermParam)
    {
        merchantTermParam = Optional.ofNullable(merchantTermParam).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam"));
        Optional<MerchantTermParam> optional = merchantTermParamService.create(merchantTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("MerchantTermParam "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody MerchantTermParam merchantTermParam)
    {
        merchantTermParam = Optional.ofNullable(merchantTermParam).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam Id"));
        merchantTermParam.setMerchantTermParamId(id);
        final long finalId = id;
        Optional<MerchantTermParam> optional = merchantTermParamService.update(merchantTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("MerchantTermParam " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam Id " + finalId));
        merchantTermParamService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchantId", required = false) Long merchantId, @RequestHeader(value="termParamId", required = false) Long termParamId)
    {
        String fxnParams = "id=" + id + ", merchant_id=" + merchantId + ", termParamId=" + termParamId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(merchantId).isPresent())
            return getByMerchantId(merchantId);

        if(Optional.ofNullable(termParamId).isPresent())
            return getByTermParamId(termParamId);

        Optional<List<MerchantTermParam>> optional = merchantTermParamService.findAll();
        List<MerchantTermParam> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTermParams"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam Id " + finalId));
        Optional<MerchantTermParam> optional = merchantTermParamService.findByMerchantTermParamId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTermParam " + finalId));
        return responseEntity;
    }


    public Object getByMerchantId(Long merchantId)
    {
        String fxnParams = "merchantId=" + merchantId;
        final long finalId = merchantId;
        merchantId = Optional.ofNullable(merchantId).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam MerchantId " + finalId));

        Optional<MerchantTermParam> optional = merchantTermParamService.findByMerchantId(merchantId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTermParam " + finalId));
        return responseEntity;
    }

    public Object getByTermParamId(Long termParamId)
    {
        String fxnParams = "termParamId=" + termParamId;
        final long finalId = termParamId;
        Optional<Long> optionalId = Optional.ofNullable(termParamId);
        termParamId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantTermParam TermParamId " + finalId));
        List<MerchantTermParam> list = merchantTermParamService.findByTermParamId(termParamId).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTermParams " + finalId));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    public MerchantTermParam getLinks(MerchantTermParam merchantTerminal){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantTermParamController.class).slash(merchantTerminal.getMerchantTermParamId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(merchantTerminal.getTermParamId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("termparam");
        merchantTerminal.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(merchantTerminal.getMerchantId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("merchant");
        merchantTerminal.add(link1);

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
