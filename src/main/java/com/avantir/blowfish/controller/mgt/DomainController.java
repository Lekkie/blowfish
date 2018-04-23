package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Domain;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.DomainService;
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
 * Created by lekanomotayo on 06/02/2018.
 */

@RestController
@RequestMapping(value = "api/v1/domains", produces = "application/hal+json")
public class DomainController {


    private static final Logger logger = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    DomainService domainService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Domain domain)
    {
        domain = Optional.ofNullable(domain).orElseThrow(() -> new BlowfishIllegalArgumentException("Domain"));

        Optional<Domain> optional = domainService.create(domain);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(d ->{
                    return new ResponseEntity<Object>(d, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Domain "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Domain domain)
    {
        domain = Optional.ofNullable(domain).orElseThrow(() -> new BlowfishIllegalArgumentException("Domain"));
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Domain Id"));

        domain.setDomainId(id);
        final long finalId = id;
        Optional<Domain> optional = domainService.update(domain);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Domain " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("Domain Id " + finalId));

        domainService.delete(id);
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

        List<Domain> list = domainService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("Domains"));
        List<Domain> newList = list.stream()
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
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Domain Id " + finalId));

        Optional<Domain> optional = domainService.findByDomainId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Domain " + finalId));
        return responseEntity;
    }

    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        final String finalCode = code;
        Optional<String> optionalId = Optional.ofNullable(code);
        code = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Domain Code " + finalCode));

        Optional<Domain> optional = domainService.findByCode(code);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Domain " + finalCode));
        return responseEntity;
    }


    public Domain getLinks(Domain domain){
        Link selfLink = ControllerLinkBuilder.linkTo(DomainController.class).slash(domain.getDomainId()).withSelfRel();
        domain.add(selfLink);
        return domain;
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
