package za.co.discovery.assignment.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import za.co.discovery.assignment.GetPathRequest;
import za.co.discovery.assignment.GetPathResponse;
import za.co.discovery.assignment.services.PathService;

@Endpoint
public class PathEndPoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private PathService pathService;

    @Autowired
    public PathEndPoint(PathService pathService) {
        this.pathService = pathService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPathRequest")
    @ResponsePayload
    public GetPathResponse getPath(@RequestPayload GetPathRequest request) {
        GetPathResponse response = new GetPathResponse();
        response.setPath(pathService.findPath(request.getName()));

        return response;
    }

}
