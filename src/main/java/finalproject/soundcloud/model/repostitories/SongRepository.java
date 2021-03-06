package finalproject.soundcloud.model.repostitories;

import finalproject.soundcloud.model.pojos.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SongRepository extends JpaRepository<Song,Long> {
    ArrayList<Song> findAllBySongName(String name);
    Song findById(long id);
    Song findBySongNameAndUserId(String name, long id);
    ArrayList<Song> findAllByUserId(long userId);


}
