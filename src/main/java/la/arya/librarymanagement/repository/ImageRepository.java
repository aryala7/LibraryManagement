package la.arya.librarymanagement.repository;

import la.arya.librarymanagement.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByFileName(String name);

}
