package com.avantir.blowfish.entity;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by lekanomotayo on 07/04/2018.
 */
public class AcquirerTest {


    @Test
    public void shouldReturnTrueForSameAcquirers(){

        Date createdOn = new Date();
        Acquirer acquirer1 = new Acquirer.Builder().acquirerId(1L)
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

        Acquirer acquirer2 = new Acquirer.Builder().acquirerId(1L)
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

        assertThat(acquirer2, is(acquirer1));
    }


    @Test
    public void shouldReturnFalseForDifferentAcquirers(){

        Date createdOn = new Date();
        Acquirer acquirer1 = new Acquirer.Builder().acquirerId(1L)
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

        Acquirer acquirer2 = new Acquirer.Builder().acquirerId(2L)
                .name("GTBank")
                .code("057")
                .binCode("628051")
                .cbnCode("057")
                .address("635 Akin Adesola Street, Victoria Island, Lagos")
                .phoneNo("+234 1 448 0000")
                .domainId(2L)
                .enableAllTranType(true)
                .enableAllBin(true)
                .description("Guaranty Trust Bank")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        assertThat(acquirer2, is(not(acquirer1)));
    }

}
