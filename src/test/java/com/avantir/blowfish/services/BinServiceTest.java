package com.avantir.blowfish.services;

import com.avantir.blowfish.entity.Bin;
import com.avantir.blowfish.repository.BinRepository;
import com.avantir.blowfish.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;


/**
 * Created by lekanomotayo on 07/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class BinServiceTest {

    private BinService binService;
    @Mock
    private BinRepository binRepository;


    @Before
    public void setup() throws Exception{
        binService = new BinService();
        TestUtils.setField(binService, "binRepository", binRepository);
    }



    @Test
    public void testGetLongestBinByPanFromManyMatches(){

        List<Bin> manyMatchBinList = getManyMatchBinData();
        given(binRepository.findAll())
                .willReturn(manyMatchBinList);

        Optional<Bin> optActual = binService.findByPan("5333678534562343");

        assertThat(optActual.isPresent(), is(true));
        Bin actual = optActual.get();
        Bin expected = new Bin();
        expected.setBinId(3L);
        expected.setCode("53336785345");
        expected.setDescription("Mastercard Test Bin3");
        expected.setStatus(1);
        expected.setCreatedBy("User");
        assertThat(actual.getBinId(), is(expected.getBinId()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getDescription(), is(expected.getDescription()));
        assertThat(actual.getStatus(), is(expected.getStatus()));
        assertThat(actual.getCreatedBy(), is(expected.getCreatedBy()));
    }


    @Test
    public void testGetMatchedBinByPan() throws Exception{

        List<Bin> uniqueBinList = getUniqueBinData();
        given(binRepository.findAll())
                .willReturn(uniqueBinList);

        Optional<Bin> optActual = binService.findByPan("5333678534562343");

        assertThat(optActual.isPresent(), is(true));
        Bin actual = optActual.get();
        Bin expected = new Bin();
        expected.setBinId(2L);
        expected.setCode("533367");
        expected.setDescription("Mastercard Test Bin2");
        expected.setStatus(1);
        expected.setCreatedBy("User");
        assertThat(actual.getBinId(), is(expected.getBinId()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getDescription(), is(expected.getDescription()));
        assertThat(actual.getStatus(), is(expected.getStatus()));
        assertThat(actual.getCreatedBy(), is(expected.getCreatedBy()));
    }


    private List<Bin> getManyMatchBinData(){

        List binList = new ArrayList();
        Bin bin1 = new Bin();
        bin1.setBinId(1L);
        bin1.setCode("53");
        bin1.setDescription("Mastercard Test Bin1");
        bin1.setStatus(1);
        bin1.setCreatedBy("User");
        bin1.setCreatedOn(new Date());

        Bin bin2 = new Bin();
        bin2.setBinId(2L);
        bin2.setCode("533367");
        bin2.setDescription("Mastercard Test Bin2");
        bin2.setStatus(1);
        bin2.setCreatedBy("User");
        bin2.setCreatedOn(new Date());

        Bin bin3 = new Bin();
        bin3.setBinId(3L);
        bin3.setCode("53336785345");
        bin3.setDescription("Mastercard Test Bin3");
        bin3.setStatus(1);
        bin3.setCreatedBy("User");
        bin3.setCreatedOn(new Date());

        Bin bin4 = new Bin();
        bin4.setBinId(3L);
        bin4.setCode("533312");
        bin4.setDescription("Mastercard Test Bin3");
        bin4.setStatus(1);
        bin4.setCreatedBy("User");
        bin4.setCreatedOn(new Date());

        Bin bin5 = new Bin();
        bin5.setBinId(3L);
        bin5.setCode("411111");
        bin5.setDescription("Visa Test Bin");
        bin5.setStatus(1);
        bin5.setCreatedBy("User");
        bin5.setCreatedOn(new Date());

        binList.add(bin1);
        binList.add(bin2);
        binList.add(bin3);
        binList.add(bin4);
        binList.add(bin5);

        return binList;
    }


    private List<Bin> getUniqueBinData(){

        List binList = new ArrayList();
        Bin bin2 = new Bin();
        bin2.setBinId(2L);
        bin2.setCode("533367");
        bin2.setDescription("Mastercard Test Bin2");
        bin2.setStatus(1);
        bin2.setCreatedBy("User");
        bin2.setCreatedOn(new Date());

        Bin bin4 = new Bin();
        bin4.setBinId(3L);
        bin4.setCode("533312");
        bin4.setDescription("Mastercard Test Bin3");
        bin4.setStatus(1);
        bin4.setCreatedBy("User");
        bin4.setCreatedOn(new Date());

        Bin bin5 = new Bin();
        bin5.setBinId(3L);
        bin5.setCode("411111");
        bin5.setDescription("Visa Test Bin");
        bin5.setStatus(1);
        bin5.setCreatedBy("User");
        bin5.setCreatedOn(new Date());

        binList.add(bin2);
        binList.add(bin4);
        binList.add(bin5);

        return binList;
    }

}
