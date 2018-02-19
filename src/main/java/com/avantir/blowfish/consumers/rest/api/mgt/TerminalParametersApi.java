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
import java.util.List;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping("/parameters")
public class TerminalParametersApi {


    private static final Logger logger = LoggerFactory.getLogger(TerminalParametersApi.class);

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
    @Autowired
    TerminalTerminalParameterService terminalTerminalParameterService;
    @Autowired
    MerchantTerminalParameterService merchantTerminalParameterService;
    @Autowired
    AcquirerTerminalParameterService acquirerTerminalParameterService;
    @Autowired
    MerchantTerminalService merchantTerminalService;
    @Autowired
    AcquirerMerchantService acquirerMerchantService;
    @Autowired
    MerchantService merchantService;


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

            List<TerminalParameter> terminalParameterList = terminalParameterService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            return terminalParameterList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    /*
    @RequestMapping(method= RequestMethod.GET,
            value = "/{id}",
            headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable("id") long id, HttpServletResponse response)
    */
    public Object getById(long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            TerminalParameter terminalParameter = terminalParameterService.findById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return terminalParameter;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    /*
    @RequestMapping(method= RequestMethod.GET,
            value = "/{deviceSerialNo}",
            headers = "Accept=application/json")
    @ResponseBody
    */
    public Object getByDeviceSerialNo(String deviceSerialNo, HttpServletResponse response)
    {
        String fxnParams = "deviceSerialNo=" + deviceSerialNo + ",HttpServletResponse=" + response.toString();
        try
        {
            Terminal terminal = terminalService.findByDeviceSerialNo(deviceSerialNo);
            TerminalParameter terminalParameter = null;
            MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminal.getId());
            if(merchantTerminal == null)
                throw new Exception("Terminal has not been provisioned yet (Missing Merchant)");
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(merchantTerminal.getMerchantId());
            if(acquirerMerchant == null)
                throw new Exception("Terminal has not been provisioned yet (Missing Acquirer)");

            TerminalTerminalParameter terminalTerminalParameter = terminalTerminalParameterService.findByTerminalId(terminal.getId());
            if(terminalTerminalParameter == null){
                MerchantTerminalParameter merchantTerminalParameter = merchantTerminalParameterService.findByMerchantId(merchantTerminal.getId());
                if(merchantTerminalParameter == null){
                    AcquirerTerminalParameter acquirerTerminalParameter = acquirerTerminalParameterService.findByAcquirerId(acquirerMerchant.getAcquirerId());
                    if(acquirerTerminalParameter != null){
                        terminalParameter = terminalParameterService.findById(acquirerTerminalParameter.getTerminalParameterId());
                    }
                }
                else{
                    terminalParameter = terminalParameterService.findById(merchantTerminalParameter.getTerminalParameterId());
                }
            }
            else{
                terminalParameter = terminalParameterService.findById(terminalTerminalParameter.getTerminalParameterId());
            }

            if(terminalParameter == null)
                throw new Exception("No Terminal Parameter configured");

            //terminalParameterService.findById(terminal.getId());
            Acquirer acquirer = acquirerService.findById(acquirerMerchant.getAcquirerId());
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
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new Error(IsoUtil.RESP_06, ex.getMessage());
        }
    }

}
