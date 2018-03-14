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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/acquirers/bins", produces = "application/hal+json")
@PreAuthorize("permitAll")
//@PreAuthorize("hasAuthority('ADMIN_USER')")
//@PreAuthorize("hasRole('ADMIN_USER')")
public class AcquirerBinController {


    private static final Logger logger = LoggerFactory.getLogger(AcquirerBinController.class);
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    AcquirerBinService acquirerBinService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    //public ResponseEntity<?> create(@RequestBody AcquirerBin acquirerBin, HttpServletResponse response)
    public ResponseEntity<?> create(@RequestBody AcquirerBin acquirerBin)
    {
        try{
            acquirerBinService.create(acquirerBin);
            acquirerBin = getLinks(acquirerBin);
            //response.setStatus(HttpServletResponse.SC_CREATED);
            return new ResponseEntity<Object>("", HttpStatus.CREATED);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AcquirerBin newAcquirerBin)
    {
        try{
            if(newAcquirerBin == null)
                throw new Exception();

            newAcquirerBin.setAcquirerBinId(id);
            newAcquirerBin = acquirerBinService.update(newAcquirerBin);
            newAcquirerBin = getLinks(newAcquirerBin);
            //response.setStatus(HttpServletResponse.SC_OK);
            //return newAcquirerBin;
            return new ResponseEntity<Object>(newAcquirerBin, HttpStatus.OK);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            acquirerBinService.delete(id);
            //response.setStatus(HttpServletResponse.SC_OK);
            //return "";
            return new ResponseEntity<Object>("", HttpStatus.OK);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="acquirerId", required = false) Long acquirerId, @RequestHeader(value="binId", required = false) Long binId)
    {
        String fxnParams = "id=" + id + ", acquirerId=" + acquirerId + ", binId=" + binId;
        try
        {
            if(id != null && id > 0)
                return getById(id);


            if(acquirerId != null && acquirerId > 0)
                return getByAcquirerId(acquirerId);

            if(binId != null && binId > 0)
                return getByBinId(binId);

            List<AcquirerBin> acquirerBinList = acquirerBinService.findAll();
            for (AcquirerBin acquirerBin : acquirerBinList) {
                acquirerBin = getLinks(acquirerBin);
            }

            //response.setStatus(HttpServletResponse.SC_OK);
            //return acquirerBinList;
            return new ResponseEntity<Object>(acquirerBinList, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
            AcquirerBin acquirerBin = acquirerBinService.findByAcquirerBinId(id);
            acquirerBin = getLinks(acquirerBin);
            //response.setStatus(HttpServletResponse.SC_OK);
            //return acquirerBin;
            return new ResponseEntity<Object>(acquirerBin, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    public Object getByAcquirerId(Long acquirerId)
    {
        String fxnParams = "acquirerId=" + acquirerId;
        try
        {
            List<AcquirerBin> acquirerBinList = acquirerBinService.findByAcquirerId(acquirerId);
            for (AcquirerBin acquirerBin : acquirerBinList) {
                acquirerBin = getLinks(acquirerBin);
            }

            //response.setStatus(HttpServletResponse.SC_OK);
            //return acquirerBinList;
            return new ResponseEntity<Object>(acquirerBinList, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public Object getByBinId(Long binId)
    {
        String fxnParams = "binId=" + binId;
        try
        {
            AcquirerBin acquirerBin = acquirerBinService.findByBinId(binId);
            acquirerBin = getLinks(acquirerBin);
            //response.setStatus(HttpServletResponse.SC_OK);
            //return acquirerBin;
            return new ResponseEntity<Object>(acquirerBin, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    private AcquirerBin getLinks(AcquirerBin acquirerBin){
        Link selfLink = ControllerLinkBuilder.linkTo(AcquirerBinController.class).slash(acquirerBin.getAcquirerBinId()).withSelfRel();
        acquirerBin.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(AcquirerController.class).getById(acquirerBin.getAcquirerId());
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("acquirer");
        acquirerBin.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(BinController.class).getById(acquirerBin.getBinId());
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("bin");
        acquirerBin.add(link1);

        return acquirerBin;
    }

}
