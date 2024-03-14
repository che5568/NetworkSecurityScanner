package com.example.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetworkScannerController {

    private static final int TIMEOUT_MS = 1000; // 1 second timeout
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private NetworkScannerService scannerService;

    @GetMapping("/scan")
    public String scanNetwork() {
        StringBuilder result = new StringBuilder();
        result.append("Starting network scan...\n");

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            result.append("Local IP Address: ").append(localhost.getHostAddress()).append("\n");

            // Define range of ports to scan
            int startPort = 1;
            int endPort = 65535;

            // Scan ports in the defined range
            for (int port = startPort; port <= endPort; port++) {
                final int currentPort = port;
                executorService.submit(() -> {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(localhost, currentPort), TIMEOUT_MS);
                        result.append("Port ").append(currentPort).append(" is open\n");

                        // Optional: Add service detection logic here
                        // Example: String serviceBanner = detectServiceBanner(socket);
                        // result.append("Service on port ").append(currentPort).append(": ").append(serviceBanner).append("\n");
                    } catch (IOException e) {
                        // Port is closed or unreachable
                        // Optionally, handle specific exceptions to distinguish between different error types
                        // Example: if (e instanceof ConnectException) { /* Handle connection refused */ }
                    }
                });
            }

            // Shutdown the executor service
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES); // Wait for tasks to complete
        } catch (IOException | InterruptedException e) {
            // Error handling for network-related issues
            result.append("Error occurred during network scan: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }

    // Optional: Method to detect service banner for a given socket
    private String detectServiceBanner(Socket socket) {
        // Implement your service detection logic here
        return "Service banner"; // Placeholder example
    }
}
