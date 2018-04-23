package com.avantir.blowfish.controller.mgt;

import com.avantir.blowfish.entity.BlowfishLog;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.entity.KeyUsageType;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.model.Errors;
import com.avantir.blowfish.model.KeyGenerationRequest;
import com.avantir.blowfish.model.KeyComponentImportRequest;
import com.avantir.blowfish.model.KeyImportRequest;
import com.avantir.blowfish.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/keymanagers", produces = "application/hal+json")
public class KeyManagementController {


    private static final Logger logger = LoggerFactory.getLogger(KeyManagementController.class);
    @Autowired
    KeyService keyService;
    @Autowired
    ErrorService errorService;
    @Autowired
    KeyManagementService keyManagementService;
    @Autowired
    KeyUsageTypeService keyUsageTypeService;
    @Autowired
    KeyCryptographicTypeService keyCryptographicTypeService;
    //@Value("${blowfish.key.rsa.length}")
    private String rsaKeyLength = "2048";
    //@Value("${blowfish.key.rsa.modulus}")
    private String rsaKeyModulus = "010001";

    // validate check digit of each key component..
    // verify key component and chk digit
    // verify key and check digit
    // generate HSM certificate
    // load Public key from HSM certificate
    // import under HSM certificate - done
    // import under HSM LMK  - done
    // Enable HSM to support encryt data, decrypt data under DEK  - done
    // Enable HSM to support
    // Enable HSM to generate DEK key - done


    @RequestMapping(value = "/imports/components", method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object importKeyComponent(@RequestBody KeyComponentImportRequest keyComponentImportRequest)
    {
        keyComponentImportRequest = Optional.ofNullable(keyComponentImportRequest).orElseThrow(() -> new BlowfishIllegalArgumentException("KeyComponentImportRequest"));
        keyComponentImportRequest.validate();
        // Only ZMK allowed to be imported for now
        Key key = keyManagementService.importZMKUnderLMK(keyComponentImportRequest.getComponents()).orElseThrow(() -> new BlowfishProcessingErrorException("Error importing key"));
        return new ResponseEntity<Object>(key, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/imports/keys", method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object importKey(@RequestBody KeyImportRequest keyImportRequest)
    {
        keyImportRequest = Optional.ofNullable(keyImportRequest).orElseThrow(() -> new BlowfishIllegalArgumentException("KeyImportRequest"));
        keyImportRequest.validate();
        // Only ZMK allowed to be imported for now
        Key key = keyManagementService.importZMKUnderLMK(keyImportRequest.getKey()).orElseThrow(() -> new BlowfishProcessingErrorException("Error importing key"));
        return new ResponseEntity<Object>(key, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/generations", method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object generateKey(@RequestBody KeyGenerationRequest keyGenerationRequest)
    {
        keyGenerationRequest = Optional.ofNullable(keyGenerationRequest).orElseThrow(() -> new BlowfishIllegalArgumentException("KeyGenerationRequest"));
        keyGenerationRequest.validate();
        KeyUsageType keyUsageType = keyUsageTypeService.findByKeyUsageTypeId(keyGenerationRequest.getKeyUsageTypeId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key Usage Type"));
        KeyCryptographicType keyCryptographicType = keyCryptographicTypeService.findByKeyCryptographicTypeId(keyGenerationRequest.getKeyCryptographicTypeId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key Cryptographic Type"));

        if("RSA".equalsIgnoreCase(keyCryptographicType.getCode())){
            if("ZMK".equalsIgnoreCase(keyUsageType.getCode())){
                rsaKeyModulus = String.format("%6s", new Object[]{rsaKeyModulus}).replace(' ', '0');
                Key key = keyManagementService.generatePrivateKeyWithHSM(rsaKeyLength, rsaKeyModulus).orElseThrow(() -> new BlowfishProcessingErrorException("Error generating key"));
                return new ResponseEntity<Object>(key, HttpStatus.CREATED);
            }
            throw new BlowfishOperationNotSupportedException("RSA Key Usage type " + keyUsageType.getCode());
        }
        else if("3DES".equalsIgnoreCase(keyCryptographicType.getCode())){
            if("DEK".equalsIgnoreCase(keyUsageType.getCode())){
                final long parentKeyId = keyGenerationRequest.getParentKeyId();
                Key parentKey = keyService.findByKeyId(keyGenerationRequest.getParentKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Parent Key " + parentKeyId));
                Key key = keyManagementService.generateDEKWithHSM("U" + parentKey.getData()).orElseThrow(() -> new BlowfishProcessingErrorException("Error generating key"));
                return new ResponseEntity<Object>(key, HttpStatus.CREATED);
            }
            throw new BlowfishOperationNotSupportedException("3DES Key Usage type " + keyUsageType.getCode());
        }
        throw new BlowfishOperationNotSupportedException("Key Cryptographic type " + keyUsageType.getCode());
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
