package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.TerminalTermParam;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.TerminalTermParamService;
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
@RequestMapping(value = "api/v1/terminals/termparams", produces = "application/hal+json")
public class TerminalTermParamController {


    private static final Logger logger = LoggerFactory.getLogger(TerminalTermParamController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    TerminalTermParamService terminalTermParamService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity create(@RequestBody TerminalTermParam terminalTermParam)
    {
        terminalTermParam = Optional.ofNullable(terminalTermParam).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam"));
        Optional<TerminalTermParam> optional = terminalTermParamService.create(terminalTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("TerminalTermParam "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody TerminalTermParam terminalTermParam)
    {
        terminalTermParam = Optional.ofNullable(terminalTermParam).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam Id"));
        terminalTermParam.setTerminalTermParamId(id);
        final long finalId = id;
        Optional<TerminalTermParam> optional = terminalTermParamService.update(terminalTermParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("TerminalTermParam " + finalId));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam Id " + finalId));
        terminalTermParamService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="termParamId", required = false) Long termParamId, @RequestHeader(value="terminalId", required = false) Long terminalId)
    {
        String fxnParams = "id=" + id + ", termParamId=" + termParamId + ", terminalId=" + terminalId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(termParamId).isPresent())
            return getByTermParamId(termParamId);

        if(Optional.ofNullable(terminalId).isPresent())
            return getByTerminalId(terminalId);

        List<TerminalTermParam> list = terminalTermParamService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("TerminalTermParams"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam Id " + finalId));
        Optional<TerminalTermParam> optional = terminalTermParamService.findByTerminalTermParamId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("TerminalTermParam " + finalId));
        return responseEntity;
    }


    public ResponseEntity getByTerminalId(Long terminalId)
    {
        String fxnParams = "terminalId=" + terminalId;
        final long finalId = terminalId;
        terminalId = Optional.ofNullable(terminalId).orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam TerminalId " + finalId));
        Optional<TerminalTermParam> optional = terminalTermParamService.findByTerminalId(terminalId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("TerminalTermParam " + finalId));
        return responseEntity;
    }



    public ResponseEntity getByTermParamId(Long termParamId)
    {
        String fxnParams = "termParamId=" + termParamId;
        final long finalId = termParamId;
        Optional<Long> optionalId = Optional.ofNullable(termParamId);
        termParamId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("TerminalTermParam TermParamId " + finalId));
        List<TerminalTermParam> list = terminalTermParamService.findByTermParamId(termParamId).orElseThrow(() -> new BlowfishEntityNotFoundException("TerminalTermParams " + finalId));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    public TerminalTermParam getLinks(TerminalTermParam terminalTermParam){
        Link selfLink = ControllerLinkBuilder.linkTo(TerminalTermParamController.class).slash(terminalTermParam.getTerminalTermParamId()).withSelfRel();
        terminalTermParam.add(selfLink);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(terminalTermParam.getTermParamId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("termparam");
        terminalTermParam.add(link1);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TerminalController.class).getById(terminalTermParam.getTerminalId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("terminal");
        terminalTermParam.add(link2);

        return terminalTermParam;
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
