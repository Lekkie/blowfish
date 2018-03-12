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


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Acquirer acquirer, HttpServletResponse response)
    {
        try{
            acquirerService.create(acquirer);
            response.setStatus(HttpServletResponse.SC_CREATED);
            acquirer = getLinks(acquirer, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Acquirer newAcquirer, HttpServletResponse response)
    {
        try{
            if(newAcquirer == null)
                throw new Exception();

            newAcquirer.setAcquirerId(id);
            newAcquirer = acquirerService.update(newAcquirer);
            response.setStatus(HttpServletResponse.SC_OK);
            newAcquirer = getLinks(newAcquirer, response);

            return newAcquirer;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            acquirerService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(code != null && !code.isEmpty())
                return getByCode(code, response);

            List<Acquirer> acquirerList = acquirerService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (Acquirer acquirer : acquirerList) {
                acquirer = getLinks(acquirer, response);
            }
            return acquirerList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable("id") long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            Acquirer acquirer = acquirerService.findByAcquirerId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirer = getLinks(acquirer, response);

            return acquirer;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByCode(String code, HttpServletResponse response)
    {
        String fxnParams = "code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            Acquirer acquirer = acquirerService.findByCode(code);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirer = getLinks(acquirer, response);

            return acquirer;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(value = "/{acquirerId}/merchants", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerMerchantByAcquirerId(@PathVariable Long acquirerId, HttpServletResponse response)
    {
        return acquirerMerchantController.getByAcquirerId(acquirerId, response);
    }

    @RequestMapping(value = "/{acquirerId}/bins", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerBinByAcquirerId(@PathVariable Long acquirerId, HttpServletResponse response)
    {
        return null;
        //return merchantTerminalsController.getByMerchantId(acquirerId, response);
    }

    @RequestMapping(value = "/{acquirerId}/termparams", method = RequestMethod.GET)
    @ResponseBody
    public Object getAcquirerTermParamByAcquirerId(@PathVariable Long acquirerId, HttpServletResponse response)
    {
        return null;
        //return merchantTermParamsController.getByMerchantId(acquirerId, response);
    }


    private Acquirer getLinks(Acquirer acquirer, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerController.class).slash(acquirer.getAcquirerId()).withSelfRel();
        acquirer.add(selfLink);

        if (acquirerMerchantService.findByAcquirerId(acquirer.getAcquirerId()).size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerMerchantByAcquirerId(acquirer.getAcquirerId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerMerchants");
            acquirer.add(link);
        }

        if (acquirerBinService.findByAcquirerId(acquirer.getAcquirerId()).size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerBinByAcquirerId(acquirer.getAcquirerId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerBins");
            acquirer.add(link);
        }

        if (acquirerTermParamService.findByAcquirerId(acquirer.getAcquirerId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(AcquirerController.class).getAcquirerTermParamByAcquirerId(acquirer.getAcquirerId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allAcquirerTermParams");
            acquirer.add(link);
        }

        return acquirer;
    }

}
