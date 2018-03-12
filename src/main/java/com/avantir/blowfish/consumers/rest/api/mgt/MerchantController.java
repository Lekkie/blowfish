package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.*;
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
@RequestMapping(value = "api/v1/merchants", produces = "application/hal+json")
public class MerchantController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTerminalService merchantTerminalService;
    @Autowired
    MerchantTermParamService merchantTerminalParameterService;
    @Autowired
    MerchantTerminalTranTypeBinService merchantTerminalTranTypeBinService;
    @Autowired
    MerchantTerminalController merchantTerminalsController;
    @Autowired
    MerchantTermParamController merchantTermParamsController;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Merchant merchant, HttpServletResponse response)
    {
        try{
            merchantService.create(merchant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            merchant = getLinks(merchant, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Merchant newMerchant, HttpServletResponse response)
    {
        try{
            if(newMerchant == null)
                throw new Exception();

            newMerchant.setMerchantId(id);
            newMerchant = merchantService.update(newMerchant);
            response.setStatus(HttpServletResponse.SC_OK);
            newMerchant = getLinks(newMerchant, response);

            return newMerchant;
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
            merchantService.delete(id);
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

            List<Merchant> merchantList = merchantService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (Merchant merchant : merchantList) {
                merchant = getLinks(merchant, response);
            }
            return merchantList;
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
            Merchant merchant = merchantService.findByMerchantId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            merchant = getLinks(merchant, response);

            return merchant;
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
            Merchant merchant = merchantService.findByCode(code);
            response.setStatus(HttpServletResponse.SC_OK);
            merchant = getLinks(merchant, response);

            return merchant;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(value = "/{merchantId}/terminals", method = RequestMethod.GET)
    @ResponseBody
    public Object getMerchantTerminalByMerchantId(@PathVariable Long merchantId, HttpServletResponse response)
    {
        return merchantTerminalsController.getByMerchantId(merchantId, response);
    }

    @RequestMapping(value = "/{merchantId}/termparams", method = RequestMethod.GET)
    @ResponseBody
    public Object getMerchantTermParamByMerchantId(@PathVariable Long merchantId, HttpServletResponse response)
    {
        return merchantTermParamsController.getByMerchantId(merchantId, response);
    }


    private Merchant getLinks(Merchant merchant, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantController.class).slash(merchant.getMerchantId()).withSelfRel();
        merchant.add(selfLink);

        Object domainMethodLinkBuilder = ControllerLinkBuilder.methodOn(DomainController.class).getById(merchant.getDomainId(), response);
        Link domainLink = ControllerLinkBuilder.linkTo(domainMethodLinkBuilder).withRel("merchantDomain");
        merchant.add(domainLink);

        if (merchantTerminalService.findByMerchantId(merchant.getMerchantId()).size() > 0) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(MerchantController.class).getMerchantTerminalByMerchantId(merchant.getMerchantId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allMerchantTerminals");
            merchant.add(link);
        }

        if (merchantTerminalParameterService.findByMerchantId(merchant.getMerchantId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(MerchantController.class).getMerchantTermParamByMerchantId(merchant.getMerchantId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allMerchantTermParams");
            merchant.add(link);
        }
        return merchant;
    }

}
