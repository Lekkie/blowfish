package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Endpoint;
import com.avantir.blowfish.services.EndpointService;
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
@RequestMapping(value = "api/v1/endpoints", produces = "application/hal+json")
public class EndpointController {


    private static final Logger logger = LoggerFactory.getLogger(EndpointController.class);
    @Autowired
    EndpointService endpointService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Endpoint endpoint, HttpServletResponse response)
    {
        try{
            endpointService.create(endpoint);
            response.setStatus(HttpServletResponse.SC_CREATED);
            endpoint = getLinks(endpoint, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Endpoint newEndpoint, HttpServletResponse response)
    {
        try{
            if(newEndpoint == null)
                throw new Exception();

            newEndpoint.setEndpointId(id);
            newEndpoint = endpointService.update(newEndpoint);
            response.setStatus(HttpServletResponse.SC_OK);
            newEndpoint = getLinks(newEndpoint, response);
            return newEndpoint;
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
            endpointService.delete(id);
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
            List<Endpoint> endpointList = endpointService.findAll();
            for (Endpoint endpoint : endpointList) {
                endpoint = getLinks(endpoint, response);
            }

            response.setStatus(HttpServletResponse.SC_OK);

            return endpointList;
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
            Endpoint endpoint = endpointService.findByEndpointId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            endpoint = getLinks(endpoint, response);
            return endpoint;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private Endpoint getLinks(Endpoint endpoint, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(EndpointController.class).slash(endpoint.getEndpointId()).withSelfRel();
        endpoint.add(selfLink);

        return endpoint;
    }


}
