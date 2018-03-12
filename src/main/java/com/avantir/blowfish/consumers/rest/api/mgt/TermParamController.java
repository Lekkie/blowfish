package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.consumers.rest.model.Parameter;
import com.avantir.blowfish.exceptions.AcquirerMerchantNotLinkedException;
import com.avantir.blowfish.exceptions.BlowfishException;
import com.avantir.blowfish.exceptions.MerchantTerminalNotLinkedException;
import com.avantir.blowfish.exceptions.TerminalNotSupportedException;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.KeyUtil;
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
@RequestMapping(value = "api/v1/termparams", produces = "application/hal+json")
public class TermParamController {


    private static final Logger logger = LoggerFactory.getLogger(TermParamController.class);

    @Autowired
    TermParamService termParamService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    AcquirerService acquirerService;
    @Autowired
    EndpointService endpointService;
    @Autowired
    KeyService keyService;
    @Autowired
    TerminalTermParamService terminalTerminalParameterService;
    @Autowired
    MerchantTermParamService merchantTerminalParameterService;
    @Autowired
    AcquirerTermParamService acquirerTerminalParameterService;
    @Autowired
    MerchantTerminalService merchantTerminalService;
    @Autowired
    AcquirerMerchantService acquirerMerchantService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    DomainService domainService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody TermParam termParam, HttpServletResponse response)
    {
        try{
            termParamService.create(termParam);
            response.setStatus(HttpServletResponse.SC_CREATED);
            termParam = getLinks(termParam, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody TermParam newTermParam, HttpServletResponse response)
    {
        try{
            if(newTermParam == null)
                throw new Exception();

            newTermParam.setTermParamId(id);
            newTermParam = termParamService.update(newTermParam);
            response.setStatus(HttpServletResponse.SC_OK);
            newTermParam = getLinks(newTermParam, response);
            return newTermParam;
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
            termParamService.delete(id);
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
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="deviceSerialNo", required = false) String deviceSerialNo, @RequestHeader(value="devicePublicKey", required = false) String devicePublicKey, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", deviceSerialNo=" + deviceSerialNo + ",devicePublicKey=" + devicePublicKey + ",HttpServletResponse=" + response.toString();
        try
        {
            //String token = MDC.get(Slf4jMDCFilterConfig.DEFAULT_MDC_UUID_TOKEN_KEY);
            logger.debug(fxnParams);
            if(id != null && id > 0)
                return getById(id, response);

            if(deviceSerialNo != null && !deviceSerialNo.isEmpty())
                return getByDeviceSerialNo(deviceSerialNo, devicePublicKey, response);

            List<TermParam> termParamList = termParamService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (TermParam termParam : termParamList) {
                termParam = getLinks(termParam, response);
            }

            return termParamList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error("Error occurred - {}", log.toString());
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
            TermParam termParam = termParamService.findByTermParamId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            termParam = getLinks(termParam, response);
            return termParam;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    /*
    @RequestMapping(method= RequestMethod.GET,
            value = "/{deviceSerialNo}",
            headers = "Accept=application/json")
    @ResponseBody
    */
    public Object getByDeviceSerialNo(String deviceSerialNo, String devicePublicKey, HttpServletResponse response)
    {
        String fxnParams = "deviceSerialNo=" + deviceSerialNo + ",devicePublicKey=" + devicePublicKey + ",HttpServletResponse=" + response.toString();
        try
        {
            Domain domain = domainService.findByCode("OWNER");
            if(domain == null || domain.getName().isEmpty())
                throw new BlowfishException("1403", "Missing PTSP");

            if(devicePublicKey == null || devicePublicKey.isEmpty())
                throw new BlowfishException("1403", "Expecting a public key for this terminal");

            Terminal terminal = terminalService.findBySerialNo(deviceSerialNo);
            if(terminal == null)
                throw new TerminalNotSupportedException("Terminal has not been provisioned yet (Missing Terminal)");
            TermParam terminalParameter = null;
            MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminal.getTerminalId());
            if(merchantTerminal == null)
                throw new MerchantTerminalNotLinkedException("Terminal has not been linked with any merchant (Missing Merchant)");
            AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(merchantTerminal.getMerchantId());
            if(acquirerMerchant == null)
                throw new AcquirerMerchantNotLinkedException("Merchant has not been linked with  an acquirer (Missing Acquirer)");

            TerminalTermParam terminalTerminalParameter = terminalTerminalParameterService.findByTerminalId(terminal.getTerminalId());
            if(terminalTerminalParameter == null){
                MerchantTermParam merchantTerminalParameter = merchantTerminalParameterService.findByMerchantId(merchantTerminal.getMerchantTerminalId());
                if(merchantTerminalParameter == null){
                    AcquirerTermParam acquirerTerminalParameter = acquirerTerminalParameterService.findByAcquirerId(acquirerMerchant.getAcquirerId());
                    if(acquirerTerminalParameter != null){
                        terminalParameter = termParamService.findByTermParamId(acquirerTerminalParameter.getTermParamId());
                    }
                }
                else{
                    terminalParameter = termParamService.findByTermParamId(merchantTerminalParameter.getTermParamId());
                }
            }
            else{
                terminalParameter = termParamService.findByTermParamId(terminalTerminalParameter.getTermParamId());
            }

            if(terminalParameter == null)
                throw new BlowfishException("1401", "No Terminal Parameter configured");

            Acquirer acquirer = acquirerService.findByAcquirerId(acquirerMerchant.getAcquirerId());
            Merchant merchant = merchantService.findByMerchantId(acquirerMerchant.getMerchantId());
            Endpoint endpoint = endpointService.findByEndpointId(terminalParameter.getTmsEndpointId());
            Key ctmkKey = keyService.findByKeyId(terminalParameter.getCtmkKeyId());
            Key bdkKey = keyService.findByKeyId(terminalParameter.getBdkKeyId());
            //  decrypt ctmkKey.getData(), before re-encrypting under RSA
            String base64Ctmk = KeyUtil.encryptWithRSA(devicePublicKey, ctmkKey.getData());
            if(base64Ctmk == null)
                throw new BlowfishException("1404", "Unable to encrypt ctmk");
            String base64Bdk = KeyUtil.encryptWithRSA(devicePublicKey, bdkKey.getData());
            if(base64Bdk == null)
                throw new BlowfishException("1405", "Unable to encrypt bdk");

            Parameter parameter = new Parameter();
            parameter.setMerchantName(merchant.getName());
            parameter.setDesc(terminalParameter.getDescription());
            parameter.setName(terminalParameter.getName());
            parameter.setForceOnline(terminalParameter.isForceOnline());
            parameter.setSupportDefaultTDOL(terminalParameter.isSupportDefaultTDOL());
            parameter.setSupportDefaultDDOL(terminalParameter.isSupportDefaultDDOL());
            parameter.setSupportPSESelection(terminalParameter.isSupportPSESelection());
            parameter.setIccData(terminalParameter.getIccData());
            parameter.setKeyDownlIntervalInMin(terminalParameter.getKeyDownloadIntervalInMin());
            parameter.setKeyDownlTimeInMin(terminalParameter.getKeyDownloadTimeInMin());
            parameter.setPosDataCode(terminalParameter.getPosDataCode());
            parameter.setTermCap(terminalParameter.getTerminalCapabilities());
            parameter.setTermExCap(terminalParameter.getTerminalExtraCapabilities());
            parameter.setTermType(terminalParameter.getTerminalType());
            parameter.setTransCurr(terminalParameter.getTransactionCurrency());
            parameter.setTransRefCurr(terminalParameter.getTransactionReferenceCurrency());
            parameter.setTransCurrExp(terminalParameter.getTransactionCurrencyExponent());
            parameter.setRefCurrExp(terminalParameter.getReferenceCurrencyExponent());
            parameter.setRefCurrConv(terminalParameter.getReferenceCurrencyConversion());
            parameter.setUseLocalNetworkConfig(terminalParameter.isUseLocalNetworkConfig());
            parameter.setStatus(terminalParameter.getStatus());
            parameter.setAcquirer(acquirer.getBinCode());
            parameter.setTmsHost(endpoint.getHostname());
            parameter.setTmsIp(endpoint.getIp());
            parameter.setTmsPort(endpoint.getPort());
            parameter.setTmsTimeout(endpoint.getTimeout());
            parameter.setTmsSsl(endpoint.isSslEnabled());
            parameter.setCtmk(base64Ctmk);
            parameter.setBdk(base64Bdk);
            parameter.setBdkChkDigit(bdkKey.getCheckDigit());
            parameter.setCtmkChkDigit(ctmkKey.getCheckDigit());
            parameter.setTerminalId(terminal.getCode());
            parameter.setPtsp(domain.getName());

            response.setStatus(HttpServletResponse.SC_OK);
            return parameter;
        }
        catch(BlowfishException ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(ex.getCode(), ex.getMessage());
        }
    }



    private TermParam getLinks(TermParam terminalParameter, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(TermParamController.class).slash(terminalParameter.getTermParamId()).withSelfRel();
        terminalParameter.add(selfLink);

        Object methodLink1 = ControllerLinkBuilder.methodOn(EndpointController.class).getById(terminalParameter.getTmsEndpointId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(methodLink1).withRel("endpoint");
        terminalParameter.add(link1);

        Object methodLink2 = ControllerLinkBuilder.methodOn(KeyController.class).getById(terminalParameter.getCtmkKeyId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(methodLink2).withRel("ctmkKey");
        terminalParameter.add(link2);

        Object methodLink3 = ControllerLinkBuilder.methodOn(KeyController.class).getById(terminalParameter.getBdkKeyId(), response);
        Link link3 = ControllerLinkBuilder.linkTo(methodLink3).withRel("bdkKey");
        terminalParameter.add(link3);

        return terminalParameter;
    }

}
