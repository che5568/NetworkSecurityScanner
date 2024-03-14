import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkScanner {

    private static final int TIMEOUT_MS = 1000; // 1 second timeout
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try {
            // Collect IP addresses
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localhost.getHostAddress());

            // Collect open ports and service banners
            for (int port = 1; port <= 65535; port++) {
                final int currentPort = port; // Make a copy of port to use inside the lambda
                executorService.submit(() -> {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(localhost, currentPort), TIMEOUT_MS);
                        System.out.println("Port " + currentPort + " is open");

                        // Get the input stream to read service banner
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String banner = reader.readLine();
                        System.out.println("Service Banner: " + banner);

                        // Extract version information from the service banner
                        String version = extractVersion(banner);
                        if (version != null) {
                            System.out.println("Version: " + version);
                        }
                    } catch (IOException e) {
                        // Port is closed or unreachable
                    }
                });
            }

            // Collect network interfaces and associated IP addresses
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
            System.out.println("Network Interface: " + networkInterface.getDisplayName());
            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                System.out.println("IP Address: " + address.getAddress().getHostAddress());
            }

            // Shutdown the executor service
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES); // Wait for tasks to complete
        } catch (IOException | InterruptedException e) {
            // Error handling for network-related issues
            e.printStackTrace();
        }
    }

    private static String extractVersion(String banner) {
        // Example: Extract version information if the banner contains "Version: X.X"
        String versionPrefix = "Version: ";
        int versionIndex = banner.indexOf(versionPrefix);
        if (versionIndex != -1) {
            int endIndex = banner.indexOf(" ", versionIndex + versionPrefix.length());
            if (endIndex != -1) {
                return banner.substring(versionIndex + versionPrefix.length(), endIndex);
            }
        }
        return null; // Return null if version information is not found or cannot be parsed
    }
}
