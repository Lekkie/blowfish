package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Endpoint;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.EndpointService;
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

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/endpoints", produces = "application/hal+json")
public class EndpointController {


    private static final Logger logger = LoggerFactory.getLogger(EndpointController.class);
    @Autowired
    EndpointService endpointService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Endpoint endpoint)
    {
        endpoint = Optional.ofNullable(endpoint).orElseThrow(() -> new BlowfishIllegalArgumentException("Endpoint"));

        Optional<Endpoint> optional = endpointService.create(endpoint);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Endpoint "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Endpoint endpoint)
    {
        endpoint = Optional.ofNullable(endpoint).orElseThrow(() -> new BlowfishIllegalArgumentException("Endpoint"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Endpoint Id"));

        endpoint.setEndpointId(id);
        final long finalId = id;
        Optional<Endpoint> optional = endpointService.update(endpoint);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Endpoint " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Endpoint Id " + finalId));

        endpointService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Endpoint Id " + finalId));

        Optional<Endpoint> optional = endpointService.findByEndpointId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Endpoint " + finalId));
        return responseEntity;
    }


    public Endpoint getLinks(Endpoint endpoint){
        Link selfLink = ControllerLinkBuilder.linkTo(EndpointController.class).slash(endpoint.getEndpointId()).withSelfRel();
        endpoint.add(selfLink);

        return endpoint;
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
