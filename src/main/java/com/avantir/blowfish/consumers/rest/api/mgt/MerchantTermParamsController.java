package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.MerchantTermParam;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.MerchantTermParamService;
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
@RequestMapping(value = "api/v1/merchants/termparams", produces = "application/hal+json")
public class MerchantTermParamsController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantTermParamsController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantTermParamService merchantTermParamService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantTermParam merchantTermParam, HttpServletResponse response)
    {
        try{
            merchantTermParamService.create(merchantTermParam);
            response.setStatus(HttpServletResponse.SC_CREATED);
            merchantTermParam = getLinks(merchantTermParam, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody MerchantTermParam newMerchantTermParam, HttpServletResponse response)
    {
        try{
            if(newMerchantTermParam == null)
                throw new Exception();

            newMerchantTermParam.setMerchantTermParamId(id);
            newMerchantTermParam = merchantTermParamService.update(newMerchantTermParam);
            response.setStatus(HttpServletResponse.SC_OK);
            newMerchantTermParam = getLinks(newMerchantTermParam, response);
            return newMerchantTermParam;
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
            merchantTermParamService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchantId", required = false) Long merchantId, @RequestHeader(value="termParamId", required = false) Long termParamId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", merchant_id=" + merchantId + ", termParamId=" + termParamId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(merchantId != null && merchantId > 0)
                return getByMerchantId(merchantId, response);

            if(termParamId != null && termParamId > 0)
                return getByTermParamId(termParamId, response);

            List<MerchantTermParam> merchantTermParamList = merchantTermParamService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (MerchantTermParam merchantTermParam : merchantTermParamList) {
                merchantTermParam = getLinks(merchantTermParam, response);
            }


            return merchantTermParamList;
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
            MerchantTermParam merchantTermParam = merchantTermParamService.findByMerchantTermParamId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            merchantTermParam = getLinks(merchantTermParam, response);
            return merchantTermParam;
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
            MerchantTermParam merchantTermParam = merchantTermParamService.findByMerchantId(merchantId);
            response.setStatus(HttpServletResponse.SC_OK);
            merchantTermParam = getLinks(merchantTermParam, response);
            return merchantTermParam;
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
            List<MerchantTermParam> merchantTerminalList = merchantTermParamService.findByTermParamId(termParamId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (MerchantTermParam merchantTerminal : merchantTerminalList) {
                merchantTerminal = getLinks(merchantTerminal, response);
            }
            return merchantTerminalList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private MerchantTermParam getLinks(MerchantTermParam merchantTerminal, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantTermParamsController.class).slash(merchantTerminal.getMerchantTermParamId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TermParamsController.class).getById(merchantTerminal.getTermParamId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("termparam");
        merchantTerminal.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(MerchantsController.class).getById(merchantTerminal.getMerchantId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("merchant");
        merchantTerminal.add(link1);

        return merchantTerminal;
    }



}
