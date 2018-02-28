package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.consumers.rest.model.Error;
import com.avantir.blowfish.consumers.rest.model.Parameter;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping("/params")
public class ParametersApi {


    private static final Logger logger = LoggerFactory.getLogger(DomainApi.class);

    @Autowired
    TerminalParameterService terminalParameterService;
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
            /*
            TerminalParameter terminalParameter = new TerminalParameter();
            terminalParameter.setDescription(parameter.getDesc());
            terminalParameter.setName(parameter.getName());
            terminalParameter.setForceOnline(parameter.isForceOnline());
            terminalParameter.setIccData(parameter.getIccData());
            terminalParameter.setKeyDownloadIntervalInMin(parameter.getKeyDownlIntervalInMin());
            terminalParameter.setKeyDownloadTimeInMin(parameter.getKeyDownlTimeInMin());
            terminalParameter.setPosDataCode(parameter.getPosDataCode());
            terminalParameter.setTerminalCapabilities(parameter.getTermCap());
            terminalParameter.setTerminalExtraCapabilities(parameter.getTermExCap());
            terminalParameter.setTerminalType(parameter.getTermType());
            terminalParameter.setTransactionCurrency(parameter.getTransCurr());
            terminalParameter.setTransactionReferenceCurrency(parameter.getTransRefCurr());
            terminalParameter.setStatus(parameter.getStatus());
            //Acquirer acquirer = acquirerService.findByCode(parameter.getAcquirer());
            //Endpoint endpoint = endpointService.findByIpPort(parameter.getTmsIp(), parameter.getTmsPort());
            //Key bdkKey = keyService.findById(0L);
            //Key ctmkKey = keyService.findById(0L);
            terminalParameter.setAcquirerId(parameter.getAcquirerId());
            terminalParameter.setTmsEndpointId(parameter.getEndpointId());
            terminalParameter.setBdkKeyId(parameter.getBdkKeyId());
            terminalParameter.setCtmkKeyId(parameter.getCtmkKeyId());
            */

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

            /*
            TerminalParameter terminalParameter = new TerminalParameter();
            terminalParameter.setDescription(parameter.getDesc());
            terminalParameter.setName(parameter.getName());
            terminalParameter.setForceOnline(parameter.isForceOnline());
            terminalParameter.setIccData(parameter.getIccData());
            terminalParameter.setKeyDownloadIntervalInMin(parameter.getKeyDownlIntervalInMin());
            terminalParameter.setKeyDownloadTimeInMin(parameter.getKeyDownlTimeInMin());
            terminalParameter.setPosDataCode(parameter.getPosDataCode());
            terminalParameter.setTerminalCapabilities(parameter.getTermCap());
            terminalParameter.setTerminalExtraCapabilities(parameter.getTermExCap());
            terminalParameter.setTerminalType(parameter.getTermType());
            terminalParameter.setTransactionCurrency(parameter.getTransCurr());
            terminalParameter.setTransactionReferenceCurrency(parameter.getTransRefCurr());
            terminalParameter.setStatus(parameter.getStatus());
            //Acquirer acquirer = acquirerService.findByCode(parameter.getAcquirer());
            //Endpoint endpoint = endpointService.findByIpPort(parameter.getTmsIp(), parameter.getTmsPort());
            //Key bdkKey = keyService.findById(0L);
            //Key ctmkKey = keyService.findById(0L);
            terminalParameter.setAcquirerId(parameter.getAcquirerId());
            terminalParameter.setTmsEndpointId(parameter.getEndpointId());
            terminalParameter.setBdkKeyId(parameter.getBdkKeyId());
            terminalParameter.setCtmkKeyId(parameter.getCtmkKeyId());
            */

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
            //terminalParameterService.delete(id);
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
            //Parameter parameter = terminalParameterService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
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
    public Object getByDeviceSerialNo(@PathVariable("deviceSerialNo") String deviceId, HttpServletResponse response)
    {
        try
        {
            //Parameter parameter = terminalParameterService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
