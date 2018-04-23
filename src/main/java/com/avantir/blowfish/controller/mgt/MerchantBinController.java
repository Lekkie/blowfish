package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.MerchantBin;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.ErrorService;
import com.avantir.blowfish.services.MerchantBinService;
import com.avantir.blowfish.services.MerchantService;
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
@RequestMapping(value = "api/v1/merchants/bins", produces = "application/hal+json")
public class MerchantBinController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantBinController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantBinService acquirerBinService;
    @Autowired
    MerchantController merchantController;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantBin merchantBin)
    {
        merchantBin = Optional.ofNullable(merchantBin).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantBin"));
        Optional<MerchantBin> optional = acquirerBinService.create(merchantBin);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("MerchantBin "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody MerchantBin merchantBin)
    {
        merchantBin = Optional.ofNullable(merchantBin).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantBin"));
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantBin Id"));
        merchantBin.setMerchantBinId(id);
        final long finalId = id;
        Optional<MerchantBin> optional = acquirerBinService.update(merchantBin);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("MerchantBin " + finalId));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantBin Id " + finalId));
        acquirerBinService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchantId", required = false) Long merchantId, @RequestHeader(value="binId", required = false) Long binId)
    {
        String fxnParams = "id=" + id + ", merchantId=" + merchantId + ", binId=" + binId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(merchantId).isPresent())
            return getByMerchantId(merchantId);

        if(Optional.ofNullable(binId).isPresent())
            return getByBinId(binId);

        List<MerchantBin> merchantBinList = acquirerBinService.findAll().orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantBins"));
        List<MerchantBin> newList = merchantBinList.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(newList, HttpStatus.OK);
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id ;
        final long finalId = id;
        id = Optional.ofNullable(id).orElseThrow(() -> new BlowfishIllegalArgumentException("MerchantBins Id " + finalId));
        Optional<MerchantBin> optional = acquirerBinService.findByMerchantBinId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantBin " + finalId));
        return responseEntity;
    }


    public Object getByMerchantId(Long merchantId)
    {
        String fxnParams = "merchantId=" + merchantId;
        final Long finalCode = merchantId;
        List<MerchantBin> list = acquirerBinService.findByMerchantId(merchantId).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantBins"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }


    public Object getByBinId(Long binId)
    {
        String fxnParams = "binId=" + binId;
        final Long finalCode = binId;
        List<MerchantBin> list = acquirerBinService.findByBinId(binId).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantBins"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }



    public MerchantBin getLinks(MerchantBin merchantBin){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantBinController.class).slash(merchantBin.getMerchantBinId()).withSelfRel();
        merchantBin.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(merchantBin.getMerchantId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("merchant");
        merchantBin.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(BinController.class).getById(merchantBin.getBinId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("bin");
        merchantBin.add(link1);

        return merchantBin;
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
