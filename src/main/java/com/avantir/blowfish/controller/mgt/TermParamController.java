package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.model.Parameter;
import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.services.*;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.utils.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    ErrorService errorService;
    @Autowired
    KeyManagementService keyManagementService;
    @Autowired
    KeyMapService keyMapService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody TermParam termParam)
    {
        Optional<TermParam> optionalNewAcq = Optional.ofNullable(termParam);
        termParam = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("TermParam"));

        Optional<TermParam> optional = termParamService.create(termParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.CREATED);
                })
                .orElseThrow(() -> new BlowfishEntityNotCreatedException("TermParam "));
        return responseEntity;
    }


    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody TermParam termParam)
    {
        Optional<TermParam> optionalNewAcq = Optional.ofNullable(termParam);
        termParam = optionalNewAcq.orElseThrow(() -> new BlowfishIllegalArgumentException("TermParam"));
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("TermParam Id"));

        termParam.setTermParamId(id);
        final long finalId = id;
        Optional<TermParam> optional = termParamService.update(termParam);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotUpdatedException("TermParam " + finalId));
        return responseEntity;
    }



    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id)
    {
        final long finalId = id;
        Optional<Long> optional = Optional.ofNullable(id);
        id = optional.orElseThrow(() -> new BlowfishIllegalArgumentException("TermParam Id " + finalId));

        termParamService.delete(id);
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="deviceSerialNo", required = false) String deviceSerialNo, @RequestHeader(value="devicePublicKey", required = false) String devicePublicKey)
    {
        String fxnParams = "id=" + id + ", deviceSerialNo=" + deviceSerialNo + ",devicePublicKey=" + devicePublicKey;
        if(Optional.ofNullable(id).isPresent())
            return getById(id);

        if(Optional.ofNullable(deviceSerialNo).isPresent()){
            Optional.ofNullable(devicePublicKey).orElseThrow(() -> new BlowfishIllegalArgumentException("Expecting a Public Key"));
            return getByDeviceSerialNo(deviceSerialNo, devicePublicKey);
        }

        Optional<List<TermParam>> optional = termParamService.findAll();
        List<TermParam> list = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("TermParams"));
        list = list.stream()
                .map(this::getLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }



    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id)
    {
        String fxnParams = "id=" + id;
        final long finalId = id;
        Optional<Long> optionalId = Optional.ofNullable(id);
        id = optionalId.orElseThrow(() -> new BlowfishIllegalArgumentException("TermParam Id " + finalId));

        Optional<TermParam> optional = termParamService.findByTermParamId(id);
        ResponseEntity responseEntity = optional.map(this::getLinks)
                .map(acq ->{
                    return new ResponseEntity<Object>(acq, HttpStatus.OK);
                })
                .orElseThrow(() -> new BlowfishEntityNotFoundException("TermParam " + finalId));
        return responseEntity;
    }


    /*
    @RequestMapping(method= RequestMethod.GET,
            value = "/{deviceSerialNo}",
            headers = "Accept=application/json")
    @ResponseBody
    */
    public Object getByDeviceSerialNo(String deviceSerialNo, String devicePublicKey)
    {
        String fxnParams = "deviceSerialNo=" + deviceSerialNo + ",devicePublicKey=" + devicePublicKey;
        Domain domain = domainService.findByCode("OWNER").orElseThrow(() -> new BlowfishRuntimeException(1403, "Missing PTSP Domain"));
        Terminal terminal = terminalService.findBySerialNo(deviceSerialNo).orElseThrow(() -> new TerminalNotSupportedException("Terminal has not been provisioned yet (Missing Terminal)"));
        MerchantTerminal merchantTerminal = merchantTerminalService.findByTerminalId(terminal.getTerminalId()).orElseThrow(() -> new MerchantTerminalNotLinkedException("Terminal has not been linked with any merchant (Missing Merchant)"));
        AcquirerMerchant acquirerMerchant = acquirerMerchantService.findByMerchantId(merchantTerminal.getMerchantId()).orElseThrow(() -> new AcquirerMerchantNotLinkedException("Merchant has not been linked with any acquirer (Missing Acquirer)"));
        Optional<TerminalTermParam> terminalTermParamOptional = terminalTerminalParameterService.findByTerminalId(terminal.getTerminalId());
        Optional<TermParam> optionalTermParam = Optional.empty();

        if(!terminalTermParamOptional.isPresent())
        {
            Optional<MerchantTermParam> merchantTermParamOptional = merchantTerminalParameterService.findByMerchantId(merchantTerminal.getMerchantTerminalId());
            if(!merchantTermParamOptional.isPresent()){
                Optional<AcquirerTermParam> optionalAcqTermParam = acquirerTerminalParameterService.findByAcquirerId(acquirerMerchant.getAcquirerId());
                AcquirerTermParam acquirerTerminalParameter = optionalAcqTermParam.orElseThrow(() -> new BlowfishRuntimeException(1401, "No Terminal Parameter configured"));
                optionalTermParam = termParamService.findByTermParamId(acquirerTerminalParameter.getTermParamId());
            }
            else{
                MerchantTermParam merchantTerminalParameter = merchantTermParamOptional.get();
                optionalTermParam = termParamService.findByTermParamId(merchantTerminalParameter.getTermParamId());
            }
        }
        else{
            TerminalTermParam terminalTermParam = terminalTermParamOptional.get();
            optionalTermParam = termParamService.findByTermParamId(terminalTermParam.getTermParamId());
        }

        TermParam terminalParameter = optionalTermParam.orElseThrow(() -> new BlowfishRuntimeException(ErrorType.ENTITY_NOT_FOUND.getCode(), "No Terminal Parameter configured"));
        Acquirer acquirer = acquirerService.findByAcquirerId(acquirerMerchant.getAcquirerId()).orElseThrow(() -> new AcquirerNotSupportedException("No Acquirer configured"));
        Merchant merchant = merchantService.findByMerchantId(acquirerMerchant.getMerchantId()).orElseThrow(() -> new MerchantNotSupportedException("Merchant not found"));
        Endpoint endpoint = endpointService.findByEndpointId(terminalParameter.getTmsEndpointId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Endpoint"));
        Key ctmkKey = keyService.findByKeyId(terminalParameter.getCtmkKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("CTMK Key"));
        Key bdkKey = keyService.findByKeyId(terminalParameter.getBdkKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("BDK Key"));

        String base64Ctmk = keyManagementService.exportZMKUnderLMKToUnderRsaKeyWithHSMInBase64(ctmkKey.getData(), devicePublicKey).orElseThrow(() -> new BlowfishRuntimeException(ErrorType.ENCRYPT_ERROR.getCode(), "Unable to encrypt ctmk"));
        String base64Bdk = keyManagementService.exportZMKUnderLMKToUnderRsaKeyWithHSMInBase64(bdkKey.getData(), devicePublicKey).orElseThrow(() -> new BlowfishRuntimeException(ErrorType.ENCRYPT_ERROR.getCode(), "Unable to encrypt bdk"));

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

        return new ResponseEntity<Object>(parameter, HttpStatus.OK);
    }



    public TermParam getLinks(TermParam terminalParameter){
        Link selfLink = ControllerLinkBuilder.linkTo(TermParamController.class).slash(terminalParameter.getTermParamId()).withSelfRel();
        if(!terminalParameter.hasLink(selfLink.getRel()))
            terminalParameter.add(selfLink);

        Object methodLink1 = ControllerLinkBuilder.methodOn(EndpointController.class).getById(terminalParameter.getTmsEndpointId());
        Link link1 = ControllerLinkBuilder.linkTo(methodLink1).withRel("endpoint");
        if(!terminalParameter.hasLink(link1.getRel()))
            terminalParameter.add(link1);

        Object methodLink2 = ControllerLinkBuilder.methodOn(KeyController.class).getById(terminalParameter.getCtmkKeyId());
        Link link2 = ControllerLinkBuilder.linkTo(methodLink2).withRel("ctmkKey");
        if(!terminalParameter.hasLink(link2.getRel()))
            terminalParameter.add(link2);

        Object methodLink3 = ControllerLinkBuilder.methodOn(KeyController.class).getById(terminalParameter.getBdkKeyId());
        Link link3 = ControllerLinkBuilder.linkTo(methodLink3).withRel("bdkKey");
        if(!terminalParameter.hasLink(link3.getRel()))
            terminalParameter.add(link3);

        return terminalParameter;
    }


    @ExceptionHandler(BlowfishRuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Errors handleRuntimeException(HttpServletRequest httpServletRequest, BlowfishRuntimeException brex) {
        Set<MediaType> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        httpServletRequest.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
        BlowfishLog log = new BlowfishLog(brex.getMessage(), brex);
        logger.error(log.toString());
        return errorService.getError(brex);
    }

}
