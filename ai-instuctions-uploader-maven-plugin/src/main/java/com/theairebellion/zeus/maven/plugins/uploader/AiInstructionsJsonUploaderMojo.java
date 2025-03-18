package com.theairebellion.zeus.maven.plugins.uploader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.theairebellion.zeus.ai.metadata.model.UsageLevel.COMPREHENSIVE;
import static com.theairebellion.zeus.ai.metadata.model.UsageLevel.EXTENDED;
import static com.theairebellion.zeus.ai.metadata.model.UsageLevel.MANDATORY;

@Mojo(name = "upload-ai", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class AiInstructionsJsonUploaderMojo extends AbstractMojo {

    @Parameter(property = "sourceFile", required = true)
    private File sourceFile;

    @Parameter(property = "filePrefix", required = true)
    private String filePrefix;

    @Parameter(property = "awsAccessKey", required = true)
    private String awsAccessKey;

    @Parameter(property = "awsSecretKey", required = true)
    private String awsSecretKey;

    @Parameter(property = "bucketName", required = true)
    private String bucketName;

    @Parameter(property = "awsRegion", defaultValue = "eu-north-1")
    private String awsRegion;

    @Parameter(property = "filterEnabled", defaultValue = "true")
    private boolean filterEnabled;


    @Override
    public void execute() throws MojoExecutionException {
        try {
            if (!sourceFile.exists()) {
                getLog().warn("Source file does not exist: " + sourceFile.getAbsolutePath());
                return;
            }

            if (awsAccessKey == null || awsSecretKey == null) {
                throw new MojoExecutionException("AWS credentials not found in environment variables!");
            }

            S3Client s3 = S3Client.builder()
                              .region(Region.of(awsRegion))
                              .credentialsProvider(
                                  StaticCredentialsProvider.create(
                                      AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                              .build();

            if (!filterEnabled) {
                // Simply upload the original JSON
                uploadSingleFile(s3, sourceFile, filePrefix + ".json");
            } else {
                filterAndUpload(s3);
            }
            s3.close();

        } catch (Exception e) {
            throw new MojoExecutionException("Error in AiInstructionsJsonUploaderMojo", e);
        }
    }


    private void filterAndUpload(S3Client s3) throws Exception {
        getLog().info("Filtering: " + sourceFile);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(sourceFile);

        // Each item in items[] has { "key": "...", "value": [ { usageLevel: "MANDATORY", ... }, ... ] }
        ArrayNode itemsArray = (ArrayNode) root.get("items");
        if (itemsArray == null) {
            throw new RuntimeException("Invalid JSON: 'items' array missing at root.");
        }

        // Map the usage level suffixes to the allowed usage levels
        // e.g. "mandatory" => [MANDATORY], "extended" => [MANDATORY, EXTENDED], etc.
        Map<String, List<String>> typeMap = Map.of(
            MANDATORY.name().toLowerCase(), List.of(MANDATORY.name()),
            EXTENDED.name().toLowerCase(), Arrays.asList(MANDATORY.name(), EXTENDED.name()),
            COMPREHENSIVE.name().toLowerCase(), Arrays.asList(MANDATORY.name(), EXTENDED.name(), COMPREHENSIVE.name())
        );

        // For each usage level category, build a filtered JSON and upload
        for (var entry : typeMap.entrySet()) {
            String suffix = entry.getKey();              // e.g. "mandatory", "extended", "comprehensive"
            List<String> allowedLevels = entry.getValue(); // which usage levels pass the filter

            // We'll build a new JSON root with the same structure, but filtered
            ObjectNode newRoot = mapper.createObjectNode();
            ArrayNode newItemsArray = mapper.createArrayNode();  // for "items"

            // Iterate existing items
            for (JsonNode itemNode : itemsArray) {
                // We'll keep the same "key" property
                String itemKey = itemNode.get("key").asText("");
                ArrayNode originalValue = (ArrayNode) itemNode.get("value");


                // Filter the "value" array by usageLevel
                ArrayNode filteredValue = mapper.createArrayNode();
                if (originalValue != null) {
                    for (JsonNode valueObj : originalValue) {
                        String usageLevel = valueObj.has("usageLevel")
                                                ? valueObj.get("usageLevel").asText("")
                                                : "";
                        // if usageLevel is in the allowed set, keep this object
                        if (allowedLevels.contains(usageLevel)) {
                            filteredValue.add(valueObj);
                        }
                    }
                }

                // Add new item to newItemsArray only if there's something to keep
                if (!filteredValue.isEmpty()) {
                    ObjectNode newItem = mapper.createObjectNode();
                    newItem.put("key", itemKey);
                    newItem.set("value", filteredValue);
                    newItemsArray.add(newItem);
                }
            }

            // Put the filtered items array in the new root
            newRoot.set("items", newItemsArray);

            // Write out to a temp file
            File tmpFile = File.createTempFile(filePrefix + "-" + suffix, ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(tmpFile, newRoot);

            // Upload to S3
            String finalKey = filePrefix + "-" + suffix + ".json";
            uploadSingleFile(s3, tmpFile, finalKey);

            // Clean up
            tmpFile.delete();
        }
    }


    private void uploadSingleFile(S3Client s3, File fileToUpload, String s3Key) throws Exception {
        getLog().info("Uploading " + fileToUpload.getName() + " -> s3://" + bucketName + "/" + s3Key);

        PutObjectRequest putReq = PutObjectRequest.builder()
                                      .bucket(bucketName)
                                      .key(s3Key)
                                      .contentType("application/json")
                                      .build();

        s3.putObject(putReq, fileToUpload.toPath());
    }

}