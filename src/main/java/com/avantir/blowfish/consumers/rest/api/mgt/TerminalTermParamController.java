package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.TerminalTermParam;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.services.TerminalTermParamService;
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
@RequestMapping(value = "api/v1/terminals/termparams", produces = "application/hal+json")
public class TerminalTermParamController {


    private static final Logger logger = LoggerFactory.getLogger(TerminalTermParamController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    TerminalTermParamService terminalTermParamService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody TerminalTermParam terminalTermParam, HttpServletResponse response)
    {
        try{
            terminalTermParamService.create(terminalTermParam);
            response.setStatus(HttpServletResponse.SC_CREATED);
            terminalTermParam = getLinks(terminalTermParam, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody TerminalTermParam newTerminalTermParam, HttpServletResponse response)
    {
        try{
            if(newTerminalTermParam == null)
                throw new Exception();

            newTerminalTermParam.setTerminalTermParamId(id);
            newTerminalTermParam = terminalTermParamService.update(newTerminalTermParam);
            response.setStatus(HttpServletResponse.SC_OK);
            newTerminalTermParam = getLinks(newTerminalTermParam, response);
            return newTerminalTermParam;
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
            terminalTermParamService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="termParamId", required = false) Long termParamId, @RequestHeader(value="terminalId", required = false) Long terminalId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", termParamId=" + termParamId + ", terminalId=" + terminalId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(termParamId != null && termParamId > 0)
                return getByTermParamId(termParamId, response);

            if(terminalId != null && terminalId > 0)
                return getByTerminalId(terminalId, response);

            List<TerminalTermParam> merchantTerminalList = terminalTermParamService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (TerminalTermParam merchantTerminal : merchantTerminalList) {
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


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            TerminalTermParam terminalTermParam = terminalTermParamService.findByTerminalTermParamId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            terminalTermParam = getLinks(terminalTermParam, response);
            return terminalTermParam;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByTerminalId(Long terminalId, HttpServletResponse response)
    {
        String fxnParams = "terminalId=" + terminalId + ",HttpServletResponse=" + response.toString();
        try
        {
            TerminalTermParam merchantTerminal = terminalTermParamService.findByTerminalId(terminalId);
            response.setStatus(HttpServletResponse.SC_OK);
            merchantTerminal = getLinks(merchantTerminal, response);
            return merchantTerminal;
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
            List<TerminalTermParam> terminalTermParamList = terminalTermParamService.findByTermParamId(termParamId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (TerminalTermParam terminalTermParam : terminalTermParamList) {
                terminalTermParam = getLinks(terminalTermParam, response);
            }
            return terminalTermParamList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private TerminalTermParam getLinks(TerminalTermParam terminalTermParam, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(TerminalTermParamController.class).slash(terminalTermParam.getTerminalTermParamId()).withSelfRel();
        terminalTermParam.add(selfLink);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(TermParamController.class).getById(terminalTermParam.getTermParamId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("termparam");
        terminalTermParam.add(link1);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(TerminalController.class).getById(terminalTermParam.getTerminalId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("terminal");
        terminalTermParam.add(link2);

        return terminalTermParam;
    }



}
