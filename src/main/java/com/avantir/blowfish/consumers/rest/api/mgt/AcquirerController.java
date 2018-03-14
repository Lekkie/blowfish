package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.services.*;
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
@RequestMapping(value = "api/v1/acquirers", produces = "application/hal+json")
public class AcquirerController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerMerchantService acquirerMerchantService;
    @Autowired
    AcquirerBinService acquirerBinService;
    @Autowired
    AcquirerTermParamService acquirerTermParamService;
    @Autowired
    AcquirerMerchantTranTypeBinService acquirerMerchantTranTypeBinService;
    @Autowired
    AcquirerMerchantController acquirerMerchantController;
    @Autowired
    AcquirerTermParamController acquirerTermParamController;
    @Autowired
    AcquirerBinController acquirerBinController;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Acquirer acquirer)
    {
        try{
            acquirerService.create(acquirer);
            acquirer = getLinks(acquirer);

            return new ResponseEntity<Object>("", HttpStatus.CREATED);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Acquirer acquirer)
    {
        try{
            if(acquirer == null)
                throw new Exception();

            acquirer.setAcquirerId(id);
            acquirer = acquirerService.update(acquirer);
            acquirer = getLinks(acquirer);

            return new ResponseEntity<Object>(acquirer, HttpStatus.OK);
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
            acquirerService.delete(id);

            return new ResponseEntity<Object>("", HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code)
    {
        String fxnParams = "id=" + id + ", code=" + code;
        try
        {
            if(id != null && id > 0)
                return getById(id);

            if(code != null && !code.isEmpty())
                return getByCode(code);

            List<Acquirer> acquirerList = acquirerService.findAll();
            for (Acquirer acquirer : acquirerList) {
                acquirer = getLinks(acquirer);
            }

            return new ResponseEntity<Object>(acquirerList, HttpStatus.OK);
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
    public Object getById(@PathVariable("id") long id)
    {
        String fxnParams = "id=" + id ;
        try
        {
            Acquirer acquirer = acquirerService.findByAcquirerId(id);
            acquirer = getLinks(acquirer);

            return new ResponseEntity<Object>(acquirer, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        try
        {
            Acquirer acquirer = acquirerService.findByCode(code);
            acquirer = getLinks(acquirer);

            return new ResponseEntity<Object>(acquirer, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/{acquirerId}/merchants", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerMerchantByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerMerchantController.getByAcquirerId(acquirerId);
    }

    @RequestMapping(value = "/{acquirerId}/bins", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerBinByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerBinController.getByAcquirerId(acquirerId);
    }

    @RequestMapping(value = "/{acquirerId}/termparams", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerTermParamByAcquirerId(@PathVariable Long acquirerId)
    {
        return acquirerTermParamController.getByAcquirerId(acquirerId);
    }


    private Acquirer getLinks(Acquirer acquirer){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerController.class).slash(acquirer.getAcquirerId()).withSelfRel();
        acquirer.add(selfLink);

        if (acquirerMerchantService.findByAcquirerId(acquirer.getAcquirerId()).size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerMerchantByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerMerchants");
            acquirer.add(link);
        }

        if (acquirerBinService.findByAcquirerId(acquirer.getAcquirerId()).size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerBinByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerBins");
            acquirer.add(link);
        }

        if (acquirerTermParamService.findByAcquirerId(acquirer.getAcquirerId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerTermParamByAcquirerId(acquirer.getAcquirerId());
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerTermParams");
            acquirer.add(link);
        }

        return acquirer;
    }

}
