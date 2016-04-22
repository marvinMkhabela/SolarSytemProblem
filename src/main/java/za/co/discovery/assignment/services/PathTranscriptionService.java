package za.co.discovery.assignment.services;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.models.Planet;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathTranscriptionService {

    protected PathTranscriptionService() {

    }

    public List<PathResponseDto> decipherRawPaths(String[] rawPaths, List<Planet> planets) {

        List<PathResponseDto> paths = new ArrayList<>();
        String[] tempPath;
        int tempIdx;
        PathResponseDto tempPathResponse;
        for (String s : rawPaths) {
            tempPath = s.split(",");
            tempPathResponse = new PathResponseDto();
            String path = "Travel from ";
            for (String str : tempPath) {
                tempIdx = Integer.parseInt(str);
                if (tempIdx == 0) {
                    path += planets.get(tempIdx).getName();
                } else {
                    path += ", to " + planets.get(tempIdx).getName();
                }
            }
            tempIdx = Integer.parseInt(tempPath[tempPath.length - 1]);
            tempPathResponse.setName(planets.get(tempIdx).getName());
            tempPathResponse.setPath(path);

            paths.add(tempPathResponse);
        }

        return paths;
    }
}
