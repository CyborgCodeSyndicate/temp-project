package com.theairebellion.zeus.ui.util;

import com.theairebellion.zeus.framework.util.ResourceLoader;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;

import java.util.List;

import static com.theairebellion.zeus.framework.util.ObjectFormatter.escapeHtml;

public final class ResponseFormatter {

    private ResponseFormatter() {
    }

    /**
     * Formats a list of responses into an HTML string, summarizing request counts and formatting individual responses.
     *
     * @param responses a list of response objects, which may include lists of responses
     * @return a string containing the generated HTML with formatted responses
     */
    public static String formatResponses(List<ApiResponse> responses) {
        int totalRequests = 0;
        int successCount = 0;
        int warningCount = 0;
        int errorCount = 0;

        for (ApiResponse response : responses) {
            totalRequests++;
            int status = response.getStatus();
            if (status >= 400) {
                errorCount++;
            } else if (status >= 300) {
                warningCount++;
            } else if (status > 0) {
                successCount++;
            }
        }

        String templateHtml = ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html");
        StringBuilder htmlBuilder = new StringBuilder(
            templateHtml.replace("{{total}}", String.valueOf(totalRequests))
                .replace("{{success}}", String.valueOf(successCount))
                .replace("{{warning}}", String.valueOf(warningCount))
                .replace("{{error}}", String.valueOf(errorCount)));

        for (int i = 0; i < responses.size(); i++) {
            appendResponseAccordion(htmlBuilder, responses.get(i), i);
        }

        return htmlBuilder.toString();
    }


    /**
     * Appends an accordion HTML block for a given response to the provided StringBuilder.
     *
     * @param html     the StringBuilder to append the HTML content to
     * @param response the response object to format
     * @param index    the index used to uniquely identify the accordion elements
     */
    private static void appendResponseAccordion(StringBuilder html, ApiResponse response, int index) {
        try {

            String url = response.getUrl();
            String method = response.getMethod();
            int status = response.getStatus();
            String body = response.getBody();

            String statusClass = (status >= 400) ? "status-error" :
                                     (status >= 300) ? "status-warning" :
                                         "status-success";

            String endpoint = extractEndpoint(url);

            html.append("<div class='accordion'>")
                .append("<div class='accordion-header' id='header-").append(index)
                .append("' onclick='toggleAccordion(").append(index).append(")'>")
                .append("<div class='method'>").append(method).append("</div>")
                .append("<div class='url'>").append(endpoint).append("</div>")
                .append("<div class='status ").append(statusClass).append("'>")
                .append(status).append("</div>")
                .append("<span class='chevron'>&#9660;</span>")
                .append("</div>")
                .append("<div id='content-").append(index).append("' class='accordion-content'>")
                .append("<pre>").append(escapeHtml(body != null ? body : "")).append("</pre>")
                .append("</div>")
                .append("</div>");
        } catch (Exception ignore) {
        }
    }


    /**
     * Extracts the endpoint path from a URL string.
     *
     * @param url the full URL string
     * @return the endpoint path if available, or the original URL if extraction fails
     */
    private static String extractEndpoint(String url) {
        try {
            java.net.URL urlObj = new java.net.URL(url);
            String endpoint = urlObj.getPath();
            return endpoint.isEmpty() ? "/" : endpoint;
        } catch (Exception e) {
            return url;
        }
    }

}
