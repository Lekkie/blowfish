package com.avantir.blowfish.clients.tcp;

import com.avantir.blowfish.entity.ActiveHsm;
import com.avantir.blowfish.entity.HsmEndpoint;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.exceptions.BlowfishHSMConnectionException;
import com.avantir.blowfish.services.ActiveHsmService;
import com.avantir.blowfish.services.EndpointService;
import com.avantir.blowfish.services.HsmEndpointService;
import com.avantir.hsm.client.connection.HSMConnection;
import com.avantir.hsm.client.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by lekanomotayo on 19/04/2018.
 */

@Configuration
public class ThalesHSMClient {

    @Autowired
    ActiveHsmService activeHsmService;


    public byte[] sendReceive(byte[] requestBytes) {
        try{
            System.out.println(Arrays.toString(requestBytes));
            HsmEndpoint hsmEndpoint = activeHsmService.getActiveTMSHsm().orElseThrow(() -> new BlowfishEntityNotFoundException("HSMEndpoint for TMS"));
            HSMConnection hsmConnection = new HSMConnection(hsmEndpoint.getIp(), hsmEndpoint.getPort());
            byte[] responseBytes = hsmConnection.send_recv(requestBytes);
            System.out.println(Arrays.toString(responseBytes));
            return responseBytes;
        }
        catch(IOException ex){
            ex.printStackTrace();
            throw new BlowfishHSMConnectionException(ex.getMessage());
        }
    }



}
