package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.consumers.rest.model.Error;
import com.avantir.blowfish.consumers.rest.model.Parameter;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping("/parameters")
public class TerminalParametersApi {


    private static final Logger logger = LoggerFactory.getLogger(DomainApi.class);

    @Autowired
    TerminalParameterService terminalParameterService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    EndpointService endpointService;
    @Autowired
    KeyService keyService;


    @RequestMapping(method= RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public Object create(@RequestBody TerminalParameter terminalParameter, HttpServletResponse response)
    {
        try{
            terminalParameterService.create(terminalParameter);
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
    public Object update(@PathVariable("id") long id, @RequestBody TerminalParameter newTerminalParameter, HttpServletResponse response)
    {
        try{
            if(newTerminalParameter == null)
                throw new Exception();

            newTerminalParameter.setId(id);
            newTerminalParameter = terminalParameterService.update(newTerminalParameter);
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
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
            terminalParameterService.delete(id);
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
            TerminalParameter terminalParameter = terminalParameterService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return terminalParameter;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(method= RequestMethod.GET,
            value = "/{deviceSerialNo}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object getByDeviceSerialNo(@PathVariable("deviceSerialNo") String deviceSerialNo, HttpServletResponse response)
    {
        try
        {
            Terminal terminal = terminalService.findByDeviceSerialNo(deviceSerialNo);
            TerminalParameter terminalParameter = terminalParameterService.findById(terminal.getId());
            Acquirer acquirer = acquirerService.findById(terminalParameter.getAcquirerId());
            Endpoint endpoint = endpointService.findById(terminalParameter.getTmsEndpointId());
            Key ctmkKey = keyService.findById(terminalParameter.getCtmkKeyId());
            Key bdkKey = keyService.findById(terminalParameter.getBdkKeyId());

            Parameter parameter = new Parameter();
            parameter.setDesc(terminalParameter.getDescription());
            parameter.setName(terminalParameter.getName());
            parameter.setForceOnline(terminalParameter.isForceOnline());
            parameter.setIccData(terminalParameter.getIccData());
            parameter.setKeyDownlIntervalInMin(terminalParameter.getKeyDownloadIntervalInMin());
            parameter.setKeyDownlTimeInMin(terminalParameter.getKeyDownloadTimeInMin());
            parameter.setPosDataCode(terminalParameter.getPosDataCode());
            parameter.setTermCap(terminalParameter.getTerminalCapabilities());
            parameter.setTermExCap(terminalParameter.getTerminalExtraCapabilities());
            parameter.setTermType(terminalParameter.getTerminalType());
            parameter.setTransCurr(terminalParameter.getTransactionCurrency());
            parameter.setTransRefCurr(terminalParameter.getTransactionReferenceCurrency());
            parameter.setStatus(terminalParameter.getStatus());
            parameter.setAcquirer(acquirer.getBinCode());
            parameter.setTmsIp(endpoint.getIp());
            parameter.setTmsPort(endpoint.getPort());
            parameter.setTmsTimeout(endpoint.getTimeout());
            parameter.setTmsSsl(endpoint.isSsl());
            parameter.setBdk(bdkKey.getData());
            parameter.setCtmk(ctmkKey.getData());
            parameter.setBdkChkDigit(bdkKey.getCheckDigit());
            parameter.setCtmkChkDigit(ctmkKey.getCheckDigit());

            response.setStatus(HttpServletResponse.SC_OK);
            return parameter;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
