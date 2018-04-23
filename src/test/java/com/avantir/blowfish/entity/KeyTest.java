package com.avantir.blowfish.entity;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by lekanomotayo on 07/04/2018.
 */
public class KeyTest {


    @Test
    public void shouldReturnTrueForSameKeys(){

        Date createdOn = new Date();
        Key key1 = new Key.Builder().keyId(1L)
                .data("DBEECACCB4210977ACE73A1D873CA59F")
                .checkDigit("1DDD47")
                .description("Encrypt Key version 1")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        Key key2 = new Key.Builder().keyId(1L)
                .data("DBEECACCB4210977ACE73A1D873CA59F")
                .checkDigit("1DDD47")
                .description("Encrypt Key version 1")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        assertThat(key2, is(key1));
    }


    @Test
    public void shouldReturnFalseForDifferentKeys(){

        Date createdOn = new Date();
        Key key1 = new Key.Builder().keyId(1L)
                .data("DBEECACCB4210977ACE73A1D873CA59F")
                .checkDigit("1DDD47")
                .description("Encrypt Key version 1")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        Key key2 = new Key.Builder().keyId(2L)
                .data("DBEECACCB4210977ACE73A1D873CA59F")
                .checkDigit("1DDD47")
                .description("Encrypt Key version 1")
                .status(1)
                .createdBy("System")
                .createdOn(createdOn)
                .build();

        assertThat(key2, is(not(key1)));
    }

}
