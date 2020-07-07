package swjtu.hmsb.userserver.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long>{

    @Query(value = "select * from hmsb_user where vector_id=?1"
            , nativeQuery = true)
    public User findUserByVectorId(Long vectorId);
}
