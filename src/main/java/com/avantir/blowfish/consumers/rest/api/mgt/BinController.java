package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.services.BinService;
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
@RequestMapping(value = "api/v1/bins", produces = "application/hal+json")
public class BinController {


    private static final Logger logger = LoggerFactory.getLogger(BinController.class);
    @Autowired
    BinService binService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Bin bin, HttpServletResponse response)
    {
        try{
            binService.create(bin);
            bin = getLinks(bin);
            //response.setStatus(HttpServletResponse.SC_CREATED);
            //return "";
            return new ResponseEntity<Object>("", HttpStatus.CREATED);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Bin newBin, HttpServletResponse response)
    {
        try{
            if(newBin == null)
                throw new Exception();

            newBin.setBinId(id);
            newBin = binService.update(newBin);
            response.setStatus(HttpServletResponse.SC_OK);
            newBin = getLinks(newBin);
            return new ResponseEntity<Object>(newBin, HttpStatus.OK);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE,
            consumes = "application/json",
            value = "/{id}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            binService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return new ResponseEntity<Object>("", HttpStatus.OK);
        }
        catch(Exception ex){
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.GET,
            headers = "Accept=application/json")
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

            List<Bin> binList = binService.findAll();
            for (Bin bin : binList) {
                bin = getLinks(bin);
            }

            //response.setStatus(HttpServletResponse.SC_OK);
            return new ResponseEntity<Object>(binList, HttpStatus.OK);
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
    public Object getById(@PathVariable long id)
    {
        String fxnParams = "id=" + id;
        try
        {
            Bin bin = binService.findByBinId(id);
            bin = getLinks(bin);

            //response.setStatus(HttpServletResponse.SC_OK);
            return new ResponseEntity<Object>(bin, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    public Object getByCode(String code)
    {
        String fxnParams = "code=" + code;
        try
        {
            Bin bin = binService.findByCode(code);
            bin = getLinks(bin);

            //response.setStatus(HttpServletResponse.SC_OK);
            return new ResponseEntity<Object>(bin, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<Object>(BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private Bin getLinks(Bin terminal){
        Link selfLink = ControllerLinkBuilder.linkTo(BinController.class).slash(terminal.getBinId()).withSelfRel();
        terminal.add(selfLink);

        return terminal;
    }


}
