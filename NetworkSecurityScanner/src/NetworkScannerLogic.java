import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkScannerLogic {

    // Method to collect data from network devices
    private static Map<String, List<String>> collectData(List<String> targetHosts, List<Integer> targetPorts) {
        Map<String, List<String>> collectedData = new HashMap<>();

        for (String host : targetHosts) {
            List<String> hostData = new ArrayList<>();

            for (int port : targetPorts) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 1000); // Connect with 1 second timeout

                    // Port is open, add to host data
                    hostData.add("Port " + port + " is open");

                    // Read service banner
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String banner = reader.readLine();
                    if (banner != null) {
                        hostData.add("Service banner: " + banner);
                    }
                } catch (IOException e) {
                    // Port is closed or unreachable
                }
            }

            collectedData.put(host, hostData);
        }

        return collectedData;
    }

    // Method to analyze collected data and identify vulnerabilities
    public static Map<String, List<String>> analyzeData(List<String> targetHosts, List<Integer> targetPorts) {
        Map<String, List<String>> collectedData = collectData(targetHosts, targetPorts);
        Map<String, List<String>> scanResults = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : collectedData.entrySet()) {
            String ipAddress = entry.getKey();
            List<String> data = entry.getValue();

            // Perform vulnerability detection based on collected data
            List<String> vulnerabilities = detectVulnerabilities(data);

            // Add vulnerabilities (if any) to the scan results
            scanResults.put(ipAddress, vulnerabilities);
        }

        return scanResults;
    }

    // Method to perform vulnerability detection based on collected data
    private static List<String> detectVulnerabilities(List<String> data) {
        List<String> vulnerabilities = new ArrayList<>();

        // Check for common vulnerabilities based on open ports and service banners
        for (String serviceInfo : data) {
            if (serviceInfo.contains("Port 22") && serviceInfo.contains("SSH")) {
                vulnerabilities.add("SSH service detected on port 22");
            } else if (serviceInfo.contains("Port 80") && serviceInfo.contains("HTTP")) {
                vulnerabilities.add("HTTP service detected on port 80");
            }
            // Add more checks as needed
        }

        return vulnerabilities;
    }

    // Method to generate a report based on scan results
    public static String generateReport(Map<String, List<String>> scanResults) {
        StringBuilder report = new StringBuilder();

        // Generate report header
        report.append("Network Scan Report:\n");
        report.append("-----------------------------------\n\n");

        // Iterate over scan results and append details to the report
        for (Map.Entry<String, List<String>> entry : scanResults.entrySet()) {
            String ipAddress = entry.getKey();
            List<String> vulnerabilities = entry.getValue();

            report.append("IP Address: ").append(ipAddress).append("\n");
            report.append("Vulnerabilities:\n");
            if (vulnerabilities.isEmpty()) {
                report.append("  No vulnerabilities detected\n");
            } else {
                for (String vulnerability : vulnerabilities) {
                    report.append("  - ").append(vulnerability).append("\n");
                }
            }
            report.append("\n");
        }

        return report.toString();
    }

    // Example method to initiate a network scan
    public static void startScan(List<String> targetHosts, List<Integer> targetPorts) {
        System.out.println("Starting network scan...");

        // Analyze collected data and generate report
        Map<String, List<String>> scanResults = analyzeData(targetHosts, targetPorts);
        String report = generateReport(scanResults);

        // Print the generated report
        System.out.println(report);
    }

    // Main method for testing the scanner logic
    public static void main(String[] args) {
        // List of target hosts to scan
        List<String> targetHosts = List.of("192.168.0.1", "192.168.0.2");

        // List of target ports to scan
        List<Integer> targetPorts = List.of(22, 80, 443);

        // Start the network scan
        startScan(targetHosts, targetPorts);
    }
}
