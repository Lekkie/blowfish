package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.MerchantTerminal;
import com.avantir.blowfish.services.AcquirerMerchantService;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody AcquirerMerchant acquirerMerchant)
    {
        try{
            acquirerMerchantService.create(acquirerMerchant);
            return new ResponseEntity<Object>("", HttpStatus.CREATED);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerMerchant acquirerMerchant)
    {
        try{
            if(acquirerMerchant == null)
                throw new Exception();

            acquirerMerchant.setAcquirerMerchantId(id);
            acquirerMerchant = acquirerMerchantService.update(acquirerMerchant);
            acquirerMerchant = getLinks(acquirerMerchant);

            return new ResponseEntity<Object>(acquirerMerchant, HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        try{
            acquirerMerchantService.delete(id);
            return new ResponseEntity<Object>("", HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="merchantId", required = false) Long merchantId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", merchantId=" + merchantId;
        try
        {
            if(id != null && id > 0)
                return getById(id);


            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId);

            if(merchantId != null && acquirerId > 0)
                return getByMerchantId(merchantId);

            List<AcquirerMerchant> acquirerMerchantList = acquirerMerchantService.findAll();
            for (AcquirerMerchant acquirerMerchant : acquirerMerchantList) {
                acquirerMerchant = getLinks(acquirerMerchant);
            }

            return new ResponseEntity<Object>(acquirerMerchantList, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        try
        {
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByAcquirerMerchantId(id);
            acquirerMerchant = getLinks(acquirerMerchant);
            return new ResponseEntity<Object>(acquirerMerchant, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    public Object getByAcquirerId(Long acquirerId)
    {
        String fxnParams = "acquirerId=" + acquirerId;
        try
        {
            List<AcquirerMerchant> acquirerMerchantList = acquirerMerchantService.findByAcquirerId(acquirerId);
            for (AcquirerMerchant acquirerMerchant : acquirerMerchantList) {
                acquirerMerchant = getLinks(acquirerMerchant);
            }

            return new ResponseEntity<Object>(acquirerMerchantList, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public Object getByMerchantId(Long merchantId)
    {
        String fxnParams = "merchantId=" + merchantId;
        try
        {
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(merchantId);
            acquirerMerchant = getLinks(acquirerMerchant);
            return new ResponseEntity<Object>(acquirerMerchant, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    private AcquirerMerchant getLinks(AcquirerMerchant acquirerMerchant){
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

}
