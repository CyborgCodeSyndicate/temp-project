package com.theairebellion.zeus.maven.plugins.navigator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.ai.metadata.model.AiUsage;
import com.theairebellion.zeus.ai.metadata.service.AIMetadataService;
import com.theairebellion.zeus.maven.plugins.navigator.controller.AIUsageController;
import com.theairebellion.zeus.maven.plugins.navigator.usage.AIUsageService;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Mojo(
    name = "navigate-ai",
    defaultPhase = LifecyclePhase.COMPILE,
    requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class AiNavigatorMojo extends AbstractMojo {

    @Parameter(property = "aiNavigator.awsAccessKey", required = true)
    private String awsAccessKey;

    @Parameter(property = "aiNavigator.awsSecretKey", required = true)
    private String awsSecretKey;

    @Parameter(property = "aiNavigator.awsRegion", defaultValue = "eu-north-1")
    private String awsRegion;

    @Parameter(property = "aiNavigator.s3Bucket", required = true)
    private String s3Bucket;

    @Parameter(property = "aiNavigator.use.api", defaultValue = "false")
    private boolean useApi;

    @Parameter(property = "aiNavigator.use.ui", defaultValue = "false")
    private boolean useUi;

    @Parameter(property = "aiNavigator.use.db", defaultValue = "false")
    private boolean useDb;

    @Parameter(property = "aiNavigator.output.file", defaultValue = "instructions.json")
    private String outputFile;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    private File outputDirectory;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        writeSystemProperties();
        AiUsage aiUsage;

        try (S3Client s3Client = createS3Client()) {
            Reflections reflections = createReflectionObject();
            AIMetadataService metadataService = new AIMetadataService(reflections);
            AIUsageService usageService = new AIUsageService(metadataService, reflections, getLog(), s3Client,
                s3Bucket);

            AIUsageController controller = new AIUsageController(usageService);
            aiUsage = controller.generateUsage(useApi, useUi, useDb);
        } catch (Exception e) {
            throw new MojoExecutionException("Error initializing S3Client in AiNavigatorMojo", e);
        }

        writeInstructionsToFile(aiUsage);
    }


    private S3Client createS3Client() {
        return S3Client.builder()
                   .region(Region.of(awsRegion))
                   .credentialsProvider(
                       StaticCredentialsProvider.create(
                           AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                   .build();
    }


    private void writeInstructionsToFile(AiUsage aiUsage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputFile), aiUsage);
        } catch (IOException e) {
            getLog().error("Failed to write AI usage instructions to file: " + outputFile, e);
            throw new RuntimeException(e);
        }
    }


    private void writeSystemProperties() throws MojoExecutionException {
        if (project == null) {
            throw new MojoExecutionException(
                "MavenProject is null; make sure this plugin is executed within a project.");
        }

        File propsFile = new File(project.getBuild().getOutputDirectory(), "system.properties");

        if (!propsFile.exists()) {
            getLog().info("No system.properties found at " + propsFile.getAbsolutePath());
            return;
        }

        getLog().info("Loading system.properties from " + propsFile.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(propsFile)) {
            Properties properties = new Properties();
            properties.load(fis);
            properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to load system.properties from " + propsFile.getAbsolutePath(), e);
        }
    }


    private Reflections createReflectionObject() {
        URLClassLoader classLoader = getClassLoader();
        List<URL> urls = new ArrayList<>(Arrays.asList(classLoader.getURLs()));

        try {
            urls.add(outputDirectory.toURI().toURL());
        } catch (MalformedURLException e) {
            getLog().error("Malformed URL for output directory", e);
        }

        return new Reflections(new ConfigurationBuilder()
                                   .setUrls(urls)
                                   .addClassLoaders(classLoader));
    }


    private URLClassLoader getClassLoader() {
        Set<URL> urls = new LinkedHashSet<>();

        try {
            addUrlsFromPaths(project.getTestClasspathElements(), urls);
            addUrlsFromPaths(project.getCompileClasspathElements(), urls);
        } catch (DependencyResolutionRequiredException | MalformedURLException e) {
            throw new RuntimeException("Failed to construct class loader", e);
        }

        return new URLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }


    private void addUrlsFromPaths(List<String> paths, Set<URL> urls) throws MalformedURLException {
        for (String path : paths) {
            urls.add(new File(path).toURI().toURL());
        }
    }

}
