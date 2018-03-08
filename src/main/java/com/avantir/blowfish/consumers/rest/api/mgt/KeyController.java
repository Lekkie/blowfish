package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Endpoint;
import com.avantir.blowfish.model.Key;
import com.avantir.blowfish.services.KeyService;
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
@RequestMapping(value = "api/v1/keys", produces = "application/hal+json")
public class KeyController {


    private static final Logger logger = LoggerFactory.getLogger(KeyController.class);
    @Autowired
    KeyService keyService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Key key, HttpServletResponse response)
    {
        try{
            keyService.create(key);
            response.setStatus(HttpServletResponse.SC_CREATED);
            key = getLinks(key, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Key newKey, HttpServletResponse response)
    {
        try{
            if(newKey == null)
                throw new Exception();

            newKey.setKeyId(id);
            newKey = keyService.update(newKey);
            response.setStatus(HttpServletResponse.SC_OK);
            newKey = getLinks(newKey, response);
            return newKey;
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
            keyService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);
            List<Key> keyList = keyService.findAll();
            for (Key key : keyList) {
                key = getLinks(key, response);
            }

            response.setStatus(HttpServletResponse.SC_OK);

            return keyList;
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
            Key key = keyService.findByKeyId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            key = getLinks(key, response);
            return key;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private Key getLinks(Key key, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(KeyController.class).slash(key.getKeyId()).withSelfRel();
        key.add(selfLink);

        return key;
    }


}
