package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.AcquirerTermParam;
import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.services.AcquirerTermParamService;
import com.avantir.blowfish.services.ErrorService;
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
@RequestMapping(value = "api/v1/acquirers/termparams", produces = "application/hal+json")
public class AcquirerTermParamController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerTermParamController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerTermParamService acquirerTermParamService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody AcquirerTermParam acquirerTermParam)
    {
        Optional<AcquirerTermParam> optionalNewAcq = Optional.ofNullable(acquirerTermParam);
        acquirerTermParam = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam"));

        Optional<AcquirerTermParam> optional = acquirerTermParamService.create(acquirerTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("AcquirerTermParam "));
        return responseEntity;
    }



    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerTermParam acquirerTermParam)
    {
        Optional<AcquirerTermParam> optionalNewAcq = Optional.ofNullable(acquirerTermParam);
        acquirerTermParam = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam"));
        Optional<Long> optionalAcqId = Optional.ofNullable(id);
        id = optionalAcqId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam Id"));

        acquirerTermParam.setAcquirerTermParamId(id);
        final long acqId = id;
        Optional<AcquirerTermParam> optional = acquirerTermParamService.update(acquirerTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("AcquirerTermParam " + acqId));
        return responseEntity;
    }



    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long acqId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam Id " + acqId));

        acquirerTermParamService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="termParamId", required = false) Long termParamId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", termParamId=" + termParamId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(acquirerId).isPresent())
            return getByAcquirerId(acquirerId);

        if(Optional.ofNullable(termParamId).isPresent())
            return getByTermParamId(termParamId);

        Optional<List<AcquirerTermParam>> optional = acquirerTermParamService.findAll();
        List<AcquirerTermParam> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerTermParams"));
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
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam Id " + finalId));

        Optional<AcquirerTermParam> optional = acquirerTermParamService.findByAcquirerTermParamId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerTermParam " + finalId));
        return responseEntity;
    }


    public Object getByAcquirerId(Long acquirerId)
    {
        String fxnParams = "acquirerId=" + acquirerId;
        final long finalId = acquirerId;
        acquirerId = Optional.ofNullable(acquirerId).orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam AcquirerId " + finalId));
        Optional<AcquirerTermParam> optional = acquirerTermParamService.findByAcquirerId(acquirerId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerTermParam " + finalId));
        return responseEntity;
    }

    public Object getByTermParamId(Long termParamId)
    {
        String fxnParams = "termParamId=" + termParamId;
        final long finalId = termParamId;
        Optional<Long> optionalId = Optional.ofNullable(termParamId);
        termParamId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerTermParam TermParamId " + finalId));

        Optional<List<AcquirerTermParam>> optional = acquirerTermParamService.findByTermParamId(termParamId);
        List<AcquirerTermParam> acquirerList = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerTermParams " + finalId));
        acquirerList = acquirerList.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(acquirerList, HttpStatus.OK);
    }


    public AcquirerTermParam getLinks(AcquirerTermParam merchantTerminal){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerTermParamController.class).slash(merchantTerminal.getAcquirerTermParamId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(merchantTerminal.getTermParamId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("termparam");
        merchantTerminal.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(merchantTerminal.getAcquirerId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("acquirer");
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
