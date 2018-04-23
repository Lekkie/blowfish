package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.AcquirerBin;
import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.AcquirerBinService;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.services.ErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/acquirers/bins", produces = "application/hal+json")
@PreAuthorize("permitAll")
//@PreAuthorize("hasAuthority('ADMIN_USER')")
//@PreAuthorize("hasRole('ADMIN_USER')")
public class AcquirerBinController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerBinController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerBinService acquirerBinService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody AcquirerBin acquirerBin)
    {
        Optional<AcquirerBin> optionalNew = Optional.ofNullable(acquirerBin);
        acquirerBin = optionalNew.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin"));

        Optional<AcquirerBin> optional = acquirerBinService.create(acquirerBin);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("AcquirerBin "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AcquirerBin acquirerBin)
    {
        Optional<AcquirerBin> optionalNewAcq = Optional.ofNullable(acquirerBin);
        acquirerBin = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin"));
        Optional<Long> optionalAcqId = Optional.ofNullable(id);
        id = optionalAcqId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin Id"));

        acquirerBin.setAcquirerBinId(id);
        final long acqBinId = id;
        Optional<AcquirerBin> optional = acquirerBinService.update(acquirerBin);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("AcquirerBin " + acqBinId));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        final long acqBinId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin Id " + acqBinId));

        acquirerBinService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="binId", required = false) Long binId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", binId=" + binId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(acquirerId).isPresent())
            return getByAcquirerId(acquirerId);

        if(Optional.ofNullable(binId).isPresent())
            return getByBinId(binId);

        Optional<List<AcquirerBin>> optional = acquirerBinService.findAll();
        List<AcquirerBin> acquirerBinList = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerBins"));
        acquirerBinList = acquirerBinList.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(acquirerBinList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer Id " + finalId));

        Optional<AcquirerBin> optional = acquirerBinService.findByAcquirerBinId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerBin " + finalId));
        return responseEntity;
    }



    public Object getByAcquirerId(Long acquirerId)
    {
        String fxnParams = "acquirerId=" + acquirerId;
        final long finalId = acquirerId;
        Optional<Long> optionalId = Optional.ofNullable(acquirerId);
        acquirerId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin AcquirerId " + finalId));

        Optional<List<AcquirerBin>> optional = acquirerBinService.findByAcquirerId(acquirerId);
        List<AcquirerBin> acquirerList = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerBins " + finalId));
        acquirerList = acquirerList.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(acquirerList, HttpStatus.OK);
    }


    public Object getByBinId(Long binId)
    {
        String fxnParams = "binId=" + binId;
        final long finalId = binId;
        Optional<Long> optionalId = Optional.ofNullable(binId);
        binId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerBin BinId " + finalId));

        Optional<AcquirerBin> optional = acquirerBinService.findByBinId(binId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerBin for BinId " + finalId));
        return responseEntity;
    }



    public AcquirerBin getLinks(AcquirerBin acquirerBin){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerBinController.class).slash(acquirerBin.getAcquirerBinId()).withSelfRel();
        acquirerBin.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(acquirerBin.getAcquirerId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("acquirer");
        acquirerBin.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(BinController.class).getById(acquirerBin.getBinId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("bin");
        acquirerBin.add(link1);

        return acquirerBin;
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
