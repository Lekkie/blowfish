package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.AcquirerTermParam;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.MerchantTermParam;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.services.AcquirerTermParamService;
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
@RequestMapping(value = "api/v1/acquirers/termparams", produces = "application/hal+json")
public class AcquirerTermParamController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerTermParamController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerTermParamService acquirerTermParamService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody AcquirerTermParam acquirerTermParam, HttpServletResponse response)
    {
        try{
            acquirerTermParamService.create(acquirerTermParam);
            response.setStatus(HttpServletResponse.SC_CREATED);
            acquirerTermParam = getLinks(acquirerTermParam, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerTermParam acquirerTermParam, HttpServletResponse response)
    {
        try{
            if(acquirerTermParam == null)
                throw new Exception();

            acquirerTermParam.setAcquirerTermParamId(id);
            acquirerTermParam = acquirerTermParamService.update(acquirerTermParam);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerTermParam = getLinks(acquirerTermParam, response);
            return acquirerTermParam;
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
            acquirerTermParamService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="termParamId", required = false) Long termParamId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", termParamId=" + termParamId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId, response);

            if(termParamId != null && termParamId > 0)
                return getByTermParamId(termParamId, response);

            List<AcquirerTermParam> acquirerTermParamList = acquirerTermParamService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerTermParam acquirerTermParam : acquirerTermParamList) {
                acquirerTermParam = getLinks(acquirerTermParam, response);
            }


            return acquirerTermParamList;
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
            AcquirerTermParam acquirerTermParam = acquirerTermParamService.findByAcquirerTermParamId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerTermParam = getLinks(acquirerTermParam, response);
            return acquirerTermParam;
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
            AcquirerTermParam acquirerTermParam = acquirerTermParamService.findByAcquirerId(acquirerId);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerTermParam = getLinks(acquirerTermParam, response);
            return acquirerTermParam;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    public Object getByTermParamId(Long termParamId, HttpServletResponse response)
    {
        String fxnParams = "termParamId=" + termParamId + ",HttpServletResponse=" + response.toString();
        try
        {
            List<AcquirerTermParam> acquirerTermParamList = acquirerTermParamService.findByTermParamId(termParamId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerTermParam acquirerTermParam : acquirerTermParamList) {
                acquirerTermParam = getLinks(acquirerTermParam, response);
            }
            return acquirerTermParamList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private AcquirerTermParam getLinks(AcquirerTermParam merchantTerminal, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerTermParamController.class).slash(merchantTerminal.getAcquirerTermParamId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(merchantTerminal.getTermParamId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("termparam");
        merchantTerminal.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(merchantTerminal.getAcquirerId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("acquirer");
        merchantTerminal.add(link1);

        return merchantTerminal;
    }



}
