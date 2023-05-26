package ProiectMPP.ReposInterfaces;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface Repository<E> {
    void add(E entity);
    void delete(UUID entity);
    void update(E entity);
    int size();
    E readEntity(UUID entityID);
    List<E> readEntities();

}

