package com.example.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.springframework.stereotype.Service;

@Service
public class NetworkScannerService {

    private static final int TIMEOUT_MS = 1000; // 1 second timeout
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public List<Integer> scanPorts(String ipAddress, int startPort, int endPort) {
        List<Integer> openPorts = new ArrayList<>();

        try {
            InetAddress targetAddress = InetAddress.getByName(ipAddress);

            // Scan ports in the defined range
            for (int port = startPort; port <= endPort; port++) {
                final int currentPort = port;
                executorService.submit(() -> {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(targetAddress, currentPort), TIMEOUT_MS);
                        openPorts.add(currentPort);
                    } catch (IOException e) {
                        // Port is closed or unreachable
                    }
                });
            }

            // Shutdown the executor service
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES); // Wait for tasks to complete
        } catch (IOException | InterruptedException e) {
            // Error handling for network-related issues
            e.printStackTrace();
        }

        return openPorts;
    }
}
