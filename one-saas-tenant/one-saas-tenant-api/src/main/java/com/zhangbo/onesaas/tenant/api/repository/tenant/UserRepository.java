package com.zhangbo.onesaas.tenant.api.repository.tenant;

import com.zhangbo.onesaas.tenant.api.entity.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhangbo
 * @description:
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
