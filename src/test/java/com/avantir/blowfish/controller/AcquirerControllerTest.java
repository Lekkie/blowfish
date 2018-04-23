package com.avantir.blowfish.controller;

import au.com.dius.pact.consumer.PactProviderRuleMk2;
import com.avantir.blowfish.controller.mgt.AcquirerController;
import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.services.AcquirerBinService;
import com.avantir.blowfish.services.AcquirerMerchantService;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.services.AcquirerTermParamService;
import com.avantir.blowfish.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

/**
 * Created by lekanomotayo on 07/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class AcquirerControllerTest {

    AcquirerController acquirerController;
    @Mock
    private AcquirerService acquirerService;
    @Mock
    private AcquirerMerchantService acquirerMerchantService;
    @Mock
    private AcquirerBinService acquirerBinService;
    @Mock
    private AcquirerTermParamService acquirerTermParamService;

    @Before
    public void setup() throws Exception{
        acquirerController = new AcquirerController();
        TestUtils.setField(acquirerController, "acquirerService", acquirerService);
        TestUtils.setField(acquirerController, "acquirerMerchantService", acquirerMerchantService);
        TestUtils.setField(acquirerController, "acquirerBinService", acquirerBinService);
        TestUtils.setField(acquirerController, "acquirerTermParamService", acquirerTermParamService);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void shouldReturnResponseEntityForCreatedObject(){

        Date createdOn = new Date();
        Acquirer acquirer = new Acquirer.Builder()
                .name("Access bank")
                .code("044")
                .binCode("628050")
                .cbnCode("044")
                .address("Head Office, Plot 999c, Danmole Street, Off Adeola Odeku/Idejo Street, Victoria Island, Lagos, Nigeria")
                .phoneNo("+234 1- 2712005-7")
                .domainId(2L)
                .enableAllTranType(true)
                .enableAllBin(true)
                .description("Access Bank")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        Acquirer savedAcquirer = new Acquirer.Builder().acquirerId(1L)
                .name("Access bank")
                .code("044")
                .binCode("628050")
                .cbnCode("044")
                .address("Head Office, Plot 999c, Danmole Street, Off Adeola Odeku/Idejo Street, Victoria Island, Lagos, Nigeria")
                .phoneNo("+234 1- 2712005-7")
                .domainId(2L)
                .enableAllTranType(true)
                .enableAllBin(true)
                .description("Access Bank")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();


        List<AcquirerMerchant> acquirerMerchantList = new ArrayList<>();
        AcquirerMerchant acquirerMerchant = new AcquirerMerchant();
        acquirerMerchant.setAcquirerMerchantId(1L);
        acquirerMerchant.setAcquirerId(1L);
        acquirerMerchant.setMerchantId(1L);
        acquirerMerchantList.add(acquirerMerchant);

        List<AcquirerBin> acquirerBinList = new ArrayList<>();
        AcquirerBin acquirerBin = new AcquirerBin();
        acquirerBin.setAcquirerBinId(1L);
        acquirerBin.setAcquirerId(1L);
        acquirerBin.setBinId(1L);
        acquirerBinList.add(acquirerBin);
        AcquirerTermParam acquirerTermParam = new AcquirerTermParam();
        acquirerTermParam.setAcquirerTermParamId(1L);
        acquirerTermParam.setAcquirerId(1L);
        acquirerTermParam.setTermParamId(1L);
        given(acquirerService.create(acquirer))
                .willReturn(Optional.ofNullable(savedAcquirer));
        given(acquirerMerchantService.findByAcquirerId(savedAcquirer.getAcquirerId()))
                .willReturn(Optional.ofNullable(acquirerMerchantList));
        given(acquirerBinService.findByAcquirerId(savedAcquirer.getAcquirerId()))
                .willReturn(Optional.ofNullable(acquirerBinList));
        given(acquirerTermParamService.findByAcquirerId(savedAcquirer.getAcquirerId()))
                .willReturn(Optional.ofNullable(acquirerTermParam));

        Object object = acquirerController.create(acquirer);

        assertThat(object, instanceOf(ResponseEntity.class));
        ResponseEntity responseEntity = (ResponseEntity) object;
        assertThat(object, instanceOf(ResponseEntity.class));
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        Object body = responseEntity.getBody();
        assertThat(body, instanceOf(Acquirer.class));
        Acquirer actual = (Acquirer) body;

        Acquirer expected = new Acquirer.Builder().acquirerId(1L)
                .name("Access bank")
                .code("044")
                .binCode("628050")
                .cbnCode("044")
                .address("Head Office, Plot 999c, Danmole Street, Off Adeola Odeku/Idejo Street, Victoria Island, Lagos, Nigeria")
                .phoneNo("+234 1- 2712005-7")
                .domainId(2L)
                .enableAllTranType(true)
                .enableAllBin(true)
                .description("Access Bank")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();
        expected.add(new Link("http://localhost/api/v1/acquirers/1"));
        expected.add(new Link("http://localhost/api/v1/acquirers/1/merchants", "allAcquirerMerchants"));
        expected.add(new Link("http://localhost/api/v1/acquirers/1/bins", "allAcquirerBins"));
        expected.add(new Link("http://localhost/api/v1/acquirers/1/termparams", "allAcquirerTermParams"));
        assertThat(actual, is(expected));
    }

}
