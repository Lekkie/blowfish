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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Object create(@RequestBody AcquirerTermParam acquirerTermParam)
    {
        try{
            acquirerTermParamService.create(acquirerTermParam);
            acquirerTermParam = getLinks(acquirerTermParam);

            return new ResponseEntity<Object>("", HttpStatus.CREATED);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerTermParam acquirerTermParam)
    {
        try{
            if(acquirerTermParam == null)
                throw new Exception();

            acquirerTermParam.setAcquirerTermParamId(id);
            acquirerTermParam = acquirerTermParamService.update(acquirerTermParam);
            acquirerTermParam = getLinks(acquirerTermParam);

            return new ResponseEntity<Object>(acquirerTermParam, HttpStatus.OK);
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
            acquirerTermParamService.delete(id);
            return new ResponseEntity<Object>("", HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="termParamId", required = false) Long termParamId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", termParamId=" + termParamId;
        try
        {
            if(id != null && id > 0)
                return getById(id);

            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId);

            if(termParamId != null && termParamId > 0)
                return getByTermParamId(termParamId);

            List<AcquirerTermParam> acquirerTermParamList = acquirerTermParamService.findAll();
            for (AcquirerTermParam acquirerTermParam : acquirerTermParamList) {
                acquirerTermParam = getLinks(acquirerTermParam);
            }


            return new ResponseEntity<Object>(acquirerTermParamList, HttpStatus.OK);
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
            AcquirerTermParam acquirerTermParam = acquirerTermParamService.findByAcquirerTermParamId(id);
            acquirerTermParam = getLinks(acquirerTermParam);

            return new ResponseEntity<Object>(acquirerTermParam, HttpStatus.OK);
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
            AcquirerTermParam acquirerTermParam = acquirerTermParamService.findByAcquirerId(acquirerId);
            acquirerTermParam = getLinks(acquirerTermParam);

            return new ResponseEntity<Object>(acquirerTermParam, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public Object getByTermParamId(Long termParamId)
    {
        String fxnParams = "termParamId=" + termParamId;
        try
        {
            List<AcquirerTermParam> acquirerTermParamList = acquirerTermParamService.findByTermParamId(termParamId);
            for (AcquirerTermParam acquirerTermParam : acquirerTermParamList) {
                acquirerTermParam = getLinks(acquirerTermParam);
            }

            return new ResponseEntity<Object>(acquirerTermParamList, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    private AcquirerTermParam getLinks(AcquirerTermParam merchantTerminal){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerTermParamController.class).slash(merchantTerminal.getAcquirerTermParamId()).withSelfRel();
        merchantTerminal.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(merchantTerminal.getTermParamId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("termparam");
        merchantTerminal.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(merchantTerminal.getAcquirerId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("acquirer");
        merchantTerminal.add(link1);

        return merchantTerminal;
    }



}
