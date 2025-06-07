package com.ib.custom.helper;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.custom.model.IbClient;

public class IbConnectionHelper {
    public static IbClient initIbClient() throws InterruptedException {
        IbClient ibClient = new IbClient();
        EClientSocket m_client = ibClient.getClient();
        EReaderSignal m_signal = ibClient.getSignal();
        m_client.setConnectOptions("+PACEAPI");
        m_client.eConnect("127.0.0.1", 4001, 0);
        final EReader reader = new EReader(m_client, m_signal);
        reader.start();
        new Thread(() -> {
            while (m_client.isConnected()) {
                m_signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        }).start();
        return ibClient;
    }
}
