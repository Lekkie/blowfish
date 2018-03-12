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
            response.setStatus(HttpServletResponse.SC_CREATED);
            bin = getLinks(bin, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
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
            newBin = getLinks(newBin, response);
            return newBin;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
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
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET,
            headers = "Accept=application/json")
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

            List<Bin> binList = binService.findAll();
            for (Bin bin : binList) {
                bin = getLinks(bin, response);
            }

            response.setStatus(HttpServletResponse.SC_OK);

            return binList;
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
    public Object getById(@PathVariable long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            Bin bin = binService.findByBinId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            bin = getLinks(bin, response);
            return bin;
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
            Bin bin = binService.findByCode(code);
            response.setStatus(HttpServletResponse.SC_OK);
            bin = getLinks(bin, response);
            return bin;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    private Bin getLinks(Bin terminal, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(BinController.class).slash(terminal.getBinId()).withSelfRel();
        terminal.add(selfLink);

        return terminal;
    }


}
