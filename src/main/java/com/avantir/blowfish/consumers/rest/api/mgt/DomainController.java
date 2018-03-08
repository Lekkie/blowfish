package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.services.DomainService;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lekanomotayo on 06/02/2018.
 */

@RestController
@RequestMapping(value = "api/v1/domains", produces = "application/hal+json")
public class DomainController {


    private static final Logger logger = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    DomainService domainService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Domain domain, HttpServletResponse response)
    {
        try{
            domainService.create(domain);
            Link selfLink = ControllerLinkBuilder.linkTo(DomainController.class).slash(domain.getDomainId()).withSelfRel();
            domain.add(selfLink);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return domain;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Domain newDomain, HttpServletResponse response)
    {
        try{
            if(newDomain == null)
                throw new Exception();

            newDomain.setDomainId(id);
            newDomain = domainService.update(newDomain);
            response.setStatus(HttpServletResponse.SC_OK);
            Link selfLink = ControllerLinkBuilder.linkTo(DomainController.class).slash(newDomain.getDomainId()).withSelfRel();
            newDomain.add(selfLink);
            return newDomain;
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
            domainService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object get(@PathVariable("id") long id, HttpServletResponse response)
    {
        try
        {
            Domain domain = domainService.findByDomainId(id);
            Link selfLink = ControllerLinkBuilder.linkTo(DomainController.class).slash(domain.getDomainId()).withSelfRel();
            domain.add(selfLink);
            response.setStatus(HttpServletResponse.SC_OK);
            return domain;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
