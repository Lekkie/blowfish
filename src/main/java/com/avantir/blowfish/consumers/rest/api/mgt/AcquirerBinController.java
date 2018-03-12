package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.services.AcquirerBinService;
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
@RequestMapping(value = "api/v1/acquirers/bins", produces = "application/hal+json")
public class AcquirerBinController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerBinController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerBinService acquirerBinService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody AcquirerBin acquirerBin, HttpServletResponse response)
    {
        try{
            acquirerBinService.create(acquirerBin);
            response.setStatus(HttpServletResponse.SC_CREATED);
            acquirerBin = getLinks(acquirerBin, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody AcquirerBin newAcquirerBin, HttpServletResponse response)
    {
        try{
            if(newAcquirerBin == null)
                throw new Exception();

            newAcquirerBin.setAcquirerBinId(id);
            newAcquirerBin = acquirerBinService.update(newAcquirerBin);
            response.setStatus(HttpServletResponse.SC_OK);
            newAcquirerBin = getLinks(newAcquirerBin, response);
            return newAcquirerBin;
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
            acquirerBinService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="binId", required = false) Long binId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", binId=" + binId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);


            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId, response);

            if(binId != null && binId > 0)
                return getByBinId(binId, response);

            List<AcquirerBin> acquirerBinList = acquirerBinService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerBin acquirerBin : acquirerBinList) {
                acquirerBin = getLinks(acquirerBin, response);
            }

            return acquirerBinList;
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
            AcquirerBin acquirerBin = acquirerBinService.findByAcquirerBinId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerBin = getLinks(acquirerBin, response);
            return acquirerBin;
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
            List<AcquirerBin> acquirerBinList = acquirerBinService.findByAcquirerId(acquirerId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (AcquirerBin acquirerBin : acquirerBinList) {
                acquirerBin = getLinks(acquirerBin, response);
            }
            return acquirerBinList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByBinId(Long binId, HttpServletResponse response)
    {
        String fxnParams = "binId=" + binId + ",HttpServletResponse=" + response.toString();
        try
        {
            AcquirerBin acquirerBin = acquirerBinService.findByBinId(binId);
            response.setStatus(HttpServletResponse.SC_OK);
            acquirerBin = getLinks(acquirerBin, response);
            return acquirerBin;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }



    private AcquirerBin getLinks(AcquirerBin acquirerBin, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerBinController.class).slash(acquirerBin.getAcquirerBinId()).withSelfRel();
        acquirerBin.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(acquirerBin.getAcquirerId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("acquirer");
        acquirerBin.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(BinController.class).getById(acquirerBin.getBinId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("bin");
        acquirerBin.add(link1);

        return acquirerBin;
    }

}
