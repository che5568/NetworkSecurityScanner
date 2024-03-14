Network Security Scanner


The code consists of a network scanner application implemented in Java. It utilizes multithreading and socket programming to scan ports on local and target IP addresses, 
collect information about open ports and service banners, analyze vulnerabilities, and generate reports.

The network security scanner consists of several components:


1.)NetworkScanner

  This class is responsible for scanning local ports and collecting network interface information.
    It first retrieves the local IP address using InetAddress.getLocalHost() and prints it.
    Then, it iterates over ports from 1 to 65535 and submits tasks to an executor service for each port.
    For each port, it attempts to connect to it using a socket. If the connection is successful, it prints that the port is open and reads the service banner (if available) from the input stream of the socket.
    The service banner usually contains information about the service running on the port, such as its version.
    The extractVersion() method attempts to extract version information from the service banner.
    Additionally, it collects information about network interfaces and associated IP addresses.

2. NetworkScannerLogic

    This class contains the logic for collecting data from network devices, analyzing vulnerabilities, and generating reports.
    The collectData() method collects data by attempting to connect to target hosts and ports specified in the input lists.
    For each host and port combination, it opens a socket and collects information about whether the port is open and, if so, any service banners.
    The analyzeData() method analyzes the collected data and identifies vulnerabilities based on open ports and service banners.
    The detectVulnerabilities() method checks for common vulnerabilities based on open ports and service banners.
    The generateReport() method generates a report based on the scan results.

3. ReportGenerator

    This class provides a utility method to generate a report based on the scan results.
    It formats the scan results into a human-readable report with information about detected vulnerabilities for each IP address.

4. NetworkScannerService and NetworkScannerController

    These classes are part of a Spring Boot application.
    NetworkScannerService is a Spring service responsible for scanning ports on target IP addresses.
    NetworkScannerController is a Spring REST controller providing an endpoint (/scan) to initiate a network scan.
    When the /scan endpoint is accessed, it starts a network scan, and the results are returned as a string.

5. NetworkScannerApplication

    This is the main Spring Boot application class responsible for running the Spring Boot application.

## Usage

To use this network security scanner:

1. Clone the repository:
Navigate to the project directory:

2.)cd NetworkSecurityScanner

3.)Build and run the Spring Boot application:

./gradlew bootRun

4.)Access the network scanner endpoint in your browser or using a REST client:

http://localhost:8080/scan

Dependencies

    Java
    Spring Boot
    Gradle
