import java.util.List;
import java.util.Map;

public class ReportGenerator {
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
}
