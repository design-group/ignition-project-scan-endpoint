# Project Scan Endpoint Module

This module provides a REST endpoint for triggering a project scan in Ignition. It allows you to initiate a project scan programmatically by sending a POST request to the specified endpoint.

## API Endpoint

### Trigger Project Scan

`POST /data/project-scan-endpoint/scan`

This endpoint triggers a project scan when called.

#### Parameters
| Parameter | Description |
| --------- | ----------- |
| `updateDesigners` | A boolean value indicating whether the project scan should update open designers. If `true`, the designer information will be updated. |
| `forceUpdate` | A boolean value indicating whether the project scan should force an update. If `true`, and `updateDesigners` is `true`, the project scan will be forced in the Designer |

#### Example Usage

```sh
curl -X POST -H "Content-Type: application/json" https://project-scan.localtest.me/data/project-scan-endpoint/scan?updateDesigners=true&forceUpdate=true
```

#### Response

The response will be a JSON object containing the result of the project scan.

### Confirm Project Scan Support

`GET /data/project-scan-endpoint/confirm-support`

This endpoint allows you to check if the gateway supports the project scan functionality.

#### Example Usage

```sh
curl http://localhost:8088/data/project-scan-endpoint/confirm-support
```

#### Response

The response will be a JSON object indicating whether the project scan functionality is supported.

```json
{
  "supported": true
}
```

- If the gateway supports project scan, the `supported` field will be `true`.

## Building the Module

Within the root directory, there is a file named `gradle.properties.template`. This file should be copied to `gradle.properties`, and the properties within it should be filled out with the appropriate values.

| Property | Description |
| -------- | ----------- |
| `ignition.signing.keystoreFile` | The path to the keystore file. |
| `ignition.signing.keystorePassword` | The password for the keystore. |
| `ignition.signing.certFile` | The path to the certificate file. |
| `ignition.signing.certAlias` | The alias of the certificate. |
| `ignition.signing.certPassword` | The password for the certificate. |

Once the `gradle.properties` file has been filled out, the module can be built by running the following command:

```sh
./gradlew build
```

### Example Environment Setup

#### Leveraging SDKMAN

1. Install SDKMAN

```sh
curl -s "https://get.sdkman.io" | bash
```

2. Install Java

```sh
sdk install java 17.0.11-zulu
```

3. Install Gradle

```sh
sdk install gradle 7.5.1
```

4. If you are going to deploy to a gateway with non-standard certificates installed, you will need to add the gateway's certificate to the Java truststore. This can be done by running the following commands:

```sh
keytool -import -cacerts -alias root_ca -file /path/to/root_ca.crt -storepass changeit
keytool -import -cacerts -alias server_cert -file /path/to/server.crt -storepass changeit
```
