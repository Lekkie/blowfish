package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.KeyService;
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
@RequestMapping(value = "api/v1/keys", produces = "application/hal+json")
public class KeyController {


    private static final Logger logger = LoggerFactory.getLogger(KeyController.class);
    @Autowired
    KeyService keyService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Key key)
    {
        key = Optional.ofNullable(key).orElseThrow(() -> new BlowfishIllegalArgumentException("Key"));

        Optional<Key> optional = keyService.create(key);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(o ->{
                    return new ResponseEntity<Object>(o, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Key"));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Key key)
    {
        key = Optional.ofNullable(key).orElseThrow(() -> new BlowfishIllegalArgumentException("Key"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Key Id"));

        key.setKeyId(id);
        final long finalId = id;
        Optional<Key> optional = keyService.update(key);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Key " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Key Id " + finalId));

        keyService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable long id)
    {
        String fxnParams = "id=" + id ;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("Key Id " + finalId));

        Optional<Key> optional = keyService.findByKeyId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + finalId));
        return responseEntity;
    }


    public Key getLinks(Key key){
        Link selfLink = ControllerLinkBuilder.linkTo(KeyController.class).slash(key.getKeyId()).withSelfRel();
        key.add(selfLink);

        return key;
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
