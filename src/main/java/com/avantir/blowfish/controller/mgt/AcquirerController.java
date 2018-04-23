package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.Acquirer;
import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.*;
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
@RequestMapping(value = "api/v1/acquirers", produces = "application/hal+json")
public class AcquirerController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerMerchantService acquirerMerchantService;
    @Autowired
    AcquirerBinService acquirerBinService;
    @Autowired
    AcquirerTermParamService acquirerTermParamService;
    @Autowired
    AcquirerMerchantTranTypeBinService acquirerMerchantTranTypeBinService;
    @Autowired
    AcquirerMerchantController acquirerMerchantController;
    @Autowired
    AcquirerTermParamController acquirerTermParamController;
    @Autowired
    AcquirerBinController acquirerBinController;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Acquirer acquirer)
    {
        acquirer = Optional.ofNullable(acquirer).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer"));
        Optional<Acquirer> optional = acquirerService.create(acquirer);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Acquirer "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Acquirer acquirer)
    {
        acquirer = Optional.ofNullable(acquirer).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer Id"));
        acquirer.setAcquirerId(id);
        final long finalId = id;
        Optional<Acquirer> optional = acquirerService.update(acquirer);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Acquirer " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer Id " + finalId));
        acquirerService.delete(id);
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
        List<Acquirer> list = acquirerService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("Acquirers"));
        List<Acquirer> newList = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(newList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable("id") long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer Id " + finalId));
        Optional<Acquirer> optional = acquirerService.findByAcquirerId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Acquirer " + finalId));
        return responseEntity;
    }


    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        final String finalCode = code;
        code = Optional.ofNullable(code).orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer Code " + finalCode));
        Optional<Acquirer> optional = acquirerService.findByCode(code);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Acquirer " + finalCode));
        return responseEntity;
    }


    @RequestMapping(value = "/{acquirerId}/merchants", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerMerchantByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerMerchantController.getByAcquirerId(acquirerId);
    }

    @RequestMapping(value = "/{acquirerId}/bins", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerBinByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerBinController.getByAcquirerId(acquirerId);
    }

    @RequestMapping(value = "/{acquirerId}/termparams", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerTermParamByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerTermParamController.getByAcquirerId(acquirerId);
    }


    public Acquirer getLinks(Acquirer acquirer){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerController.class).slash(acquirer.getAcquirerId()).withSelfRel();
        if(!acquirer.hasLink(selfLink.getRel()))
            acquirer.add(selfLink);

        List<Acquirer> acqMerchList = acquirerMerchantService.findByAcquirerId(acquirer.getAcquirerId()).orElse(new ArrayList());
        if (acqMerchList.size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerMerchantByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerMerchants");
            if(!acquirer.hasLink(link.getRel()))
                acquirer.add(link);
        }

        List<Acquirer> acqBinList = acquirerBinService.findByAcquirerId(acquirer.getAcquirerId()).orElse(new ArrayList());
        if (acqBinList.size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerBinByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerBins");
            if(!acquirer.hasLink(link.getRel()))
                acquirer.add(link);
        }

        if (acquirerTermParamService.findByAcquirerId(acquirer.getAcquirerId()).isPresent()) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerTermParamByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerTermParams");
            if(!acquirer.hasLink(link.getRel()))
                acquirer.add(link);
        }

        return acquirer;
    }


    @ExceptionHandler(BlowfishRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Errors handleRuntimeException(HttpServletRequest httpServletRequest, BlowfishRuntimeException brex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(brex.getMessage(), brex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }

    /*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Errors handleException(HttpServletRequest httpServletRequest, Exception ex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(ex.getMessage(), ex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }
    */


}
