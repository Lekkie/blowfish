package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Terminal;
import com.avantir.blowfish.model.TerminalTermParam;
import com.avantir.blowfish.services.TerminalService;
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
@RequestMapping(value = "api/v1/terminals", produces = "application/hal+json")
public class TerminalsController {


    private static final Logger logger = LoggerFactory.getLogger(TerminalsController.class);
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalTermParamService terminalTerminalParameterService;
    @Autowired
    TerminalTermParamsController terminalTermParamsController;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Terminal terminal, HttpServletResponse response)
    {
        try{
            terminalService.create(terminal);
            response.setStatus(HttpServletResponse.SC_CREATED);
            terminal = getLinks(terminal, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH,
            consumes = "application/json",
            value = "/{id}",
            produces = "application/json")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Terminal newTerminal, HttpServletResponse response)
    {
        try{
            if(newTerminal == null)
                throw new Exception();

            newTerminal.setTerminalId(id);
            newTerminal = terminalService.update(newTerminal);
            response.setStatus(HttpServletResponse.SC_OK);
            newTerminal = getLinks(newTerminal, response);
            return newTerminal;
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
            terminalService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="deviceSerialNo", required = false) String deviceSerialNo, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", deviceSerialNo=" + deviceSerialNo + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(deviceSerialNo != null && !deviceSerialNo.isEmpty())
                return getByDeviceSerialNo(deviceSerialNo, response);

            List<Terminal> terminalList = terminalService.findAll();
            for (Terminal terminal : terminalList) {
                terminal = getLinks(terminal, response);
            }

            response.setStatus(HttpServletResponse.SC_OK);

            return terminalList;
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
            Terminal terminal = terminalService.findByTerminalId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            terminal = getLinks(terminal, response);
            return terminal;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByDeviceSerialNo(String deviceSerialNo, HttpServletResponse response)
    {
        String fxnParams = "deviceSerialNo=" + deviceSerialNo + ",HttpServletResponse=" + response.toString();
        try
        {
            Terminal terminal = terminalService.findBySerialNo(deviceSerialNo);
            response.setStatus(HttpServletResponse.SC_OK);
            terminal = getLinks(terminal, response);
            return terminal;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(value = "/{terminalId}/termparams", method = RequestMethod.GET)
    public Object getTerminalTermParamsForTerminal(@PathVariable long terminalId, HttpServletResponse response) {
        return terminalTermParamsController.getByTerminalId(terminalId, response);
    }


    private Terminal getLinks(Terminal terminal, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(TerminalsController.class).slash(terminal.getTerminalId()).withSelfRel();
        terminal.add(selfLink);

        if (terminalTerminalParameterService.findByTerminalId(terminal.getTerminalId()) != null) {
            Object methodLinkBuilder = ControllerLinkBuilder.methodOn(TerminalsController.class).getTerminalTermParamsForTerminal(terminal.getTerminalId(), response);
            Link link = ControllerLinkBuilder.linkTo(methodLinkBuilder).withRel("allTermParams");
            terminal.add(link);
        }
        return terminal;
    }


}
