package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.consumers.rest.model.Error;
import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.services.DomainService;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lekanomotayo on 06/02/2018.
 */

@RestController
@RequestMapping("/domains")
public class DomainApi {


    private static final Logger logger = LoggerFactory.getLogger(DomainApi.class);

    @Autowired
    DomainService domainService;


    @CachePut(cacheNames="domain", key="#id")
    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object create(@RequestBody Domain domain, HttpServletResponse response)
    {
        try{
            domainService.create(domain);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH,
            consumes = "application/json",
            value = "/{id}",
            produces = "application/json")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Domain newDomain, HttpServletResponse response)
    {
        try{
            if(newDomain == null)
                throw new Exception();

            newDomain.setId(id);
            newDomain = domainService.update(newDomain);
            response.setStatus(HttpServletResponse.SC_OK);
            return newDomain;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
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
            domainService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/{id}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object get(@PathVariable("id") long id, HttpServletResponse response)
    {
        try
        {
            Domain domain = domainService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return domain;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
