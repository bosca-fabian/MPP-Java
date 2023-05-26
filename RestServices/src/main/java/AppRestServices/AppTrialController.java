package AppRestServices;

import ProiectMPP.Model.Trial;
import ProiectMPP.Repos.TrialRepository;
import ProiectMPP.ReposInterfaces.Repository;
import ProiectMPP.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import java.util.UUID;

@RestController
@RequestMapping("/app/trials")
public class AppTrialController {


    @Autowired
    private Repository<Trial> trialRepository = new TrialRepository();

    @RequestMapping(method = RequestMethod.GET)
    public List<Trial> getAll() {
        return this.trialRepository.readEntities();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id) {
        Trial trial = trialRepository.readEntity(UUID.fromString(id));
        if (trial == null)
            return new ResponseEntity<String>("Trial not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Trial>(trial, HttpStatus.OK);
    }

    @RequestMapping(value = "/size", method = RequestMethod.GET)
    public int getSize() {
        return this.trialRepository.size();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Trial addTrial(@RequestBody Trial trial) {
        this.trialRepository.add(trial);
        return trial;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.trialRepository.delete(UUID.fromString(id));
        return new ResponseEntity<Trial>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Trial update(@RequestBody Trial trial) {
        this.trialRepository.update(trial);
        return trial;
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();

    }
}
