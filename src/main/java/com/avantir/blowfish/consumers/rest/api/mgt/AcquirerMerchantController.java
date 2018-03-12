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
    public Object create(@RequestBody AcquirerMerchant acquirerMerchant, HttpServletResponse response)
    {
        try{
            acquirerMerchantService.create(acquirerMerchant);
            response.setStatus(HttpServletResponse.SC_CREATED);
            acquirerMerchant = getLinks(acquirerMerchant, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerMerchant newAcquirerMerchant, HttpServletResponse response)
    {
        try{
            if(newAcquirerMerchant == null)
                throw new Exception();

            newAcquirerMerchant.setAcquirerMerchantId(id);
            newAcquirerMerchant = acquirerMerchantService.update(newAcquirerMerchant);
            response.setStatus(HttpServletResponse.SC_OK);
            newAcquirerMerchant = getLinks(newAcquirerMerchant, response);
            return newAcquirerMerchant;
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
            acquirerMerchantService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="merchantId", required = false) Long merchantId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", merchantId=" + merchantId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);


            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId, response);

            if(merchantId != null && acquirerId > 0)
                return getByMerchantId(merchantId, response);

            List<AcquirerMerchant> acquirerMerchantList = acquirerMerchantService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerMerchant acquirerMerchant : acquirerMerchantList) {
                acquirerMerchant = getLinks(acquirerMerchant, response);
            }

            return acquirerMerchantList;
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
    public Object getById(@PathVariable Long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByAcquirerMerchantId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerMerchant = getLinks(acquirerMerchant, response);
            return acquirerMerchant;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }



    public Object getByAcquirerId(Long acquirerId, HttpServletResponse response)
    {
        String fxnParams = "acquirerId=" + acquirerId + ",HttpServletResponse=" + response.toString();
        try
        {
            List<AcquirerMerchant> acquirerMerchantList = acquirerMerchantService.findByAcquirerId(acquirerId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerMerchant acquirerMerchant : acquirerMerchantList) {
                acquirerMerchant = getLinks(acquirerMerchant, response);
            }
            return acquirerMerchantList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByMerchantId(Long merchantId, HttpServletResponse response)
    {
        String fxnParams = "merchantId=" + merchantId + ",HttpServletResponse=" + response.toString();
        try
        {
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(merchantId);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerMerchant = getLinks(acquirerMerchant, response);
            return acquirerMerchant;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }



    private AcquirerMerchant getLinks(AcquirerMerchant acquirerMerchant, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerMerchantController.class).slash(acquirerMerchant.getAcquirerMerchantId()).withSelfRel();
        acquirerMerchant.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(acquirerMerchant.getAcquirerId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("acquirer");
        acquirerMerchant.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(acquirerMerchant.getMerchantId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("merchant");
        acquirerMerchant.add(link1);

        return acquirerMerchant;
    }

}
