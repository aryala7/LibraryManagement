package la.arya.librarymanagement.util;

import org.hashids.Hashids;
import org.springframework.stereotype.Service;

@Service
public class Hashid {
    private final Hashids hashids;

    public Hashid() {
        this.hashids = new Hashids("aryala", 10); //todo: move this to env
    }

    public String encode(Long id) {
        return hashids.encode(id);
    }

    public Long decode(String hash) {
        long[] decoded = hashids.decode(hash);
        return decoded.length > 0 ? decoded[0] : null;
    }


}
