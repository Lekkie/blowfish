package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.*;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/bins", produces = "application/hal+json")
public class BinController {


    private static final Logger logger = LoggerFactory.getLogger(BinController.class);
    @Autowired
    BinService binService;
    @Autowired
    AcquirerBinService acquirerBinService;
    @Autowired
    MerchantBinService merchantBinService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Bin bin)
    {
        Optional<Bin> optionalBin = Optional.ofNullable(bin);
        bin = optionalBin.orElseThrow(() -> new BlowfishIllegalArgumentException("Acquirer"));

        Optional<Bin> optionalBin2 = binService.create(bin);
        ResponseEntity responseEntity = optionalBin2.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("Acquirer "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Bin bin)
    {
        Optional<Bin> optBin = Optional.ofNullable(bin);
        bin = optBin.orElseThrow(() -> new BlowfishIllegalArgumentException("Bin"));
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Bin Id"));

        bin.setBinId(id);
        final long acqId = id;
        Optional<Bin> optional = binService.update(bin);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("Bin " + acqId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE,
            consumes = "application/json",
            value = "/{id}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("Bin Id " + finalId));

        binService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET,
            headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code)
    {
        String fxnParams = "id=" + id + ", code=" + code;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(code).isPresent())
            return getByCode(code);

        Optional<List<Bin>> optional = binService.findAll();
        List<Bin> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("Bins"));
        List<Bin> newList = list.stream()
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
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Bin Id " + finalId));

        Optional<Bin> optional = binService.findByBinId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Bin " + finalId));
        return responseEntity;
    }


    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        final String finalCode = code;
        Optional<String> optionalId = Optional.ofNullable(code);
        code = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("Bin Code " + finalCode));

        Optional<Bin> optional = binService.findByCode(code);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("Bin " + finalCode));
        return responseEntity;
    }

    public Bin getLinks(Bin bin){
        Link selfLink = ControllerLinkBuilder.linkTo(BinController.class).slash(bin.getBinId()).withSelfRel();
        if(!bin.hasLink(selfLink.getRel()))
            bin.add(selfLink);

        if (acquirerBinService.findByBinId(bin.getBinId()).isPresent()) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerBinController.class).getByBinId(bin.getBinId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("acquirerBin");
            if(!bin.hasLink(link.getRel()))
                bin.add(link);
        }

        List<MerchantBin> merchBin = merchantBinService.findByBinId(bin.getBinId()).orElse(new ArrayList<>());
        if (merchBin.size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(MerchantBinController.class).getByBinId(bin.getBinId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allMerchantBins");
            if(!bin.hasLink(link.getRel()))
                bin.add(link);
        }

        return bin;
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
