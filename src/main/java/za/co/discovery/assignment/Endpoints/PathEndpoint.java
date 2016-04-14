package za.co.discovery.assignment.Endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import za.co.discovery.assignment.GetPathRequest;
import za.co.discovery.assignment.GetPathResponse;
import za.co.discovery.assignment.PathRepositoryCreation;

@Endpoint
public class PathEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private PathRepositoryCreation pathRepositoryCreation;

    @Autowired
    public PathEndpoint(PathRepositoryCreation pathRepositoryCreation) {
        this.pathRepositoryCreation = pathRepositoryCreation;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPathRequest")
    @ResponsePayload
    public GetPathResponse getPath(@RequestPayload GetPathRequest request) {
        GetPathResponse response = new GetPathResponse();
        response.setPath(pathRepositoryCreation.findPath(request.getName()));

        return response;
    }

}
