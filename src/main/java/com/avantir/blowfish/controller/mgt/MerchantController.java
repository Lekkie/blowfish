package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Merchant;
import com.avantir.blowfish.entity.MerchantTerminal;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/merchants", produces = "application/hal+json")
public class MerchantController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTerminalService merchantTerminalService;
    @Autowired
    MerchantTermParamService merchantTerminalParameterService;
    @Autowired
    MerchantTerminalTranTypeBinService merchantTerminalTranTypeBinService;
    @Autowired
    MerchantTerminalController merchantTerminalsController;
    @Autowired
    MerchantTermParamController merchantTermParamsController;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Merchant merchant)
    {
        merchant = Optional.ofNullable(merchant).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant"));
        Optional<Merchant> optional = merchantService.create(merchant);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Merchant"));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Merchant merchant)
    {
        merchant = Optional.ofNullable(merchant).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant Id"));
        merchant.setMerchantId(id);
        final long finalId = id;
        Optional<Merchant> optional = merchantService.update(merchant);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Merchant " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant Id " + finalId));
        merchantService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code)
    {
        String fxnParams = "id=" + id + ", code=" + code;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);
        if(Optional.ofNullable(code).isPresent())
            return getByCode(code);
        List<Merchant> list = merchantService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("Merchants"));
        List<Merchant> newList = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(newList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable("id") long id)
    {
        String fxnParams = "id=" + id ;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant Id " + finalId));
        Optional<Merchant> optional = merchantService.findByMerchantId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Merchant " + finalId));
        return responseEntity;
    }


    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        final String finalCode = code;
        code = Optional.ofNullable(code).orElseThrow(() -> new BlowfishIllegalArgumentException("Merchant Code " + finalCode));
        Optional<Merchant> optional = merchantService.findByCode(code);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Merchant " + finalCode));
        return responseEntity;
    }


    @RequestMapping(value = "/{merchantId}/terminals", method = RequestMethod.GET)
    @ResponseBody
    public Object getMerchantTerminalByMerchantId(@PathVariable Long merchantId)
    {
        return merchantTerminalsController.getByMerchantId(merchantId);
    }

    @RequestMapping(value = "/{merchantId}/termparams", method = RequestMethod.GET)
    @ResponseBody
    public Object getMerchantTermParamByMerchantId(@PathVariable Long merchantId)
    {
        return merchantTermParamsController.getByMerchantId(merchantId);
    }


    public Merchant getLinks(Merchant merchant){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantController.class).slash(merchant.getMerchantId()).withSelfRel();
        merchant.add(selfLink);

        Object domainMethodLinkBuilder = ControllerLinkBuilder.methodOn(DomainController.class).getById(merchant.getDomainId());
        Link domainLink = ControllerLinkBuilder.linkTo(domainMethodLinkBuilder).withRel("merchantDomain");
        merchant.add(domainLink);

        List<MerchantTerminal> list = merchantTerminalService.findByMerchantId(merchant.getMerchantId()).orElse(new ArrayList<>());
        if (list.size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(MerchantController.class).getMerchantTerminalByMerchantId(merchant.getMerchantId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allMerchantTerminals");
            merchant.add(link);
        }

        if (merchantTerminalParameterService.findByMerchantId(merchant.getMerchantId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(MerchantController.class).getMerchantTermParamByMerchantId(merchant.getMerchantId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allMerchantTermParams");
            merchant.add(link);
        }
        return merchant;
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
