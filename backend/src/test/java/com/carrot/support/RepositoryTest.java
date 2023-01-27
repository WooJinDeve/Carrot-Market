package com.carrot.support;

import com.carrot.global.config.JpaConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfiguration.class)
public class RepositoryTest {
}
