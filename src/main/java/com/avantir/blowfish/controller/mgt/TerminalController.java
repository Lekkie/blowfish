package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Terminal;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.TerminalService;
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
@RequestMapping(value = "api/v1/terminals", produces = "application/hal+json")
public class TerminalController {


    private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalTermParamService terminalTerminalParameterService;
    @Autowired
    TerminalTermParamController terminalTermParamsController;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Terminal terminal)
    {
        terminal = Optional.ofNullable(terminal).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal"));
        Optional<Terminal> optional = terminalService.create(terminal);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Terminal "));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Terminal terminal)
    {
        terminal = Optional.ofNullable(terminal).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal Id"));
        terminal.setTerminalId(id);
        final long finalId = id;
        Optional<Terminal> optional = terminalService.update(terminal);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Terminal " + finalId));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal Id " + finalId));
        terminalService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="deviceSerialNo", required = false) String deviceSerialNo)
    {
        String fxnParams = "id=" + id + ", deviceSerialNo=" + deviceSerialNo;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);
        if(Optional.ofNullable(deviceSerialNo).isPresent())
            return getByDeviceSerialNo(deviceSerialNo);
        List<Terminal> list = terminalService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("Terminals"));
        List<Terminal> newList = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(newList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal Id " + finalId));
        Optional<Terminal> optional = terminalService.findByTerminalId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Terminal " + finalId));
        return responseEntity;
    }


    public Object getByDeviceSerialNo(String deviceSerialNo)
    {
        String fxnParams = "deviceSerialNo=" + deviceSerialNo;
        final String finalSerialNo = deviceSerialNo;
        deviceSerialNo = Optional.ofNullable(deviceSerialNo).orElseThrow(() -> new BlowfishIllegalArgumentException("Terminal Serial No " + finalSerialNo));
        Optional<Terminal> optional = terminalService.findBySerialNo(deviceSerialNo);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Terminal " + finalSerialNo));
        return responseEntity;
    }


    @RequestMapping(value = "/{terminalId}/termparams", method = RequestMethod.GET)
    public Object getTerminalTermParamsForTerminal(@PathVariable long terminalId) {
        return terminalTermParamsController.getByTerminalId(terminalId);
    }


    public Terminal getLinks(Terminal terminal){
        Link selfLink = ControllerLinkBuilder.linkTo(TerminalController.class).slash(terminal.getTerminalId()).withSelfRel();
        terminal.add(selfLink);

        if (terminalTerminalParameterService.findByTerminalId(terminal.getTerminalId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(TerminalController.class).getTerminalTermParamsForTerminal(terminal.getTerminalId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allTermParams");
            terminal.add(link);
        }
        return terminal;
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
