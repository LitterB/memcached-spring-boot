/*
 * Copyright 2017 Sixhours.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sixhours.memcached.cache;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Condition {@link OnMissingSpringCacheTypeTest} tests.
 *
 * @author Sasa Bolic
 */
public class OnMissingSpringCacheTypeTest {

    private final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void whenSpringCacheTypeIsPresentThenOutcomeShouldNotMatch() {
        loadContext(MissingSpringCacheTypeConfig.class, "spring.cache.type=none");

        assertThat(this.applicationContext.containsBean("foo")).isFalse();
    }

    @Test
    public void whenSpringCacheTypeIsNotPresentThenOutcomeShouldMatch() {
        loadContext(MissingSpringCacheTypeConfig.class);

        assertThat(this.applicationContext.containsBean("foo")).isTrue();
    }

    private void loadContext(Class<?> configuration, String... environment) {
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);

        applicationContext.register(configuration);
        applicationContext.refresh();
    }

    @Configuration
    @ConditionalOnMissingSpringCacheType
    static class MissingSpringCacheTypeConfig {

        @Bean
        public String foo() {
            return "foo-" + UUID.randomUUID();
        }
    }

}
