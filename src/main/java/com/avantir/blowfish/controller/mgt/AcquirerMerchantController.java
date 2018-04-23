package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.AcquirerMerchant;
import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.services.AcquirerMerchantService;
import com.avantir.blowfish.services.AcquirerService;
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
@RequestMapping(value = "api/v1/acquirers/merchants", produces = "application/hal+json")
public class AcquirerMerchantController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerMerchantController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerMerchantService acquirerMerchantService;
    @Autowired
    ErrorService errorService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody AcquirerMerchant acquirerMerchant)
    {
        Optional<AcquirerMerchant> optionalNew = Optional.ofNullable(acquirerMerchant);
        acquirerMerchant = optionalNew.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant"));

        Optional<AcquirerMerchant> optional = acquirerMerchantService.create(acquirerMerchant);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("AcquirerMerchant "));
        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerMerchant acquirerMerchant)
    {
        Optional<AcquirerMerchant> optionalNewAcq = Optional.ofNullable(acquirerMerchant);
        acquirerMerchant = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant"));
        Optional<Long> optionalAcqId = Optional.ofNullable(id);
        id = optionalAcqId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant Id"));

        acquirerMerchant.setAcquirerMerchantId(id);
        final long acqId = id;
        Optional<AcquirerMerchant> optional = acquirerMerchantService.update(acquirerMerchant);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("AcquirerMerchant " + acqId));
        return responseEntity;
    }



    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long acqId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant Id " + acqId));

        acquirerMerchantService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="merchantId", required = false) Long merchantId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", merchantId=" + merchantId;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(acquirerId).isPresent())
            return getByAcquirerId(acquirerId);

        if(Optional.ofNullable(merchantId).isPresent())
            return getByMerchantId(merchantId);

        Optional<List<AcquirerMerchant>> optional = acquirerMerchantService.findAll();
        List<AcquirerMerchant> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerMerchants"));
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
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant Id " + finalId));

        Optional<AcquirerMerchant> optional = acquirerMerchantService.findByAcquirerMerchantId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerMerchant " + finalId));
        return responseEntity;
    }



    public Object getByAcquirerId(Long acquirerId)
    {
        String fxnParams = "acquirerId=" + acquirerId;
        final long finalId = acquirerId;
        Optional<Long> optionalId = Optional.ofNullable(acquirerId);
        acquirerId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant AcquirerId " + finalId));

        Optional<List<AcquirerMerchant>> optional = acquirerMerchantService.findByAcquirerId(acquirerId);
        List<AcquirerMerchant> acquirerList = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerMerchants " + finalId));
        acquirerList = acquirerList.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(acquirerList, HttpStatus.OK);
    }


    public Object getByMerchantId(Long merchantId)
    {
        String fxnParams = "merchantId=" + merchantId;
        final long finalId = merchantId;
        Optional<Long> optionalId = Optional.ofNullable(merchantId);
        merchantId = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("AcquirerMerchant merchantId " + finalId));

        Optional<AcquirerMerchant> optional = acquirerMerchantService.findByMerchantId(merchantId);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerMerchant " + finalId));
        return responseEntity;
    }



    public AcquirerMerchant getLinks(AcquirerMerchant acquirerMerchant){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerMerchantController.class).slash(acquirerMerchant.getAcquirerMerchantId()).withSelfRel();
        acquirerMerchant.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(acquirerMerchant.getAcquirerId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("acquirer");
        acquirerMerchant.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(acquirerMerchant.getMerchantId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("merchant");
        acquirerMerchant.add(link1);

        return acquirerMerchant;
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
